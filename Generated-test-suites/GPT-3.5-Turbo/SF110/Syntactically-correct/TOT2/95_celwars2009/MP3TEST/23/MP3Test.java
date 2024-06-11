import org.junit.Test;
import static org.junit.Assert.*;

public class MP3Test {

    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
    }

    @Test
    public void testRun() {
        // Test running the MP3 thread
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();
        // Additional assertion can be added here for specific scenarios
    }

    @Test
    public void testRunException() {
        // Test handling exception in run method
        MP3 mp3 = new MP3("test.mp3");
        // Simulate an exception for testing
        mp3.filename = "nonexistent.mp3";
        mp3.run();
        // Additional assertion for expected exception handling
    }

    // Additional test cases for other public methods can be added here

}