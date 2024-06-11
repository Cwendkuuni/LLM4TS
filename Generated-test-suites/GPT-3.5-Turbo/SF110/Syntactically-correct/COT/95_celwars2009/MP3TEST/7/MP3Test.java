import org.junit.Test;

public class MP3Test {

    @Test
    public void testConstructor() {
        // Test constructor functionality
        MP3 mp3 = new MP3("test.mp3");
        assertNotNul(mp3);
    }
    
    @Test
    public void testRun() {
        // Test run method functionality (edge case)
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();
        // Check if the thread is running
        assertTrue(mp3.isAlive());
    }
    
    @Test
    public void testRun_Exception() {
        // Test run method exception handling scenario
        MP3 mp3 = new MP3("invalidfile.mp3");
        mp3.run();
        // Check if exception is caught and handled
        assertFalse(mp3.isAlive());
    }
}