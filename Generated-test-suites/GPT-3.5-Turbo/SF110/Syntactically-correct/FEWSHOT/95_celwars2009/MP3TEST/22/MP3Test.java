import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import javax.sound.sampled.*;
import org.junit.Test;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MP3Test {

    private static final String TEST_FILE_PATH = "test.mp3";

    @Test
    public void testConstructor() {
        String filename = TEST_FILE_PATH;
        MP3 mp3 = new MP3(filename);

        assertNotNull(mp3);
        assertEquals(filename, getFilename(mp3));
    }

    @Test
    public void testRun() {
        MP3 mp3 = new MP3(TEST_FILE_PATH);

        runThreadAndWait(mp3);

        // Verify that the audio was played without any exceptions
        assertNull(mp3.getException());
    }

    @Test
    public void testRunInvalidFile() {
        String invalidFilename = "invalid.mp3";
        MP3 mp3 = new MP3(invalidFilename);

        runThreadAndWait(mp3);

        // Verify that an exception occurred indicating an invalid file
        assertNotNull(mp3.getException());
    }

    @Test
    public void testGetFilename() {
        String filename = TEST_FILE_PATH;
        MP3 mp3 = new MP3(filename);

        assertEquals(filename, mp3.getFilename());
    }

    @Test
    public void testSetFilename() {
        String filename = TEST_FILE_PATH;
        MP3 mp3 = new MP3("");

        mp3.setFilename(filename);

        assertEquals(filename, mp3.getFilename());
    }

    @Test
    public void testOpenLine() throws Exception {
        MP3 mp3 = new MP3(TEST_FILE_PATH);

        AudioFormat baseFormat = (AudioFormat) getField(mp3, "baseFormat");
        AudioFormat decodedFormat = (AudioFormat) getField(mp3, "decodedFormat");
        SourceDataLine line = (SourceDataLine) getField(mp3, "line");

        mp3.openLine();

        assertNotNull(line.getLineInfo());
        assertEquals(decodedFormat, line.getFormat());
        assertEquals(AudioSystem.isLineSupported(line.getLineInfo()), line.isOpen());
    }

    @Test
    public void testReadAndWriteData() throws Exception {
        MP3 mp3 = new MP3(TEST_FILE_PATH);

        AudioInputStream din = (AudioInputStream) getField(mp3, "din");
        SourceDataLine line = (SourceDataLine) getField(mp3, "line");

        byte[] data = new byte[4096];
        int nBytesRead;

        // Read and write some data
        while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
            line.write(data, 0, nBytesRead);
        }

        // Verify that data was written to the line
        assertEquals(din.available(), 0);
    }

    @Test
    public void testCloseLine() throws Exception {
        MP3 mp3 = new MP3(TEST_FILE_PATH);

        SourceDataLine line = (SourceDataLine) getField(mp3, "line");

        mp3.closeLine();

        // Verify that the line is closed
        assertFalse(line.isOpen());
    }

    @Test
    public void testCloseStreams() throws Exception {
        MP3 mp3 = new MP3(TEST_FILE_PATH);

        AudioInputStream din = (AudioInputStream) getField(mp3, "din");

        mp3.closeStreams();

        // Verify that the streams are closed
        assertEquals(din.available(), 0);
    }

    @Test
    public void testGetException() {
        MP3 mp3 = new MP3(TEST_FILE_PATH);

        assertNull(mp3.getException());
    }

    private String getFilename(MP3 mp3) {
        try {
            Field filenameField = MP3.class.getDeclaredField("filename");
            filenameField.setAccessible(true);
            return (String) filenameField.get(mp3);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private Object getField(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void runThreadAndWait(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}