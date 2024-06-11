import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class MP3Test {
    
    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
    }
    
    @Test
    public void testRun_ValidFile() {
        MP3 mp3 = new MP3("validfile.mp3");
        try {
            mp3.run();
        } catch (Exception e) {
            fail("Exception caught!");
        }
    }
    
    @Test
    public void testRun_FileNotFound() {
        MP3 mp3 = new MP3("nofile.mp3");
        try {
            mp3.run();
            fail("No exception caught!");
        } catch (Exception e) {
            assertTrue(e instanceof FileNotFoundException);
        }
    }
    
    @Test
    public void testRun_InvalidAudioFile() {
        MP3 mp3 = new MP3("invalidfile.mp3");
        try {
            mp3.run();
            fail("No exception caught!");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedAudioFileException);
        }
    }
    
    @Test
    public void testRun_IOException() {
        MP3 mp3 = new MP3("ioexception.mp3");
        try {
            mp3.run();
            fail("No exception caught!");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }
    
    @Test
    public void testCloseInputStream() {
        MP3 mp3 = new MP3("test.mp3");
        try {
            mp3.closeInputStream();
        } catch (IOException e) {
            fail("Exception caught!");
        }
    }
    
    @Test
    public void testCloseInputStream_NullInputStream() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.in = null;
        try {
            mp3.closeInputStream();
            fail("No exception caught!");
        } catch (IOException e) {
            assertTrue(e instanceof NullPointerException);
        }
    }
    
    @Test
    public void testCloseInputStream_IOError() {
        MP3 mp3 = new MP3("test.mp3");
        try {
            mp3.closeInputStream();
            fail("No exception caught!");
        } catch (IOException e) {
            assertEquals("Error closing input stream", e.getMessage());
        }
    }
    
    @Test
    public void testCloseInputStream_IOErrorWithCause() {
        MP3 mp3 = new MP3("test.mp3");
        IOException cause = new IOException("Cause");
        try {
            mp3.closeInputStream(cause);
            fail("No exception caught!");
        } catch (IOException e) {
            assertEquals("Error closing input stream", e.getMessage());
            assertEquals(cause, e.getCause());
        }
    }
}