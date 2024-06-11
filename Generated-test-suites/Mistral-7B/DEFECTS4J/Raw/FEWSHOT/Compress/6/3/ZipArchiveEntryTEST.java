package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.ExtraFieldUtils;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;

public class ZipArchiveEntryTest {

    @Test
    public void testCreateEntry() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertNull(entry.getMethod());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getPlatform());
        assertNull(entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
        assertNull(entry.getName());

        assertEquals("test.txt", entry.getName());
        assertFalse(entry.isDirectory());
        assertFalse(entry.isSupportedCompressionMethod());
        assertEquals(-1, entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getPlatform());
        assertEquals(0, entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
    }

    @Test
    public void testCreateEntryFromZipEntry() throws ZipException {
        String testName = "test.txt";
        ZipEntry zipEntry = new ZipEntry(testName);
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);

        assertNull(entry.getMethod());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getPlatform());
        assertNull(entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
        assertNull(entry.getName());

        assertEquals(testName, entry.getName());
        assertFalse(entry.isDirectory());
        assertFalse(entry.isSupportedCompressionMethod());
        assertEquals(-1, entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getPlatform());
        assertEquals(0, entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
    }

    @Test(expected = ZipException.class)
    public void testCreateEntryFromInvalidZipEntry() throws ZipException {
        String testName = "test.txt";
        ZipEntry zipEntry = new ZipEntry(testName);
        ZipArchiveEntry entry = new ZipArchiveEntry(null); // null ZipEntry

        // Should throw ZipException
    }

    @Test(expected = ZipException.class)
    public void testCreateEntryFromInvalidZipEntry2() throws ZipException {
        String testName = "test.txt";
        ZipEntry zipEntry = new ZipEntry(testName); // null name
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);

        // Should throw ZipException
    }

    @Test
    public void testCreateEntryFromFile() {
        String testName = "test.txt";
        File inputFile = new File(testName);
        ZipArchiveEntry entry = new ZipArchiveEntry(inputFile, testName);

        assertEquals("test.txt", entry.getName());
        assertFalse(entry.isDirectory());
        assertFalse(entry.isSupportedCompressionMethod());
        assertEquals(-1, entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getPlatform());
        assertEquals(inputFile.length(), entry.getSize());
        assertEquals(inputFile.lastModified(), entry.getTime());
        assertNull(entry.getExtraFields());
    }

    @Test
    public void testSetAndGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    @Test
    public void testGetAndSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(0x0101);
        assertEquals(0x0101, entry.getInternalAttributes());
        entry.setInternalAttributes(0x0202);
        assertEquals(0x0202, entry.getInternalAttributes());
    }

    @Test
    public void testGetAndSetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
        entry.setPlatform(ZipArchiveEntry.PLATFORM_FAT);
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
    }

    @Test
    public void testGetAndSetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(0x01010101L);
        assertEquals(0x01010101L, entry.getExternalAttributes());
        entry.setExternalAttributes(0x02020202L);
        assertEquals(0x02020202L, entry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0644);
        assertEquals(0644, entry.getUnixMode());
    }

    @Test
    public void testGetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0644);
        assertEquals(0644, entry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
        entry.setPlatform(ZipArchiveEntry.PLATFORM_FAT);
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField extraField1 = new ZipCustomField("custom1", "customValue1");
        ZipExtraField extraField2 = new ZipCustomField("custom2", "customValue2");

        ZipExtraField[] extraFields = { extraField1, extraField2 };
        entry.setExtraFields(extraFields);

        ZipExtraField[] expectedExtraFields = { extraField1, extraField2 };
        ZipExtraField[] actualExtraFields = entry.getExtraFields();

        assertArrayEquals(expectedExtraFields, actualExtraFields);
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField extraField1 = new ZipCustomField("custom1", "customValue1");
        ZipExtraField extraField2 = new ZipCustomField("custom2", "customValue2");

        entry.addExtraField(extraField1);
        entry.addExtraField(extraField2);

        ZipExtraField[] expectedExtraFields = { extraField1, extraField2 };
        ZipExtraField[] actualExtraFields = entry.getExtraFields();

        assertArrayEquals(expectedExtraFields, actualExtraFields);
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField extraField1 = new ZipCustomField("custom1", "customValue1");
        ZipExtraField extraField2 = new ZipCustomField("custom2", "customValue2");

        entry.addAsFirstExtraField(extraField2);
        entry.addAsFirstExtraField(extraField1);

        ZipExtraField[] expectedExtraFields = { extraField1, extraField2 };
        ZipExtraField[] actualExtraFields = entry.getExtraFields();

        assertArrayEquals(expectedExtraFields, actualExtraFields);
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField extraField1 = new ZipCustomField("custom1", "customValue1");
        ZipExtraField extraField2 = new ZipCustomField("custom2", "customValue2");

        entry.addExtraField(extraField1);
        entry.addExtraField(extraField2);

        entry.removeExtraField(ZipShort.CUSTOM_FIELD_NAME);

        ZipExtraField[] expectedExtraFields = { extraField2 };
        ZipExtraField[] actualExtraFields = entry.getExtraFields();

        assertArrayEquals(expectedExtraFields, actualExtraFields);
    }

    @Test
    public void testGetExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField extraField1 = new ZipCustomField("custom1", "customValue1");
        ZipExtraField extraField2 = new ZipCustomField("custom2", "customValue2");

        entry.addExtraField(extraField1);
        entry.addExtraField(extraField2);

        ZipExtraField actualExtraField1 = entry.getExtraField(ZipShort.CUSTOM_FIELD_NAME);
        ZipExtraField actualExtraField2 = entry.getExtraField(ZipShort.COMMENT_FIELD_NAME);

        assertEquals(extraField1, actualExtraField1);
        assertNull(actualExtraField2);
    }

    @Test
    public void testSetExtra() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        byte[] extraData = new byte[10];
        for (int i = 0; i < extraData.length; i++) {
            extraData[i] = (byte) i;
        }

        entry.setExtra(extraData);

        ZipExtraField[] expectedExtraFields = new ZipExtraField[1];
        expectedExtraFields[0] = new ZipGenericTextKeywordField("comment", "testComment");

        ByteArrayInputStream inputStream = new ByteArrayInputStream(extraData);
        ZipExtraField[] actualExtraFields = ExtraFieldUtils.parse(inputStream);

        assertArrayEquals(expectedExtraFields, actualExtraFields);
    }

    @Test
    public void testSetCentralDirectoryExtra() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        byte[] centralDirectoryExtraData = new byte[10];
        for (int i = 0; i < centralDirectoryExtraData.length; i++) {
            centralDirectoryExtraData[i] = (byte) i;
        }

        entry.setCentralDirectoryExtra(centralDirectoryExtraData);

        ZipExtraField[] expectedExtraFields = new ZipExtraField[1];
        expectedExtraFields[0] = new ZipGenericTextKeywordField("comment", "testComment");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        entry.writeCentralDirectoryExtra(outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipExtraField[] actualExtraFields = ExtraFieldUtils.parse(inputStream);

        assertArrayEquals(expectedExtraFields, actualExtraFields);
    }

}