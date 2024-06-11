import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);

        assertNotNull(tao);
    }

    @Test
    public void testSetLongFileMode() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        tao.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tao.longFileMode);
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        byte[] data = "Test Data".getBytes();
        tao.putArchiveEntry(new TarArchiveEntry("testFile.txt"));
        tao.write(data, 0, data.length);
        tao.closeArchiveEntry();
        tao.close();

        // Check if data was written
        // This is a basic test case, further edge cases can be added
        // to test different scenarios
        // For example, writing data larger than the block size
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tao.putArchiveEntry(entry);

        assertEquals(true, tao.haveUnclosedEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tao.putArchiveEntry(entry);
        tao.closeArchiveEntry();

        assertEquals(false, tao.haveUnclosedEntry);
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);

        // Add some entries
        tao.putArchiveEntry(new TarArchiveEntry("testFile1.txt"));
        tao.closeArchiveEntry();
        tao.putArchiveEntry(new TarArchiveEntry("testFile2.txt"));
        tao.closeArchiveEntry();

        tao.finish();

        assertEquals(true, tao.finished);
    }

    @Test
    public void testClose() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        tao.close();

        assertEquals(true, tao.closed);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        File inputFile = new File("testFile.txt");
        ArchiveEntry entry = tao.createArchiveEntry(inputFile, "testFile.txt");

        assertNotNull(entry);
        assertEquals(TarArchiveEntry.class, entry.getClass());
    }
}