package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

public class ZipArchiveEntryTest {

    @Test
    public void testCreateAndGetEntry() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getExtraFields());
        assertFalse(entry.isDirectory());
        assertEquals(-1, entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0L, entry.getExternalAttributes());
        assertEquals(0, entry.getPlatform());
        assertEquals(0, entry.getUnixMode());
        assertEquals(0, entry.getSize());
        assertEquals(0, entry.getCrc());
        assertEquals(0, entry.getCompressedSize());
        assertEquals(0, entry.getLocalFileDataExtra().length);
        assertEquals(0, entry.getCentralDirectoryExtra().length);
    }

    @Test
    public void testCreateAndGetEntryWithExtraFields() {
        ZipExtraField extraField1 = new ZipExtraField(new ZipShort(1), "Test1");
        ZipExtraField extraField2 = new ZipExtraField(new ZipShort(2), "Test2");
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.addExtraField(extraField1);
        entry.addExtraField(extraField2);

        assertEquals(2, entry.getExtraFields().length);
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertSame(extraField1, extraFields[0]);
        assertSame(extraField2, extraFields[1]);
    }

    @Test
    public void testGetSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUnixModeNegative() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(-1);
    }

    @Test
    public void testGetSetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(0);
        assertEquals(0, entry.getPlatform());
        entry.setPlatform(3);
        assertEquals(3, entry.getPlatform());
    }

    @Test
    public void testGetSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodNegative() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(-1);
    }

    @Test
    public void testGetSetSize() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setSize(123);
        assertEquals(123, entry.getSize());
    }

    @Test
    public void testGetSetCrc() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setCrc(123);
        assertEquals(123, entry.getCrc());
    }

    @Test
    public void testGetSetCompressedSize() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setCompressedSize(123);
        assertEquals(123, entry.getCompressedSize());
    }

    @Test
    public void testGetLocalFileDataExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[123];
        entry.setExtra(extraData);
        assertEquals(extraData.length, entry.getLocalFileDataExtra().length);
    }

    @Test
    public void testGetCentralDirectoryExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] centralDirectoryExtraData = new byte[123];
        entry.setCentralDirectoryExtra(centralDirectoryExtraData);
        assertEquals(centralDirectoryExtraData.length, entry.getCentralDirectoryExtra().length);
    }

    @Test
    public void testClone() {
        ZipArchiveEntry original = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry clone = (ZipArchiveEntry) original.clone();

        assertNotSame(original, clone);
        assertEquals(original.getName(), clone.getName());
        assertArrayEquals(original.getExtraFields(), clone.getExtraFields());
        assertEquals(original.getInternalAttributes(), clone.getInternalAttributes());
        assertEquals(original.getExternalAttributes(), clone.getExternalAttributes());
        assertEquals(original.getPlatform(), clone.getPlatform());
        assertEquals(original.getUnixMode(), clone.getUnixMode());
        assertEquals(original.getMethod(), clone.getMethod());
        assertEquals(original.getSize(), clone.getSize());
        assertEquals(original.getCrc(), clone.getCrc());
        assertEquals(original.getCompressedSize(), clone.getCompressedSize());
        assertArrayEquals(original.getLocalFileDataExtra(), clone.getLocalFileDataExtra());
        assertArrayEquals(original.getCentralDirectoryExtra(), clone.getCentralDirectoryExtra());
    }

    @Test
    public void testCreateFromZipEntry() throws IOException {
        String name = "test.txt";
        byte[] extraData = new byte[123];
        ZipEntry zipEntry = new ZipEntry(name);
        zipEntry.setExtra(extraData);
        int method = ZipEntry.STORED;
        zipEntry.setMethod(method);
        int internalAttributes = File.TYPE;
        zipEntry.setInternalAttributes(internalAttributes);
        long externalAttributes = 0x12345678L;
        zipEntry.setExternalAttributes(externalAttributes);

        ZipArchiveEntry archiveEntry = new ZipArchiveEntry(zipEntry);

        assertEquals(name, archiveEntry.getName());
        assertArrayEquals(extraData, archiveEntry.getLocalFileDataExtra());
        assertEquals(method, archiveEntry.getMethod());
        assertEquals(internalAttributes, archiveEntry.getInternalAttributes());
        assertEquals(externalAttributes, archiveEntry.getExternalAttributes());
        assertEquals(0, archiveEntry.getPlatform());
        assertEquals(0, archiveEntry.getUnixMode());
    }

    @Test
    public void testCreateFromZipArchiveEntry() throws IOException, CloneNotSupportedException {
        String name = "test.txt";
        byte[] extraData = new byte[123];
        ZipShort headerId1 = new ZipShort((short) 1);
        ZipShort headerId2 = new ZipShort((short) 2);
        ZipExtraField extraField1 = new ZipExtraField(headerId1, "Test1", extraData);
        ZipExtraField extraField2 = new ZipExtraField(headerId2, "Test2", new byte[0]);
        ZipArchiveEntry original = new ZipArchiveEntry(name);
        original.addExtraField(extraField1);
        original.addExtraField(extraField2);

        ZipArchiveEntry clone = (ZipArchiveEntry) original.clone();

        ZipArchiveEntry newEntry = new ZipArchiveEntry(clone);
        assertEquals(name, newEntry.getName());
        assertArrayEquals(original.getLocalFileDataExtra(), newEntry.getLocalFileDataExtra());
        assertArrayEquals(original.getCentralDirectoryExtra(), newEntry.getCentralDirectoryExtra());
        assertEquals(original.getExtraFields().length, newEntry.getExtraFields().length);
        ZipExtraField[] extraFields = newEntry.getExtraFields();
        assertSame(extraField1, extraFields[0]);
        assertSame(extraField2, extraFields[1]);
    }
}