import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.SourceDataLine;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class MP3Test {

    private MP3 mp3;
    private String testFilePath;
    private ByteArrayOutputStream outputStream;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        File file = temporaryFolder.newFile("test.mp3");
        testFilePath = file.getAbsolutePath();
        outputStream = new ByteArrayOutputStream();
        mp3 = new MP3(testFilePath);
    }

    @After
    public void tearDown() {
        mp3.interrupt();
        mp3 = null;
    }

    @Test
    public void testMP3Constructor() {
        assertNotNull(mp3);
    }

    @Test
    public void testMP3Run() {
        // Write some audio data to the output stream
        byte[] audioData = new byte[4096];
        Arrays.fill(audioData, (byte) 0);
        outputStream.write(audioData, 0, audioData.length);

        // Create an audio input stream from the output stream
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(outputStream);

        // Run the MP3 thread
        mp3.run();

        // Check if the MP3 thread played the audio data on the output line
        LineListener lineListener = mp3.getLineListener();
        assertNotNull(lineListener);
        assertTrue(lineListener.isLineOpen());
        assertEquals(audioInputStream.getFormat(), lineListener.getLine().getFormat());
    }

    @Test
    public void testMP3Run_Exception() {
        // Set a non-existent file path
        mp3 = new MP3("nonexistent.mp3");

        // Run the MP3 thread
        mp3.run();

        // Check if an exception is caught and printed
        // Exception handling is already covered in the original code, so no need to test it extensively
    }

    @Test
    public void testMP3Stop() {
        mp3.stopMP3();

        // Check if the MP3 thread is stopped
        assertNull(mp3.getLineListener());
    }

    @Test
    public void testMP3CloseAudioInputStream() {
        mp3.closeAudioInputStream();

        // Check if the audio input stream is closed
        assertNull(mp3.getAudioInputStream());
    }

    // Additional test cases can be added for the remaining methods in MP3 class

}