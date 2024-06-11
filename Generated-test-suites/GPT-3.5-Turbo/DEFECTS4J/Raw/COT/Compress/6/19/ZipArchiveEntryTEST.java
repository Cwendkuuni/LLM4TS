import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.zip");
        
        assertEquals("test.zip", zipEntry.getName());
        assertFalse(zipEntry.isDirectory());
    }

    @Test
    public void testConstructorWithZipEntry() {
        java.util.zip.ZipEntry entry = new java.util.zip.ZipEntry("test.zip");
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(entry);
        
        assertEquals("test.zip", zipEntry.getName());
        assertFalse(zipEntry.isDirectory());
    }

    @Test
    public void testConstructorWithZipArchiveEntry() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(entry);
        
        assertEquals("test.zip", zipEntry.getName());
        assertFalse(zipEntry.isDirectory());
    }

    @Test
    public void testUnixMode() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.zip");
        zipEntry.setUnixMode(755);
        
        assertEquals(755, zipEntry.getUnixMode());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry zipEntry1 = new ZipArchiveEntry("test.zip");
        zipEntry1.setMethod(ZipArchiveEntry.STORED);
        
        assertTrue(zipEntry1.isSupportedCompressionMethod());
        
        ZipArchiveEntry zipEntry2 = new ZipArchiveEntry("test.zip");
        zipEntry2.setMethod(ZipArchiveEntry.DEFLATED);
        
        assertTrue(zipEntry2.isSupportedCompressionMethod());
        
        ZipArchiveEntry zipEntry3 = new ZipArchiveEntry("test.zip");
        zipEntry3.setMethod(2);
        
        assertFalse(zipEntry3.isSupportedCompressionMethod());
    }

    // ... more test cases for the other methods

}