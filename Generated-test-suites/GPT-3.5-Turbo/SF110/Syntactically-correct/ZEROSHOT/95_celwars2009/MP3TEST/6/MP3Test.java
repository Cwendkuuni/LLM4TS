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
        mp3.start();
        try {
            Thread.sleep(1000); // Wait for the thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(mp3.isAlive());
    }

    @Test
    public void testRunWithDataLineNotNull() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.start();
        try {
            Thread.sleep(1000); // Wait for the thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotNull(mp3.line);
    }

    // Add more test methods to cover all functionalities of the MP3 class

}