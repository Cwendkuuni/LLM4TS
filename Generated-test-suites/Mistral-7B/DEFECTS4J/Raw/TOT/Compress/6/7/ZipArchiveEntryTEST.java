package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ExtraField;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for {@link ZipArchiveEntry}.
 */
public class ZipArchiveEntryTest {

    @Test
    public void testBasicConstruction() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertNull(entry.getExtraFields());
        Assert.assertEquals(-1, entry.getMethod());
        Assert.assertNull(entry.getInternalAttributes());
        Assert.assertEquals(0, entry.getExternalAttributes());
        Assert.assertNull(entry.getPlatform());
        Assert.assertFalse(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testConstructionFromZipEntry() throws ZipException {
        String testEntryName = "test.txt";
        ZipEntry zipEntry = new ZipEntry(testEntryName);
        ZipArchiveEntry archiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals(testEntryName, archiveEntry.getName());
        Assert.assertNull(archiveEntry.getExtraFields());
        Assert.assertEquals(-1, archiveEntry.getMethod());
        Assert.assertNull(archiveEntry.getInternalAttributes());
        Assert.assertEquals(0, archiveEntry.getExternalAttributes());
        Assert.assertEquals(org.apache.commons.compress.archivers.zip.ZipArchiveEntry.PLATFORM_FAT, archiveEntry.getPlatform());
        Assert.assertFalse(archiveEntry.isSupportedCompressionMethod());
    }

    @Test
    public void testConstructionFromZipArchiveEntry() throws ZipException, CloneNotSupportedException {
        String testEntryName = "test.txt";
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(testEntryName);
        ZipArchiveEntry clone = (ZipArchiveEntry) zipArchiveEntry.clone();
        Assert.assertEquals(testEntryName, clone.getName());
        Assert.assertNull(clone.getExtraFields());
        Assert.assertEquals(-1, clone.getMethod());
        Assert.assertNull(clone.getInternalAttributes());
        Assert.assertEquals(0, clone.getExternalAttributes());
        Assert.assertEquals(org.apache.commons.compress.archivers.zip.ZipArchiveEntry.PLATFORM_FAT, clone.getPlatform());
        Assert.assertFalse(clone.isSupportedCompressionMethod());
    }

    @Test
    public void testConstructionFromFile() {
        File inputFile = new File("nonexistentfile.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(inputFile, "test.txt");
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertNull(entry.getExtraFields());
        Assert.assertEquals(-1, entry.getMethod());
        Assert.assertEquals(inputFile.length(), entry.getSize());
        Assert.assertEquals(inputFile.lastModified(), entry.getTime());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        Assert.assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testGetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        Assert.assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, entry.getMethod());
        Assert.assertTrue(entry.isSupportedCompressionMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(-1);
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, entry.getMethod());
        Assert.assertTrue(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(0, entry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(0x0F);
        Assert.assertEquals(0x0F, entry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(0, entry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(0x0F0F0F0F0F0F0F0F);
        Assert.assertEquals(0x0F0F0F0F0F0F0F0F, entry.getExternalAttributes());
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ExtraField testExtraField = new ExtraField(ZipShort.UNIX_USER, "user".getBytes());
        entry.addExtraField(testExtraField);
        Assert.assertNotNull(entry.getExtraFields());
        Assert.assertEquals(1, entry.getExtraFields().length);
        ZipArchiveEntry clone = (ZipArchiveEntry) entry.clone();
        Assert.assertEquals(1, clone.getExtraFields().length);
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ExtraField testExtraField = new ExtraField(ZipShort.UNIX_USER, "user".getBytes());
        entry.addAsFirstExtraField(testExtraField);
        Assert.assertNotNull(entry.getExtraFields());
        Assert.assertEquals(1, entry.getExtraFields().length);
        ZipArchiveEntry clone = (ZipArchiveEntry) entry.clone();
        Assert.assertEquals(1, clone.getExtraFields().length);
        Assert.assertEquals(testExtraField, clone.getExtraFields()[0]);
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ExtraField testExtraField = new ExtraField(ZipShort.UNIX_USER, "user".getBytes());
        entry.addExtraField(testExtraField);
        entry.removeExtraField(ZipShort.UNIX_USER);
        Assert.assertNull(entry.getExtraFields());
    }

    @Test
    public void testGetExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ExtraField testExtraField = new ExtraField(ZipShort.UNIX_USER, "user".getBytes());
        entry.addExtraField(testExtraField);
        Assert.assertEquals(testExtraField, entry.getExtraField(ZipShort.UNIX_USER));
    }

    @Test
    public void testSetExtraData() throws ZipException {
        String testExtraFieldName = "test";
        byte[] extraData = new byte[12];
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExtraFields(new ExtraField[]{new ExtraField(ZipShort.COMMENT, testExtraFieldName.getBytes())});
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(ExtraFieldUtils.encodeLocalFileData(new ExtraField[]{new ExtraField(ZipShort.COMMENT, testExtraFieldName.getBytes()),
                new ExtraField(ZipShort.COMMENT, testExtraFieldName.getBytes())}));
        entry.setExtra(byteStream.toByteArray());
        byte[] testResult = entry.getLocalFileDataExtra();
        Assert.assertArrayEquals(extraData, testResult);
    }

    @Test
    public void testSetExtraDataWithInvalidData() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        try {
            byte[] invalidData = new byte[1];
            entry.setExtra(invalidData);
            Assert.fail();
        } catch (RuntimeException e) {
            // expected
        }
    }

    @Test
    public void testSetCentralDirectoryExtra() {
        String testExtraFieldName = "test";
        byte[] extraData = new byte[12];
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExtraFields(new ExtraField[]{new ExtraField(ZipShort.COMMENT, testExtraFieldName.getBytes())});
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(ExtraFieldUtils.encodeCentralDirectoryData(new ExtraField[]{new ExtraField(ZipShort.COMMENT, testExtraFieldName.getBytes()),
                new ExtraField(ZipShort.COMMENT, testExtraFieldName.getBytes())}));
        entry.setCentralDirectoryExtra(byteStream.toByteArray());
        byte[] testResult = entry.getCentralDirectoryExtra();
        Assert.assertArrayEquals(extraData, testResult);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        ZipArchiveEntry originalEntry = new ZipArchiveEntry("test.txt");
        originalEntry.setName("test_cloned.txt");
        originalEntry.setMethod(ZipEntry.STORED);
        originalEntry.setInternalAttributes(0x0F);
        originalEntry.setExternalAttributes(0x0F0F0F0F0F0F0F0F);
        ExtraField testExtraField = new ExtraField(ZipShort.UNIX_USER, "user".getBytes());
        originalEntry.addExtraField(testExtraField);
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) originalEntry.clone();
        Assert.assertEquals(originalEntry.getName(), clonedEntry.getName());
        Assert.assertEquals(originalEntry.getMethod(), clonedEntry.getMethod());
        Assert.assertEquals(originalEntry.getInternalAttributes(), clonedEntry.getInternalAttributes());
        Assert.assertEquals(originalEntry.getExternalAttributes(), clonedEntry.getExternalAttributes());
        Assert.assertArrayEquals(originalEntry.getExtraFields(), clonedEntry.getExtraFields());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test1.txt");
        Assert.assertEquals(entry1, entry1);
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test2.txt");
        Assert.assertNotEquals(entry1, entry2);
        ZipArchiveEntry entry3 = new ZipArchiveEntry("test1.txt");
        Assert.assertEquals(entry1, entry3);
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        String name = "test.txt";
        Assert.assertEquals(name.hashCode(), entry.hashCode());
    }

    @Test
    public void testToString() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("ZipArchiveEntry[name=test.txt, size=0, compLevel=-1, method=, isDirectory=false]", entry.toString());
    }

    @Test
    public void testGetLocalFileDataExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertArrayEquals(new byte[0], entry.getLocalFileDataExtra());
    }

    @Test
    public void testGetCentralDirectoryExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertArrayEquals(new byte[0], entry.getCentralDirectoryExtra());
    }

    @Test
    public void testIsDirectory() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertFalse(entry.isDirectory());
        ZipArchiveEntry directoryEntry = new ZipArchiveEntry("test/");
        Assert.assertTrue(directoryEntry.isDirectory());
    }

    @Test
    public void testGetName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testGetLastModifiedDate() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Date date = new Date();
        entry.setTime(date.getTime());
        Assert.assertEquals(date, entry.getLastModifiedDate());
    }
}