import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testGetMethodName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setMethod(8);
        assertEquals("getMethod() should return 8", 8, entry.getMethod());
    }

    @Test
    public void testGetName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        assertEquals("getName() should return 'test.zip'", "test.zip", entry.getName());
    }

    @Test
    public void testIsDirectory() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("directory/");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("file.txt");
        assertTrue("isDirectory() should return true for 'directory/'", entry1.isDirectory());
        assertFalse("isDirectory() should return false for 'file.txt'", entry2.isDirectory());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setUnixMode(755);
        assertEquals("getUnixMode() should return 755", 755, entry.getUnixMode());
    }

    @Test
    public void testSetMethodNegative() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        try {
            entry.setMethod(-1);
            fail("setMethod() should throw IllegalArgumentException for negative method");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.zip");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.zip");
        ZipArchiveEntry entry3 = new ZipArchiveEntry("other.zip");
        assertTrue("equals() should return true for the same entry",
                entry1.equals(entry2));
        assertFalse("equals() should return false for different entries",
                entry1.equals(entry3));
    }

    @Test
    public void testLastModifiedDate() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setTime(1558975789000L);
        assertEquals("getLastModifiedDate() should return the specified Date",
                new Date(1558975789000L), entry.getLastModifiedDate());
    }

}