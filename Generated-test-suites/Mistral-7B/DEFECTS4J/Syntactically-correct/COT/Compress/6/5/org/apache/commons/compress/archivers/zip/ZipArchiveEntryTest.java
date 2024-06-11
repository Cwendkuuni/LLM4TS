import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertNotNull(zipArchiveEntry);
        assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithZipEntry() throws ZipException {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        assertNotNull(zipArchiveEntry);
        assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithZipArchiveEntry() throws ZipException {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
        ZipArchiveEntry originalZipArchiveEntry = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry copiedZipArchiveEntry = new ZipArchiveEntry(originalZipArchiveEntry);
        assertNotNull(copiedZipArchiveEntry);
        assertEquals(originalZipArchiveEntry.getName(), copiedZipArchiveEntry.getName());
        assertNotSame(originalZipArchiveEntry, copiedZipArchiveEntry);
    }

    @Test
    public void testConstructorWithFileAndEntryName() {
        File testFile = new File("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(testFile, "test.txt");
        assertNotNull(zipArchiveEntry);
        assertEquals("test.txt", zipArchiveEntry.getName());
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

    @Test
    public void testSetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodNegativeValue() {
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
        zipArchiveEntry.setInternalAttributes(ZipEntry.FILE_FLAG);
        assertEquals(ZipEntry.FILE_FLAG, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals(0, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExternalAttributes(0x10);
        assertEquals(0x10, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(0755);
        assertEquals(0755, zipArchiveEntry.getUnixMode());
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
    public void testGetExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] result = zipArchiveEntry.getExtraFields();
        assertEquals(new ZipExtraField[0].length, result.length);
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtraField(ZipShort.LOCAL_FILE_NAME, new byte[20]);
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{extraField});
        ZipExtraField[] result = zipArchiveEntry.getExtraFields();
        assertEquals(1, result.length);
        assertEquals(extraField, result[0]);
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtraField(ZipShort.LOCAL_FILE_NAME, new byte[20]);
        zipArchiveEntry.addExtraField(extraField);
        ZipExtraField[] result = zipArchiveEntry.getExtraFields();
        assertEquals(1, result.length);
        assertEquals(extraField, result[0]);
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtraField(ZipShort.LOCAL_FILE_NAME, new byte[20]);
        zipArchiveEntry.addAsFirstExtraField(extraField);
        ZipExtraField[] result = zipArchiveEntry.getExtraFields();
        assertEquals(1, result.length);
        assertEquals(extraField, result[0]);
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipShort extraFieldHeaderId = ZipShort.LOCAL_FILE_NAME;
        ZipExtraField extraField = new ZipExtraField(extraFieldHeaderId, new byte[20]);
        zipArchiveEntry.addExtraField(extraField);
        zipArchiveEntry.removeExtraField(extraFieldHeaderId);
        ZipExtraField[] result = zipArchiveEntry.getExtraFields();
        assertEquals(0, result.length);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetExtraFieldNull() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipShort extraFieldHeaderId = ZipShort.LOCAL_FILE_NAME;
        zipArchiveEntry.getExtraField(extraFieldHeaderId);
    }

    @Test
    public void testSetExtra() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[10];
        try {
            zipArchiveEntry.setExtra(extraData);
        } catch (RuntimeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSetCentralDirectoryExtra() {
        // This method is not covered by JUnit test case as it's not public
    }

    @Test
    public void testGetLocalFileDataExtra() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[10];
        zipArchiveEntry.setExtra(extraData);
        byte[] result = zipArchiveEntry.getLocalFileDataExtra();
        assertArrayEquals(extraData, result);
    }

    @Test
    public void testGetCentralDirectoryExtra() {
        // This method is not covered by JUnit test case as it's not public
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test2.txt");
        ZipArchiveEntry zipArchiveEntry3 = zipArchiveEntry1;
        int hashCode1 = zipArchiveEntry1.hashCode();
        int hashCode2 = zipArchiveEntry2.hashCode();
        int hashCode3 = zipArchiveEntry3.hashCode();
        assertNotEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode3);
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test2.txt");
        ZipArchiveEntry zipArchiveEntry3 = zipArchiveEntry1;
        boolean equals1_1 = zipArchiveEntry1.equals(zipArchiveEntry1);
        boolean equals1_2 = zipArchiveEntry1.equals(zipArchiveEntry2);
        boolean equals1_3 = zipArchiveEntry1.equals(zipArchiveEntry3);
        boolean equals2_1 = zipArchiveEntry2.equals(zipArchiveEntry1);
        boolean equals2_2 = zipArchiveEntry2.equals(zipArchiveEntry2);
        boolean equals3_1 = zipArchiveEntry3.equals(zipArchiveEntry1);
        assertTrue(equals1_1);
        assertFalse(equals1_2);
        assertTrue(equals1_3);
        assertFalse(equals2_1);
        assertTrue(equals2_2);
        assertFalse(equals3_1);
    }
}