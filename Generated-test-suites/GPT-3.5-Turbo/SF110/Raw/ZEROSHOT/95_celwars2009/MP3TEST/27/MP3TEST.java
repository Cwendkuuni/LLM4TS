import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.sound.sampled.*;
import java.io.*;

public class MP3Test {
    private String filename;
    private MP3 mp3;
    
    @Before
    public void setUp() {
        filename = "test.mp3";
        mp3 = new MP3(filename);
    }
    
    @Test
    public void testRun() {
        // Test if the run method executes without any errors
        mp3.run();
    }
    
    @Test
    public void testConstructor() {
        // Test if the constructor initializes the instance variables correctly
        Assert.assertEquals(filename, mp3.filename);
        Assert.assertNull(mp3.in);
        Assert.assertNull(mp3.din);
    }
    
    @Test
    public void testOpenAndCloseLine() {
        // Test if the SourceDataLine opens and closes correctly
        SourceDataLine line = null;
        AudioFormat decodedFormat = null;
        try {
            File file = new File(filename);
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            AudioFormat baseFormat = in.getFormat();
            decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
                                            baseFormat.getChannels(), baseFormat.getChannels() * 2,
                                            baseFormat.getSampleRate(), false);
            AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, in);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
            line = (SourceDataLine)AudioSystem.getLine(info);
            line.open(decodedFormat);
            
            Assert.assertTrue(line.isOpen());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (line != null) {
                line.close();
                Assert.assertFalse(line.isOpen());
            }
        }
    }
    
    @Test
    public void testReadAndWrite() {
        // Test if the audio data is read and written correctly
        AudioInputStream din = null;
        try {
            File file = new File(filename);
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
                                                        baseFormat.getChannels(), baseFormat.getChannels() * 2,
                                                        baseFormat.getSampleRate(), false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);
            byte[] data = new byte[4096];
            int nBytesRead;
            while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
                Assert.assertTrue(nBytesRead > 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (din != null) {
                try {
                    din.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}