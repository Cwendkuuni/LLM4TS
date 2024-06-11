import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() {
        tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
    }

    @After
    public void tearDown() {
        try {
            tarOutputStream.close();
        } catch (IOException e) {
            fail("Error closing TarArchiveOutputStream: " + e.getMessage());
        }
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(10);

        try {
            tarOutputStream.putArchiveEntry(entry);
        } catch (IOException e) {
            fail("Error putting archive entry: " + e.getMessage());
        }

        assertEquals(0, tarOutputStream.getCurrentSize());
        assertEquals("file.txt", tarOutputStream.getCurrentName());
        assertEquals(10, tarOutputStream.getCurrentBytes());
        assertEquals(true, tarOutputStream.hasUnclosedEntry());
    }

    @Test
    public void testCloseArchiveEntry() {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(10);

        try {
            tarOutputStream.putArchiveEntry(entry);
            tarOutputStream.write(new byte[10]);
            tarOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            fail("Error closing archive entry: " + e.getMessage());
        }

        assertEquals(10, tarOutputStream.getCurrentBytes());
        assertEquals(false, tarOutputStream.hasUnclosedEntry());
    }

    @Test
    public void testWrite() {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(10);

        try {
            tarOutputStream.putArchiveEntry(entry);
            tarOutputStream.write(new byte[5]);
            tarOutputStream.write(new byte[5]);
            tarOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            fail("Error writing to archive entry: " + e.getMessage());
        }

        assertEquals(10, tarOutputStream.getCurrentBytes());
    }

    @Test
    public void testWriteExceedsSize() {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(10);

        try {
            tarOutputStream.putArchiveEntry(entry);
            tarOutputStream.write(new byte[11]);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("request to write '11' bytes exceeds size in header of '10' bytes for entry 'file.txt'", e.getMessage());
        }
    }

    @Test
    public void testFinishNoUnclosedEntry() {
        try {
            tarOutputStream.finish();
        } catch (IOException e) {
            fail("Error finishing tar output stream: " + e.getMessage());
        }
    }

    @Test
    public void testFinishWithUnclosedEntry() {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(10);

        try {
            tarOutputStream.putArchiveEntry(entry);
            tarOutputStream.finish();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("This archives contains unclosed entries.", e.getMessage());
        }
    }

    @Test
    public void testClose() {
        try {
            tarOutputStream.close();
            assertEquals(true, tarOutputStream.isClosed());
        } catch (IOException e) {
            fail("Error closing tar output stream: " + e.getMessage());
        }
    }

    @Test
    public void testCloseAlreadyClosed() {
        try {
            tarOutputStream.close();
            tarOutputStream.close();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals(true, tarOutputStream.isClosed());
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        File inputFile = new File("file.txt");
        String entryName = "file.txt";

        try {
            TarArchiveEntry entry = (TarArchiveEntry) tarOutputStream.createArchiveEntry(inputFile, entryName);
            assertEquals(inputFile, entry.getFile());
            assertEquals(entryName, entry.getName());
        } catch (IOException e) {
            fail("Error creating archive entry: " + e.getMessage());
        }
    }
}