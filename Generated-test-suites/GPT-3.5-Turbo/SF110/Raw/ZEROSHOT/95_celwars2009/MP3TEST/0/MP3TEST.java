import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class MP3Test {

    @Test
    public void testRun() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        // Setup
        MP3 mp3 = new MP3("testfile.mp3");

        File fileMock = Mockito.mock(File.class);
        AudioInputStream inMock = Mockito.mock(AudioInputStream.class);
        AudioFormat baseFormatMock = Mockito.mock(AudioFormat.class);
        AudioFormat decodedFormatMock = Mockito.mock(AudioFormat.class);
        AudioInputStream dinMock = Mockito.mock(AudioInputStream.class);
        DataLine.Info infoMock = Mockito.mock(DataLine.Info.class);
        SourceDataLine lineMock = Mockito.mock(SourceDataLine.class);

        Mockito.when(fileMock.exists()).thenReturn(true);
        Mockito.when(AudioSystem.getAudioInputStream(fileMock)).thenReturn(inMock);
        Mockito.when(inMock.getFormat()).thenReturn(baseFormatMock);
        Mockito.when(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormatMock.getSampleRate(), 16, baseFormatMock.getChannels(), baseFormatMock.getChannels() * 2, baseFormatMock.getSampleRate(), false)).thenReturn(decodedFormatMock);
        Mockito.when(AudioSystem.getAudioInputStream(decodedFormatMock, inMock)).thenReturn(dinMock);
        Mockito.when(new DataLine.Info(SourceDataLine.class, decodedFormatMock)).thenReturn(infoMock);
        Mockito.when(AudioSystem.getLine(infoMock)).thenReturn(lineMock);
        Mockito.when(dinMock.read(Mockito.any(byte[].class), Mockito.eq(0), Mockito.anyInt())).thenReturn(4096, -1);

        // Action
        mp3.run();

        // Verify
        Mockito.verify(lineMock).open(decodedFormatMock);
        Mockito.verify(lineMock).start();
        Mockito.verify(dinMock, Mockito.times(2)).read(Mockito.any(byte[].class), Mockito.eq(0), Mockito.anyInt());
        Mockito.verify(lineMock).drain();
        Mockito.verify(lineMock).stop();
        Mockito.verify(lineMock).close();
        Mockito.verify(dinMock).close();
    }
    
    @Test
    public void testConstructor() {
        // Setup
        String filename = "testfile.mp3";

        // Action
        MP3 mp3 = new MP3(filename);

        // Verify
        Assert.assertEquals(filename, mp3.filename);
        Assert.assertTrue(mp3.isAlive());
    }
}