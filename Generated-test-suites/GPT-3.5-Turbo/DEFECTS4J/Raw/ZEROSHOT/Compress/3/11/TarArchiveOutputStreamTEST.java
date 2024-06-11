import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        tarArchiveOutputStream = new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(2);
        assertEquals(2, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        ArchiveEntry archiveEntry = (ArchiveEntry) entry;
        try {
            tarArchiveOutputStream.putArchiveEntry(archiveEntry);
            assertEquals(entry, tarArchiveOutputStream.getArchiveEntry());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        ArchiveEntry archiveEntry = (ArchiveEntry) entry;
        try {
            tarArchiveOutputStream.putArchiveEntry(archiveEntry);
            tarArchiveOutputStream.closeArchiveEntry();
            assertNull(tarArchiveOutputStream.getArchiveEntry());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testWrite() {
        byte[] buf = new byte[] {0x00, 0x01};
        try {
            tarArchiveOutputStream.write(buf, 0, buf.length);
            assertEquals(buf.length, tarArchiveOutputStream.getBytesWritten());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testFinish() {
        try {
            tarArchiveOutputStream.finish();
            assertTrue(tarArchiveOutputStream.getFinished());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        try {
            tarArchiveOutputStream.close();
            assertTrue(tarArchiveOutputStream.isClosed());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        File inputFile = new File("test.txt");
        String entryName = "file.txt";
        try {
            ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
            assertTrue(archiveEntry instanceof TarArchiveEntry);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

}