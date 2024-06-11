import org.junit.Test;
import static org.junit.Assert.*;

public class MP3Test {

    @Test
    public void testConstructor() {
        // Test with a valid filename
        MP3 mp3 = new MP3("test.mp3");
        
        // Verify that the filename is set correctly
        assertEquals("test.mp3", mp3.filename);
    }
    
    @Test
    public void testRun() {
        // In order to test the run method, we can verify that it throws an exception when given an invalid filename
        MP3 mp3 = new MP3("invalid.mp3");
        
        try {
            mp3.run();
            
            // If no exception occurs, the test fails
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            // Exception successfully thrown, test passed
        }
    }
}