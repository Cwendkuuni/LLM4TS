package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

/**
 * Test class for ZipArchiveEntry.
 */
public class ZipArchiveEntryTest {

    

    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithEntry() throws ZipException {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithAnotherZipArchiveEntry() throws ZipException {
        ZipArchiveEntry originalZipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry newZipArchiveEntry = new ZipArchiveEntry(originalZipArchiveEntry);
        Assert.assertEquals(originalZipArchiveEntry.getName(), newZipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithFile() {
        File file = new File("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, "test.txt");
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
        Assert.assertEquals(file.length(), zipArchiveEntry.getSize());
        Assert.assertEquals(file.lastModified(), zipArchiveEntry.getTime());
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
    public void testSetMethodNegativeValue() {
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
        zipArchiveEntry.setInternalAttributes(0x01);
        Assert.assertEquals(0x01, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(0L, zipArchiveEntry.getExternalAttributes());
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
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtraField("TestExtraField", ZipShort.create((short) 0x01));
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{extraField});
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
    }

    @Test
    public void testGetExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        Assert.assertEquals(0, extraFields.length);
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtraField("TestExtraField", ZipShort.create((short) 0x01));
        zipArchiveEntry.addExtraField(extraField);
        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        Assert.assertEquals(1, extraFields.length);
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtraField("TestExtraField", ZipShort.create((short) 0x01));
        zipArchiveEntry.addAsFirstExtraField(extraField);
        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        Assert.assertEquals(1, extraFields.length);
        Assert.assertEquals(extraField, extraFields[0]);
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtraField("TestExtraField", ZipShort.create((short) 0x01));
        zipArchiveEntry.addExtraField(extraField);
        zipArchiveEntry.removeExtraField(ZipShort.create((short) 0x01));
        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        Assert.assertEquals(0, extraFields.length);
    }

    @Test
    public void testGetExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtraField("TestExtraField", ZipShort.create((short) 0x01));
        zipArchiveEntry.addExtraField(extraField);
        ZipExtraField field = zipArchiveEntry.getExtraField(ZipShort.create((short) 0x01));
        Assert.assertEquals(extraField, field);
    }

    @Test
    public void testSetLocalFileDataExtra() throws IOException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[10];
        zipArchiveEntry.setExtra(extraData);
        Assert.assertNotNull(zipArchiveEntry.getLocalFileDataExtra());
    }

    @Test
    public void testSetCentralDirectoryExtra() throws IOException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[10];
        zipArchiveEntry.setCentralDirectoryExtra(extraData);
        Assert.assertNotNull(zipArchiveEntry.getCentralDirectoryExtra());
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test2.txt");
        Assert.assertEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry1.hashCode());
        Assert.assertNotEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry2.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test1.txt");
        Assert.assertTrue(zipArchiveEntry1.equals(zipArchiveEntry1));
        Assert.assertTrue(zipArchiveEntry1.equals(zipArchiveEntry2));

        ZipArchiveEntry zipArchiveEntry3 = new ZipArchiveEntry("test2.txt");
        Assert.assertFalse(zipArchiveEntry1.equals(zipArchiveEntry3));

        ZipArchiveEntry zipArchiveEntry4 = new ZipArchiveEntry("test1.txt");
        zipArchiveEntry4.setName(null);
        Assert.assertFalse(zipArchiveEntry1.equals(zipArchiveEntry4));
    }

    @Test
    public void testGetLastModifiedDate() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Date date = new Date();
        zipArchiveEntry.setTime(date.getTime());
        Assert.assertEquals(date, zipArchiveEntry.getLastModifiedDate());
    }

    
}