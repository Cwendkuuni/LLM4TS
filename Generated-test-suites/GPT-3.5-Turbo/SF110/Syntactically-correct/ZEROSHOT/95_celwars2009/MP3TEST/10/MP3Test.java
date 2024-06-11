import org.junit.Test;
import static org.junit.Assert.*;

public class MP3Test {

    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
        assertTrue(mp3.isAlive());
    }

    @Test
    public void testRun() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.start();
        try {
            Thread.sleep(1000); // wait for the thread to finish running
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(mp3.isAlive());
    }

    @Test
    public void testReadFile() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.start();
        try {
            Thread.sleep(1000); // wait for the thread to finish running
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // verify that the file was successfully read
        // insert your assertions here
    }

    @Test
    public void testCloseStreams() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.start();
        try {
            Thread.sleep(1000); // wait for the thread to finish running
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // verify that the audio streams were successfully closed
        // insert your assertions here
    }
}