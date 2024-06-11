import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithZipEntry() {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithZipArchiveEntry() {
        ZipArchiveEntry original = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry copy = new ZipArchiveEntry(original);
        assertEquals(original.getName(), copy.getName());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals(-1, zipArchiveEntry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodNegative() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(-1);
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals(0, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setInternalAttributes(1);
        assertEquals(1, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals(0, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExternalAttributes(1);
        assertEquals(1, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(123);
        assertEquals(123, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetExtraFieldsEmpty() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] extraFields = {};
        zipArchiveEntry.setExtraFields(extraFields);
        assertNull(zipArchiveEntry.getExtraFields());
    }

    @Test
    public void testSetExtraFieldsNonNull() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipStringExtraField("test", 1);
        ZipExtraField[] extraFields = {extraField};
        zipArchiveEntry.setExtraFields(extraFields);
        assertArrayEquals(extraFields, zipArchiveEntry.getExtraFields());
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField1 = new ZipStringExtraField("test1", 1);
        ZipExtraField extraField2 = new ZipStringExtraField("test2", 2);
        zipArchiveEntry.addExtraField(extraField1);
        zipArchiveEntry.addExtraField(extraField2);
        assertArrayEquals(new ZipExtraField[]{extraField1, extraField2}, zipArchiveEntry.getExtraFields());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testRemoveExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipShort type = new ZipShort((short) 1);
        zipArchiveEntry.removeExtraField(type);
    }

    @Test
    public void testGetExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipStringExtraField extraField = new ZipStringExtraField("test", 1);
        zipArchiveEntry.addExtraField(extraField);
        assertEquals(extraField, zipArchiveEntry.getExtraField(new ZipShort((short) 1)));
    }

    @Test
    public void testSetExtra() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] extraBytes = new byte[1];
        ZipExtraField[] extraFields = {new ZipStringExtraField("test", 1)};
        zipArchiveEntry.setExtra(extraBytes);
        zipArchiveEntry.setExtraFields(extraFields);
        assertEquals(extraBytes, zipArchiveEntry.getExtra());
    }

    @Test(expected = RuntimeException.class)
    public void testSetExtraInvalid() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] extraBytes = new byte[1];
        zipArchiveEntry.setExtra(extraBytes);
    }

    @Test
    public void testGetName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testIsDirectory() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertFalse(zipArchiveEntry.isDirectory());
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test2.txt");
        int hashCode1 = zipArchiveEntry1.hashCode();
        int hashCode2 = zipArchiveEntry2.hashCode();
        assertNotSame(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test1.txt");
        assertTrue(zipArchiveEntry1.equals(zipArchiveEntry2));

        ZipArchiveEntry zipArchiveEntry3 = new ZipArchiveEntry("test2.txt");
        assertFalse(zipArchiveEntry1.equals(zipArchiveEntry3));

        ZipArchiveEntry zipArchiveEntry4 = new ZipArchiveEntry("test1.txt");
        zipArchiveEntry4.setName("test2.txt");
        assertFalse(zipArchiveEntry1.equals(zipArchiveEntry4));
    }

    @Test
    public void testGetLastModifiedDate() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Date lastModifiedDate = new Date();
        zipArchiveEntry.setTime(lastModifiedDate.getTime());
        assertEquals(lastModifiedDate, zipArchiveEntry.getLastModifiedDate());
    }
}