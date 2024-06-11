import javax.sound.sampled.*;
import java.io.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MP3Test {
    
    private MP3 mp3;
    
    @Before
    public void setUp() {
        mp3 = new MP3("test.mp3");
    }
    
    @After
    public void tearDown() {
        mp3.stop();
    }
    
    @Test
    public void testRun() {
        // To test the run method, we need to make sure it executes without any exceptions
        mp3.run();
        assertTrue(true);
    }
    
    @Test
    public void testConstructor() {
        // To test the constructor, we can check if the filename is correctly assigned
        assertEquals("test.mp3", mp3.filename);
    }
}