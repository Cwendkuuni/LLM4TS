import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testConstructorWithZipEntry() throws IOException {
        ZipArchiveEntry originalEntry = new ZipArchiveEntry(new ZipEntry("test.txt"));
        ZipArchiveEntry copyEntry = new ZipArchiveEntry(originalEntry);
        Assert.assertEquals(originalEntry.getName(), copyEntry.getName());
        Assert.assertEquals(originalEntry.getSize(), copyEntry.getSize());
        Assert.assertEquals(originalEntry.getTime(), copyEntry.getTime());
        Assert.assertEquals(originalEntry.getMethod(), copyEntry.getMethod());
        Assert.assertEquals(originalEntry.getInternalAttributes(), copyEntry.getInternalAttributes());
        Assert.assertEquals(originalEntry.getExternalAttributes(), copyEntry.getExternalAttributes());
        Assert.assertEquals(originalEntry.getExtraFields(), copyEntry.getExtraFields());
    }

    @Test
    public void testConstructorWithFile() {
        File inputFile = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(inputFile, "test.txt");
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(inputFile.length(), entry.getSize());
        Assert.assertEquals(inputFile.lastModified(), entry.getTime());
        Assert.assertNull(entry.getMethod());
        Assert.assertArrayEquals(new byte[0], entry.getExtra());
    }

    @Test
    public void testSetAndGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(-1, entry.getMethod());
        entry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, entry.getMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        Assert.assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertFalse(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipEntry.STORED);
        Assert.assertTrue(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        Assert.assertTrue(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(0, entry.getInternalAttributes());
        entry.setInternalAttributes(ZipEntry.FLAG_UNCHANGED | ZipEntry.FLAG_NAMED);
        Assert.assertEquals(ZipEntry.FLAG_UNCHANGED | ZipEntry.FLAG_NAMED, entry.getInternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        Assert.assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipShort headerId = new ZipShort(20001);
        ZipExtraField extraField = new GenericZipExtraField(headerId, new byte[10], "test");
        entry.setExtraFields(new ZipExtraField[]{extraField});
        Assert.assertEquals(1, entry.getExtraFields().length);
        Assert.assertEquals(headerId, entry.getExtraField(headerId).getHeaderId());
        Assert.assertEquals("test", entry.getExtraField(headerId).getData().toString());
    }

    @Test
    public void testSetLocalFileDataExtra() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipShort headerId = new ZipShort(20001);
        byte[] data = "test".getBytes();
        entry.setExtra(new byte[10]);
        entry.setExtraFields(new ZipExtraField[]{new GenericZipExtraField(headerId, data)});
        entry.setLocalFileDataExtra(data);
        byte[] result = entry.getLocalFileDataExtra();
        Assert.assertArrayEquals(data, result);
    }

    @Test
    public void testSetCentralDirectoryExtra() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipShort headerId = new ZipShort(20001);
        byte[] data = "test".getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ExtraFieldUtils.writeLocalFileData(new ZipExtraField[]{new GenericZipExtraField(headerId, data)}));
        entry.setCentralDirectoryExtra(outputStream.toByteArray());
        byte[] result = entry.getCentralDirectoryExtra();
        Assert.assertArrayEquals(outputStream.toByteArray(), result);
    }

    @Test
    public void testClone() throws CloneNotSupportedException, IOException {
        ZipArchiveEntry originalEntry = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) originalEntry.clone();
        Assert.assertEquals(originalEntry.getName(), clonedEntry.getName());
        Assert.assertEquals(originalEntry.getSize(), clonedEntry.getSize());
        Assert.assertEquals(originalEntry.getTime(), clonedEntry.getTime());
        Assert.assertEquals(originalEntry.getMethod(), clonedEntry.getMethod());
        Assert.assertEquals(originalEntry.getInternalAttributes(), clonedEntry.getInternalAttributes());
        Assert.assertEquals(originalEntry.getExternalAttributes(), clonedEntry.getExternalAttributes());
        Assert.assertEquals(originalEntry.getExtraFields(), clonedEntry.getExtraFields());
    }

    @Test
    public void testGetHashCode() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test2.txt");
        Assert.assertEquals(entry1.hashCode(), entry1.hashCode());
        Assert.assertEquals(entry1.hashCode(), entry1.getName().hashCode());
        Assert.assertNotEquals(entry1.hashCode(), entry2.hashCode());
        Assert.assertNotEquals(entry1.hashCode(), entry2.getName().hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry entry3 = new ZipArchiveEntry("test2.txt");
        Assert.assertTrue(entry1.equals(entry1));
        Assert.assertTrue(entry1.equals(entry2));
        Assert.assertFalse(entry1.equals(entry3));
        Assert.assertFalse(entry1.equals(null));
        Assert.assertFalse(entry1.equals("test"));
    }

    @Test
    public void testToString() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        String result = entry.toString();
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("name", "test.txt");
        expectedMap.put("size", 0);
        expectedMap.put("time", 0);
        expectedMap.put("method", "-1");
        expectedMap.put("internalAttributes", 0);
        expectedMap.put("externalAttributes", 0);
        expectedMap.put("extraFields", new Object[0]);
        String expected = "ZipArchiveEntry{name='test.txt', size=0, time=0, method=-1, internalAttributes=0, externalAttributes=0, extraFields=[]}";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetLastModifiedDate() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Date date = new Date();
        entry.setTime(date.getTime());
        Assert.assertEquals(date, entry.getLastModifiedDate());
    }

    @Test
    public void testReadAndWriteData() throws IOException {
        String data = "test data";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setSize(data.length());
        entry.setTime(System.currentTimeMillis());
        entry.setData(inputStream);
        byte[] readData = new byte[data.length()];
        int length = entry.read(readData);
        Assert.assertEquals(data.length(), length);
        Assert.assertArrayEquals(data.getBytes(), readData);

        entry.setData(outputStream.toByteArray());
        outputStream.write(data.getBytes());
        outputStream.close();
        byte[] writeData = entry.getExtra();
        Assert.assertArrayEquals(data.getBytes(), writeData);

        InputStream testInputStream = new ByteArrayInputStream(writeData);
        int readLength = IOUtils.copy(testInputStream, outputStream, data.length());
        Assert.assertEquals(data.length(), readLength);
        Assert.assertArrayEquals(data.getBytes(), outputStream.toByteArray());
    }

    @Test
    public void testGetLocalFileDataExtraWithLargeData() throws IOException {
        byte[] largeData = new byte[1024 * 1024];
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExtra(largeData);
        byte[] readData = new byte[largeData.length];
        int length = entry.getLocalFileDataExtra().length;
        Assert.assertEquals(largeData.length, length);
        System.arraycopy(entry.getLocalFileDataExtra(), 0, readData, 0, length);
        Assert.assertArrayEquals(largeData, readData);
    }

    @Test(expected = RuntimeException.class)
    public void testSetExtraWithInvalidData() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] invalidData = new byte[]{-1, 2};
        entry.setExtra(invalidData);
    }

    @Test
    public void testSetUnsupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.FILTERED);
        Assert.assertFalse(entry.isSupportedCompressionMethod());
    }
}