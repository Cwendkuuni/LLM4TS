import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.sound.sampled.*;
import java.io.*;

import org.junit.Before;
import org.junit.Test;

public class MP3Test {

    private MP3 mp3;
    private AudioInputStream mockIn;
    private AudioInputStream mockDin;
    private File mockFile;
    private AudioFormat mockBaseFormat;
    private AudioFormat mockDecodedFormat;
    private DataLine.Info mockInfo;
    private SourceDataLine mockLine;

    @Before
    public void setUp() throws Exception {
        mockIn = mock(AudioInputStream.class);
        mockDin = mock(AudioInputStream.class);
        mockFile = mock(File.class);
        mockBaseFormat = mock(AudioFormat.class);
        mockDecodedFormat = mock(AudioFormat.class);
        mockInfo = mock(DataLine.Info.class);
        mockLine = mock(SourceDataLine.class);
    }

    @Test
    public void testConstructor() throws Exception {
        String filename = "test.mp3";
        mp3 = new MP3(filename);
        assertEquals(filename, mp3.filename);
        assertNotNull(mp3.in);
        assertNotNull(mp3.din);
        assertTrue(mp3.isAlive());
    }

    @Test
    public void testRun() throws Exception {
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.length()).thenReturn(1000L);
        when(mockFile.isFile()).thenReturn(true);
        when(AudioSystem.getAudioInputStream(mockFile)).thenReturn(mockIn);
        when(mockIn.getFormat()).thenReturn(mockBaseFormat);
        when(mockBaseFormat.getSampleRate()).thenReturn(44100.0f);
        when(mockBaseFormat.getChannels()).thenReturn(2);
        when(mockIn.getFrameLength()).thenReturn(100);
        when(AudioSystem.getAudioInputStream(mockDecodedFormat, mockIn)).thenReturn(mockDin);
        when(mockLine.getFramePosition()).thenReturn(0L);
        when(mockLine.getBufferSize()).thenReturn(1024);
        when(mockLine.isActive()).thenReturn(true);

        mp3 = new MP3("test.mp3");
        mp3.run();

        verify(mockLine).open(mockDecodedFormat);
        verify(mockLine).start();
        verify(mockDin, times(100)).read(new byte[4096], 0, 4096);
        verify(mockLine).drain();
        verify(mockLine).stop();
        verify(mockLine).close();
        verify(mockDin).close();
    }

    @Test
    public void testRun_FileNotExists() throws Exception {
        when(mockFile.exists()).thenReturn(false);

        mp3 = new MP3("test.mp3");
        mp3.run();

        assertNull(mp3.in);
        assertNull(mp3.din);
    }

    @Test
    public void testRun_NotFile() throws Exception {
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isFile()).thenReturn(false);

        mp3 = new MP3("test.mp3");
        mp3.run();

        assertNull(mp3.in);
        assertNull(mp3.din);
    }

    @Test(expected = IOException.class)
    public void testRun_AudioInputStreamException() throws Exception {
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isFile()).thenReturn(true);
        when(AudioSystem.getAudioInputStream(mockFile)).thenThrow(IOException.class);

        mp3 = new MP3("test.mp3");
        mp3.run();
    }

    @Test(expected = LineUnavailableException.class)
    public void testRun_SourceDataLineNotAvailable() throws Exception {
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isFile()).thenReturn(true);
        when(AudioSystem.getAudioInputStream(mockFile)).thenReturn(mockIn);
        when(mockIn.getFormat()).thenReturn(mockBaseFormat);
        when(mockLine.open(mockDecodedFormat)).thenThrow(LineUnavailableException.class);

        mp3 = new MP3("test.mp3");
        mp3.run();
    }

    @Test(expected = IOException.class)
    public void testRun_AudioInputStreamCloseException() throws Exception {
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isFile()).thenReturn(true);
        when(AudioSystem.getAudioInputStream(mockFile)).thenReturn(mockIn);
        when(mockIn.getFormat()).thenReturn(mockBaseFormat);
        when(mockLine.open(mockDecodedFormat)).thenReturn(mockLine);
        when(mockDin.read(new byte[4096], 0, 4096)).thenReturn(-1);
        when(mockLine.isActive()).thenReturn(false);
        when(mockDin.close()).thenThrow(IOException.class);

        mp3 = new MP3("test.mp3");
        mp3.run();
    }

    @Test
    public void testRun_IOExceptionWhileClosingAudioInputStream() throws Exception {
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isFile()).thenReturn(true);
        when(AudioSystem.getAudioInputStream(mockFile)).thenReturn(mockIn);
        when(mockIn.getFormat()).thenReturn(mockBaseFormat);
        when(mockLine.open(mockDecodedFormat)).thenReturn(mockLine);
        when(mockDin.read(new byte[4096], 0, 4096)).thenReturn(-1);
        when(mockLine.isActive()).thenReturn(false);
        when(mockDin.close()).thenThrow(IOException.class);

        mp3 = new MP3("test.mp3");
        mp3.run();

        verify(mockDin).close();
    }

    @Test(expected = IOException.class)
    public void testRun_IOExceptionWhileClosingDataLine() throws Exception {
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isFile()).thenReturn(true);
        when(AudioSystem.getAudioInputStream(mockFile)).thenReturn(mockIn);
        when(mockIn.getFormat()).thenReturn(mockBaseFormat);
        when(mockLine.open(mockDecodedFormat)).thenReturn(mockLine);
        when(mockDin.read(new byte[4096], 0, 4096)).thenReturn(-1);
        when(mockLine.isActive()).thenReturn(false);
        when(mockLine.close()).thenThrow(IOException.class);

        mp3 = new MP3("test.mp3");
        mp3.run();

        verify(mockLine).close();
    }
}