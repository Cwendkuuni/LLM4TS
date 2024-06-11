import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testCreateZipArchiveEntryWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testCreateZipArchiveEntryWithFile() {
        File file = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(file, "test.txt");
        assertEquals("test.txt", entry.getName());
        assertEquals(file.length(), entry.getSize());
        assertEquals(file.lastModified(), entry.getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateZipArchiveEntryWithNegativeMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt", -1);
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertFalse(entry.isSupportedCompressionMethod());

        entry.setMethod(ZipEntry.STORED);
        assertTrue(entry.isSupportedCompressionMethod());

        entry.setMethod(ZipEntry.DEFLATED);
        assertTrue(entry.isSupportedCompressionMethod());

        entry.setMethod(ZipEntry.UNKNOWN);
        assertFalse(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(ZipEntry.FLAG_UNCHANGED | ZipEntry.FLAG_ENCRYPTED);
        assertEquals(ZipEntry.FLAG_UNCHANGED | ZipEntry.FLAG_ENCRYPTED, entry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipShort headerId = new ZipShort(3);
        ZipExtraField extraField = new GenericExtraField(headerId, "Test");

        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExtraFields(new ZipExtraField[] { extraField });

        ZipExtraField retrievedExtraField = entry.getExtraField(headerId);
        assertEquals(extraField, retrievedExtraField);
    }

    @Test
    public void testSetAndGetName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setName("newName.txt");
        assertEquals("newName.txt", entry.getName());
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry3 = new ZipArchiveEntry("newName.txt");

        assertEquals(entry1.hashCode(), entry1.hashCode());
        assertEquals(entry1.hashCode(), entry2.hashCode());
        assertNotEquals(entry1.hashCode(), entry3.hashCode());
    }
}