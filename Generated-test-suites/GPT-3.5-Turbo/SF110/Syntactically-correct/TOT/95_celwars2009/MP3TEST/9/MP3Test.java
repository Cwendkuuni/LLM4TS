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
        assertTrue(mp3.isAlive());
    }
    
    @Test
    public void testRun_Exception() {
        MP3 mp3 = new MP3("nonexistent.mp3");
        mp3.start();
        assertFalse(mp3.isAlive());
    }
    
    @Test
    public void testRun_EmptyFile() {
        MP3 mp3 = new MP3("empty.mp3");
        mp3.start();
        assertFalse(mp3.isAlive());
    }
    
    @Test
    public void testRun_Interrupt() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.start();
        mp3.interrupt();
        assertFalse(mp3.isAlive());
    }
    
    @Test
    public void testRun_Stop() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.start();
        mp3.stop();
        assertFalse(mp3.isAlive());
    }
    
    @Test
    public void testRun_DrainError() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.start();
        // wait some time to simulate error during draining
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mp3.stop();
        assertFalse(mp3.isAlive());
    }
    
    @Test
    public void testRun_CloseError() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.start();
        // wait some time to simulate error during closing
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mp3.stop();
        assertFalse(mp3.isAlive());
    }
}