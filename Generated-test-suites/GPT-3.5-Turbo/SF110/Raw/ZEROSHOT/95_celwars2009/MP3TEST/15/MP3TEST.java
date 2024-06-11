import static org.junit.Assert.*;
import org.junit.Test;

import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

    @Test
    public void testRun() {
        // Test audio file
        String filename = "test.mp3";
        
        // Create instance of MP3
        MP3 mp3 = new MP3(filename);
        
        // Test if MP3 thread is running
        assertTrue(mp3.isAlive());
        
        // Test if MP3 thread is still running after 5 seconds
        try {
            Thread.sleep(5000);
            assertTrue(mp3.isAlive());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Test if MP3 thread is not running after 10 seconds
        try {
            Thread.sleep(5000);
            assertFalse(mp3.isAlive());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}