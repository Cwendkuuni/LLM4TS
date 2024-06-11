import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class MP3Test {

    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");

        assertNotNull(mp3);
        assertEquals("test.mp3", mp3.filename);
        assertEquals(null, mp3.in);
        assertEquals(null, mp3.din);
        assertEquals(Thread.State.NEW, mp3.getState());
    }

    @Test
    public void testRun() {
        try {
            // Prepare test file
            File testFile = new File("test.mp3");
            AudioSystem.write(new AudioInputStream(new ByteArrayInputStream(new byte[4096]), new AudioFormat(44100, 16, 2, true, false), 4096), AudioFileFormat.Type.WAVE, testFile);

            // Run MP3 thread
            MP3 mp3 = new MP3("test.mp3");
            mp3.start();
            mp3.join();

            // Assert
            assertNotNull(mp3.in);
            assertNotNull(mp3.din);
        } catch (Exception e) {
            assertNull(e);
        } finally {
            // Clean up test file
            File testFile = new File("test.mp3");
            testFile.delete();
        }
    }
}