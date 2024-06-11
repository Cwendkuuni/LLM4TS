import static org.junit.Assert.*;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        tar.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tar.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() {
        // Test putting a regular file entry
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        try {
            tar.putArchiveEntry(entry);
            tar.closeArchiveEntry();
        } catch (IOException e) {
            fail("Exception thrown while putting archive entry");
        }

        // Test putting a directory entry
        TarArchiveOutputStream tar2 = new TarArchiveOutputStream(null);
        ArchiveEntry entry2 = new TarArchiveEntry("testDir/", true);
        try {
            tar2.putArchiveEntry(entry2);
            tar2.closeArchiveEntry();
        } catch (IOException e) {
            fail("Exception thrown while putting archive entry");
        }
    }

    @Test
    public void testWrite() {
        byte[] data = "Test Data".getBytes();
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        try {
            tar.write(data, 0, data.length);
        } catch (IOException e) {
            fail("Exception thrown while writing data");
        }
    }

    @Test
    public void testFinish() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        try {
            tar.finish();
            assertTrue(tar.isFinished());
        } catch (IOException e) {
            fail("Exception thrown while finishing TarArchive");
        }
    }

    @Test
    public void testClose() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        try {
            tar.close();
            assertTrue(tar.isClosed());
        } catch (IOException e) {
            fail("Exception thrown while closing TarArchiveOutputStream");
        }
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tar.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        File file = new File("test.txt");
        try {
            ArchiveEntry entry = tar.createArchiveEntry(file, "test.txt");
            assertTrue(entry instanceof TarArchiveEntry);
        } catch (IOException e) {
            fail("Exception thrown while creating archive entry");
        }
    }

}