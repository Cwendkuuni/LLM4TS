import org.junit.Test;
import static org.junit.Assert.*;

public class MP3Test {

    @Test
    public void testMP3Constructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
    }

    @Test
    public void testRunMethod() {
        // Create a test file with audio data to use in testing
        String testFilename = "test_audio.mp3";
        MP3 mp3 = new MP3(testFilename);
        mp3.run(); // Ensure no exceptions are thrown
    }

    @Test
    public void testIOExceptionHandling() {
        // Test handling of IOException in run method
        String nonExistentFile = "non_existent_file.mp3";
        MP3 mp3 = new MP3(nonExistentFile);
        mp3.run(); // Ensure IOException handled gracefully
    }
}