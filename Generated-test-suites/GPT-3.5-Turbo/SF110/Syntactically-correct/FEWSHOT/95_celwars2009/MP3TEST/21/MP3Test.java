import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import org.junit.Test;

public class MP3Test {

    @Test
    public void testConstructor() throws IOException {
        File file = mock(File.class);
        AudioInputStream audioInputStream = mock(AudioInputStream.class);
        AudioFormat baseFormat = mock(AudioFormat.class);
        AudioFormat decodedFormat = mock(AudioFormat.class);
        AudioInputStream decodedInputStream = mock(AudioInputStream.class);
        SourceDataLine line = mock(SourceDataLine.class);

        when(file.getAbsolutePath()).thenReturn("test.mp3");
        when(AudioSystem.getAudioInputStream(file)).thenReturn(audioInputStream);
        when(audioInputStream.getFormat()).thenReturn(baseFormat);
        when(baseFormat.getSampleRate()).thenReturn(44100f);
        when(AudioSystem.getAudioInputStream(decodedFormat, audioInputStream)).thenReturn(decodedInputStream);
        when(AudioSystem.getLine(any(DataLine.Info.class))).thenReturn(line);

        MP3 mp3 = new MP3("", file);

        verify(line).open(decodedFormat);
        verify(line).start();
        verify(decodedInputStream).read(any(byte[].class), anyInt(), anyInt());
        verify(line).write(any(byte[].class), anyInt(), anyInt());
        verify(line).drain();
        verify(line).stop();
        verify(line).close();
        verify(decodedInputStream).close();
        verify(audioInputStream).close();
    }

    @Test
    public void testRunWithError() throws IOException {
        File file = mock(File.class);
        AudioInputStream audioInputStream = mock(AudioInputStream.class);
        AudioFormat baseFormat = mock(AudioFormat.class);
        AudioFormat decodedFormat = mock(AudioFormat.class);
        AudioInputStream decodedInputStream = mock(AudioInputStream.class);
        SourceDataLine line = mock(SourceDataLine.class);

        when(file.getAbsolutePath()).thenReturn("test.mp3");
        when(AudioSystem.getAudioInputStream(file)).thenReturn(audioInputStream);
        when(audioInputStream.getFormat()).thenReturn(baseFormat);
        when(baseFormat.getSampleRate()).thenReturn(44100f);
        when(AudioSystem.getAudioInputStream(decodedFormat, audioInputStream)).thenReturn(decodedInputStream);
        when(AudioSystem.getLine(any(DataLine.Info.class))).thenThrow(new IOException("Error opening line"));

        MP3 mp3 = new MP3("", file);

        verify(line).close();
        verify(decodedInputStream).close();
        verify(audioInputStream).close();
    }

    @Test
    public void testRun() throws IOException {
        File file = mock(File.class);
        AudioInputStream audioInputStream = mock(AudioInputStream.class);
        AudioFormat baseFormat = mock(AudioFormat.class);
        AudioFormat decodedFormat = mock(AudioFormat.class);
        AudioInputStream decodedInputStream = mock(AudioInputStream.class);
        SourceDataLine line = mock(SourceDataLine.class);

        when(file.getAbsolutePath()).thenReturn("test.mp3");
        when(AudioSystem.getAudioInputStream(file)).thenReturn(audioInputStream);
        when(audioInputStream.getFormat()).thenReturn(baseFormat);
        when(baseFormat.getSampleRate()).thenReturn(44100f);
        when(AudioSystem.getAudioInputStream(decodedFormat, audioInputStream)).thenReturn(decodedInputStream);
        when(AudioSystem.getLine(any(DataLine.Info.class))).thenReturn(line);

        MP3 mp3 = new MP3("", file);

        verify(line).open(decodedFormat);
        verify(line).start();
        verify(decodedInputStream).read(any(byte[].class), anyInt(), anyInt());
        verify(line).write(any(byte[].class), anyInt(), anyInt());
        verify(line).drain();
        verify(line).stop();
        verify(line).close();
        verify(decodedInputStream).close();
        verify(audioInputStream).close();
    }
}