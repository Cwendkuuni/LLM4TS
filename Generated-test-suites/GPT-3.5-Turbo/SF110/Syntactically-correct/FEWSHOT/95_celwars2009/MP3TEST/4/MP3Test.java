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
        // Add assertions to check the behavior of the run method
    }

    @Test
    public void testRunWithInvalidFile() {
        MP3 mp3 = new MP3("invalidfile.mp3");
        mp3.run();
        // Add assertions to check the behavior of the run method with an invalid file
    }

    // Add more test methods to comprehensively test all methods in the MP3 class

}