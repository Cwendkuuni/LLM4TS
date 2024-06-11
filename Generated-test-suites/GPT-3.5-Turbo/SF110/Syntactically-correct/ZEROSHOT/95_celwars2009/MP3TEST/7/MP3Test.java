import static org.junit.Assert.*;
import org.junit.Test;

public class MP3Test {
    
    @Test
    public void testMP3() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
    }
    
    @Test
    public void testRun() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();
        assertTrue(mp3.isAlive());
    }
    
    @Test
    public void testClosedStream() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();
        assertTrue(mp3.isAlive());
        mp3.closeStream();
        assertFalse(mp3.isAlive());
    }
    
    @Test
    public void testReadData() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();
        assertTrue(mp3.isAlive());
        byte[] data = mp3.readData();
        assertNotNull(data);
    }
    
    @Test
    public void testWriteData() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();
        assertTrue(mp3.isAlive());
        byte[] data = new byte[4096];
        mp3.writeData(data);
        // Add assertion for proper data writing
    }
    
    @Test
    public void testStopPlaying() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();
        assertTrue(mp3.isAlive());
        mp3.stopPlaying();
        assertFalse(mp3.isAlive());
    }
    
    @Test(expected = Exception.class)
    public void testExceptionHandling() {
        MP3 mp3 = new MP3("nonexistent.mp3");
        mp3.run();
        // Add assertion for proper exception handling
    }
}