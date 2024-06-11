import org.junit.Test;
import static org.junit.Assert.*;

public class MP3Test {

    @Test
    public void testMP3Constructor() {
        MP3 mp3 = new MP3("test.mp3");
        
        assertEquals("test.mp3", mp3.filename);
        assertTrue(mp3.isAlive());
    }

    @Test
    public void testRun() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.start();

        // Test functionality of run() method
        // You can add assertions here to check the expected behavior
    }

    @Test
    public void testRun_ExceptionHandling() throws Exception {
        MP3 mp3 = new MP3("nonexistent.mp3");
        mp3.start();

        // Test exception handling in run() method
        // You can add assertions here to check if the exception is handled correctly
    }

    @Test
    public void testRun_EdgeCases() {
        // Test edge cases in run() method
        // You can add assertions here to test specific edge cases
    }

}