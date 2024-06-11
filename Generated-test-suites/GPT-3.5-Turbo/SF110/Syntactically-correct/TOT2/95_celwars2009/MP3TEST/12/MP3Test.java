import static org.junit.Assert.*;
import org.junit.Test;

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
        // Add assertions to check the functionality of the run method
    }
    
    @Test
    public void testRunException() {
        MP3 mp3 = new MP3("non_existing_file.mp3");
        mp3.run();
        // Add assertions to check exception handling in the run method
    }
    
    // Add more test cases for other methods in the MP3 class
    
}