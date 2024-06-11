import org.junit.*;
import static org.junit.Assert.*;

public class MP3Test {
    
    @Test
    public void testMP3Constructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
    }
    
    @Test
    public void testMP3Run() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();
        // Add assertions for the expected behavior of the run method
    }
    
    // Add more test methods to cover all methods in the MP3 class
    
}