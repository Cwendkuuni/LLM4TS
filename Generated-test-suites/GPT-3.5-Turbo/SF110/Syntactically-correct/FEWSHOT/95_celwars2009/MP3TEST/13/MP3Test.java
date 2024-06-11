import static org.junit.Assert.*;
import org.junit.Test;

import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {
    
    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
        assertTrue(mp3.isAlive());
    }
    
    @Test
    public void testRun() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();

        // Add assertions to verify the functionality of the run method 

    }
}