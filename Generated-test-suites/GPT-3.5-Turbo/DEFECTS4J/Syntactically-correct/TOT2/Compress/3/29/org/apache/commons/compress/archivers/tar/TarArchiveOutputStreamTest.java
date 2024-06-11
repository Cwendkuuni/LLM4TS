import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() {
        // Initialize TarArchiveOutputStream for testing
        tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
    }

    @Test
    public void testSetLongFileMode() {
        // Test setting long file mode to LONGFILE_ERROR
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.longFileMode);

        // Test setting long file mode to LONGFILE_TRUNCATE
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.longFileMode);

        // Test setting long file mode to LONGFILE_GNU
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.longFileMode);
    }

    @Test
    public void testWrite() {
        // Test writing bytes to the current tar archive entry
        byte[] data = { 65, 66, 67, 68, 69 };
        try {
            tarOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
            tarOutputStream.write(data);
            tarOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            fail("IOException occurred while writing data: " + e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        // Test closing an entry
        try {
            tarOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
            tarOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            fail("IOException occurred while closing entry: " + e.getMessage());
        }
    }

    @Test
    public void testFinish() {
        // Test finishing the TAR archive
        try {
            tarOutputStream.finish();
            // Verify that two EOF records were written
            assertEquals(2 * tarOutputStream.getRecordSize(), tarOutputStream.currBytes);
        } catch (IOException e) {
            fail("IOException occurred while finishing TAR archive: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        // Test closing the TAR archive
        try {
            tarOutputStream.close();
            assertTrue(tarOutputStream.closed);
        } catch (IOException e) {
            fail("IOException occurred while closing TAR archive: " + e.getMessage());
        }
    }

    @Test
    public void testGetRecordSize() {
        // Test getting the record size being used by TarBuffer
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() {
        // Test putting an entry on the output stream
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        try {
            tarOutputStream.putArchiveEntry(entry);
            assertEquals(entry.getName(), tarOutputStream.currName);
        } catch (IOException e) {
            fail("IOException occurred while putting archive entry: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        // Test creating a new TarArchiveEntry
        File file = new File("test.txt");
        try {
            ArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(file, file.getName());
            assertTrue(archiveEntry instanceof TarArchiveEntry);
            assertEquals(file.getName(), archiveEntry.getName());
        } catch (IOException e) {
            fail("IOException occurred while creating archive entry: " + e.getMessage());
        }
    }
}