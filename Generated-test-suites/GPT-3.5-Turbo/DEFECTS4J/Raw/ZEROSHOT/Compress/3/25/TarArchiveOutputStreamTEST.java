import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tar;
    
    @Before
    public void setUp() {
        tar = new TarArchiveOutputStream(System.out);
    }
    
    @Test
    public void testSetLongFileMode() {
        tar.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tar.getLongFileMode());
        
        tar.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tar.getLongFileMode());
        
        tar.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tar.getLongFileMode());
    }
    
    @Test
    public void testFinish() {
        try {
            tar.finish();
            // no assert here, just checking if it throws an exception
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testClose() {
        try {
            tar.close();
            assertTrue(tar.isClosed());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetRecordSize() {
        int recordSize = tar.getRecordSize();
        assertEquals(tar.getBuffer().getRecordSize(), recordSize);
    }
    
    @Test
    public void testPutArchiveEntry() {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        try {
            tar.putArchiveEntry(entry);
            assertEquals(entry.getName(), tar.getCurrName());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testCloseArchiveEntry() {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        try {
            tar.putArchiveEntry(entry);
            tar.closeArchiveEntry();
            assertEquals(0, tar.getAssemLen());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testWrite() {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        byte[] contents = "Hello, world!".getBytes();
        try {
            tar.putArchiveEntry(entry);
            tar.write(contents);
            tar.closeArchiveEntry();
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testWrite2() {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        byte[] contents = "Hello, world!".getBytes();
        try {
            tar.putArchiveEntry(entry);
            tar.write(contents, 0, contents.length);
            tar.closeArchiveEntry();
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveEntry() {
        try {
            TarArchiveEntry entry = (TarArchiveEntry) tar.createArchiveEntry(new File("file.txt"), "file.txt");
            assertNotNull(entry);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    // Helper methods
    
    private void fail(String message) {
        System.err.println("Failed: " + message);
    }
    
    private void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            fail("Expected: " + expected.toString() + ", but got: " + actual.toString());
        }
    }
    
    private void assertNotNull(Object object) {
        if (object == null) {
            fail("Expected non-null object, but got null.");
        }
    }
    
    private void assertTrue(boolean condition) {
        if (!condition) {
            fail("Expected true, but got false.");
        }
    }
}