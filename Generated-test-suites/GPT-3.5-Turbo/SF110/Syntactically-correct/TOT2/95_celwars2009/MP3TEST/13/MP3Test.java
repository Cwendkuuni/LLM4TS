import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class MP3Test {

    @Test
    public void testMP3Constructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
    }

    @Test
    public void testMP3Run() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.run(); // Testing the run method directly as start() method is automatically called in constructor
        // Add assertion based on expected output or result
    }

    @Test
    public void testMP3RunWithInvalidFileName() {
        MP3 mp3 = new MP3("invalid.mp3");
        mp3.run();
        // Add assertion for exception handling
    }

    // Add more test cases for other public methods in MP3 class

}