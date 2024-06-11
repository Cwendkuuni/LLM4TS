import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MP3Test {

    private MP3 mp3;

    @Before
    public void setUp() {
        mp3 = new MP3("test.mp3");
    }

    @Test
    public void testRun() {
        mp3.run();
    }
    
    @Test
    public void testFilename() {
        assertEquals("test.mp3", mp3.filename);
    }
    
    @Test
    public void testStart() {
        mp3.start();
        assertTrue(mp3.isAlive());
    }
}