package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertNotNull(zipArchiveEntry);
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithEntry() throws ZipException {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertNotNull(zipArchiveEntry);
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithFile() {
        Path filePath = Paths.get("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(filePath.toFile(), "test.txt");
        Assert.assertNotNull(zipArchiveEntry);
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testSetAndGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodWithNegativeNumber() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(-1);
    }

    @Test
    public void testSetAndGetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setInternalAttributes(1);
        Assert.assertEquals(1, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testSetAndGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExternalAttributes(1);
        Assert.assertEquals(1, zipArchiveEntry.getExternalAttributes());
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
    public void testSetAndGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetAndGetExtraFields() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        byte[] extraData = new byte[10];
        for (int i = 0; i < extraData.length; i++) {
            extraData[i] = (byte) i;
        }

        ZipExtraField zipExtraField = new ZipExtraField(ZipShort.CRC32, extraData);
        zipArchiveEntry.addExtraField(zipExtraField);

        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(ZipShort.CRC32, zipArchiveEntry.getExtraFields()[0].getHeaderId());

        ZipArchiveEntry copiedZipArchiveEntry = zipArchiveEntry.clone();
        Assert.assertEquals(1, copiedZipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(ZipShort.CRC32, copiedZipArchiveEntry.getExtraFields()[0].getHeaderId());
    }

    @Test
    public void testClone() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        zipArchiveEntry.setInternalAttributes(1);
        zipArchiveEntry.setExternalAttributes(1);

        Map<ZipShort, ZipExtraField> extraFields = new HashMap<>();
        byte[] extraData = new byte[10];
        for (int i = 0; i < extraData.length; i++) {
            extraData[i] = (byte) i;
        }
        ZipExtraField zipExtraField = new ZipExtraField(ZipShort.CRC32, extraData);
        extraFields.put(ZipShort.CRC32, zipExtraField);
        zipArchiveEntry.setExtraFields(new ZipExtraField[0]);
        zipArchiveEntry.setExtraFields(extraFields.values().toArray(new ZipExtraField[0]));

        ZipArchiveEntry clonedZipArchiveEntry = (ZipArchiveEntry) zipArchiveEntry.clone();

        Assert.assertEquals(ZipEntry.STORED, clonedZipArchiveEntry.getMethod());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, clonedZipArchiveEntry.getPlatform());
        Assert.assertEquals(1, clonedZipArchiveEntry.getInternalAttributes());
        Assert.assertEquals(1, clonedZipArchiveEntry.getExternalAttributes());
        Assert.assertEquals(1, clonedZipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(ZipShort.CRC32, clonedZipArchiveEntry.getExtraFields()[0].getHeaderId());
    }

    @Test
    public void testSetLocalFileDataExtra() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        byte[] extraData = new byte[10];
        for (int i = 0; i < extraData.length; i++) {
            extraData[i] = (byte) i;
        }

        zipArchiveEntry.setExtra(extraData);

        Assert.assertArrayEquals(extraData, zipArchiveEntry.getLocalFileDataExtra());
    }

    @Test
    public void testSetCentralDirectoryExtra() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        byte[] extraData = new byte[10];
        for (int i = 0; i < extraData.length; i++) {
            extraData[i] = (byte) i;
        }

        byte[] centralDirectoryExtra = new byte[extraData.length + 2];
        System.arraycopy(extraData, 0, centralDirectoryExtra, 0, extraData.length);
        centralDirectoryExtra[extraData.length] = 0x02;
        centralDirectoryExtra[extraData.length + 1] = 0x00;

        zipArchiveEntry.setCentralDirectoryExtra(centralDirectoryExtra);

        Assert.assertArrayEquals(extraData, zipArchiveEntry.getExtraFields()[0].getLocalFileDataData());
    }

    @Test
    public void testGetName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testIsDirectory() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertFalse(zipArchiveEntry.isDirectory());

        zipArchiveEntry = new ZipArchiveEntry("test/");
        Assert.assertTrue(zipArchiveEntry.isDirectory());
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry2.hashCode());

        ZipArchiveEntry zipArchiveEntry3 = new ZipArchiveEntry("test2.txt");
        Assert.assertNotEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry3.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test.txt");
        Assert.assertTrue(zipArchiveEntry1.equals(zipArchiveEntry2));

        ZipArchiveEntry zipArchiveEntry3 = new ZipArchiveEntry("test2.txt");
        Assert.assertFalse(zipArchiveEntry1.equals(zipArchiveEntry3));

        ZipArchiveEntry zipArchiveEntry4 = zipArchiveEntry1;
        Assert.assertTrue(zipArchiveEntry1.equals(zipArchiveEntry4));
    }

    @Test
    public void testGetLastModifiedDate() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertNotNull(zipArchiveEntry.getLastModifiedDate());

        Date date = new Date();
        zipArchiveEntry.setTime(date.getTime());
        Assert.assertEquals(date, zipArchiveEntry.getLastModifiedDate());
    }
}