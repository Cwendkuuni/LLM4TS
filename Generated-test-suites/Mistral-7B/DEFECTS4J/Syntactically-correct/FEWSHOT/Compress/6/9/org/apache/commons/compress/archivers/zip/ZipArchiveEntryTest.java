package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

public class ZipArchiveEntryTest {

    @Test
    public void testEmptyConstructor() {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        assertEquals("", entry.getName());
        assertNull(entry.getExtraFields());
        assertEquals(0, entry.getMethod());
        assertEquals(0, entry.getPlatform());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
    }

    @Test
    public void testStringConstructor() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getExtraFields());
        assertEquals(0, entry.getMethod());
        assertEquals(0, entry.getPlatform());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
    }

    @Test
    public void testFileConstructor() {
        File file = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(file, "test.txt");
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getExtraFields());
        assertEquals(0, entry.getMethod());
        assertEquals(0, entry.getPlatform());
        assertEquals(file.length(), entry.getSize());
        assertEquals(file.lastModified(), entry.getTime());
    }

    @Test
    public void testCloneConstructor() throws CloneNotSupportedException, ZipException {
        ZipArchiveEntry original = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry clone = (ZipArchiveEntry) original.clone();
        assertEquals(original.getName(), clone.getName());
        assertEquals(original.getExtraFields(), clone.getExtraFields());
        assertEquals(original.getMethod(), clone.getMethod());
        assertEquals(original.getPlatform(), clone.getPlatform());
        assertEquals(original.getInternalAttributes(), clone.getInternalAttributes());
        assertEquals(original.getExternalAttributes(), clone.getExternalAttributes());
    }

    @Test
    public void testZipEntryConstructor() throws ZipException {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getExtraFields());
        assertEquals(zipEntry.getMethod(), entry.getMethod());
        assertEquals(0, entry.getPlatform());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(zipEntry.getExtra(), entry.getExtra());
    }

    @Test
    public void testZipArchiveEntryConstructor() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry(new ZipArchiveEntry("test.txt"));
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getExtraFields());
        assertEquals(0, entry.getMethod());
        assertEquals(0, entry.getPlatform());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
    }

    @Test
    public void testGetName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testIsDirectory() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertFalse(entry.isDirectory());

        entry = new ZipArchiveEntry("test/");
        assertTrue(entry.isDirectory());
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");

        int hashCode1 = entry1.hashCode();
        int hashCode2 = entry2.hashCode();

        assertEquals(hashCode1, hashCode2);

        entry2.setName("test2.txt");
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry3 = new ZipArchiveEntry("test2.txt");

        assertTrue(entry1.equals(entry1));
        assertTrue(entry1.equals(entry2));
        assertFalse(entry1.equals(entry3));
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(-1, entry.getMethod());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testGetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getUnixMode());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] extraFields = new ZipExtraField[1];
        ZipShort testHeaderId = new ZipShort((short) 1);
        extraFields[0] = new ZipExtraField(testHeaderId, new byte[10]);
        entry.setExtraFields(extraFields);

        assertEquals(extraFields, entry.getExtraFields());
    }

    @Test
    public void testGetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(new ZipExtraField[0], entry.getExtraFields());
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] extraFields = new ZipExtraField[1];
        ZipShort testHeaderId = new ZipShort((short) 1);
        extraFields[0] = new ZipExtraField(testHeaderId, new byte[10]);
        entry.addExtraField(extraFields[0]);

        assertEquals(extraFields[0], entry.getExtraField(testHeaderId));
        assertEquals(1, entry.getExtraFields().length);
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] extraFields = new ZipExtraField[1];
        ZipShort testHeaderId = new ZipShort((short) 1);
        extraFields[0] = new ZipExtraField(testHeaderId, new byte[10]);
        entry.addAsFirstExtraField(extraFields[0]);

        assertEquals(extraFields[0], entry.getExtraFields()[0]);
        assertEquals(1, entry.getExtraFields().length);
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] extraFields = new ZipExtraField[1];
        ZipShort testHeaderId = new ZipShort((short) 1);
        extraFields[0] = new ZipExtraField(testHeaderId, new byte[10]);
        entry.addExtraField(extraFields[0]);
        entry.removeExtraField(testHeaderId);

        assertNull(entry.getExtraField(testHeaderId));
        assertEquals(0, entry.getExtraFields().length);
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testRemoveNonExistingExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.removeExtraField(new ZipShort((short) 1));
    }

    @Test
    public void testSetExtra() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[10];
        entry.setExtra(extraData);

        ZipExtraField[] extraFields = entry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals(extraData, extraFields[0].getData());
    }

    @Test(expected = RuntimeException.class)
    public void testSetExtraWithInvalidData() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[1];
        entry.setExtra(extraData);
    }

    @Test
    public void testSetCentralDirectoryExtra() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] centralDirectoryExtraData = new byte[10];
        entry.setCentralDirectoryExtra(centralDirectoryExtraData);

        assertArrayEquals(centralDirectoryExtraData, entry.getCentralDirectoryExtra());
    }

    @Test
    public void testGetLocalFileDataExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] localFileDataExtraData = new byte[10];
        entry.setExtra(localFileDataExtraData);

        assertArrayEquals(localFileDataExtraData, entry.getLocalFileDataExtra());
    }

    @Test
    public void testGetCentralDirectoryExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] centralDirectoryExtraData = new byte[10];
        entry.setCentralDirectoryExtra(centralDirectoryExtraData);

        assertArrayEquals(centralDirectoryExtraData, entry.getCentralDirectoryExtra());
    }
}