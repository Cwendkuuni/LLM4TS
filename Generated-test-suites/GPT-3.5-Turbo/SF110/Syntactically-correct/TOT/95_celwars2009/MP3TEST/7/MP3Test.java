import javax.sound.sampled.SourceDataLine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MP3Test {

    @Test
    void testRun_NormalCase() {
        MP3 mp3 = new MP3("test.mp3");

        // Mock audio inputs and outputs
        AudioInputStream mockAudioIn = mock(AudioInputStream.class);
        AudioInputStream mockDecodedIn = mock(AudioInputStream.class);
        SourceDataLine mockLine = mock(SourceDataLine.class);

        // Set up the mocks
        when(mockAudioIn.getFormat()).thenReturn(new AudioFormat(44100, 16, 2, true, false));
        when(mockDecodedIn.getFormat()).thenReturn(new AudioFormat(44100, 16, 1, true, false));
        when(mockLine.isOpen()).thenReturn(true);
        when(mockLine.write(any(), anyInt(), anyInt())).thenReturn(4096);
        when(mockLine.drain()).thenReturn(4096);

        // Set up the audio system mocks
        AudioSystem.setAudioInputStream(mockAudioIn);
        AudioSystem.setAudioInputStream(mockDecodedIn);
        AudioSystem.setLine(mockLine);

        // Run the MP3 thread
        mp3.run();

        // Verify interactions
        verify(mockLine, atLeastOnce()).open(any(AudioFormat.class));
        verify(mockLine).start();
        verify(mockLine, atLeastOnce()).write(any(), anyInt(), anyInt());
        verify(mockLine).drain();
        verify(mockLine).stop();
        verify(mockLine).close();
        verify(mockAudioIn).close();
        verify(mockDecodedIn).close();
    }

    @Test
    void testRun_ExceptionCase() {
        MP3 mp3 = new MP3("nonexistent.mp3");

        assertDoesNotThrow(mp3::run);
    }

    @Test
    void testRun_NullInputStreamCase() {
        MP3 mp3 = new MP3(null);

        assertThrows(NullPointerException.class, mp3::run);
    }
}