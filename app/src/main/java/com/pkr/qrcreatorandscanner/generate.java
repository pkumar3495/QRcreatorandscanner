package com.pkr.qrcreatorandscanner;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class generate extends AppCompatActivity {

    EditText text;
    Button gen_but;
    ImageView image;
    Button save;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        ActivityCompat.requestPermissions(generate.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        text = findViewById(R.id.text_edit);
        gen_but = findViewById(R.id.generate_but);
        image = findViewById(R.id.qr_code_image);

        save = findViewById(R.id.save_but);
        save.setEnabled(false);

        gen_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text2QR = text.getText().toString().trim();
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try{
                    BitMatrix bitMatrix = multiFormatWriter.encode(text2QR, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    image.setImageBitmap(bitmap);
                    save.setEnabled(true);

                }
                catch (WriterException e){
                    e.printStackTrace();
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File path = Environment.getExternalStorageDirectory();
                File dir = new File(path+"/qr/");
                dir.mkdirs();

                File file = new File(dir, "qr.png");
                OutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
                    out.flush();
                    out.close();
                    Toast.makeText(generate.this, "The file is saved successfully ("+path+"/qr/ )",Toast.LENGTH_LONG).show();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
