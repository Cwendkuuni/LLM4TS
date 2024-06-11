import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        assertEquals("test.zip", entry.getName());
        assertEquals(-1, entry.getMethod());
        assertFalse(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipEntry() {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.zip");
        zipEntry.setMethod(8);
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals("test.zip", entry.getName());
        assertEquals(8, entry.getMethod());
        assertTrue(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testSetUnixModeAndGetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        entry.setUnixMode(755);
        assertEquals(755, entry.getUnixMode());
    }

    @Test
    public void testSetCentralDirectoryExtraAndGetCentralDirectoryExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.zip");
        byte[] centralExtra = new byte[]{0x01, 0x02, 0x03};
        entry.setCentralDirectoryExtra(centralExtra);
        assertArrayEquals(centralExtra, entry.getCentralDirectoryExtra());
    }

    // Add more test cases for other methods

}