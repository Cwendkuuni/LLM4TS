import org.junit.*;
import static org.junit.Assert.*;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

    // Test case for constructor
    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
    }

    // Test case for run method
    @Test
    public void testRun() {
        MP3 mp3 = new MP3("test.mp3");

        // Test typical use case
        assertTrue(mp3.isAlive());

        // Test edge case: empty file
        mp3 = new MP3("");
        assertFalse(mp3.isAlive());

        // Test error scenario: non-existing file
        mp3 = new MP3("nonexistingfile.mp3");
        assertFalse(mp3.isAlive());
    }
}