package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Test class for {@link ZipArchiveEntry}.
 */
public class ZipArchiveEntryTest {

    @Test
    public void testCreateFromZipEntry() throws IOException {
        byte[] extraData = new byte[]{"Test extra data".getBytes()};
        ZipExtraField extraField = new ZipExtraField(ZipShort.CRC32, extraData);
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
        zipEntry.setExtra(extraField.getBytes());

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals(ZipShort.CRC32, zipArchiveEntry.getExtraFields()[0].getHeaderId());
        Assert.assertArrayEquals(extraData, zipArchiveEntry.getExtraFields()[0].getData());
    }

    @Test
    public void testCreateFromFile() throws IOException {
        File inputFile = new File("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(inputFile, "test.txt");
        Assert.assertEquals(inputFile.length(), zipArchiveEntry.getSize());
        Assert.assertEquals(inputFile.lastModified(), zipArchiveEntry.getTime());
    }

    @Test
    public void testClone() throws IOException {
        byte[] extraData = new byte[]{"Test extra data".getBytes()};
        ZipExtraField extraField = new ZipExtraField(ZipShort.CRC32, extraData);
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
        zipEntry.setExtra(extraField.getBytes());

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) zipArchiveEntry.clone();
        Assert.assertNotSame(zipArchiveEntry, clonedEntry);
        Assert.assertEquals(zipArchiveEntry.getExtraFields(), clonedEntry.getExtraFields());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(0755);
        Assert.assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUnixModeNegative() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(-1);
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(zipArchiveEntry1, zipArchiveEntry2);

        ZipArchiveEntry zipArchiveEntry3 = new ZipArchiveEntry("test2.txt");
        Assert.assertNotEquals(zipArchiveEntry1, zipArchiveEntry3);

        zipArchiveEntry1.setName("test.txt2");
        Assert.assertNotEquals(zipArchiveEntry1, zipArchiveEntry2);
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());

        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
        zipEntry.setMethod(java.util.zip.ZipEntry.STORED);
        zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals(java.util.zip.ZipEntry.STORED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(java.util.zip.ZipEntry.STORED);
        Assert.assertEquals(java.util.zip.ZipEntry.STORED, zipArchiveEntry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodNegative() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(-1);
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());

        zipArchiveEntry.setUnixMode(0755);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        byte[] extraData = new byte[]{"Test extra data".getBytes()};
        ZipExtraField extraField = new ZipExtraField(ZipShort.CRC32, extraData);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{extraField});
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(ZipShort.CRC32, zipArchiveEntry.getExtraFields()[0].getHeaderId());
        Assert.assertArrayEquals(extraData, zipArchiveEntry.getExtraFields()[0].getData());
    }

    @Test
    public void testAddExtraField() {
        byte[] extraData = new byte[]{"Test extra data".getBytes()};
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtraField(ZipShort.CRC32, extraData);
        zipArchiveEntry.addExtraField(extraField);
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(ZipShort.CRC32, zipArchiveEntry.getExtraFields()[0].getHeaderId());
        Assert.assertArrayEquals(extraData, zipArchiveEntry.getExtraFields()[0].getData());
    }

    @Test
    public void testAddAsFirstExtraField() {
        byte[] extraData = new byte[]{"Test extra data".getBytes()};
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtraField(ZipShort.CRC32, extraData);
        zipArchiveEntry.addAsFirstExtraField(extraField);
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(ZipShort.CRC32, zipArchiveEntry.getExtraFields()[0].getHeaderId());
        Assert.assertArrayEquals(extraData, zipArchiveEntry.getExtraFields()[0].getData());
    }

    @Test
    public void testRemoveExtraField() {
        byte[] extraData = new byte[]{"Test extra data".getBytes()};
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtraField(ZipShort.CRC32, extraData);
        zipArchiveEntry.addExtraField(extraField);
        zipArchiveEntry.removeExtraField(ZipShort.CRC32);
        Assert.assertArrayEquals(new ZipExtraField[0], zipArchiveEntry.getExtraFields());
    }

    @Test
    public void testGetExtraField() {
        byte[] extraData = new byte[]{"Test extra data".getBytes()};
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtraField(ZipShort.CRC32, extraData);
        zipArchiveEntry.addExtraField(extraField);
        Assert.assertEquals(extraField, zipArchiveEntry.getExtraField(ZipShort.CRC32));
        Assert.assertNull(zipArchiveEntry.getExtraField(ZipShort.UNDEFINED));
    }

    @Test
    public void testSetExtra() throws IOException {
        byte[] extraData = new byte[]{"Test extra data".getBytes()};
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        InputStream inputStream = new ByteArrayInputStream(extraData);
        OutputStream outputStream = new ByteArrayOutputStream();
        zipArchiveEntry.setExtra(inputStream, outputStream);
        Assert.assertNotNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(ZipShort.CRC32, zipArchiveEntry.getExtraFields()[0].getHeaderId());
        Assert.assertArrayEquals(extraData, zipArchiveEntry.getExtraFields()[0].getData());
    }

    @Test
    public void testSetCentralDirectoryExtra() throws IOException {
        byte[] extraData = new byte[]{"Test extra data".getBytes()};
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setCentralDirectoryExtra(extraData);
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(ZipShort.CRC32, zipArchiveEntry.getExtraFields()[0].getHeaderId());
        Assert.assertArrayEquals(extraData, zipArchiveEntry.getExtraFields()[0].getData());
    }

    @Test
    public void testGetLocalFileDataExtra() throws IOException {
        byte[] extraData = new byte[]{"Test extra data".getBytes()};
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        InputStream inputStream = new ByteArrayInputStream(extraData);
        OutputStream outputStream = new ByteArrayOutputStream();
        zipArchiveEntry.setExtra(inputStream, outputStream);
        byte[] localFileDataExtra = zipArchiveEntry.getLocalFileDataExtra();
        Assert.assertArrayEquals(extraData, localFileDataExtra);
    }

    @Test
    public void testGetCentralDirectoryExtra() throws IOException {
        byte[] extraData = new byte[]{"Test extra data".getBytes()};
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setCentralDirectoryExtra(extraData);
        byte[] centralDirectoryExtra = zipArchiveEntry.getCentralDirectoryExtra();
        Assert.assertArrayEquals(extraData, centralDirectoryExtra);
    }

    @Test
    public void testHashCode() {
        String name1 = "test.txt";
        String name2 = "test2.txt";
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry(name1);
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry(name2);
        Assert.assertEquals(name1.hashCode(), zipArchiveEntry1.hashCode());
        Assert.assertEquals(name2.hashCode(), zipArchiveEntry2.hashCode());
        Assert.assertNotEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry2.hashCode());
    }

    @Test
    public void testGetSize() throws IOException {
        File file = new File("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file);
        Assert.assertEquals(file.length(), zipArchiveEntry.getSize());
    }

    @Test
    public void testGetTime() throws IOException {
        File file = new File("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file);
        Assert.assertEquals(file.lastModified(), zipArchiveEntry.getTime());
    }

    @Test
    public void testToString() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("ZipArchiveEntry{name='test.txt', size=0, method=-1, internalAttributes=0, platform=0, externalAttributes=0, extraFields=[], time=0}", zipArchiveEntry.toString());
    }

    @Test
    public void testParseFromLocalFileData() throws IOException {
        byte[] extraData = new byte[]{"Test extra data".getBytes()};
        InputStream inputStream = new ByteArrayInputStream(extraData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExtra(inputStream, outputStream);
        ZipExtraField extraField = new ZipExtraField(ZipShort.CRC32, extraData);
        extraField.parseFromLocalFileData(outputStream.toByteArray(), 0, outputStream.size());
        Assert.assertEquals(ZipShort.CRC32, extraField.getHeaderId());
        Assert.assertArrayEquals(extraData, extraField.getData());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws IOException {
        byte[] extraData = new byte[]{"Test extra data".getBytes()};
        InputStream inputStream = new ByteArrayInputStream(extraData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setCentralDirectoryExtra(outputStream.toByteArray());
        ZipExtraField extraField = new ZipExtraField(ZipShort.CRC32, extraData);
        extraField.parseFromCentralDirectoryData(outputStream.toByteArray(), 0, outputStream.size());
        Assert.assertEquals(ZipShort.CRC32, extraField.getHeaderId());
        Assert.assertArrayEquals(extraData, extraField.getData());
    }
}