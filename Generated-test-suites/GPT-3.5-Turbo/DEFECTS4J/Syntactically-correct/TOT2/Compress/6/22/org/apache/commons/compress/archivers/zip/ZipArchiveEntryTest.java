import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.util.Date;
import java.util.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        assertEquals("test.zip", entry.getName());
        assertEquals(-1, entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipEntry() throws ZipException {
        java.util.zip.ZipEntry baseEntry = new java.util.zip.ZipEntry("test.zip");
        ZipArchiveEntry entry = new ZipArchiveEntry(baseEntry);
        assertEquals("test.zip", entry.getName());
        assertEquals(-1, entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipArchiveEntry() throws ZipException {
        ZipArchiveEntry baseEntry = new ZipArchiveEntry("test.zip");
        ZipArchiveEntry entry = new ZipArchiveEntry(baseEntry);
        assertEquals("test.zip", entry.getName());
        assertEquals(-1, entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
    }

    @Test
    public void testZipArchiveEntryUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setUnixMode(755);
        assertEquals(755, entry.getUnixMode());
        assertEquals(3, entry.getPlatform());
    }

    @Test
    public void testZipArchiveEntryClone() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) entry.clone();
        assertNotSame(entry, clonedEntry);
        assertEquals(entry.getName(), clonedEntry.getName());
        assertEquals(entry.getMethod(), clonedEntry.getMethod());
        assertEquals(entry.getInternalAttributes(), clonedEntry.getInternalAttributes());
        assertEquals(entry.getExternalAttributes(), clonedEntry.getExternalAttributes());
        assertNull(clonedEntry.getExtraFields());
    }

    @Test
    public void testZipArchiveEntryIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setMethod(ZipArchiveEntry.STORED);
        assertTrue(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testZipArchiveEntrySetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setMethod(5);
        assertEquals(5, entry.getMethod());
    }

    @Test
    public void testZipArchiveEntryAddExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.addExtraField(new TestExtraField());
        assertNotNull(entry.getExtraFields());
    }

    @Test
    public void testZipArchiveEntryRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        TestExtraField extraField = new TestExtraField();
        entry.addExtraField(extraField);
        entry.removeExtraField(extraField.getHeaderId());
        assertNull(entry.getExtraField(extraField.getHeaderId()));
    }

    // Additional test cases for other methods can be added here

    private class TestExtraField implements ZipExtraField {
        @Override
        public ZipShort getHeaderId() {
            return new ZipShort(1234);
        }
    }
}