package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.io.ByteArrays;

import org.junit.Test;

/**
 * Test class for ZipArchiveEntry.
 */
public class ZipArchiveEntryTest {

    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithEntry() {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithEntryAndCopyConstructor() {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry originalZipArchiveEntry = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry copyZipArchiveEntry = new ZipArchiveEntry(originalZipArchiveEntry);
        assertEquals(originalZipArchiveEntry, copyZipArchiveEntry);
    }

    @Test
    public void testConstructorWithFile() {
        File file = new File("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, "test.txt");
        assertEquals("test.txt", zipArchiveEntry.getName());
        assertEquals(file.length(), zipArchiveEntry.getSize());
        assertEquals(file.lastModified(), zipArchiveEntry.getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeMethod() {
        new ZipArchiveEntry(-1);
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
        zipArchiveEntry.setMethod(-1);
        assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        zipArchiveEntry.setInternalAttributes(0);
        assertEquals(0, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        zipArchiveEntry.setInternalAttributes(0);
        assertEquals(0, zipArchiveEntry.getInternalAttributes());
        zipArchiveEntry.setInternalAttributes(1);
        assertEquals(1, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        zipArchiveEntry.setExternalAttributes(0);
        assertEquals(0, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        zipArchiveEntry.setExternalAttributes(0);
        assertEquals(0, zipArchiveEntry.getExternalAttributes());
        zipArchiveEntry.setExternalAttributes(1);
        assertEquals(1, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        zipArchiveEntry.setUnixMode(0755);
        assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testGetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        zipArchiveEntry.setUnixMode(0755);
        assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_FAT);
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        ZipExtraField zipExtraField = new ZipExtendedTimestampExtraField();
        zipArchiveEntry.setExtraFields(new ZipExtraField[] { zipExtraField });
        assertEquals(1, zipArchiveEntry.getExtraFields().length);
        ZipExtraField extraField = zipArchiveEntry.getExtraField(ZipShort.EXT_TIME_CREATED);
        assertNotNull(extraField);
        assertEquals(zipExtraField, extraField);
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        ZipExtraField zipExtraField = new ZipExtendedTimestampExtraField();
        zipArchiveEntry.addExtraField(zipExtraField);
        assertEquals(1, zipArchiveEntry.getExtraFields().length);
        ZipExtraField extraField = zipArchiveEntry.getExtraField(ZipShort.EXT_TIME_CREATED);
        assertNotNull(extraField);
        assertEquals(zipExtraField, extraField);
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        ZipExtraField zipExtraField = new ZipExtendedTimestampExtraField();
        zipArchiveEntry.addAsFirstExtraField(zipExtraField);
        assertEquals(1, zipArchiveEntry.getExtraFields().length);
        ZipExtraField extraField = zipArchiveEntry.getExtraField(ZipShort.EXT_TIME_CREATED);
        assertNotNull(extraField);
        assertEquals(zipExtraField, extraField);
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        ZipExtraField zipExtraField = new ZipExtendedTimestampExtraField();
        zipArchiveEntry.addExtraField(zipExtraField);
        assertEquals(1, zipArchiveEntry.getExtraFields().length);
        zipArchiveEntry.removeExtraField(ZipShort.EXT_TIME_CREATED);
        assertEquals(0, zipArchiveEntry.getExtraFields().length);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetExtraFieldWithNoExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        zipArchiveEntry.getExtraField(ZipShort.EXT_TIME_CREATED);
    }

    @Test
    public void testSetExtraData() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        byte[] extraData = new byte[] { 1, 2, 3, 4, 5 };
        zipArchiveEntry.setExtra(extraData);
        byte[] extractedExtraData = zipArchiveEntry.getLocalFileDataExtra();
        assertArrayEquals(extraData, extractedExtraData);
    }

    @Test
    public void testSetCentralDirectoryExtra() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        byte[] centralDirectoryExtraData = new byte[] { 1, 2, 3, 4, 5 };
        zipArchiveEntry.setCentralDirectoryExtra(centralDirectoryExtraData);
        byte[] extractedCentralDirectoryExtraData = zipArchiveEntry.getCentralDirectoryExtra();
        assertArrayEquals(centralDirectoryExtraData, extractedCentralDirectoryExtraData);
    }

    @Test
    public void testGetName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setName("test.txt");
        assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testIsDirectory() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setName("test.txt");
        assertFalse(zipArchiveEntry.isDirectory());
        zipArchiveEntry.setName("test/");
        assertTrue(zipArchiveEntry.isDirectory());
    }

    @Test
    public void testGetSize() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        zipArchiveEntry.setSize(100);
        assertEquals(100, zipArchiveEntry.getSize());
    }

    @Test
    public void testGetTime() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        Date time = new Date(1234567890123L);
        zipArchiveEntry.setTime(time.getTime());
        assertEquals(time, new Date(zipArchiveEntry.getTime()));
    }

    @Test
    public void testGetLastModifiedDate() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(-1);
        Date lastModifiedDate = new Date(1234567890123L);
        zipArchiveEntry.setTime(lastModifiedDate.getTime());
        assertEquals(lastModifiedDate, zipArchiveEntry.getLastModifiedDate());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("");
        assertFalse(zipArchiveEntry1.equals(zipArchiveEntry2));
        zipArchiveEntry2.setName("test.txt");
        assertFalse(zipArchiveEntry1.equals(zipArchiveEntry2));
        zipArchiveEntry1.setName("test.txt");
        assertTrue(zipArchiveEntry1.equals(zipArchiveEntry2));
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        String name = "test.txt";
        assertEquals(name.hashCode(), zipArchiveEntry.hashCode());
        zipArchiveEntry.setName(name);
        assertEquals(name.hashCode(), zipArchiveEntry.hashCode());
    }
}

class ZipExtendedTimestampExtraField extends ZipExtraField {

    public ZipExtendedTimestampExtraField() {
        super(ZipShort.EXT_TIME_CREATED, 16);
    }

    @Override
    public int getHeaderId() {
        return ZipShort.EXT_TIME_CREATED;
    }

    @Override
    public byte[] getLocalFileDataData() {
        return new byte[] { 0x01, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00 };
    }

    @Override
    public void parseFromLocalFileData(byte[] source, int offset, int len) {
        ByteArrays.copy(source, offset, this.timestamp);
    }

    @Override
    public byte[] getCentralDirectoryData() {
        byte[] data = new byte[10];
        ByteArrays.putInt(data, 0, getHeaderId().getValue());
        ByteArrays.putInt(data, 4, getSize());
        ByteArrays.putLong(data, 8, getTimestamp());
        return data;
    }

    @Override
    public void parseFromCentralDirectoryData(byte[] source, int offset, int len) {
        this.headerId = new ZipShort(ByteArrays.getShort(source, offset));
        this.size = ByteArrays.getLong(source, offset + 2);
        this.timestamp = ByteArrays.getLong(source, offset + 10);
    }
}