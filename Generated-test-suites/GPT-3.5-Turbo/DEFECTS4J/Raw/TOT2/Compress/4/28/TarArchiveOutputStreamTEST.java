import static org.junit.Assert.*;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        // Test constructor with default block size and record size
        TarArchiveOutputStream t = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNotNull(t);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new ByteArrayOutputStream());
        t.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(t.LONGFILE_ERROR, t.longFileMode);
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, t.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() {
        // Test putting an archive entry
        TarArchiveOutputStream t = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry("testfile.txt");
        try {
            t.putArchiveEntry(entry);
        } catch (IOException e) {
            fail("Exception thrown when putting an archive entry");
        }
    }

    @Test
    public void testWrite() {
        // Test writing to the archive
        TarArchiveOutputStream t = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] data = "Test data".getBytes();
        try {
            t.write(data, 0, data.length);
        } catch (IOException e) {
            fail("Exception thrown when writing to the archive");
        }
    }

    @Test
    public void testWriteEOFRecord() {
        // Test writing EOF record
        TarArchiveOutputStream t = new TarArchiveOutputStream(new ByteArrayOutputStream());
        try {
            t.writeEOFRecord();
        } catch (IOException e) {
            fail("Exception thrown when writing EOF record");
        }
    }

    @Test
    public void testFinish() {
        // Test finishing the archive
        TarArchiveOutputStream t = new TarArchiveOutputStream(new ByteArrayOutputStream());
        try {
            t.finish();
        } catch (IOException e) {
            fail("Exception thrown when finishing the archive");
        }
    }
}