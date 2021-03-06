package co.csadev.kellocharts.sample

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.container, PlaceholderFragment()).commit()
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_about, container, false)

            val version = rootView.findViewById<TextView>(R.id.version)
            version.text = getAppVersionAndBuild(activity).first

            val gotToGithub = rootView.findViewById<TextView>(R.id.go_to_github)
            gotToGithub.setOnClickListener { launchWebBrowser(activity, GITHUB_URL) }

            return rootView
        }
    }

    companion object {
        val TAG = AboutActivity::class.java.simpleName
        val GITHUB_URL = "github.com/gtcompscientist/kellocharts"

        fun getAppVersionAndBuild(context: Context?): Pair<String, Int> {
            try {
                val pInfo = context!!.packageManager.getPackageInfo(context.packageName, 0)
                return Pair(pInfo.versionName, pInfo.versionCode)
            } catch (e: Exception) {
                Log.e(TAG, "Could not get version number")
                return Pair("", 0)
            }

        }

        @SuppressLint("DefaultLocale")
        fun launchWebBrowser(context: Context?, url: String): Boolean {
            var url = url
            try {
                url = url.toLowerCase()
                if (!url.startsWith("http://") || !url.startsWith("https://")) {
                    url = "http://" + url
                }

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                val resolveInfo = context!!.packageManager.resolveActivity(intent,
                        PackageManager.MATCH_DEFAULT_ONLY)
                if (null == resolveInfo) {
                    Log.e(TAG, "No activity to handle web intent")
                    return false
                }
                context.startActivity(intent)
                Log.i(TAG, "Launching browser with url: " + url)
                return true
            } catch (e: Exception) {
                Log.e(TAG, "Could not start web browser", e)
                return false
            }

        }
    }
}
