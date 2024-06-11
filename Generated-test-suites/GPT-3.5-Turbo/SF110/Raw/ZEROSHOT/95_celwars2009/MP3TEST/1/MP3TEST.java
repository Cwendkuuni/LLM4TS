import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import static org.junit.Assert.*;

public class MP3Test {

    private MP3 mp3;

    @Before
    public void setUp() {
        mp3 = new MP3("test.mp3");
    }

    @Test
    public void testConstructor() {
        assertNotNull(mp3);
    }

    @Test
    public void testRun() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        mp3.run();

        assertEquals("java.io.FileNotFoundException: test.mp3 (No such file or directory)\n", outContent.toString());
    }
}