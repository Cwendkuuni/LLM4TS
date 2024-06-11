import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ZipArchiveEntryTest {

    @Test
    public void testEmptyEntry() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        assertEquals("", zipArchiveEntry.getName());
        assertFalse(zipArchiveEntry.isDirectory());
        assertNull(zipArchiveEntry.getExtraFields());
        assertEquals(-1, zipArchiveEntry.getMethod());
        assertEquals(0, zipArchiveEntry.getInternalAttributes());
        assertEquals(0, zipArchiveEntry.getExternalAttributes());
        assertNull(zipArchiveEntry.getPlatform());
    }

    @Test
    public void testEntryWithName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", zipArchiveEntry.getName());
        assertFalse(zipArchiveEntry.isDirectory());
        assertNull(zipArchiveEntry.getExtraFields());
        assertEquals(-1, zipArchiveEntry.getMethod());
        assertEquals(0, zipArchiveEntry.getInternalAttributes());
        assertEquals(0, zipArchiveEntry.getExternalAttributes());
        assertNull(zipArchiveEntry.getPlatform());
    }

    @Test
    public void testEntryWithFile() {
        File file = new File("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, "test.txt");
        assertEquals("test.txt", zipArchiveEntry.getName());
        assertFalse(zipArchiveEntry.isDirectory());
        assertNull(zipArchiveEntry.getExtraFields());
        assertEquals(-1, zipArchiveEntry.getMethod());
        assertEquals(file.isDirectory() ? 0x10 : 0, zipArchiveEntry.getInternalAttributes());
        assertEquals(file.lastModified(), zipArchiveEntry.getTime());
        assertNull(zipArchiveEntry.getPlatform());
    }

    @Test
    public void testEntryWithZipEntry() throws ZipException {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        assertEquals("test.txt", zipArchiveEntry.getName());
        assertFalse(zipArchiveEntry.isDirectory());
        assertNull(zipArchiveEntry.getExtraFields());
        assertEquals(-1, zipArchiveEntry.getMethod());
        assertEquals(0, zipArchiveEntry.getInternalAttributes());
        assertEquals(0, zipArchiveEntry.getExternalAttributes());
        assertNull(zipArchiveEntry.getPlatform());
    }

    @Test
    public void testEntryWithZipArchiveEntry() throws ZipException {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry(zipArchiveEntry1);

        assertEquals("test.txt", zipArchiveEntry2.getName());
        assertFalse(zipArchiveEntry2.isDirectory());
        assertNull(zipArchiveEntry2.getExtraFields());
        assertEquals(-1, zipArchiveEntry2.getMethod());
        assertEquals(0, zipArchiveEntry2.getInternalAttributes());
        assertEquals(0, zipArchiveEntry2.getExternalAttributes());
        assertNull(zipArchiveEntry2.getPlatform());

        assertSame(zipArchiveEntry1, zipArchiveEntry2);
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setUnixMode(0755);
        assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        ZipExtraField extraField = new ZipExtendedInfo();
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{extraField});
        assertEquals(1, zipArchiveEntry.getExtraFields().length);
        assertSame(extraField, zipArchiveEntry.getExtraFields()[0]);
    }

    @Test
    public void testGetExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        ZipExtraField extraField1 = new ZipExtendedInfo();
        ZipExtraField extraField2 = new ZipExtendedInfo();
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{extraField1, extraField2});
        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        assertEquals(2, extraFields.length);
        assertSame(extraField1, extraFields[0]);
        assertSame(extraField2, extraFields[1]);
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        ZipExtraField extraField = new ZipExtendedInfo();
        zipArchiveEntry.addExtraField(extraField);
        assertEquals(1, zipArchiveEntry.getExtraFields().length);
        assertSame(extraField, zipArchiveEntry.getExtraFields()[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddExtraFieldWithNull() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.addExtraField(null);
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        ZipExtraField extraField1 = new ZipExtendedInfo();
        ZipExtraField extraField2 = new ZipExtendedInfo();
        zipArchiveEntry.addAsFirstExtraField(extraField2);
        assertEquals(1, zipArchiveEntry.getExtraFields().length);
        assertSame(extraField2, zipArchiveEntry.getExtraFields()[0]);
        zipArchiveEntry.addExtraField(extraField1);
        assertEquals(2, zipArchiveEntry.getExtraFields().length);
        assertSame(extraField2, zipArchiveEntry.getExtraFields()[1]);
        assertSame(extraField1, zipArchiveEntry.getExtraFields()[0]);
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        ZipExtraField extraField1 = new ZipExtendedInfo();
        ZipExtraField extraField2 = new ZipExtendedInfo();
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{extraField1, extraField2});
        zipArchiveEntry.removeExtraField(ZipShort.UNIX_USER);
        assertEquals(1, zipArchiveEntry.getExtraFields().length);
        assertSame(extraField2, zipArchiveEntry.getExtraFields()[0]);
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testRemoveExtraFieldWithEmptyExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.removeExtraField(ZipShort.UNIX_USER);
    }

    @Test
    public void testGetExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        ZipExtendedInfo ext1 = new ZipExtendedInfo();
        ZipExtendedInfo ext2 = new ZipExtendedInfo();
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{ext1, ext2});
        ZipExtraField field = zipArchiveEntry.getExtraField(ZipShort.UNIX_NAME);
        assertNull(field);
        field = zipArchiveEntry.getExtraField(ZipShort.UNIX_USER);
        assertSame(ext1, field);
    }

    @Test
    public void testSetExtra() throws RuntimeException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        byte[] extraData = new byte[]{1, 2, 3};
        zipArchiveEntry.setExtra(extraData);
        assertEquals(extraData.length, zipArchiveEntry.getExtra().length);
    }

    @Test
    public void testSetCentralDirectoryExtra() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        byte[] extraData = new byte[]{1, 2, 3};
        zipArchiveEntry.setCentralDirectoryExtra(extraData);
    }

    @Test
    public void testGetLocalFileDataExtra() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        byte[] extraData = new byte[]{1, 2, 3};
        zipArchiveEntry.setExtra(extraData);
        byte[] result = zipArchiveEntry.getLocalFileDataExtra();
        assertEquals(extraData.length, result.length);
    }

    @Test
    public void testGetCentralDirectoryExtra() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        byte[] extraData = new byte[]{1, 2, 3};
        zipArchiveEntry.setExtra(extraData);
        byte[] result = zipArchiveEntry.getCentralDirectoryExtra();
        assertEquals(extraData.length, result.length);
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test.txt");
        assertEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry2.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test.txt");
        assertTrue(zipArchiveEntry1.equals(zipArchiveEntry2));

        ZipArchiveEntry zipArchiveEntry3 = new ZipArchiveEntry("test2.txt");
        assertFalse(zipArchiveEntry1.equals(zipArchiveEntry3));

        ZipArchiveEntry zipArchiveEntry4 = new ZipArchiveEntry("test.txt");
        zipArchiveEntry4.setName("test2.txt");
        assertFalse(zipArchiveEntry1.equals(zipArchiveEntry4));
    }
}