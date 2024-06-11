import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertNotNull(zipArchiveEntry);
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithZipEntry() throws ZipException {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.zip");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertNotNull(zipArchiveEntry);
        Assert.assertEquals("test.zip", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithZipArchiveEntry() throws ZipException {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.zip");
        ZipArchiveEntry originalZipArchiveEntry = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry cloneZipArchiveEntry = (ZipArchiveEntry) originalZipArchiveEntry.clone();
        Assert.assertNotNull(cloneZipArchiveEntry);
        Assert.assertEquals(originalZipArchiveEntry.getName(), cloneZipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithFile() {
        File file = new File("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, "test.txt");
        Assert.assertNotNull(zipArchiveEntry);
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodWithNegativeNumber() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(-1);
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(STORED);
        Assert.assertEquals(STORED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setInternalAttributes(FILE_ATTRIBUTE_READ_ONLY);
        Assert.assertEquals(FILE_ATTRIBUTE_READ_ONLY, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setPlatform(PLATFORM_UNIX);
        Assert.assertEquals(PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(0755);
        Assert.assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtendedTimestamp();
        zipArchiveEntry.addExtraField(extraField);
        Assert.assertNotNull(zipArchiveEntry.getExtraField(extraField.getHeaderId()));
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtendedTimestamp();
        zipArchiveEntry.addAsFirstExtraField(extraField);
        Assert.assertNotNull(zipArchiveEntry.getExtraField(extraField.getHeaderId()));
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testGetExtraFieldWithNonExistentType() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.getExtraField(new ZipShort((short) 1));
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtendedTimestamp();
        zipArchiveEntry.addExtraField(extraField);
        zipArchiveEntry.removeExtraField(extraField.getHeaderId());
        Assert.assertNull(zipArchiveEntry.getExtraField(extraField.getHeaderId()));
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testGetExtraFieldWithEmptyExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.getExtraField(new ZipShort((short) 1));
    }
}