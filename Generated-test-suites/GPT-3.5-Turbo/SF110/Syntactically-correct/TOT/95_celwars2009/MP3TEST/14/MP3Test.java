import org.junit.Test;
import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {

    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        
        // Test that filename is properly set
        assertEquals("test.mp3", mp3.filename);
        
        // Test that thread is started
        assertTrue(mp3.isAlive());
    }

    @Test
    public void testRun() {
        MP3 mp3 = new MP3("test.mp3");
        
        // Test that audio file is properly retrieved and processed
        // ...
    }
}