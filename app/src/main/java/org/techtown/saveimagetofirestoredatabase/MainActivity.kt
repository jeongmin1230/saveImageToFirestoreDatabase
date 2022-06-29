package org.techtown.saveimagetofirestoredatabase

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private val OPEN_GALLERY = 1

    var uriPhoto : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
/* override 함수 */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == OPEN_GALLERY) {
//                var currentImageUrl : Uri? = data?.data
                uriPhoto = data?.data
                ivPictureArea.setImageURI(uriPhoto)
                btnSaveImage.setOnClickListener {
                    imageUpload(uriPhoto!!)
                }

            } else {
                Log.d("ActivityResult", "something wrong")
            }
        }
    }
/* 일반 함수 */
    // 권한 확인 함수
    private fun checkPermission() {
        var storagePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
    if(storagePermission == PackageManager.PERMISSION_GRANTED) {
        // 권한이 이미 승인 돼 있을 경우
        Toast.makeText(this, "권한이 있습니다.", Toast.LENGTH_SHORT).show()
    } else {
        // 권한이 없을 경우 요청
        requestPermission()
        }
    }

    // 권한 요청 함수
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 99)
    }

    // 파이어스토어에 이미지 업로드 하는 함수
    private fun imageUpload(uri : Uri) {
        var storage : FirebaseStorage? = FirebaseStorage.getInstance()
        // 파일 이름 생성
        var fileName = "IMAGE_${SimpleDateFormat("yyyyMMdd_HHmmss".format(Date()))}_.png"
        var imagesRef = storage!!.reference.child("images/").child(fileName)
        // 이미지 파일 업로드
        imagesRef.putFile(uri!!).addOnSuccessListener {
            Toast.makeText(this, "업로드 성공", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            println(it)
            Toast.makeText(this, "업로드 실패", Toast.LENGTH_SHORT).show()
        }
    }
/* onClick 함수 */
    // 권한 확인 버튼
    fun onClickPermission(view: View) {
        checkPermission()
    }
    // 사진 선택하는 버튼
    fun onClickBtnPick(view: View) {
        val intent : Intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, OPEN_GALLERY)
    }
}