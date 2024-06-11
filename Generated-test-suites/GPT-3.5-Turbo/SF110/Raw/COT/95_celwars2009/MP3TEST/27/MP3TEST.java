import org.junit.Test;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

    @Test
    public void testMP3() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        // Check if MP3 thread is started
        assertTrue(mp3.isAlive());
    }
    
    @Test
    public void testRun() {
        String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);
        // Test the functionality of the run method
        // ...
    }
    
    // Additional test cases for edge cases and exceptions

}