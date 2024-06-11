import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithZipEntry() {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithZipArchiveEntry() {
        ZipArchiveEntry originalZipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry clonedZipArchiveEntry = (ZipArchiveEntry) originalZipArchiveEntry.clone();
        Assert.assertEquals(originalZipArchiveEntry.getName(), clonedZipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithFileAndEntryName() {
        File inputFile = new File("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(inputFile, "test.txt");
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
        Assert.assertEquals(inputFile.length(), zipArchiveEntry.getSize());
        Assert.assertEquals(inputFile.lastModified(), zipArchiveEntry.getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(-1);
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        Assert.assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        Assert.assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        Assert.assertEquals(ZipEntry.DEFLATED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setInternalAttributes(1);
        Assert.assertEquals(1, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(0, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExternalAttributes(1);
        Assert.assertEquals(1, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(0, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(0755);
        Assert.assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testGetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(0, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipShort testHeaderId = new ZipShort((short) 1);
        ZipExtraField testExtraField = new ZipExtraField(testHeaderId, "Test Extra Field");
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{testExtraField});
        Assert.assertNotNull(zipArchiveEntry.getExtraField(testHeaderId));
    }

    @Test
    public void testGetExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipShort testHeaderId = new ZipShort((short) 1);
        ZipExtraField testExtraField = new ZipExtraField(testHeaderId, "Test Extra Field");
        ZipExtraField[] extraFields = {testExtraField};
        zipArchiveEntry.setExtraFields(extraFields);
        ZipExtraField[] result = zipArchiveEntry.getExtraFields();
        Assert.assertArrayEquals(extraFields, result);
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipShort testHeaderId = new ZipShort((short) 1);
        ZipExtraField testExtraField = new ZipExtraField(testHeaderId, "Test Extra Field");
        zipArchiveEntry.addExtraField(testExtraField);
        Assert.assertNotNull(zipArchiveEntry.getExtraField(testHeaderId));
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipShort testHeaderId = new ZipShort((short) 1);
        ZipExtraField testExtraField = new ZipExtraField(testHeaderId, "Test Extra Field");
        zipArchiveEntry.addAsFirstExtraField(testExtraField);
        Assert.assertEquals(testExtraField, zipArchiveEntry.getExtraFields()[0]);
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipShort testHeaderId = new ZipShort((short) 1);
        ZipExtraField testExtraField = new ZipExtraField(testHeaderId, "Test Extra Field");
        zipArchiveEntry.addExtraField(testExtraField);
        zipArchiveEntry.removeExtraField(testHeaderId);
        Assert.assertNull(zipArchiveEntry.getExtraField(testHeaderId));
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test2.txt");
        Assert.assertFalse(zipArchiveEntry1.equals(zipArchiveEntry2));

        ZipArchiveEntry zipArchiveEntry3 = new ZipArchiveEntry("test1.txt");
        Assert.assertTrue(zipArchiveEntry1.equals(zipArchiveEntry3));
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(TestUtils.getSHA1(zipArchiveEntry.getName().getBytes()), zipArchiveEntry.hashCode());
    }
}