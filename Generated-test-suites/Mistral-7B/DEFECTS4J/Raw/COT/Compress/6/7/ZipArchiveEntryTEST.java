package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertNull(entry.getSize());
        Assert.assertEquals(0, entry.getTime());
        Assert.assertEquals(ZipEntry.STORED, entry.getMethod());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        Assert.assertNull(entry.getInternalAttributes());
        Assert.assertEquals(0, entry.getExternalAttributes());
        Assert.assertNull(entry.getExtraFields());
    }

    @Test
    public void testConstructorWithZipEntry() throws IOException {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(123, entry.getSize());
        Assert.assertEquals(new Date(1234567890123L).getTime(), entry.getTime());
        Assert.assertEquals(ZipEntry.STORED, entry.getMethod());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        Assert.assertEquals(0, entry.getInternalAttributes());
        Assert.assertEquals(0, entry.getExternalAttributes());
        Assert.assertNull(entry.getExtraFields());
    }

    @Test
    public void testConstructorWithZipArchiveEntry() throws IOException {
        ZipArchiveEntry originalEntry = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry copyEntry = new ZipArchiveEntry(originalEntry);
        Assert.assertEquals("test.txt", copyEntry.getName());
        Assert.assertEquals(originalEntry.getSize(), copyEntry.getSize());
        Assert.assertEquals(originalEntry.getTime(), copyEntry.getTime());
        Assert.assertEquals(originalEntry.getMethod(), copyEntry.getMethod());
        Assert.assertEquals(originalEntry.getPlatform(), copyEntry.getPlatform());
        Assert.assertEquals(originalEntry.getInternalAttributes(), copyEntry.getInternalAttributes());
        Assert.assertEquals(originalEntry.getExternalAttributes(), copyEntry.getExternalAttributes());
        Assert.assertArrayEquals(originalEntry.getExtraFields(), copyEntry.getExtraFields());
    }

    @Test
    public void testConstructorWithFileAndEntryName() {
        File testFile = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(testFile, "test.txt");
        Assert.assertEquals("test.txt", entry.getName());
        Assert.assertEquals(testFile.length(), entry.getSize());
        Assert.assertEquals(testFile.lastModified(), entry.getTime());
        Assert.assertEquals(ZipEntry.STORED, entry.getMethod());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        Assert.assertEquals(0, entry.getInternalAttributes());
        Assert.assertEquals(testFile.getCanRead() ? 1 : 0, entry.getInternalAttributes());
        Assert.assertEquals(testFile.isDirectory() ? 1 : 0, entry.getInternalAttributes());
        Assert.assertEquals(0, entry.getExternalAttributes());
        Assert.assertNull(entry.getExtraFields());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        Assert.assertEquals(0755, entry.getUnixMode());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUnixModeWithNegativeNumber() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(-1);
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(-1, entry.getMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        Assert.assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodWithInvalidNumber() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(42);
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        entry.setUnixMode(0755);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(0755);
        Assert.assertEquals(0755, entry.getInternalAttributes());
        entry.setInternalAttributes(0755 | ZipEntry.FILE_FLAG_DIRECTORY);
        Assert.assertEquals(0755 | ZipEntry.FILE_FLAG_DIRECTORY, entry.getInternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(12345);
        Assert.assertEquals(12345, entry.getExternalAttributes());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipGeneralPurposeCommentField("test comment");
        entry.setExtraFields(new ZipExtraField[]{extraField});
        Assert.assertArrayEquals(new ZipExtraField[]{extraField}, entry.getExtraFields());
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipGeneralPurposeCommentField("test comment");
        entry.addExtraField(extraField);
        Assert.assertArrayEquals(new ZipExtraField[]{extraField}, entry.getExtraFields());
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipGeneralPurposeCommentField("test comment");
        entry.addAsFirstExtraField(extraField);
        Assert.assertArrayEquals(new ZipExtraField[]{extraField}, entry.getExtraFields());
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipGeneralPurposeCommentField("test comment");
        entry.addExtraField(extraField);
        entry.removeExtraField(ZipShort.COMMENT);
        Assert.assertArrayEquals(new ZipExtraField[0], entry.getExtraFields());
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testRemoveExtraFieldWithNonExistentField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.removeExtraField(ZipShort.COMMENT);
    }

    @Test
    public void testGetExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipGeneralPurposeCommentField("test comment");
        entry.addExtraField(extraField);
        Assert.assertEquals(extraField, entry.getExtraField(ZipShort.COMMENT));
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testGetExtraFieldWithNonExistentField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertNull(entry.getExtraField(ZipShort.COMMENT));
    }

    @Test
    public void testSetExtraData() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[]{0x01, 0x02, 0x03, 0x04};
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(ExtraFieldUtils.parse(new ZipExtraField[]{
                new ZipGeneralPurposeCommentField("test comment")
        }).toByteArray(true));
        os.write(extraData, 0, extraData.length);
        entry.setExtra(os.toByteArray());
        Assert.assertArrayEquals(new ZipExtraField[]{
                new ZipGeneralPurposeCommentField("test comment"),
                new ZipByteArrayField(extraData)
        }, entry.getExtraFields());
    }

    @Test
    public void testSetCentralDirectoryExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] centralDirectoryExtra = ExtraFieldUtils.parse(
                new ZipExtraField[]{
                        new ZipGeneralPurposeCommentField("test comment"),
                        new ZipByteArrayField(new byte[]{0x01, 0x02})
                }).toByteArray(false);
        entry.setCentralDirectoryExtra(centralDirectoryExtra);
        Assert.assertArrayEquals(new ZipExtraField[]{
                new ZipGeneralPurposeCommentField("test comment"),
                new ZipByteArrayField(new byte[]{0x01, 0x02})
        }, entry.getExtraFields());
    }

    @Test
    public void testGetLocalFileDataExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[]{0x01, 0x02, 0x03, 0x04};
        entry.setExtra(extraData);
        Assert.assertArrayEquals(extraData, entry.getLocalFileDataExtra());
    }

    @Test
    public void testGetCentralDirectoryExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] centralDirectoryExtra = ExtraFieldUtils.parse(
                new ZipExtraField[]{
                        new ZipGeneralPurposeCommentField("test comment"),
                        new ZipByteArrayField(new byte[]{0x01, 0x02})
                }).toByteArray(false);
        entry.setCentralDirectoryExtra(centralDirectoryExtra);
        Assert.assertArrayEquals(centralDirectoryExtra, entry.getCentralDirectoryExtra());
    }
}