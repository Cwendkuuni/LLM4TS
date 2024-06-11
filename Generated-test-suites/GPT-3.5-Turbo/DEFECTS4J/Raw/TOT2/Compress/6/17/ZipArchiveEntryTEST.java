import static org.junit.Assert.*;
import org.junit.Test;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getExtra());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipEntry() {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.zip");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals("test.zip", entry.getName());
        assertNull(entry.getExtra());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(1);
        assertEquals(1, entry.getMethod());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertFalse(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testSetUnixModeAndGetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(777);
        assertEquals(777, entry.getUnixMode());
    }

    @Test
    public void testAddExtraFieldAndGetExtraField() {
        ZipExtraField extraField = new ZipExtraField();
        extraField.setHeaderId(new ZipShort(1));
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.addExtraField(extraField);
        assertEquals(extraField, entry.getExtraField(new ZipShort(1)));
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(12345);
        assertEquals(12345, entry.getExternalAttributes());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
        assertTrue(entry1.equals(entry2));
    }

    @Test
    public void testClone() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) entry.clone();
        assertNotSame(entry, clonedEntry);
        assertEquals(entry.getName(), clonedEntry.getName());
    }
}