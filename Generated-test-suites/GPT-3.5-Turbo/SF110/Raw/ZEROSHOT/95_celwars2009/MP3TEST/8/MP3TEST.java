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
    public void testRunException() {
        MP3 mp3 = new MP3("nonexistent.mp3");
        mp3.run();
    }
}