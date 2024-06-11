import static org.junit.Assert.*;
import org.junit.Test;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructorWithName() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        assertNotNull(zipEntry);
        assertEquals("test.txt", zipEntry.getName());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipEntry() {
        java.util.zip.ZipEntry entry = new java.util.zip.ZipEntry("test.txt");
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(entry);
        assertNotNull(zipEntry);
        assertEquals("test.txt", zipEntry.getName());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        zipEntry.setMethod(8);
        assertEquals(8, zipEntry.getMethod());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        zipEntry.setMethod(2);
        assertTrue(zipEntry.isSupportedCompressionMethod());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        zipEntry.setInternalAttributes(123);
        assertEquals(123, zipEntry.getInternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        zipEntry.setExternalAttributes(456);
        assertEquals(456, zipEntry.getExternalAttributes());
    }

    @Test
    public void testSetUnixModeAndGetUnixMode() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        zipEntry.setUnixMode(755);
        assertEquals(755, zipEntry.getUnixMode());
    }

    @Test
    public void testSetNameAndGetname() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        zipEntry.setName("new_test.txt");
        assertEquals("new_test.txt", zipEntry.getName());
    }

    @Test
    public void testSetExtraFieldsAndGetExtraFields() {
        ZipExtraField mockField1 = new MockZipExtraField();
        ZipExtraField mockField2 = new MockZipExtraField();
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        zipEntry.addExtraField(mockField1);
        zipEntry.addExtraField(mockField2);
        assertEquals(2, zipEntry.getExtraFields().length);
    }

    @Test
    public void testGetCentralDirectoryExtra() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        byte[] centralDirectoryExtra = zipEntry.getCentralDirectoryExtra();
        assertNotNull(centralDirectoryExtra);
        assertEquals(0, centralDirectoryExtra.length);
    }

    // Add more test cases as needed for edge cases and error scenarios

}

class MockZipExtraField implements ZipExtraField {
    // Mock implementation for testing purposes
}