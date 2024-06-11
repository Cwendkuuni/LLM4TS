import org.junit.*;
import static org.junit.Assert.*;

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
        // Add assertions for testing if audio is played correctly
    }

    @Test
    public void testRun_Exception() {
        MP3 mp3 = new MP3("invalid.mp3");
        mp3.run();
        // Add assertions for testing if exception is handled correctly
    }

    @Test
    public void testRun_NullAudioStream() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.in = null;
        mp3.run();
        // Add assertions for testing if exception is handled correctly when AudioInputStream is null
    }

    @Test
    public void testRun_NullDecodedStream() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.din = null;
        mp3.run();
        // Add assertions for testing if exception is handled correctly when decoded AudioInputStream is null
    }

    // Add more test methods to cover all methods in the MP3 class

}