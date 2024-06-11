package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import org.junit.Before;
import org.junit.Test;

public class MP3Test {
    
    private MP3 mp3;
    
    @Before
    public void setUp() {
        mp3 = new MP3("test.mp3");
    }
    
    @Test
    public void testMP3() {
        assertNotNull(mp3);
    }
    
    @Test
    public void testRun() throws IOException {
        File testFile = new File("test.mp3");
        byte[] testData = Files.readAllBytes(testFile.toPath());
        
        // Create input and output audio streams
        AudioInputStream in = AudioSystem.getAudioInputStream(testFile);
        AudioFormat baseFormat = in.getFormat();
        AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
        AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, in);
        
        // Create data line info and source data line
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
        
        // Test opening line
        line.open(decodedFormat);
        assertEquals(decodedFormat, line.getFormat());
        
        // Test writing and reading data
        byte[] data = new byte[4096];
        line.start();
        int nBytesRead;
        while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
            line.write(data, 0, nBytesRead);
        }
        
        // Test line drain, stop, and close
        line.drain();
        line.stop();
        line.close();
        
        // Test stream closing
        in.close();
        din.close();
        
        // Test file deletion
        Files.deleteIfExists(testFile.toPath());
        assertNull(testData);
    }
}