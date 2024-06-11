import org.junit.Test;
import java.io.IOException;

public class MP3Test {

    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
    }

    @Test
    public void testRun() {
        MP3 mp3 = new MP3("test.mp3");
    }

    @Test
    public void testRun_ExceptionHandling() {
        MP3 mp3 = new MP3(""); // Empty filename should throw an exception
    }

    @Test
    public void testRun_InvalidFile() {
        MP3 mp3 = new MP3("invalidFile.mp3"); // Invalid file should throw an exception
    }
}