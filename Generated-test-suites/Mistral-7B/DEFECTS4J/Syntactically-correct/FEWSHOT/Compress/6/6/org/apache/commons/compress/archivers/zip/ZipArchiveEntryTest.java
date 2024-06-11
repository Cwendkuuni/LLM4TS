package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.ByteStreams;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class ZipArchiveEntryTest {

    private static final ZipExtraField USER_DEFINED_FIELD = new ZipExtendedInfo();

    @Test
    public void testNewInstanceWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getSize());
        assertEquals(0, entry.getExternalAttributes());
        assertEquals(0, entry.getInternalAttributes());
        assertNull(entry.getExtraFields());
        assertFalse(entry.isDirectory());
        assertFalse(entry.isSupportedCompressionMethod());
        assertEquals(ZipEntry.STORED, entry.getMethod());
    }

    @Test
    public void testNewInstanceWithFile() {
        File file = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(file, "test.txt");
        assertEquals("test.txt", entry.getName());
        assertNotNull(entry.getSize());
        assertEquals(file.lastModified(), entry.getTime());
        assertEquals(0, entry.getExternalAttributes());
        assertEquals(0, entry.getInternalAttributes());
        assertNull(entry.getExtraFields());
        assertFalse(entry.isDirectory());
        assertFalse(entry.isSupportedCompressionMethod());
        assertEquals(ZipEntry.STORED, entry.getMethod());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        ZipArchiveEntry original = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry clone = (ZipArchiveEntry) original.clone();
        assertEquals(original, clone);
        assertNotSame(original, clone);
        assertNotNull(clone.getSize());
        assertNotNull(clone.getTime());
        assertSame(original.getName(), clone.getName());
        assertSame(original.getInternalAttributes(), clone.getInternalAttributes());
        assertSame(original.getPlatform(), clone.getPlatform());
        assertArrayEquals(original.getExtraFields(), clone.getExtraFields());
    }

    @Test
    public void testConstructorWithZipEntry() throws IOException, ZipException {
        ZipArchiveEntry original = new ZipArchiveEntry("test.txt");
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry clone = new ZipArchiveEntry(zipEntry);
        assertEquals(original, clone);
        assertNotSame(original, clone);
        assertNotNull(clone.getSize());
        assertNotNull(clone.getTime());
        assertSame(original.getName(), clone.getName());
        assertSame(original.getInternalAttributes(), clone.getInternalAttributes());
        assertSame(original.getPlatform(), clone.getPlatform());
        assertArrayEquals(original.getExtraFields(), clone.getExtraFields());
    }

    @Test
    public void testConstructorWithZipArchiveEntry() throws IOException, ZipException {
        ZipArchiveEntry original = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry clone = new ZipArchiveEntry(original);
        assertEquals(original, clone);
        assertNotSame(original, clone);
        assertNotNull(clone.getSize());
        assertNotNull(clone.getTime());
        assertSame(original.getName(), clone.getName());
        assertSame(original.getInternalAttributes(), clone.getInternalAttributes());
        assertSame(original.getPlatform(), clone.getPlatform());
        assertArrayEquals(original.getExtraFields(), clone.getExtraFields());
    }

    @Test
    public void testGetUnixMode() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUnixModeWithNegativeValue() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(-1);
    }

    @Test
    public void testSetGetPlatform() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetGetMethod() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodWithInvalidValue() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(10);
    }

    @Test
    public void testIsSupportedCompressionMethod() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertFalse(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        assertTrue(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testSetGetExtraFields() throws IOException, ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] fields = { USER_DEFINED_FIELD };
        entry.setExtraFields(fields);
        ZipExtraField[] result = entry.getExtraFields();
        assertEquals(fields.length, result.length);
        for (int i = 0; i < fields.length; i++) {
            assertEquals(fields[i], result[i]);
        }
    }

    @Test
    public void testSetGetExtraField() throws IOException, ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField field = USER_DEFINED_FIELD;
        entry.addExtraField(field);
        ZipExtraField result = entry.getExtraField(field.getHeaderId());
        assertEquals(field, result);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetExtraFieldWithNonExistentId() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.addExtraField(USER_DEFINED_FIELD);
        ZipShort nonExistentId = new ZipShort((short) 0x1234);
        entry.getExtraField(nonExistentId);
    }

    @Test
    public void testSetGetExtraFieldsWithEmptyArray() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] fields = {};
        entry.setExtraFields(fields);
        ZipExtraField[] result = entry.getExtraFields();
        assertArrayEquals(fields, result);
    }

    @Test
    public void testAddRemoveExtraField() throws IOException, ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField field = USER_DEFINED_FIELD;

        // adding an existing field should replace it
        entry.addExtraField(field);
        ZipExtraField existing = entry.getExtraField(field.getHeaderId());
        assertEquals(field, existing);

        // adding a new field
        ZipExtraField newField = new ZipExtendedInfo();
        entry.addExtraField(newField);
        ZipExtraField[] result = entry.getExtraFields();
        assertEquals(2, result.length);

        // removing the first field
        entry.removeExtraField(field.getHeaderId());
        result = entry.getExtraFields();
        assertEquals(1, result.length);
        assertNotSame(field, result[0]);
    }

    @Test
    public void testSetGetExtraBytes() throws IOException, ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] bytes = "user defined extra data".getBytes();
        entry.setExtra(bytes);
        byte[] result = entry.getLocalFileDataExtra();
        assertArrayEquals(bytes, result);
    }

    @Test(expected = RuntimeException.class)
    public void testSetGetExtraBytesWithInvalidData() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03 };
        try {
            entry.setExtra(bytes);
        } catch (RuntimeException e) {
            // expected
            assertEquals("Invalid extra data", e.getMessage());
            return;
        }
        fail("Expected RuntimeException");
    }

    @Test
    public void testSetGetExtraBytesWithEmptyData() throws IOException, ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] bytes = {};
        entry.setExtra(bytes);
        byte[] result = entry.getLocalFileDataExtra();
        assertArrayEquals(bytes, result);
    }

    @Test
    public void testSetCentralDirectoryExtra() throws IOException, ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] bytes = "central directory extra data".getBytes();
        entry.setCentralDirectoryExtra(bytes);
        byte[] result = entry.getCentralDirectoryExtra();
        assertArrayEquals(bytes, result);
    }

    @Test(expected = RuntimeException.class)
    public void testSetCentralDirectoryExtraWithInvalidData() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] bytes = new byte[] { 0x01, 0x02, 0x03 };
        try {
            entry.setCentralDirectoryExtra(bytes);
        } catch (RuntimeException e) {
            // expected
            assertEquals("Invalid extra data", e.getMessage());
            return;
        }
        fail("Expected RuntimeException");
    }

    @Test
    public void testToString() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        String expected = String.format("%s; size=%d; comp=%d; mod=%d",
                entry.getName(), entry.getSize(), entry.getMethod(), entry.getTime());
        assertEquals(expected, entry.toString());
    }
}