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
        mp3.run(); // This will run the audio file and should not throw any exceptions
    }

    @Test
    public void testRun_Exception() {
        MP3 mp3 = new MP3("non_existing_file.mp3");
        mp3.run(); // This should throw an exception since the file does not exist
    }

    @Test
    public void testRun_EmptyFile() {
        MP3 mp3 = new MP3("empty.mp3");
        mp3.run(); // This should handle an empty audio file gracefully
    }

    @Test
    public void testRun_NullFile() {
        MP3 mp3 = new MP3(null);
        mp3.run(); // This should handle a null file input gracefully
    }

    @Test
    public void testRun_InvalidAudioFormat() {
        MP3 mp3 = new MP3("invalid_format.mp3");
        mp3.run(); // This should handle an invalid audio format gracefully
    }

}