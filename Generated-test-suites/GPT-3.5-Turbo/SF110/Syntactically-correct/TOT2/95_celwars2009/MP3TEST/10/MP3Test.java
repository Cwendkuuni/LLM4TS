import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class MP3Test {

    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
    }
    
    @Test
    public void testRun() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.run(); // This will test the run method functionality
        
        // Add more specific tests for run method edge cases and exceptions if needed
    }
    
    // Add more test cases for public methods in MP3 class, following the same structure as above

}