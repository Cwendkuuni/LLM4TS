import javax.sound.sampled.*;
import java.io.*;

public class MP3 extends Thread
{
    AudioInputStream in;
    AudioInputStream din;
    String filename;
    
    public MP3(final String filename) {
        this.in = null;
        this.din = null;
        this.filename = "";
        this.filename = filename;
        this.start();
    }
    
    @Override
    public void run() {
        AudioInputStream din = null;
        try {
            final File file = new File(this.filename);
            final AudioInputStream in = AudioSystem.getAudioInputStream(file);
            final AudioFormat baseFormat = in.getFormat();
            final AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);
            final DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
            final SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
            if (line != null) {
                line.open(decodedFormat);
                final byte[] data = new byte[4096];
                line.start();
                int nBytesRead;
                while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
                    line.write(data, 0, nBytesRead);
                }
                line.drain();
                line.stop();
                line.close();
                din.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (din != null) {
                try {
                    din.close();
                }
                catch (IOException ex) {}
            }
        }
    }
}
