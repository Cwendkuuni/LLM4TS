import org.junit.Before;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.*;

public class MP3Test {
    private MP3 mp3;
    private String filename;

    @Before
    public void setUp() {
        filename = "test.mp3";
        mp3 = new MP3(filename);
    }

    @Test
    public void testConstructor_setsFilename() {
        assertEquals(filename, mp3.filename);
    }

    @Test
    public void testRun_playsAudio() throws Exception {
        File file = mock(File.class);
        AudioInputStream in = mock(AudioInputStream.class);
        AudioFormat baseFormat = mock(AudioFormat.class);
        AudioFormat decodedFormat = mock(AudioFormat.class);
        AudioInputStream din = mock(AudioInputStream.class);
        DataLine.Info info = mock(DataLine.Info.class);
        SourceDataLine line = mock(SourceDataLine.class);
        byte[] data = new byte[4096];

        when(file.getAbsolutePath()).thenReturn(filename);
        when(AudioSystem.getAudioInputStream(file)).thenReturn(in);
        when(in.getFormat()).thenReturn(baseFormat);
        when(decodedFormat.getSampleRate()).thenReturn(44100f);
        when(decodedFormat.getChannels()).thenReturn(2);
        when(AudioSystem.getAudioInputStream(decodedFormat, in)).thenReturn(din);
        when(AudioSystem.getLine(info)).thenReturn(line);
        when(line.open(decodedFormat)).thenReturn(true);
        when(din.read(data, 0, data.length)).thenReturn(data.length, -1);

        mp3.run();

        verify(line).start();
        verify(line).drain();
        verify(line).stop();
        verify(line).close();
        verify(din).close();
    }

    @Test
    public void testRun_handlesException() throws Exception {
        File file = mock(File.class);

        when(file.getAbsolutePath()).thenReturn(filename);
        when(AudioSystem.getAudioInputStream(file)).thenThrow(IOException.class);

        mp3.run();
    }
}