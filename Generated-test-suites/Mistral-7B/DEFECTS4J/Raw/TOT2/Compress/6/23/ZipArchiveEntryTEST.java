package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link ZipArchiveEntry}.
 */
public class ZipArchiveEntryTest {

    @Test
    public void testCreateZipArchiveEntryWithName() {
        String name = "testFile.txt";
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name);
        Assert.assertEquals(name, zipArchiveEntry.getName());
    }

    @Test
    public void testCreateZipArchiveEntryWithZipEntry() {
        String name = "testFile.txt";
        ZipEntry zipEntry = new ZipEntry(name);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals(name, zipArchiveEntry.getName());
    }

    @Test
    public void testCreateZipArchiveEntryWithFile() {
        String filePath = "/path/to/testFile.txt";
        File file = new File(filePath);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, file.getName());
        Assert.assertEquals(filePath, zipArchiveEntry.getName());
    }

    @Test
    public void testSetAndGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("testFile.txt");
        int method = ZipEntry.STORED;
        zipArchiveEntry.setMethod(method);
        Assert.assertEquals(method, zipArchiveEntry.getMethod());
    }

    @Test
    public void testSetUnsupportedCompressionMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("testFile.txt");
        int method = ZipEntry.UNKNOWN;
        Assert.assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
        zipArchiveEntry.setMethod(method);
        Assert.assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("testFile.txt");
        int method = -1;
        zipArchiveEntry.setMethod(method);
    }

    @Test
    public void testSetAndGetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("testFile.txt");
        int internalAttributes = 0x01;
        zipArchiveEntry.setInternalAttributes(internalAttributes);
        Assert.assertEquals(internalAttributes, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testSetAndGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("testFile.txt");
        long externalAttributes = 0x01;
        zipArchiveEntry.setExternalAttributes(externalAttributes);
        Assert.assertEquals(externalAttributes, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("testFile.txt");
        int unixMode = 0755;
        zipArchiveEntry.setUnixMode(unixMode);
        Assert.assertEquals(unixMode, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("testFile.txt");
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("testFile.txt");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetAndGetExtraFields() throws ZipException {
        byte[] extraData = new byte[0];
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("testFile.txt");
        ZipExtraField extraField = new ZipExtraField("TestExtraField", "TestData".getBytes());
        zipArchiveEntry.addExtraField(extraField);
        zipArchiveEntry.setExtra(extraData);
        Assert.assertArrayEquals(extraData, zipArchiveEntry.getExtra());
        ZipExtraField extractedExtraField = zipArchiveEntry.getExtraField(extraField.getHeaderId());
        Assert.assertEquals(extraField, extractedExtraField);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidExtraFieldData() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("testFile.txt");
        byte[] invalidExtraData = new byte[1];
        zipArchiveEntry.setExtra(invalidExtraData);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullExtraFieldData() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("testFile.txt");
        zipArchiveEntry.setExtra(null);
    }

    @Test
    public void testToString() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("testFile.txt");
        String expectedToString = "ZipArchiveEntry{name='testFile.txt', size=0, compressedSize=0, method=(-1), internalAttributes=0, externalAttributes=0, unixMode=0, platform=PLATFORM_FAT, extraFields=[]}";
        String toString = zipArchiveEntry.toString();
        Assert.assertEquals(expectedToString, toString);
    }
}