import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.utils.io.ByteArrayInputStream;
import org.apache.commons.compress.utils.io.ByteArrayOutputStream;
import org.apache.commons.compress.utils.io.ByteStreams;
import org.junit.Assert;
import org.junit.Test;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", zipEntry.getName());
    }

    @Test
    public void testConstructorWithZipEntry() throws Exception {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithZipArchiveEntry() throws Exception {
        ZipArchiveEntry originalZipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry clonedZipArchiveEntry = (ZipArchiveEntry) originalZipArchiveEntry.clone();
        Assert.assertEquals(originalZipArchiveEntry.getName(), clonedZipArchiveEntry.getName());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(0644);
        Assert.assertEquals(0644, zipArchiveEntry.getUnixMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUnixModeWithNegativeNumber() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(-1);
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());

        zipArchiveEntry.setUnixMode(0644);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField zipExtraField = new ZipExtendedTimestamps();
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{zipExtraField});
        Assert.assertArrayEquals(new ZipExtraField[]{zipExtraField}, zipArchiveEntry.getExtraFields());
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField zipExtraField = new ZipExtendedTimestamps();
        zipArchiveEntry.addExtraField(zipExtraField);
        Assert.assertArrayEquals(new ZipExtraField[]{zipExtraField}, zipArchiveEntry.getExtraFields());
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField zipExtraField = new ZipExtendedTimestamps();
        zipArchiveEntry.addAsFirstExtraField(zipExtraField);
        Assert.assertArrayEquals(new ZipExtraField[]{zipExtraField}, zipArchiveEntry.getExtraFields());
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField zipExtraField = new ZipExtendedTimestamps();
        zipArchiveEntry.addExtraField(zipExtraField);
        zipArchiveEntry.removeExtraField(zipExtraField.getHeaderId());
        Assert.assertArrayEquals(new ZipExtraField[0], zipArchiveEntry.getExtraFields());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetExtraFieldWithNoExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.getExtraField(new ZipShort(1));
    }

    @Test
    public void testSetExtra() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField zipExtraField = new ZipExtendedTimestamps();

        byte[] extraBytes = zipExtraField.getBytes();
        zipArchiveEntry.setExtra(extraBytes);
        Assert.assertArrayEquals(extraBytes, zipArchiveEntry.getLocalFileDataExtra());
    }

    @Test(expected = RuntimeException.class)
    public void testSetExtraWithInvalidExtraData() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] invalidExtraData = {0x01, 0x02, 0x03};
        zipArchiveEntry.setExtra(invalidExtraData);
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry2.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test.txt");
        Assert.assertTrue(zipArchiveEntry1.equals(zipArchiveEntry2));
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test.txt");
        Object differentObject = new Object();
        Assert.assertFalse(zipArchiveEntry1.equals(differentObject));
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());

        zipArchiveEntry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());

        zipArchiveEntry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodWithInvalidMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(-1);
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(0, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setInternalAttributes(File.DIRECTORY);
        Assert.assertEquals(File.DIRECTORY, zipArchiveEntry.getInternalAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInternalAttributesWithInvalidAttribute() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setInternalAttributes(123);
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(0, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExternalAttributes(File.READ_ONLY);
        Assert.assertEquals(File.READ_ONLY, zipArchiveEntry.getExternalAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetExternalAttributesWithInvalidAttribute() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExternalAttributes(123);
    }

    @Test
    public void testIsDirectory() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertFalse(zipArchiveEntry.isDirectory());

        zipArchiveEntry.setName("test/");
        Assert.assertTrue(zipArchiveEntry.isDirectory());
    }

    @Test
    public void testGetName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());

        zipArchiveEntry.setName("test/");
        Assert.assertEquals("test/", zipArchiveEntry.getName());
    }

    @Test
    public void testSetName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setName("test.txt");
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testGetLastModifiedDate() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteStreams.write(byteArrayOutputStream, new byte[0]);
        byte[] localFileDataExtra = byteArrayOutputStream.toByteArray();
        zipArchiveEntry.setLocalFileDataExtra(localFileDataExtra);

        ZipExtraField zipExtendedTimestamps = new ZipExtendedTimestamps();
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{zipExtendedTimestamps});

        Date expectedDate = new Date(123L);
        zipExtendedTimestamps.setLocalFileModTime(expectedDate.getTime());

        Date actualDate = zipArchiveEntry.getLastModifiedDate();
        Assert.assertEquals(expectedDate, actualDate);
    }
}

class ZipExtendedTimestamps extends ZipExtraField {
    private static final int HEADER_ID = 21;
    private long localFileModTime;

    public ZipExtendedTimestamps() {
        super(HEADER_ID);
    }

    public void parseFromCentralDirectoryData(byte[] data, int offset, int length) {
        throw new UnsupportedOperationException();
    }

    public void parseFromLocalFileData(byte[] data, int offset, int length) {
        localFileModTime = ByteStreams.readIntBE(data, offset);
    }

    public byte[] getBytes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] timeBytes = ByteStreams.toByteArray(new Long(localFileModTime).longValue());
        byteArrayOutputStream.write(new byte[]{HEADER_ID});
        byteArrayOutputStream.write(timeBytes, 0, timeBytes.length);
        return byteArrayOutputStream.toByteArray();
    }

    public long getLocalFileModTime() {
        return localFileModTime;
    }

    public void setLocalFileModTime(long localFileModTime) {
        this.localFileModTime = localFileModTime;
    }
}