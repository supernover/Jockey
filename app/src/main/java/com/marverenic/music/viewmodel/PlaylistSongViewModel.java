package com.marverenic.music.viewmodel;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;

import com.marverenic.music.R;
import com.marverenic.music.activity.instance.AlbumActivity;
import com.marverenic.music.activity.instance.ArtistActivity;
import com.marverenic.music.instances.Library;
import com.marverenic.music.instances.Song;
import com.marverenic.music.player.PlayerController;
import com.marverenic.music.utils.Navigate;

import java.util.List;

public class PlaylistSongViewModel extends SongViewModel {

    private Context mContext;
    private OnRemoveListener mRemoveListener;

    public PlaylistSongViewModel(Context context, List<Song> songs,
                                 OnRemoveListener onRemoveListener) {
        super(context, songs);
        mContext = context;
        mRemoveListener = onRemoveListener;
    }

    public interface OnRemoveListener {
        void onRemove();
    }

    @Override
    public View.OnClickListener onClickMenu() {
        return v -> {
            final PopupMenu menu = new PopupMenu(mContext, v, Gravity.END);
            String[] options = mContext.getResources()
                    .getStringArray(R.array.edit_playlist_options);

            for (int i = 0; i < options.length;  i++) {
                menu.getMenu().add(Menu.NONE, i, i, options[i]);
            }
            menu.setOnMenuItemClickListener(onMenuItemClick());
            menu.show();
        };
    }

    private PopupMenu.OnMenuItemClickListener onMenuItemClick() {
        return menuItem -> {
            switch (menuItem.getItemId()) {
                case 0: //Queue this song next
                    PlayerController.queueNext(getReference());
                    return true;
                case 1: //Queue this song last
                    PlayerController.queueLast(getReference());
                    return true;
                case 2: //Go to artist
                    Navigate.to(
                            mContext,
                            ArtistActivity.class,
                            ArtistActivity.ARTIST_EXTRA,
                            Library.findArtistById(getReference().getArtistId()));
                    return true;
                case 3: // Go to album
                    Navigate.to(
                            mContext,
                            AlbumActivity.class,
                            AlbumActivity.ALBUM_EXTRA,
                            Library.findAlbumById(getReference().getAlbumId()));
                    return true;
                case 4: // Remove
                    getSongs().remove(getIndex());
                    mRemoveListener.onRemove();
                    return true;
            }
            return false;
        };
    }
}
