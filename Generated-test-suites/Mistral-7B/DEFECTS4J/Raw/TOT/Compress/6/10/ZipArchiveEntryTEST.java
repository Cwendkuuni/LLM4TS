package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.DateUtils;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link ZipArchiveEntry} class.
 */
public class ZipArchiveEntryTest {

    @Test
    public void testEmptyEntry() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        assertEquals("", entry.getName());
        assertFalse(entry.isDirectory());
        assertEquals(-1, entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
        assertNull(entry.getComment());
        assertEquals(0L, entry.getSize());
        assertEquals(0, entry.getTime());
    }

    @Test
    public void testEntryWithFileName() {
        String fileName = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(fileName);
        assertEquals(fileName, entry.getName());
        assertFalse(entry.isDirectory());
        assertEquals(-1, entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
        assertNull(entry.getComment());
        assertEquals(-1, entry.getSize());
        assertEquals(0, entry.getTime());
    }

    @Test
    public void testEntryWithFile() {
        String fileName = "test.txt";
        File testFile = new File(fileName);
        ZipArchiveEntry entry = new ZipArchiveEntry(testFile, fileName);
        assertEquals(fileName, entry.getName());
        assertFalse(entry.isDirectory());
        assertEquals(-1, entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(testFile.length(), entry.getSize());
        assertEquals(testFile.lastModified(), entry.getTime());
        assertNull(entry.getExtraFields());
        assertNull(entry.getComment());
    }

    @Test
    public void testEntryWithZipEntry() throws IOException {
        String fileName = "test.txt";
        File testFile = new File(fileName);
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(fileName);
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals(fileName, entry.getName());
        assertFalse(entry.isDirectory());
        assertEquals(-1, entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(testFile.length(), entry.getSize());
        assertEquals(testFile.lastModified(), entry.getTime());
        assertNull(entry.getExtraFields());
        assertNull(entry.getComment());
    }

    @Test
    public void testEntryWithZipEntryAndAttributes() throws IOException {
        String fileName = "test.txt";
        int internalAttributes = 0x22;
        int externalAttributes = 0xFFFFFFFF;
        long externalDate = new Date().getTime();
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(fileName);
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry, internalAttributes, externalAttributes, externalDate);
        assertEquals(fileName, entry.getName());
        assertFalse(entry.isDirectory());
        assertEquals(-1, entry.getMethod());
        assertEquals(internalAttributes, entry.getInternalAttributes());
        assertEquals(externalAttributes, entry.getExternalAttributes());
        assertEquals(externalDate, entry.getTime());
        assertNull(entry.getExtraFields());
        assertNull(entry.getComment());
        assertEquals(-1, entry.getSize());
    }

    @Test
    public void testEntryWithZipEntryAndAttributesAndExtraFields() throws IOException {
        String fileName = "test.txt";
        int internalAttributes = 0x22;
        int externalAttributes = 0xFFFFFFFF;
        long externalDate = new Date().getTime();
        ZipExtraField extraField1 = new ZipExtraField(1, 1, "Test1".getBytes());
        ZipExtraField extraField2 = new ZipExtraField(2, 1, "Test2".getBytes());
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(fileName);
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry, internalAttributes, externalAttributes, externalDate, new ZipExtraField[] {extraField1, extraField2});
        assertEquals(fileName, entry.getName());
        assertFalse(entry.isDirectory());
        assertEquals(-1, entry.getMethod());
        assertEquals(internalAttributes, entry.getInternalAttributes());
        assertEquals(externalAttributes, entry.getExternalAttributes());
        assertEquals(externalDate, entry.getTime());
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertEquals(2, extraFields.length);
        assertSame(extraField1, extraFields[0]);
        assertSame(extraField2, extraFields[1]);
        assertEquals(-1, entry.getSize());
        assertEquals(null, entry.getComment());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUnixModeNegative() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setUnixMode(-1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClone() {
        ZipArchiveEntry originalEntry = new ZipArchiveEntry("");
        ZipArchiveEntry clonedEntry = originalEntry.clone();
        assertEquals(originalEntry.getName(), clonedEntry.getName());
        assertFalse(originalEntry.isDirectory());
        assertFalse(clonedEntry.isDirectory());
        assertEquals(-1, originalEntry.getMethod());
        assertEquals(-1, clonedEntry.getMethod());
        assertEquals(originalEntry.getInternalAttributes(), clonedEntry.getInternalAttributes());
        assertEquals(originalEntry.getExternalAttributes(), clonedEntry.getExternalAttributes());
        assertNull(originalEntry.getExtraFields());
        assertNull(clonedEntry.getExtraFields());
        assertNull(originalEntry.getComment());
        assertNull(clonedEntry.getComment());
        assertEquals(-1, originalEntry.getSize());
        assertEquals(-1, clonedEntry.getSize());
        assertEquals(originalEntry.getTime(), clonedEntry.getTime());
    }

    @Test
    public void testEquals() {
        String fileName1 = "test.txt";
        String fileName2 = "test.txt";
        ZipArchiveEntry entry1 = new ZipArchiveEntry(fileName1);
        ZipArchiveEntry entry2 = new ZipArchiveEntry(fileName2);
        ZipArchiveEntry entry3 = new ZipArchiveEntry(fileName1);
        ZipArchiveEntry entry4 = new ZipArchiveEntry("test2.txt");
        assertTrue(entry1.equals(entry1));
        assertTrue(entry1.equals(entry3));
        assertTrue(entry1.equals(entry3));
        assertFalse(entry1.equals(entry2));
        assertFalse(entry1.equals(entry4));
    }

    @Test
    public void testGetHashCode() {
        String fileName = "test.txt";
        ZipArchiveEntry entry1 = new ZipArchiveEntry(fileName);
        ZipArchiveEntry entry2 = new ZipArchiveEntry(fileName);
        ZipArchiveEntry entry3 = new ZipArchiveEntry("test2.txt");
        assertEquals(entry1.hashCode(), entry1.hashCode());
        assertEquals(entry1.hashCode(), entry2.hashCode());
        assertFalse(entry1.hashCode(), entry3.hashCode());
    }

    @Test
    public void testGetComment() {
        String fileName = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(fileName);
        assertNull(entry.getComment());

        String comment = "Test comment";
        byte[] commentBytes = comment.getBytes();
        ZipExtraField commentExtraField = new ZipExtraField(3, commentBytes.length, commentBytes);
        entry.addExtraField(commentExtraField);
        assertEquals(comment, entry.getComment());
    }

    @Test
    public void testSetGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());

        entry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodNegative() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setMethod(-1);
    }

    @Test
    public void testMergeExtraFields() throws IOException {
        String fileName = "test.txt";
        ZipExtraField extraField1 = new ZipExtraField(1, 1, "Test1".getBytes());
        ZipExtraField extraField2 = new ZipExtraField(1, 1, "Test2".getBytes());
        ZipExtraField extraField3 = new ZipExtraField(2, 1, "Test3".getBytes());
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(fileName);
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] localExtraFields1 = ExtraFieldUtils.encodeLocalFileData(new ZipExtraField[] {extraField1});
        outputStream.write(localExtraFields1, 0, localExtraFields1.length);
        byte[] localExtraFields2 = ExtraFieldUtils.encodeLocalFileData(new ZipExtraField[] {extraField2});
        outputStream.write(localExtraFields2, 0, localExtraFields2.length);
        byte[] centralDirectoryExtraFields = ExtraFieldUtils.mergeCentralDirectoryData(new ZipExtraField[] {extraField3});
        entry.setLocalFileDataExtra(outputStream.toByteArray());
        entry.setCentralDirectoryExtra(centralDirectoryExtraFields);

        ZipExtraField[] extraFields = entry.getExtraFields();
        assertEquals(3, extraFields.length);
        assertSame(extraField1, extraFields[0]);
        assertSame(extraField2, extraFields[1]);
        assertSame(extraField3, extraFields[2]);
    }

    @Test(expected = RuntimeException.class)
    public void testSetExtraWithInvalidData() {
        String fileName = "test.txt";
        byte[] invalidData = new byte[10];
        ZipArchiveEntry entry = new ZipArchiveEntry(fileName);
        entry.setExtra(invalidData);
    }

    @Test(expected = ZipException.class)
    public void testSetExtraWithInvalidData2() {
        String fileName = "test.txt";
        byte[] invalidData = new byte[10];
        ZipArchiveEntry entry = new ZipArchiveEntry(fileName);
        entry.setCentralDirectoryExtra(invalidData);
    }
}