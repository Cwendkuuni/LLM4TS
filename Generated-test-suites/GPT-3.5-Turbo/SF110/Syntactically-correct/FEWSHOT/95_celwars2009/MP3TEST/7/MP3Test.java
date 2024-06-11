import org.junit.Before;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MP3Test {
    private MP3 mp3;

    @Before
    public void setup() {
        mp3 = new MP3("testFile.mp3");
    }

    @Test
    public void testConstructor() {
        assertNotNull(mp3);
    }

    @Test
    public void testRun() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream("This is a test".getBytes());
            AudioInputStream audioInputStream = new AudioInputStream(inputStream, new AudioFormat(44100, 16, 2, true, false), -1);
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File("testFile.wav");
        byte[] expectedData = outputStream.toByteArray();
        byte[] actualData = new byte[expectedData.length];

        try {
            mp3.run();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            audioInputStream.read(actualData);
            audioInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedData, actualData);

        file.delete();
    }
}