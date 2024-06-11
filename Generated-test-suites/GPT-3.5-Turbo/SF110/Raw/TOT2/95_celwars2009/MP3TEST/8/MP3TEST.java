import org.junit.Test;
import static org.junit.Assert.*;

public class MP3Test {

    @Test
    public void testMP3Constructor() {
        MP3 mp3 = new MP3("testFile.mp3");
        assertNotNull(mp3);
        assertEquals("testFile.mp3", mp3.filename);
    }

    @Test
    public void testMP3Run() {
        // unable to test run method as it involves audio playback
        // potential test could involve checking for IOException
    }

    @Test
    public void testExceptionHandling() {
        // test exception handling for incorrect file path
        try {
            MP3 mp3 = new MP3("nonExistentFile.mp3");
            fail("Expected IOException");
        } catch (IOException e) {
            // expected exception
        }
    }
}