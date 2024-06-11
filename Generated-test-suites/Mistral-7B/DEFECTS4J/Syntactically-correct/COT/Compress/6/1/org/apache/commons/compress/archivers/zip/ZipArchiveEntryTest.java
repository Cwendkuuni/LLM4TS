import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructorWithName() {
        String name = "test.txt";
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name);
        Assert.assertEquals(name, zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithZipEntry() throws ZipException {
        String name = "test.txt";
        long time = System.currentTimeMillis();
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(name);
        zipEntry.setTime(time);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals(name, zipArchiveEntry.getName());
        Assert.assertEquals(time, zipArchiveEntry.getTime());
    }

    @Test
    public void testConstructorWithZipArchiveEntry() throws ZipException, CloneNotSupportedException {
        String name = "test.txt";
        long time = System.currentTimeMillis();
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(name);
        zipEntry.setTime(time);
        ZipArchiveEntry originalZipArchiveEntry = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry newZipArchiveEntry = new ZipArchiveEntry(originalZipArchiveEntry);
        Assert.assertEquals(name, newZipArchiveEntry.getName());
        Assert.assertEquals(time, newZipArchiveEntry.getTime());
        Assert.assertNotSame(originalZipArchiveEntry, newZipArchiveEntry);
    }

    @Test
    public void testConstructorWithFile() {
        File inputFile = new File("test.txt");
        String name = inputFile.getName();
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(inputFile, name);
        Assert.assertEquals(name, zipArchiveEntry.getName());
        Assert.assertEquals(inputFile.length(), zipArchiveEntry.getSize());
        Assert.assertEquals(inputFile.lastModified(), zipArchiveEntry.getTime());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
        zipArchiveEntry.setMethod(ZipArchiveEntry.STORED);
        Assert.assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
        zipArchiveEntry.setMethod(ZipArchiveEntry.DEFLATED);
        Assert.assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
        zipArchiveEntry.setMethod(123);
        Assert.assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodNegative() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(-1);
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(ZipArchiveEntry.STORED);
        Assert.assertEquals(ZipArchiveEntry.STORED, zipArchiveEntry.getMethod());
        zipArchiveEntry.setMethod(ZipArchiveEntry.DEFLATED);
        Assert.assertEquals(ZipArchiveEntry.DEFLATED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testSetGetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setInternalAttributes(0x01);
        Assert.assertEquals(0x01, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testSetGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExternalAttributes(0x01);
        Assert.assertEquals(0x01, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetGetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(0755);
        Assert.assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testSetGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetGetExtraFields() {
        ZipShort headerId = new ZipShort((short) 0x0101);
        ZipExtraField extraField = new ZipCustomField("CustomField", "CustomValue");
        extraField.setHeaderId(headerId);

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] extraFields = {extraField};
        zipArchiveEntry.setExtraFields(extraFields);
        ZipExtraField[] fields = zipArchiveEntry.getExtraFields();
        Assert.assertArrayEquals(extraFields, fields);
        Assert.assertEquals(1, fields.length);
        Assert.assertEquals(extraField, fields[0]);
    }

    @Test
    public void testAddExtraField() {
        ZipShort headerId = new ZipShort((short) 0x0101);
        ZipExtraField extraField = new ZipCustomField("CustomField", "CustomValue");
        extraField.setHeaderId(headerId);

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.addExtraField(extraField);
        ZipExtraField[] fields = zipArchiveEntry.getExtraFields();
        Assert.assertArrayEquals(new ZipExtraField[]{extraField}, fields);
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipShort headerId = new ZipShort((short) 0x0101);
        ZipExtraField extraField = new ZipCustomField("CustomField", "CustomValue");
        extraField.setHeaderId(headerId);

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.addAsFirstExtraField(extraField);
        ZipExtraField[] fields = zipArchiveEntry.getExtraFields();
        Assert.assertArrayEquals(new ZipExtraField[]{extraField}, fields);
    }

    @Test
    public void testRemoveExtraField() {
        ZipShort headerId = new ZipShort((short) 0x0101);
        ZipExtraField extraField = new ZipCustomField("CustomField", "CustomValue");
        extraField.setHeaderId(headerId);

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.addExtraField(extraField);
        zipArchiveEntry.removeExtraField(headerId);
        ZipExtraField[] fields = zipArchiveEntry.getExtraFields();
        Assert.assertArrayEquals(new ZipExtraField[0], fields);
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testGetExtraFieldNull() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = zipArchiveEntry.getExtraField(new ZipShort((short) 0x0101));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testSetExtraNull() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] extra = null;
        zipArchiveEntry.setExtra(extra);
    }
}