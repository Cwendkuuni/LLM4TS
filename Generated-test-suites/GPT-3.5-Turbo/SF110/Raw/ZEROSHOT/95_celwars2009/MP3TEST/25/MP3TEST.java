import org.junit.*;
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
        // Add assertions to check if the audio is playing or stopped correctly
    }

    @Test
    public void testConstructorWithEmptyFilename() {
        MP3 mp3 = new MP3("");
        assertEquals("", mp3.getFilename());
    }
    
    @Test
    public void testGetFilename() {
        MP3 mp3 = new MP3("test.mp3");
        assertEquals("test.mp3", mp3.getFilename());
    }
    
    @Test
    public void testSetFilename() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.setFilename("newfile.mp3");
        assertEquals("newfile.mp3", mp3.getFilename());
    }
    
    @Test
    public void testSetFilenameWithEmptyString() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.setFilename("");
        assertEquals("", mp3.getFilename());
    }

    // Add more tests for other methods if necessary
    
}