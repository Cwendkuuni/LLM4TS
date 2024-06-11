import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.sound.sampled.*;
import java.io.*;

import org.junit.Test;

public class MP3Test {

    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertTrue(mp3.isAlive());
    }

    @Test
    public void testRun() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();
        assertTrue(true); // Successful run should not throw exception
    }
    
    @Test
    public void testRunWithException() {
        MP3 mp3 = new MP3("invalid.mp3");
        try {
            mp3.run();
            fail("Should have thrown an exception");
        } catch (Exception e) {
            assertEquals("java.io.FileNotFoundException", e.getClass().getName());
        }
    }

    // Test cases for run method can be added to test different scenarios, such as:
    // - Testing different audio formats
    // - Testing different file sizes
    // - Testing interruption during playback

}