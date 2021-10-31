package com.c.runtimemultipletedpermission;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button gallery,camera;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gallery = findViewById(R.id.gallery);
        camera = findViewById(R.id.camera);
        imageView = findViewById(R.id.imageView);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TedPermission.with(MainActivity.this)
                        .setPermissionListener(permissionGalleryListener)
                        .setPermissions(Manifest.permission.BLUETOOTH, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TedPermission.with(MainActivity.this)
                        .setPermissionListener(permissionCameraListener)
                        .setPermissions(Manifest.permission.BLUETOOTH, Manifest.permission.CAMERA)
                        .check();
            }
        });

        checkPermission();
    }
    PermissionListener permissionCameraListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(Intent.createChooser(intent, "Browse Image"), 1);

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };
    PermissionListener permissionGalleryListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Browse Image"), 2);

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };

    private void checkPermission() {
        TedPermission.with(MainActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }


        if(requestCode == 2 && resultCode == RESULT_OK){
            Uri filePath=data.getData();
            try {
                InputStream inputStream= getContentResolver().openInputStream(filePath);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}