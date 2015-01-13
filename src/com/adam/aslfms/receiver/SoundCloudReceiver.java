/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adam.aslfms.receiver;

import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.adam.aslfms.util.Track;
import com.adam.aslfms.util.Util;

/**
 * A BroadcastReceiver for intents sent by the SoundCloud application
 *
 * @see AbstractPlayStatusReceiver
 *
 * @author Malachi Soord <inverse.chi@gmail.com>
 * @since 1.4.8
 */
public class SoundCloudReceiver extends AbstractPlayStatusReceiver {

	static final String APP_PACKAGE = "com.soundcloud.android";
	static final String APP_NAME = "SoundCloud";

	public static final String ACTION_ANDROID_PLAYSTATECHANGED = "com.android.music.playstatechanged";
	public static final String ACTION_ANDROID_METACHANGED = "com.android.music.metachanged";

	static final String TAG = "SoundCloud";
	static private Track track = null;

	public static void dumpIntent(Bundle bundle) {
		if (bundle != null) {
			Set<String> keys = bundle.keySet();
			Iterator<String> it = keys.iterator();
			Log.e(TAG, "Dumping Intent start");
			while (it.hasNext()) {
				String key = it.next();
				Log.e(TAG, "[" + key + "=" + bundle.get(key) + "]");
			}
			Log.e(TAG, "Dumping Intent end");
		}
	}

	@Override
	protected void parseIntent(Context ctx, String action, Bundle bundle) {

		MusicAPI musicAPI = MusicAPI.fromReceiver(ctx, APP_NAME, APP_PACKAGE, null, false);
		setMusicAPI(musicAPI);

		if (action.equals(ACTION_ANDROID_METACHANGED)) {
			Track.Builder b = new Track.Builder();
			b.setMusicAPI(musicAPI);
			b.setWhen(Util.currentTimeSecsUTC());

			// TODO: Work out correct parsing of artist/track
			
			b.setArtist(bundle.getString("artist"));
			b.setTrack(bundle.getString("track"));
			b.setDuration(bundle.getInt("duration"));
			track = b.build();
			
		} else if (action.equals(ACTION_ANDROID_PLAYSTATECHANGED)) {
			setTrack(track);
			
			
			// TODO: Work out this part
		} else {
			dumpIntent(bundle);
		}
	}
}
