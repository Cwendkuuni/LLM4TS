import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class MP3Test {

    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
    }

    @Test
    public void testRun() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();
        // Add assertion to check if audio is played or not
    }

    @Test
    public void testFileNotFoundException() {
        try {
            MP3 mp3 = new MP3("invalid.mp3");
            mp3.run();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Exception is expected
        }
    }

    @Test
    public void testUnsupportedAudioFileException() {
        try {
            MP3 mp3 = new MP3("unsupported.wav");
            mp3.run();
            fail("Expected UnsupportedAudioFileException to be thrown");
        } catch (Exception e) {
            // Exception is expected
        }
    }

    @Test
    public void testInterruptedException() {
        try {
            Thread.currentThread().interrupt();
            MP3 mp3 = new MP3("test.mp3");
            mp3.run();
            fail("Expected InterruptedException to be thrown");
        } catch (InterruptedException e) {
            // Exception is expected
        }
    }
}