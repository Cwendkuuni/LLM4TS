
import org.junit.Test;
import static org.junit.Assert.*;
import javax.sound.sampled.*;

public class MP3Test {
    
    @Test
    public void testConstructor() {
        // Typical use case
        MP3 mp3 = new MP3("song.mp3");
        assertNotNull(mp3);
        
        // Edge case: empty file name
        MP3 mp3Empty = new MP3("");
        assertNotNull(mp3Empty);
        
        // Error scenario: non-existent file
        MP3 mp3NonExistent = new MP3("invalid.mp3");
        assertNotNull(mp3NonExistent);
    }
    
    @Test
    public void testRun() {
        // Typical use case: playing a valid MP3 file
        MP3 mp3 = new MP3("song.mp3");
        mp3.start();
        try {
            Thread.sleep(2000); // Allow some time for the sound to play
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(mp3.isAlive()); // Thread should still be running
        
        // Edge case: playing an empty MP3 file
        MP3 mp3Empty = new MP3("");
        mp3Empty.start();
        assertFalse(mp3Empty.isAlive()); // Thread should finish immediately
        
        // Error scenario: playing non-existent MP3 file
        MP3 mp3NonExistent = new MP3("invalid.mp3");
        mp3NonExistent.start();
        assertFalse(mp3NonExistent.isAlive()); // Thread should finish immediately
    }
    
}
