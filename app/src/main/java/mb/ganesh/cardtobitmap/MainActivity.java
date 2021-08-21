package mb.ganesh.cardtobitmap;

import static android.provider.MediaStore.Images.Media.insertImage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    CardView cardView;
    Bitmap bitmap;
    ProgressDialog progressDialog;


    ImageView imageView;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardView = findViewById(R.id.card_view);
        imageButton = findViewById(R.id.image_button);
        imageView = findViewById(R.id.image_view);


        cardView.setDrawingCacheEnabled(true);
        bitmap = getBitmapFromView(cardView);
        cardView.setDrawingCacheEnabled(false);
        imageView.setImageBitmap(bitmap);

        Log.e("Bitmap", bitmap.toString());


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Please wait image downloading");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }).start();
                sharePalette(bitmap);
            }
        });
    }

    private void sharePalette(Bitmap bitmap) {

        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "TodayFuelPrice", "Tirunelveli Fuel Price");
        Uri bitmapUri = Uri.parse(bitmapPath);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        startActivity(Intent.createChooser(intent, "Share"));
    }

    public static Bitmap getBitmapFromView(View view) {
        Log.e("Status", "Checked");
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }


}