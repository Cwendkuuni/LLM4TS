import static org.junit.Assert.*;
import org.junit.Test;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        assertEquals("test.zip", entry.getName());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipEntry() {
        java.util.zip.ZipEntry baseEntry = new java.util.zip.ZipEntry("test.zip");
        ZipArchiveEntry entry = new ZipArchiveEntry(baseEntry);
        assertEquals("test.zip", entry.getName());
        assertEquals(-1, entry.getMethod());
    }

    @Test
    public void testZipArchiveEntryConstructorWithAnotherZipArchiveEntry() throws ZipException {
        ZipArchiveEntry baseEntry = new ZipArchiveEntry("test.zip");
        baseEntry.setMethod(8); // Some random method
        ZipArchiveEntry entry = new ZipArchiveEntry(baseEntry);
        assertEquals("test.zip", entry.getName());
        assertEquals(8, entry.getMethod());
    }

    @Test
    public void testSetMethodWithValidMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setMethod(8);
        assertEquals(8, entry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodWithNegativeMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setMethod(-1);
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setUnixMode(493); // UNIX mode 0755
        assertEquals(493, entry.getUnixMode());
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        ZipExtraField field = new TestZipExtraField();
        entry.addExtraField(field);
        assertEquals(1, entry.getExtraFields().length);
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        ZipExtraField field = new TestZipExtraField();
        entry.addExtraField(field);
        entry.removeExtraField(new ZipShort(1));
        assertEquals(0, entry.getExtraFields().length);
    }

    private static class TestZipExtraField implements ZipExtraField {
        // Implement required methods for testing purposes
    }

    // Add more test cases for other methods as needed

}