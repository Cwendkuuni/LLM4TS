import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

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
        // Give some time for the thread to execute
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(mp3.isAlive());
    }

    @Test
    public void testRunWithInvalidFile() {
        MP3 mp3 = new MP3("invalid.mp3");
        mp3.start();
        // Give some time for the thread to execute
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(mp3.isAlive());
    }

    @Test
    public void testRunWithEmptyFile() {
        MP3 mp3 = new MP3("empty.mp3");
        mp3.start();
        // Give some time for the thread to execute
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(mp3.isAlive());
    }

    @Test
    public void testRunWithLargeFile() {
        MP3 mp3 = new MP3("large.mp3");
        mp3.start();
        // Give some time for the thread to execute
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(mp3.isAlive());
    }

    @Test
    public void testRunWithCorruptedFile() {
        MP3 mp3 = new MP3("corrupted.mp3");
        mp3.start();
        // Give some time for the thread to execute
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(mp3.isAlive());
    }

}