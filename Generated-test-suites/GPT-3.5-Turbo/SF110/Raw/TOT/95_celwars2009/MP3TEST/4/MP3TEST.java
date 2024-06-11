import static org.junit.Assert.*;
import org.junit.Test;

public class MP3Test {
    
    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
    }
    
    @Test
    public void testRun_ValidFile() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();
        assertTrue(mp3.isAlive());
    }
    
    @Test
    public void testRun_InvalidFile() {
        MP3 mp3 = new MP3("invalid.mp3");
        mp3.run();
        assertFalse(mp3.isAlive());
    }
    
    @Test
    public void testRun_NullFile() {
        MP3 mp3 = new MP3(null);
        mp3.run();
        assertFalse(mp3.isAlive());
    }
    
    @Test
    public void testRun_Exception() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();
        assertFalse(mp3.isAlive());
    }
}