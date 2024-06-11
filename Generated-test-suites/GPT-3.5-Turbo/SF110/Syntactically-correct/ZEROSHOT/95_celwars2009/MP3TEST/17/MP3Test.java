import static org.junit.Assert.*;
import org.junit.Test;

public class MP3Test {
    
    @Test
    public void testRun() {
        // Test if the audio file is successfully loaded and played
        MP3 mp3 = new MP3("test_audio.mp3");
        mp3.run();
        // Add assertions here
        
        // Test if an exception is thrown when the filename is invalid
        MP3 invalidMP3 = new MP3("invalid_file.mp3");
        invalidMP3.run();
        // Add assertions here
    }
}