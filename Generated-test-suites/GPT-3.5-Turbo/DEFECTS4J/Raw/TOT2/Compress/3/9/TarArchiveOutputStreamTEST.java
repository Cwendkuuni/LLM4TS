import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructorWithDefaultBlockSizeAndRecordSize() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(os);

        assertNotNull(tar);
    }

    @Test
    public void testSetLongFileMode() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(os);
        tar.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tar.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(os);

        try {
            tar.putArchiveEntry(new TarArchiveEntry("test.txt"));
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(os);

        try {
            tar.putArchiveEntry(new TarArchiveEntry("test.txt"));
            tar.write("Test".getBytes());
            tar.closeArchiveEntry();
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testWrite() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(os);

        try {
            tar.putArchiveEntry(new TarArchiveEntry("test.txt"));
            tar.write("Test".getBytes());
            tar.closeArchiveEntry();
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testFinish() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(os);

        try {
            tar.finish();
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testClose() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(os);

        try {
            tar.close();
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

}