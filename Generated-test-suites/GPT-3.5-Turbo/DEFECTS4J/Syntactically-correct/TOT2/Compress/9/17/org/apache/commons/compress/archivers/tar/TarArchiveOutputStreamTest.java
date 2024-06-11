import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tarOutputStream;
    
    @Before
    public void setUp() {
        // Initialize TarArchiveOutputStream for testing
        tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
    }
    
    @After
    public void tearDown() {
        // Clean up resources
        try {
            tarOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testPutArchiveEntry() {
        // Test putting a file entry into the archive
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        try {
            tarOutputStream.putArchiveEntry(entry);
            tarOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            fail("Exception thrown while putting archive entry: " + e.getMessage());
        }
    }
    
    @Test
    public void testCloseArchiveEntry() {
        // Test closing an archive entry
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        try {
            tarOutputStream.putArchiveEntry(entry);
            tarOutputStream.closeArchiveEntry();
            assertFalse("Expected no unclosed entry after closing", tarOutputStream.haveUnclosedEntry);
        } catch (IOException e) {
            fail("Exception thrown while closing archive entry: " + e.getMessage());
        }
    }
    
    @Test
    public void testWrite() {
        // Test writing bytes to the current archive entry
        byte[] testData = new byte[] {1, 2, 3, 4, 5};
        try {
            tarOutputStream.putArchiveEntry(new TarArchiveEntry("testFile.txt"));
            tarOutputStream.write(testData);
            tarOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            fail("Exception thrown while writing bytes: " + e.getMessage());
        }
    }
    
    @Test
    public void testSetLongFileMode() {
        // Test setting the long file mode
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals("Expected long file mode to be set to TRUNCATE", TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.longFileMode);
    }
    
    @Test
    public void testGetRecordSize() {
        // Test getting the record size used by the TarBuffer
        assertEquals("Expected default record size of TarBuffer", TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }
    
    @Test
    public void testFinish() {
        // Test finishing the TAR archive
        try {
            tarOutputStream.finish();
            assertTrue("Expected archive to be finished", tarOutputStream.finished);
        } catch (IOException e) {
            fail("Exception thrown while finishing archive: " + e.getMessage());
        }
    }
    
    @Test
    public void testClose() {
        // Test closing the TAR archive
        try {
            tarOutputStream.close();
            assertTrue("Expected archive to be closed", tarOutputStream.closed);
        } catch (IOException e) {
            fail("Exception thrown while closing archive: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveEntry() {
        // Test creating a TarArchiveEntry
        File file = new File("testFile.txt");
        try {
            ArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(file, "testFile.txt");
            assertTrue("Expected created entry to be a TarArchiveEntry", archiveEntry instanceof TarArchiveEntry);
        } catch (IOException e) {
            fail("Exception thrown while creating archive entry: " + e.getMessage());
        }
    }
}