import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.ZipExtraField;
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
    }

    @Test
    public void testConstructorWithFile() {
        File inputFile = new File("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(inputFile, "test.txt");
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
    public void testSetMethodWithNegativeNumber() {
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
        zipArchiveEntry.setInternalAttributes(ZipArchiveConstants.FILE_ATTRIBUTE_READ_ONLY);
        assertEquals(ZipArchiveConstants.FILE_ATTRIBUTE_READ_ONLY, zipArchiveEntry.getInternalAttributes());
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

    @Test(expected = IllegalArgumentException.class)
    public void testSetUnixModeWithNegativeNumber() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(-1);
    }

    @Test
    public void testGetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals(0, zipArchiveEntry.getUnixMode());
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
    public void testSetExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtendedTimestamps();
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{extraField});
        assertTrue(zipArchiveEntry.getExtraFields().length > 0);
    }

    @Test
    public void testGetExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtendedTimestamps();
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{extraField});
        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals(extraField.getClass(), extraFields[0].getClass());
    }

    @Test(expected = RuntimeException.class)
    public void testSetExtraWithInvalidData() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] invalidData = new byte[0];
        zipArchiveEntry.setExtra(invalidData);
    }

    @Test
    public void testSetExtraWithValidData() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtendedTimestamps();
        byte[] validData = extraField.getData();
        zipArchiveEntry.setExtra(validData);
        assertTrue(zipArchiveEntry.getExtraFields().length > 0);
    }

    @Test
    public void testSetCentralDirectoryExtra() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtendedTimestamps();
        byte[] centralDirectoryExtraData = extraField.getCentralDirectoryData();
        zipArchiveEntry.setCentralDirectoryExtra(centralDirectoryExtraData);
        assertTrue(zipArchiveEntry.getExtraFields().length > 0);
    }

    @Test
    public void testGetLocalFileDataExtra() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtendedTimestamps();
        byte[] localFileDataExtraData = extraField.getLocalFileDataData();
        zipArchiveEntry.setExtra(localFileDataExtraData);
        byte[] result = zipArchiveEntry.getLocalFileDataExtra();
        assertArrayEquals(localFileDataExtraData, result);
    }

    @Test
    public void testGetCentralDirectoryExtra() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtendedTimestamps();
        byte[] centralDirectoryExtraData = extraField.getCentralDirectoryData();
        zipArchiveEntry.setCentralDirectoryExtra(centralDirectoryExtraData);
        byte[] result = zipArchiveEntry.getCentralDirectoryExtra();
        assertArrayEquals(centralDirectoryExtraData, result);
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtendedTimestamps();
        zipArchiveEntry.addExtraField(extraField);
        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals(extraField.getClass(), extraFields[0].getClass());
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtendedTimestamps();
        zipArchiveEntry.addAsFirstExtraField(extraField);
        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals(extraField.getClass(), extraFields[0].getClass());
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipShort type = new ZipShort((short) 1);
        zipArchiveEntry.removeExtraField(type);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipShort type = new ZipShort((short) 1);
        assertNull(zipArchiveEntry.getExtraField(type));
    }
}