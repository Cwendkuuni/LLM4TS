import static org.junit.Assert.*;
import org.junit.Test;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructorWithName() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("testFile.txt");
        assertEquals("testFile.txt", zipEntry.getName());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipEntry() throws Exception {
        java.util.zip.ZipEntry baseEntry = new java.util.zip.ZipEntry("baseEntry.txt");
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(baseEntry);
        assertEquals("baseEntry.txt", zipEntry.getName());
    }

    @Test
    public void testZipArchiveEntryIsDirectory() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("directory/");
        assertTrue(zipEntry.isDirectory());
    }

    @Test
    public void testZipArchiveEntrySetAndGetUnixMode() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("testFile.txt");
        zipEntry.setUnixMode(755);
        assertEquals(755, zipEntry.getUnixMode());
    }

    @Test
    public void testZipArchiveEntrySetAndGetExternalAttributes() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("testFile.txt");
        zipEntry.setExternalAttributes(123456);
        assertEquals(123456, zipEntry.getExternalAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZipArchiveEntrySetMethodWithNegativeValue() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("testFile.txt");
        zipEntry.setMethod(-1);
    }

    @Test
    public void testZipArchiveEntrySetAndGetExtraFields() {
        ZipExtraField[] extraFields = new ZipExtraField[1];
        extraFields[0] = new TestZipExtraField();
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("testFile.txt");
        zipEntry.setExtraFields(extraFields);
        assertEquals(1, zipEntry.getExtraFields().length);
    }

    private class TestZipExtraField implements ZipExtraField {
        // Implement methods for test purposes
    }
}