package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.utils.IOUtils;

public class ZipArchiveEntryTest {

    @org.junit.Test
    public void testEmptyConstructor() {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        assertEquals("", entry.getName());
        assertNull(entry.getExtraFields());
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        assertEquals(-1, entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0L, entry.getExternalAttributes());
        assertEquals(0, entry.getSize());
        assertNull(entry.getTime());
    }

    @org.junit.Test
    public void testConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getExtraFields());
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        assertEquals(-1, entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0L, entry.getExternalAttributes());
        assertEquals(0, entry.getSize());
        assertNull(entry.getTime());
    }

    @org.junit.Test
    public void testConstructorWithZipEntry() throws IOException {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getExtraFields());
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        assertEquals(-1, entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0L, entry.getExternalAttributes());
        assertEquals(zipEntry.getSize(), entry.getSize());
        assertEquals(new Date(zipEntry.getTime()), entry.getTime());
    }

    @org.junit.Test
    public void testConstructorWithZipArchiveEntry() throws IOException, ZipException {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry originalEntry = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry entry = new ZipArchiveEntry(originalEntry);
        assertEquals("test.txt", entry.getName());
        assertArrayEquals(originalEntry.getExtra(), entry.getExtra());
        assertEquals(originalEntry.getPlatform(), entry.getPlatform());
        assertEquals(originalEntry.getMethod(), entry.getMethod());
        assertEquals(originalEntry.getInternalAttributes(), entry.getInternalAttributes());
        assertEquals(originalEntry.getExternalAttributes(), entry.getExternalAttributes());
        assertEquals(originalEntry.getSize(), entry.getSize());
        assertEquals(originalEntry.getTime(), entry.getTime());
    }

    @org.junit.Test
    public void testNullNameConstructor() {
        try {
            new ZipArchiveEntry(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    @org.junit.Test
    public void testGetName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setName("test.txt");
        assertEquals("test.txt", entry.getName());
    }

    @org.junit.Test
    public void testSetAndGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());
    }

    @org.junit.Test
    public void testSetAndGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setInternalAttributes(ZipEntry.FLAG_UNCHANGED | ZipEntry.FLAG_COMPRESSED);
        assertEquals(ZipEntry.FLAG_UNCHANGED | ZipEntry.FLAG_COMPRESSED, entry.getInternalAttributes());
    }

    @org.junit.Test
    public void testSetAndGetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setExternalAttributes(0x8000L);
        assertEquals(0x8000L, entry.getExternalAttributes());
    }

    @org.junit.Test
    public void testSetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipExtraField testField = new ZipCustomField("TestField");
        entry.setExtraFields(new ZipExtraField[] { testField });
        ZipExtraField extractedField = entry.getExtraField(new ZipShort(0));
        assertEquals(testField, extractedField);
    }

    @org.junit.Test
    public void testSetExtraFieldsWithNullArray() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setExtraFields(null);
        assertNull(entry.getExtraFields());
    }

    @org.junit.Test
    public void testSetExtraFieldsWithEmptyArray() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setExtraFields(new ZipExtraField[0]);
        assertArrayEquals(new ZipExtraField[0], entry.getExtraFields());
    }

    @org.junit.Test
    public void testSetExtraFieldsWithMultipleFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipExtraField testField1 = new ZipCustomField("TestField1");
        ZipExtraField testField2 = new ZipCustomField("TestField2");
        ZipExtraField[] fields = { testField1, testField2 };
        entry.setExtraFields(fields);
        ZipExtraField[] extractedFields = entry.getExtraFields();
        assertEquals(2, extractedFields.length);
        assertEquals(testField1, extractedFields[0]);
        assertEquals(testField2, extractedFields[1]);
    }

    @org.junit.Test
    public void testSetAndGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @org.junit.Test
    public void testSetExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipExtraField testField = new ZipCustomField("TestField");
        entry.setExtraField(testField);
        ZipExtraField extractedField = entry.getExtraField(new ZipShort(0));
        assertEquals(testField, extractedField);
    }

    @org.junit.Test
    public void testSetExtraFieldWithNullField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        try {
            entry.setExtraField(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipCustomField testField = new ZipCustomField("TestField");
        entry.setExtraFields(new ZipExtraField[] { testField });
        entry.removeExtraField(new ZipShort(0));
        assertNull(entry.getExtraField(new ZipShort(0)));
    }

    @org.junit.Test(expected = java.util.NoSuchElementException.class)
    public void testRemoveExtraFieldWithNonExistentField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.removeExtraField(new ZipShort(0));
    }

    @org.junit.Test
    public void testAddExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipCustomField testField = new ZipCustomField("TestField");
        entry.addExtraField(testField);
        ZipExtraField extractedField = entry.getExtraField(new ZipShort(0));
        assertEquals(testField, extractedField);
    }

    @org.junit.Test
    public void testAddExtraFieldWithExistingField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipCustomField testField1 = new ZipCustomField("TestField1");
        entry.setExtraFields(new ZipExtraField[] { testField1 });
        ZipCustomField testField2 = new ZipCustomField("TestField2");
        entry.addExtraField(testField2);
        ZipExtraField[] extractedFields = entry.getExtraFields();
        assertEquals(2, extractedFields.length);
        assertEquals(testField1, extractedFields[0]);
        assertEquals(testField2, extractedFields[1]);
    }

    @org.junit.Test
    public void testSetExtraFromBytes() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipCustomField testField = new ZipCustomField("TestField");
        byte[] testData = testField.getBytes();
        entry.setExtra(testData);
        ZipExtraField extractedField = entry.getExtraField(new ZipShort(0));
        assertEquals(testField, extractedField);
    }

    @org.junit.Test
    public void testSetExtraFromCentralDirectoryData() throws IOException, ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipCustomField testField = new ZipCustomField("TestField");
        byte[] testData = testField.getBytes();
        byte[] centralData = new byte[testData.length + 2];
        System.arraycopy(testData, 0, centralData, 0, testData.length);
        assertEquals((short) 0, centralData[testData.length]);
        assertEquals((short) 0, centralData[testData.length + 1]);
        entry.setCentralDirectoryExtra(centralData);
        ZipExtraField extractedField = entry.getExtraField(new ZipShort(0));
        assertEquals(testField, extractedField);
    }

    @org.junit.Test
    public void testParseExtraData() throws IOException, ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        byte[] testData = new byte[] { 0x04, 0x00, 0x00, 0x08, 0x02, 0x00, 0x00, 0x15, 0x00, 0x12, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x02 };
        entry.setExtra(testData);
        ZipCustomField extractedField = (ZipCustomField) entry.getExtraField(new ZipShort(0));
        assertEquals("TestField", extractedField.getName());
        assertEquals("TestValue", extractedField.getValue());
    }

    @org.junit.Test
    public void testClone() throws IOException, ZipException {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry originalEntry = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) originalEntry.clone();
        assertEquals("test.txt", clonedEntry.getName());
        assertArrayEquals(originalEntry.getExtra(), clonedEntry.getExtra());
        assertEquals(originalEntry.getPlatform(), clonedEntry.getPlatform());
        assertEquals(originalEntry.getMethod(), clonedEntry.getMethod());
        assertEquals(originalEntry.getInternalAttributes(), clonedEntry.getInternalAttributes());
        assertEquals(originalEntry.getExternalAttributes(), clonedEntry.getExternalAttributes());
        assertEquals(originalEntry.getSize(), clonedEntry.getSize());
        assertEquals(originalEntry.getTime(), clonedEntry.getTime());
    }

    @org.junit.Test
    public void testToString() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        String result = entry.toString();
        assertTrue(result.startsWith("ZipArchiveEntry["));
        assertTrue(result.endsWith("]"));
        String name = entry.getName();
        int index = result.indexOf(name);
        String expectedResult = "ZipArchiveEntry[name=" + name + ", platform=" + entry.getPlatform() + ", method=" + entry.getMethod() + ", internalAttributes=" + entry.getInternalAttributes() + ", externalAttributes=" + entry.getExternalAttributes() + ", size=" + entry.getSize() + ", time=" + entry.getTime() + ']';
        assertEquals(expectedResult, result.substring(index, result.length()));
    }
}