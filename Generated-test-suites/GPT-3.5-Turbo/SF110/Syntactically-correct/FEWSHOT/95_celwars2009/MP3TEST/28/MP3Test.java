import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import javax.sound.sampled.*;
import java.io.*;

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
        // Verify the behavior of the run() method
    }

    @Test
    public void testRun_Exception() {
        MP3 mp3 = new MP3("nonexistent.mp3");
        mp3.run();
        // Verify the exception handling in the run() method
    }
}