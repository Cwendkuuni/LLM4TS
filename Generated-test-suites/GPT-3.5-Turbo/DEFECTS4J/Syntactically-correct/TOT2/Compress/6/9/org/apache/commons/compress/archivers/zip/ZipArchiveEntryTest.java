import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ZipArchiveEntryTest {
  
    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        assertEquals("test.zip", entry.getName());
    }

    @Test
    public void testConstructorWithZipEntry() {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.zip");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals("test.zip", entry.getName());
    }

    @Test
    public void testIsDirectoryTrue() {
        ZipArchiveEntry entry = new ZipArchiveEntry("directory/");
        assertTrue(entry.isDirectory());
    }

    @Test
    public void testIsDirectoryFalse() {
        ZipArchiveEntry entry = new ZipArchiveEntry("file.txt");
        assertFalse(entry.isDirectory());
    }

    @Test
    public void testSetUnixModeAndGetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("file.txt");
        entry.setUnixMode(755);
        assertEquals(755, entry.getUnixMode());
    }

    @Test
    public void testSetMethodAndGetMethodName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("file.txt");
        entry.setMethod(8);
        assertEquals(8, entry.getMethod());
    }

    @Test
    public void testIsSupportedCompressionMethodTrue() {
        ZipArchiveEntry entry = new ZipArchiveEntry("file.txt");
        entry.setMethod(0); // STORED method
        assertTrue(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testIsSupportedCompressionMethodFalse() {
        ZipArchiveEntry entry = new ZipArchiveEntry("file.txt");
        entry.setMethod(9);
        assertFalse(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("file1.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("file2.txt");
        assertTrue(entry1.hashCode() != entry2.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("file.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("file.txt");
        assertTrue(entry1.equals(entry2));
    }
}