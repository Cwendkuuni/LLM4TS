import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.DataLine;
import java.io.File;
import java.io.IOException;

public class MP3Test {

    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
    }

    @Test
    public void testRun() throws Exception {
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(AudioSystem.getAudioInputStream(file)).thenReturn(mock(AudioInputStream.class));

        AudioFormat baseFormat = mock(AudioFormat.class);
        when(baseFormat.getSampleRate()).thenReturn(44100f);
        when(baseFormat.getChannels()).thenReturn(2);

        AudioFormat.Encoding encoding = mock(AudioFormat.Encoding.class);
        when(encoding.toString()).thenReturn("PCM_SIGNED");

        AudioFormat decodedFormat = mock(AudioFormat.class);
        when(decodedFormat.getChannels()).thenReturn(baseFormat.getChannels());
        when(decodedFormat.getSampleRate()).thenReturn(baseFormat.getSampleRate());
        when(decodedFormat.getEncoding()).thenReturn(encoding);

        AudioInputStream in = mock(AudioInputStream.class);
        when(AudioSystem.getAudioInputStream(decodedFormat, in)).thenReturn(mock(AudioInputStream.class));

        SourceDataLine line = mock(SourceDataLine.class);
        when(AudioSystem.getLine(any(DataLine.Info.class))).thenReturn(line);

        MP3 mp3 = new MP3("test.mp3");
        mp3.run();

        verify(line, times(1)).open(decodedFormat);
        verify(line, times(1)).start();
        verify(line, atLeast(1)).write(any(byte[].class), eq(0), any(int.class));
        verify(line, times(1)).drain();
        verify(line, times(1)).stop();
        verify(line, times(1)).close();
        verify(in, times(1)).close();
    }
}