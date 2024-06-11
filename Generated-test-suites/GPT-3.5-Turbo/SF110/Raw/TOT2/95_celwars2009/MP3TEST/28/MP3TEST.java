import org.junit.Test;
import static org.junit.Assert.*;

public class MP3Test {

    @Test
    public void testMP3Constructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
    }

    @Test
    public void testMP3Run() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.start();
        assertNotNull(mp3);
    }

    @Test
    public void testMP3ExceptionHandling() {
        MP3 mp3 = new MP3("non_existing_file.mp3");
        mp3.start();
        assertNotNull(mp3); // Ensure program doesn't crash with invalid file
    }

    // Add more test cases here for other public methods if needed

}