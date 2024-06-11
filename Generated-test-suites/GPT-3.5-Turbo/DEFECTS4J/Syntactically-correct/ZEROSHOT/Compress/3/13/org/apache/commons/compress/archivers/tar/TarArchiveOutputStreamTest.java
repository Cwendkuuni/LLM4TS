import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);

        // Creating TarArchiveEntry object
        TarArchiveEntry entry = new TarArchiveEntry("file1.txt");

        try {
            // Putting archive entry
            tarOutputStream.putArchiveEntry(entry);

            // Verifying if the current entry is correct
            assertEquals(entry, tarOutputStream.getCurrentEntry());
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }

    }

    @Test
    public void testCloseArchiveEntry() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);

        // Creating TarArchiveEntry object
        TarArchiveEntry entry = new TarArchiveEntry("file1.txt");

        try {
            // Putting archive entry
            tarOutputStream.putArchiveEntry(entry);

            // Closing archive entry
            tarOutputStream.closeArchiveEntry();

            // Verifying if the current entry is null
            assertNull(tarOutputStream.getCurrentEntry());
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testWrite() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);

        // Creating TarArchiveEntry object
        TarArchiveEntry entry = new TarArchiveEntry("file1.txt");

        // Input data
        byte[] data = "This is a test file.".getBytes();

        try {
            // Putting archive entry
            tarOutputStream.putArchiveEntry(entry);

            // Writing data
            tarOutputStream.write(data);

            // Closing archive entry
            tarOutputStream.closeArchiveEntry();

            // Verifying the size of the entry
            assertEquals(data.length, tarOutputStream.getCurrentEntry().getSize());
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFinish() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);

        // Creating TarArchiveEntry object
        TarArchiveEntry entry = new TarArchiveEntry("file1.txt");

        try {
            // Putting archive entry
            tarOutputStream.putArchiveEntry(entry);

            // Finishing tar archive
            tarOutputStream.finish();

            // Verifying if the current entry is null
            assertNull(tarOutputStream.getCurrentEntry());
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);

        // Creating TarArchiveEntry object
        TarArchiveEntry entry = new TarArchiveEntry("file1.txt");

        try {
            // Putting archive entry
            tarOutputStream.putArchiveEntry(entry);

            // Closing tar archive
            tarOutputStream.close();

            // Verifying if the current entry is null
            assertNull(tarOutputStream.getCurrentEntry());
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);

        try {
            // Setting long file mode
            tarOutputStream.setLongFileMode(1);

            // Verifying if the long file mode is set correctly
            assertEquals(1, tarOutputStream.getLongFileMode());
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);

        // Default record size
        int defaultRecordSize = 512;

        // Verifying the record size
        assertEquals(defaultRecordSize, tarOutputStream.getRecordSize());
    }
}