import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MP3Test {

    private MP3 mp3;

    @Before
    public void setUp() {
        mp3 = new MP3("test.mp3"); // Assuming test.mp3 exists for testing purposes
    }

    @Test
    public void testConstructor() {
        assertNotNull(mp3);
        assertEquals("test.mp3", mp3.filename);
    }

    @Test
    public void testRunPlayback() throws InterruptedException, UnsupportedAudioFileException {
        // Redirecting System.out to capture printing during the run method
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Run the thread
        mp3.run();

        // Wait for the thread to finish
        mp3.join();

        // Assert that the thread ran without any exceptions
        assertTrue(outContent.toString().isEmpty());

        // Restore System.out
        System.setOut(originalOut);
    }

    @Test
    public void testRunInvalidFile() {
        // Redirecting System.err to capture error printing during the run method
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));

        // Creating a new MP3 object with a nonexistent file
        MP3 mp3Invalid = new MP3("nonexistent.mp3");

        // Run the thread
        mp3Invalid.run();

        // Wait for the thread to finish
        mp3Invalid.join();

        // Assert that the thread printed an error message to stderr
        assertTrue(errContent.toString().contains("java.io.FileNotFoundException"));

        // Restore System.err
        System.setErr(originalErr);
    }

    @Test
    public void testRunUnsupportedFile() {
        // Redirecting System.err to capture error printing during the run method
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));

        // Creating a new MP3 object with an unsupported audio file
        // In this case, a text file is used instead of an audio file
        MP3 mp3Unsupported = new MP3("unsupported.txt");

        // Run the thread
        mp3Unsupported.run();

        // Wait for the thread to finish
        mp3Unsupported.join();

        // Assert that the thread printed an error message to stderr
        assertTrue(errContent.toString().contains("javax.sound.sampled.UnsupportedAudioFileException"));

        // Restore System.err
        System.setErr(originalErr);
    }

    @Test
    public void testRunInterrupted() throws InterruptedException {
        // Interrupt the thread before it starts running
        mp3.interrupt();

        // Run the thread
        mp3.run();

        // Wait for the thread to finish
        mp3.join();

        // Assert that the thread threw an InterruptedException
        assertTrue(Thread.currentThread().isInterrupted());
    }
}