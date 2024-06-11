import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class MP3Test {

    private MP3 mp3;

    @Before
    public void setUp() {
        mp3 = new MP3("test.mp3");
    }

    @Test
    public void testRun() throws Exception {
        File mockFile = mock(File.class);
        AudioInputStream mockInputStream = mock(AudioInputStream.class);
        AudioFormat baseFormat = mock(AudioFormat.class);
        AudioFormat decodedFormat = mock(AudioFormat.class);
        AudioInputStream mockDecodedStream = mock(AudioInputStream.class);
        DataLine.Info mockInfo = mock(DataLine.Info.class);
        SourceDataLine mockLine = mock(SourceDataLine.class);

        whenNew(File.class).withArguments("test.mp3").thenReturn(mockFile);
        when(AudioSystem.getAudioInputStream(mockFile)).thenReturn(mockInputStream);
        when(mockInputStream.getFormat()).thenReturn(baseFormat);
        whenNew(AudioFormat.class).withArguments(AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
                baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false)
                .thenReturn(decodedFormat);
        when(AudioSystem.getAudioInputStream(decodedFormat, mockInputStream))
                .thenReturn(mockDecodedStream);
        whenNew(DataLine.Info.class)
                .withArguments(SourceDataLine.class, decodedFormat)
                .thenReturn(mockInfo);
        when(AudioSystem.getLine(mockInfo)).thenReturn(mockLine);

        mp3.run();

        verify(mockLine).open(decodedFormat);
        verify(mockLine).start();
        verify(mockDecodedStream).read(any(), eq(0), eq(4096));
        verify(mockLine).write(any(), eq(0), anyInt());
        verify(mockLine).drain();
        verify(mockLine).stop();
        verify(mockLine).close();
        verify(mockDecodedStream).close();
    }
}