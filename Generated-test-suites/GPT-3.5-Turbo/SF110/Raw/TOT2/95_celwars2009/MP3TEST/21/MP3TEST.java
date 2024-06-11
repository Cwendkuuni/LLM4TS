import org.junit.Test;
import static org.junit.Assert.*;
import java.io.FileNotFoundException;
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
        mp3.start();
    }

    @Test
    public void testRunExceptionHandling() {
        MP3 mp3 = new MP3("non_existent_file.mp3");
        mp3.start();
    }

    @Test
    public void testRunEdgeCases() {
        MP3 mp3 = new MP3("empty_file.mp3");
        mp3.start();
    }
}