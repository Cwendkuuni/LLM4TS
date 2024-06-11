import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import javax.sound.sampled.*;
import java.io.IOException;

public class MP3Test {

    //Test case for constructor
    @Test
    public void testConstructor() {
        MP3 mp3 = new MP3("test.mp3");
        assertNotNull(mp3);
    }

    //Test case for run method
    @Test
    public void testRun() {
        try {
            File file = new File("test.mp3");
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, in);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
            SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
            
            MP3 mp3 = new MP3("test.mp3");
            mp3.run();

            assertNotNull(in);
            assertNotNull(baseFormat);
            assertNotNull(decodedFormat);
            assertNotNull(din);
            assertNotNull(info);
            assertNotNull(line);

            line.open(decodedFormat);
            byte[] data = new byte[4096];
            line.start();
            int nBytesRead;
            while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
                line.write(data, 0, nBytesRead);
            }
            line.drain();
            line.stop();
            line.close();
            din.close();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }
}