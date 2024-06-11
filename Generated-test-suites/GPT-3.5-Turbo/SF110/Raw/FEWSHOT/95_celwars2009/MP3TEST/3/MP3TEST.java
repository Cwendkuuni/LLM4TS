import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class MP3Test {

    @Test
    public void testConstructorStartsThread() {
        MP3 mp3 = new MP3("test.mp3");
        assertTrue(mp3.isAlive());
    }

    @Test
    public void testRunOpensAudioStreamAndPlays() throws Exception {
        File fileMock = mock(File.class);
        AudioFormat audioFormatMock = mock(AudioFormat.class);
        AudioInputStream audioInputStreamMock = mock(AudioInputStream.class);
        AudioInputStream decodedFormatInputStreamMock = mock(AudioInputStream.class);
        DataLine.Info dataLineInfoMock = mock(DataLine.Info.class);
        SourceDataLine sourceDataLineMock = mock(SourceDataLine.class);
        
        when(fileMock.getAbsolutePath()).thenReturn("test.mp3");
        when(AudioSystem.getAudioInputStream(any(File.class))).thenReturn(audioInputStreamMock);
        when(audioInputStreamMock.getFormat()).thenReturn(audioFormatMock);
        when(AudioSystem.getAudioInputStream(any(AudioFormat.class), any(AudioInputStream.class))).thenReturn(decodedFormatInputStreamMock);
        when(dataLineInfoMock.getLineClass()).thenReturn(SourceDataLine.class);
        when(AudioSystem.getLine(dataLineInfoMock)).thenReturn(sourceDataLineMock);
        
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();

        verify(fileMock).getAbsolutePath();
        verify(AudioSystem).getAudioInputStream(eq(fileMock));
        verify(audioInputStreamMock).getFormat();
        verify(AudioSystem).getAudioInputStream(eq(decodedFormatMock), eq(audioInputStreamMock));
        verify(AudioSystem).getLine(dataLineInfoMock);
        verify(sourceDataLineMock).open(decodedFormatMock);
        verify(sourceDataLineMock).start();
        verify(decodedFormatInputStreamMock, Mockito.atLeastOnce()).read(any(byte[].class), anyInt(), anyInt());
        verify(sourceDataLineMock).drain();
        verify(sourceDataLineMock).stop();
        verify(sourceDataLineMock).close();
        verify(decodedFormatInputStreamMock).close();
    }
}