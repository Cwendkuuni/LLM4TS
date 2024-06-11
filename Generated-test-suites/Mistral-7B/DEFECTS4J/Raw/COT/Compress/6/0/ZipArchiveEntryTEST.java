import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructor_WithName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructor_WithZipEntry() throws IOException {
        byte[] testData = "Test Data".getBytes();
        String testName = "test.txt";
        ZipEntry zipEntry = new ZipEntry(testName);
        zipEntry.setSize(testData.length);
        zipEntry.setExtra(testData);

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals(testName, zipArchiveEntry.getName());
        Assert.assertEquals(testData.length, zipArchiveEntry.getSize());
    }

    @Test
    public void testConstructor_WithZipArchiveEntry() throws IOException, CloneNotSupportedException {
        byte[] testData = "Test Data".getBytes();
        String testName = "test.txt";
        ZipEntry zipEntry = new ZipEntry(testName);
        zipEntry.setSize(testData.length);
        zipEntry.setExtra(testData);

        ZipArchiveEntry originalZipArchiveEntry = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry cloneZipArchiveEntry = (ZipArchiveEntry) originalZipArchiveEntry.clone();

        Assert.assertEquals(originalZipArchiveEntry.getName(), cloneZipArchiveEntry.getName());
        Assert.assertEquals(originalZipArchiveEntry.getSize(), cloneZipArchiveEntry.getSize());
        Assert.assertArrayEquals(originalZipArchiveEntry.getExtra(), cloneZipArchiveEntry.getExtra());
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
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethod_WithNegativeValue() {
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
        zipArchiveEntry.setInternalAttributes(ZipEntry.FLAG_UNIX_GROUP);
        Assert.assertEquals(ZipEntry.FLAG_UNIX_GROUP, zipArchiveEntry.getInternalAttributes());
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
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        byte[] testData = "Test Data".getBytes();
        ZipShort testHeaderId = new ZipShort((short) 0x0001);
        ZipExtraField testExtraField = new ZipExtendedTimestamps();
        testExtraField.setData(new byte[10]);

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{testExtraField});
        Assert.assertNotNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
    }

    @Test
    public void testAddExtraField() {
        byte[] testData = "Test Data".getBytes();
        ZipShort testHeaderId = new ZipShort((short) 0x0001);
        ZipExtraField testExtraField = new ZipExtendedTimestamps();
        testExtraField.setData(new byte[10]);

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{testExtraField});
        zipArchiveEntry.addExtraField(testExtraField);
        Assert.assertNotNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(2, zipArchiveEntry.getExtraFields().length);
    }

    @Test
    public void testRemoveExtraField() {
        byte[] testData = "Test Data".getBytes();
        ZipShort testHeaderId = new ZipShort((short) 0x0001);
        ZipExtraField testExtraField1 = new ZipExtendedTimestamps();
        testExtraField1.setData(new byte[10]);

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{testExtraField1});
        zipArchiveEntry.removeExtraField(testHeaderId);
        Assert.assertNull(zipArchiveEntry.getExtraField(testHeaderId));
        Assert.assertNotNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(0, zipArchiveEntry.getExtraFields().length);
    }

    @Test
    public void testParseFromLocalFileData() throws IOException {
        byte[] testData = "Test Data".getBytes();
        ZipShort testHeaderId = new ZipShort((short) 0x0001);
        ZipExtendedTimestamps testExtraField = new ZipExtendedTimestamps();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        testExtraField.writeTo(byteArrayOutputStream);
        byte[] localExtraData = byteArrayOutputStream.toByteArray();
        testExtraField.parseFromLocalFileData(localExtraData, 0, localExtraData.length);

        Assert.assertEquals(testExtraField.getCreationTime(), new Date(testExtraField.getCreationTime()));
        Assert.assertEquals(testExtraField.getModificationTime(), new Date(testExtraField.getModificationTime()));
    }

    @Test
    public void testParseFromCentralDirectoryData() throws IOException {
        byte[] testData = "Test Data".getBytes();
        ZipShort testHeaderId = new ZipShort((short) 0x0001);
        ZipExtendedTimestamps testExtraField = new ZipExtendedTimestamps();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        testExtraField.writeTo(byteArrayOutputStream);

        String testName = "test.txt";
        ZipEntry zipEntry = new ZipEntry(testName);
        zipEntry.setSize(testData.length);
        zipEntry.setExtra(testData);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ZipFile zipFile = new ZipFile(new File("test.zip"), true);
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream("test.zip"));
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(testData, 0, testData.length);
        zipOutputStream.closeEntry();
        zipOutputStream.finish();
        zipFile.close();

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        zipArchiveEntry.parseFromCentralDirectoryData(testData, 0, testData.length);

        Assert.assertEquals(testExtraField.getCreationTime(), zipArchiveEntry.getCreationTime());
        Assert.assertEquals(testExtraField.getModificationTime(), zipArchiveEntry.getLastModifiedDate().getTime());
    }

    @Test
    public void testSetLocalFileDataExtra() throws IOException {
        byte[] testData = "Test Data".getBytes();
        ZipShort testHeaderId = new ZipShort((short) 0x0001);
        ZipExtendedTimestamps testExtraField = new ZipExtendedTimestamps();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        testExtraField.writeTo(byteArrayOutputStream);
        byte[] localExtraData = byteArrayOutputStream.toByteArray();

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{testExtraField});
        zipArchiveEntry.setLocalFileDataExtra(localExtraData);
        Assert.assertArrayEquals(localExtraData, zipArchiveEntry.getExtra());
    }

    @Test
    public void testSetCentralDirectoryExtra() throws IOException {
        byte[] testData = "Test Data".getBytes();
        ZipShort testHeaderId = new ZipShort((short) 0x0001);
        ZipExtendedTimestamps testExtraField = new ZipExtendedTimestamps();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        testExtraField.writeTo(byteArrayOutputStream);
        byte[] centralDirectoryExtraData = byteArrayOutputStream.toByteArray();

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{testExtraField});
        zipArchiveEntry.setCentralDirectoryExtra(centralDirectoryExtraData);
        Assert.assertArrayEquals(centralDirectoryExtraData, zipArchiveEntry.getCentralDirectoryExtra());
    }
}