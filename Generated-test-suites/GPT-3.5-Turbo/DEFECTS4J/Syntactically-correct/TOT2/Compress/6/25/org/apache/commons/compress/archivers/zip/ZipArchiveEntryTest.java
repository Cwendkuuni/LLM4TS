import static org.junit.Assert.*;
import org.junit.Test;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("testEntry");
        assertEquals("testEntry", entry.getName());
        assertFalse(entry.isDirectory());
        assertNull(entry.getExtraFields());
        assertEquals(-1, entry.getMethod());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipEntry() {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("testZipEntry");
        zipEntry.setSize(100);
        zipEntry.setMethod(5);

        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals("testZipEntry", entry.getName());
        assertEquals(100, entry.getSize());
        assertEquals(5, entry.getMethod());
    }

    @Test
    public void testSetMethodValidMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("validMethodTest");
        entry.setMethod(3);
        assertEquals(3, entry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodInvalidMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("invalidMethodTest");
        entry.setMethod(-1);
    }

    @Test
    public void testGetUnixModePlatformUnix() {
        ZipArchiveEntry entry = new ZipArchiveEntry("unixModeTest");
        entry.setUnixMode(755);
        assertEquals(755, entry.getUnixMode());
        assertEquals(3, entry.getPlatform());
    }

    @Test
    public void testAddAndRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("extraFieldTest");
        ZipExtraField ze = new ZipExtraField(new ZipShort(1), new byte[]{1, 2, 3});

        entry.addExtraField(ze);
        assertEquals(ze, entry.getExtraField(new ZipShort(1)));

        entry.removeExtraField(new ZipShort(1));
        assertNull(entry.getExtraField(new ZipShort(1)));
    }

    @Test
    public void testSetCentralDirectoryExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("centralDirectoryExtraTest");
        byte[] b = new byte[]{1, 2, 3};

        entry.setCentralDirectoryExtra(b);
        assertArrayEquals(b, entry.getCentralDirectoryExtra());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("testEquals");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("testEquals");

        assertTrue(entry1.equals(entry2));
    }
}