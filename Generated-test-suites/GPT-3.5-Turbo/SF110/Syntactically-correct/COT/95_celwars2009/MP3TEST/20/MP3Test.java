import org.junit.*;
import static org.junit.Assert.*;

public class MP3Test {

    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertEquals("test.mp3", mp3.filename);
    }

    @Test
    public void testRun() {
        MP3 mp3 = new MP3("test.mp3");
        
        // TODO: Write test cases for run() method
        
    }
    
    // TODO: Add more test cases for the other methods in MP3 class
    
}