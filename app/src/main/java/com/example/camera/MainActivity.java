package com.example.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{
    private ImageView ms_img_show;
    private Button btn_camera;

    //定义一个保存图片的 File 变量
    private File currentImageFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ms_img_show = (ImageView) findViewById(R.id.image);
        btn_camera  = (Button) findViewById(R.id.btn_camera);

        btn_camera.setOnClickListener(this);
    }

    //在按钮点击事件处写上这些东西，这些是在SD卡创建图片文件的:
    @Override
    public void onClick(View v) {
        File dir = getExternalFilesDir("pictures");
        if(dir.exists()){
            dir.mkdirs();
        }
        currentImageFile = new File(dir,System.currentTimeMillis() + ".jpg");
        if(!currentImageFile.exists()){
            try {
                currentImageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Uri imageUri = null;
        if (Build.VERSION.SDK_INT>=24)
        {
            imageUri = FileProvider.getUriForFile(MainActivity.this,getPackageName() + ".fileprovider",currentImageFile);
        }
        else
        {
            imageUri = Uri.fromFile(currentImageFile);
        }


            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // 进入这儿表示没有权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                    // 提示已经禁止
                    Toast.makeText(MainActivity.this, "你没有权限",Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                }
            } else {
                //调用相机
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                it.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(it, Activity.DEFAULT_KEYS_DIALER);
            }
        }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Activity.DEFAULT_KEYS_DIALER) {
            ms_img_show.setImageURI(Uri.fromFile(currentImageFile));
        }
    }

}
