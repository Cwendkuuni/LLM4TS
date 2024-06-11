import static org.junit.Assert.*;
import org.junit.Test;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        assertEquals("test.zip", entry.getName());
        assertEquals(-1, entry.getMethod());
    }

    @Test
    public void testConstructorWithZipEntry() {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.zip");
        zipEntry.setMethod(8); // Just a random method for testing
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals("test.zip", entry.getName());
        assertEquals(8, entry.getMethod());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setMethod(0); // STORED method
        assertTrue(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setUnixMode(755); // Unix mode
        assertEquals(755, entry.getUnixMode());
    }

    @Test
    public void testAddExtraField() {
        ZipExtraField extraField = new TestZipExtraField();
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.addExtraField(extraField);
        assertNotNull(entry.getExtraField(new ZipShort(1))); // Assuming header id of TestZipExtraField is 1
    }

    // Add more test cases for other methods as required

    private static class TestZipExtraField implements ZipExtraField {
        // Implement methods required for ZipExtraField
    }

}