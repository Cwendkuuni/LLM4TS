import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() {
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(100);
        try {
            tOut.putArchiveEntry(entry);
        } catch (Exception e) {
            fail("putArchiveEntry should not throw an exception");
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(100);
        try {
            tOut.putArchiveEntry(entry);
            tOut.closeArchiveEntry();
        } catch (Exception e) {
            fail("closeArchiveEntry should not throw an exception");
        }
    }

    @Test
    public void testWrite() {
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(100);
        try {
            tOut.putArchiveEntry(entry);
            tOut.write("test data".getBytes());
            tOut.closeArchiveEntry();
        } catch (Exception e) {
            fail("write should not throw an exception");
        }
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        try {
            tOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        } catch (Exception e) {
            fail("setLongFileMode should not throw an exception");
        }
    }

    @Test
    public void testFinish() {
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        try {
            tOut.finish();
        } catch (Exception e) {
            fail("finish should not throw an exception");
        }
    }

    @Test
    public void testClose() {
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        try {
            tOut.close();
        } catch (Exception e) {
            fail("close should not throw an exception");
        }
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tOut.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry() {
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String entryName = "test.txt";
        File inputFile = new File("test.txt");
        try {
            TarArchiveEntry entry = (TarArchiveEntry) tOut.createArchiveEntry(inputFile, entryName);
            assertEquals(entryName, entry.getName());
        } catch (Exception e) {
            fail("createArchiveEntry should not throw an exception");
        }
    }
}