import org.junit.Test;
import org.mockito.Mockito;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class MP3Test {

    @Test
    public void testRun() throws IOException {
        File fileMock = Mockito.mock(File.class);
        AudioInputStream audioInputStreamMock = Mockito.mock(AudioInputStream.class);
        AudioFormat baseFormatMock = Mockito.mock(AudioFormat.class);
        AudioFormat decodedFormatMock = Mockito.mock(AudioFormat.class);
        AudioInputStream decodedAudioInputStreamMock = Mockito.mock(AudioInputStream.class);
        DataLine.Info infoMock = Mockito.mock(DataLine.Info.class);
        SourceDataLine lineMock = Mockito.mock(SourceDataLine.class);

        when(fileMock.getPath()).thenReturn("test.mp3");
        when(AudioSystem.getAudioInputStream(fileMock)).thenReturn(audioInputStreamMock);
        when(audioInputStreamMock.getFormat()).thenReturn(baseFormatMock);
        when(baseFormatMock.getSampleRate()).thenReturn(44100.0f);
        when(AudioSystem.getAudioInputStream(decodedFormatMock, audioInputStreamMock))
                .thenReturn(decodedAudioInputStreamMock);
        when(decodedAudioInputStreamMock.read(any(byte[].class), eq(0), eq(4096)))
                .thenReturn(100)
                .thenReturn(-1);
        when(infoMock.getLineClass()).thenReturn(SourceDataLine.class);
        when(AudioSystem.getLine(infoMock)).thenReturn(lineMock);
        when(lineMock.isOpen()).thenReturn(true);

        MP3 mp3 = new MP3("test.mp3");
        mp3.run();

        verify(fileMock, times(1)).getPath();
        verify(AudioSystem, times(1)).getAudioInputStream(fileMock);
        verify(audioInputStreamMock, times(1)).getFormat();
        verify(baseFormatMock, times(1)).getSampleRate();
        verify(AudioSystem, times(1)).getAudioInputStream(decodedFormatMock, audioInputStreamMock);
        verify(decodedAudioInputStreamMock, times(2)).read(any(byte[].class), eq(0), eq(4096));
        verify(infoMock, times(1)).getLineClass();
        verify(AudioSystem, times(1)).getLine(infoMock);
        verify(lineMock, times(1)).isOpen();
    }
}