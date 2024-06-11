import javax.sound.sampled.*;
import java.io.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MP3Test {

    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertEquals("test.mp3", mp3.filename);
        assertEquals(null, mp3.in);
        assertEquals(null, mp3.din);
    }

    @Test
    public void testRun() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();
        // Add assertions to verify the functionality of the run() method
    }


    // Add test cases to comprehensively test all methods in the MP3 class

}