import javax.sound.sampled.*;
import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MP3Test {
    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
    }
    
    @Test
    public void testRun() {
        MP3 mp3 = new MP3("test.mp3");
        mp3.run();
    }
    
    @Test
    public void testRunWithInvalidFilePath() {
        MP3 mp3 = new MP3("invalid.mp3");
        mp3.run();
    }
    
    @Test
    public void testRunWithNullFilePath() {
        MP3 mp3 = new MP3(null);
        mp3.run();
    }
}