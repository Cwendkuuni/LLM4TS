import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructor() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertNotNull(zipArchiveEntry);
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipEntry() throws Exception {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(new java.util.zip.ZipEntry("test.txt")); ;
        assertNotNull(zipArchiveEntry);
    }

    @Test
    public void testZipArchiveEntryClone() throws Exception {
        ZipArchiveEntry original = new ZipArchiveEntry(new java.util.zip.ZipEntry("test.txt")); ;
        ZipArchiveEntry clone = (ZipArchiveEntry) original.clone();
        assertEquals(original, clone);
    }

    @Test
    public void testZipArchiveEntryWithFile() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(new File("test.txt"), "test.txt");
        assertNotNull(zipArchiveEntry);
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals(-1, zipArchiveEntry.getMethod());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodWithNegativeNumber() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(-1);
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals(0, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setInternalAttributes(1);
        assertEquals(1, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals(0, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExternalAttributes(1);
        assertEquals(1, zipArchiveEntry.getExternalAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetExternalAttributesWithNegativeNumber() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExternalAttributes(-1);
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(444);
        assertEquals(444, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    // Add test methods for other methods and edge cases as needed
}