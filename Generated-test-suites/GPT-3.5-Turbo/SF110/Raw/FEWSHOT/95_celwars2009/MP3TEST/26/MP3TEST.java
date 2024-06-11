import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class MP3Test {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Test
    public void testConstructor() {
        final String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);

        assertNotNull(mp3);
        assertEquals(filename, mp3.filename);
    }

    @Test
    public void testRun() {
        final String filename = "test.mp3";
        MP3 mp3 = new MP3(filename);

        // Save original System.out
        System.setOut(new PrintStream(outContent));

        // Execute the run method
        mp3.run();

        // Restore original System.out
        System.setOut(originalOut);

        // Verify output
        assertEquals("Exception occurred: java.io.FileNotFoundException\r\n", outContent.toString());
    }

    @Test
    public void testRun_SampleMP3File() {
        final String filename = "sample.mp3";
        MP3 mp3 = new MP3(filename);

        // Save original System.out
        System.setOut(new PrintStream(outContent));

        // Execute the run method
        mp3.run();

        // Restore original System.out
        System.setOut(originalOut);

        // Verify no exception occurred
        assertEquals("", outContent.toString());
    }
}