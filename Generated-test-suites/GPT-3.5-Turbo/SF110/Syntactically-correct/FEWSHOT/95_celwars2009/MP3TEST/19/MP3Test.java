import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class MP3Test {
    
    @Test
    public void testRun() {
        MP3 mp3 = new MP3("test.mp3");
        
        File file = new File(mp3.filename);
        AudioFormat baseFormat;
        AudioFormat decodedFormat;
        AudioInputStream in;
        AudioInputStream din;
        DataLine.Info info;
        SourceDataLine line;
        byte[] data = new byte[4096];
        int nBytesRead;
        
        try {
            in = AudioSystem.getAudioInputStream(file);
            baseFormat = in.getFormat();
            decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);
            info = new DataLine.Info(SourceDataLine.class, decodedFormat);
            line = (SourceDataLine)AudioSystem.getLine(info);
            
            line.open(decodedFormat);
            line.start();
            
            while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
                line.write(data, 0, nBytesRead);
            }
            
            line.drain();
            line.stop();
            line.close();
            
            din.close();
            
            assertNull(mp3.din);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mp3.din != null) {
                try {
                    mp3.din.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}