import org.junit.Test;
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
        // Add assertion for testing the functionality of run() method
    }

    @Test
    public void testExceptionHandling() {
        MP3 mp3 = new MP3("nonexistent.mp3");
        mp3.run(); // This should trigger an exception
        // Add assertion to check if the exception is handled correctly
    }

    // Add more test cases for other public methods if any

}