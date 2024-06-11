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
        mp3.run(); // Test if the run method completes without throwing exceptions
    }

    // Edge Case: Test with an invalid filename
    @Test(expected = Exception.class)
    public void testInvalidFilename() {
        MP3 mp3 = new MP3("invalid.mp3");
        mp3.run(); // Expecting an exception to be thrown
    }

    // Edge Case: Test with an empty filename
    @Test(expected = NullPointerException.class)
    public void testEmptyFilename() {
        MP3 mp3 = new MP3("");
        mp3.run(); // Expecting a NullPointerException to be thrown
    }

    // Error Scenario: Test with a corrupted MP3 file
    @Test(expected = Exception.class)
    public void testCorruptedMP3File() {
        MP3 mp3 = new MP3("corrupted.mp3");
        mp3.run(); // Expecting an exception to be thrown
    }
}