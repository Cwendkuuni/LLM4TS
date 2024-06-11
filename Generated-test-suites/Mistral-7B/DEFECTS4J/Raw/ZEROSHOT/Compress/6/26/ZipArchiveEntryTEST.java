import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstruction() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertNotNull(zipArchiveEntry);
        assertEquals("test.txt", zipArchiveEntry.getName());
        assertEquals(-1, zipArchiveEntry.getMethod());
        assertNull(zipArchiveEntry.getExtraFields());
        assertEquals(0, zipArchiveEntry.getInternalAttributes());
        assertEquals(0, zipArchiveEntry.getExternalAttributes());
        assertEquals(ZipEntry.STORED, zipArchiveEntry.getCompressionMethod());
    }

    @Test
    public void testZipArchiveEntryClone() {
        ZipArchiveEntry original = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry cloned = (ZipArchiveEntry) original.clone();
        assertNotSame(original, cloned);
        assertEquals(original.getName(), cloned.getName());
        assertEquals(original.getMethod(), cloned.getMethod());
        assertEquals(original.getInternalAttributes(), cloned.getInternalAttributes());
        assertEquals(original.getExternalAttributes(), cloned.getExternalAttributes());
        assertArrayEquals(original.getExtraFields(), cloned.getExtraFields());
    }

    @Test
    public void testZipArchiveEntryConstructionFromZipEntry() throws ZipException {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        assertNotNull(zipArchiveEntry);
        assertEquals("test.txt", zipArchiveEntry.getName());
        assertEquals(zipEntry.getMethod(), zipArchiveEntry.getMethod());
        assertNull(zipArchiveEntry.getExtraFields());
        assertEquals(zipEntry.getCompressionMethod(), zipArchiveEntry.getCompressionMethod());
        assertEquals(zipEntry.getSize(), zipArchiveEntry.getSize());
        assertEquals(zipEntry.getTime(), zipArchiveEntry.getLastModifiedDate().getTime());
    }

    @Test
    public void testZipArchiveEntryConstructionFromZipArchiveEntry() throws ZipException {
        ZipArchiveEntry original = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry cloned = new ZipArchiveEntry(original);
        assertNotSame(original, cloned);
        assertEquals(original.getName(), cloned.getName());
        assertEquals(original.getMethod(), cloned.getMethod());
        assertEquals(original.getInternalAttributes(), cloned.getInternalAttributes());
        assertEquals(original.getExternalAttributes(), cloned.getExternalAttributes());
        assertArrayEquals(original.getExtraFields(), cloned.getExtraFields());
    }

    @Test
    public void testZipArchiveEntryConstructionFromFile() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(new File("test.txt"), "test.txt");
        assertNotNull(zipArchiveEntry);
        assertEquals("test.txt", zipArchiveEntry.getName());
        assertEquals(-1, zipArchiveEntry.getMethod());
        assertNull(zipArchiveEntry.getExtraFields());
        assertEquals(0, zipArchiveEntry.getInternalAttributes());
        assertEquals(0, zipArchiveEntry.getExternalAttributes());
        assertEquals(File.TYPE_FILE, zipArchiveEntry.getFileType());
        assertEquals(zipArchiveEntry.getSize(), new File("test.txt").length());
        assertEquals(new File("test.txt").lastModified(), zipArchiveEntry.getLastModifiedDate().getTime());
    }

    @Test
    public void testZipArchiveEntryGetSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(0755);
        assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testZipArchiveEntryGetSetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testZipArchiveEntryGetSetExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField zipExtraField1 = new ZipExtraField("Test Extra Field 1", (byte) 1);
        ZipExtraField zipExtraField2 = new ZipExtraField("Test Extra Field 2", (byte) 2);
        zipArchiveEntry.setExtraFields(new ZipExtraField[] {zipExtraField1, zipExtraField2});
        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        assertNotNull(extraFields);
        assertEquals(2, extraFields.length);
        assertEquals(zipExtraField1.getName(), extraFields[0].getName());
        assertEquals(zipExtraField1.getHeaderId(), extraFields[0].getHeaderId());
        assertEquals(zipExtraField2.getName(), extraFields[1].getName());
        assertEquals(zipExtraField2.getHeaderId(), extraFields[1].getHeaderId());
    }

    @Test
    public void testZipArchiveEntrySetExtra() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05};
        zipArchiveEntry.setExtra(extraData);
        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals("Unknown", extraFields[0].getName());
        assertEquals((short) 0x0005, extraFields[0].getHeaderId());
        assertEquals(extraData.length, extraFields[0].getSize());
    }

    @Test
    public void testZipArchiveEntrySetExtraParsingException() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[] {(byte) 0x01, (byte) 0x02, (byte) 0x03};
        try {
            zipArchiveEntry.setExtra(extraData);
            fail();
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Unknown extra field type"));
        }
    }

    @Test
    public void testZipArchiveEntrySetCentralDirectoryExtra() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] centralDirectoryExtraData = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05};
        zipArchiveEntry.setCentralDirectoryExtra(centralDirectoryExtraData);
        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals("Unknown", extraFields[0].getName());
        assertEquals((short) 0x0005, extraFields[0].getHeaderId());
        assertEquals(centralDirectoryExtraData.length, extraFields[0].getSize());
    }

    @Test
    public void testZipArchiveEntrySetLocalFileDataExtra() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] localFileDataExtraData = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05};
        zipArchiveEntry.setLocalFileDataExtra(localFileDataExtraData);
        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals("Unknown", extraFields[0].getName());
        assertEquals((short) 0x0005, extraFields[0].getHeaderId());
        assertEquals(localFileDataExtraData.length, extraFields[0].getSize());
    }
}