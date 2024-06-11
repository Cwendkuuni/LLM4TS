import javax.sound.sampled.*;
import java.io.*;
import org.junit.Test;
import static org.junit.Assert.assertNull;

public class MP3Test {

    @Test
    public void testRun() {
        MP3 mp3 = new MP3("test.mp3");
    }
    
    @Test
    public void testRun_Exception() {
        MP3 mp3 = new MP3(null);
    }

    @Test
    public void testRun_AudioInputStream() {
        AudioInputStream in = null;
        MP3 mp3 = new MP3("");
        in = mp3.in;
        assertNull(in);
    }

    @Test
    public void testRun_DataLine() {
        SourceDataLine line = null;
        MP3 mp3 = new MP3("");
        try {
            final AudioInputStream in = AudioSystem.getAudioInputStream(new File(mp3.filename));
            final AudioFormat baseFormat = in.getFormat();
            final AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            final AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, in);
            final DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
            line = (SourceDataLine) AudioSystem.getLine(info);
            mp3.din = din;
            if (line != null) {
                line.open(decodedFormat);
                final byte[] data = new byte[4096];
                line.start();
                int nBytesRead;
                while ((nBytesRead = mp3.din.read(data, 0, data.length)) != -1) {
                    line.write(data, 0, nBytesRead);
                }
                line.drain();
                line.stop();
                line.close();
                mp3.din.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (line != null) {
                line.close();
            }
            if (mp3.din != null) {
                try {
                    mp3.din.close();
                } catch (IOException ex) {
                }
            }
        }
    }
}