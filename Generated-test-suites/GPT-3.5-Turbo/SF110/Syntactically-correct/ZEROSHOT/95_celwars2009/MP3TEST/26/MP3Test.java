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
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();
    }

    @Test
    public void testInvalidFile() {
        MP3 mp3 = new MP3("invalid.mp3");
        mp3.run();
        // Add assertions here to check for expected behavior with invalid file
    }
  
    // Add more test methods here to test other functionalities of the MP3 class

}