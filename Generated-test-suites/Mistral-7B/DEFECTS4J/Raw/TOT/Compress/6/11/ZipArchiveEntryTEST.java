package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link ZipArchiveEntry}.
 */
public class ZipArchiveEntryTest {

    private ZipFile zipFile;

    @Before
    public void setUp() throws Exception {
        String testFile = "test-zip-entry.zip";
        zipFile = new ZipFile(testFile);
    }

    @Test
    public void testCreateEntryWithFileName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test-entry.txt");
        assertEquals("test-entry.txt", entry.getName());
        assertFalse(entry.isDirectory());
        assertEquals(-1, entry.getMethod());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getPlatform());
        assertNull(entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
    }

    @Test
    public void testCreateEntryWithZipEntry() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals("test-entry.txt", entry.getName());
        assertFalse(entry.isDirectory());
        assertEquals(-1, entry.getMethod());
        assertEquals(zipEntry.getCompressedSize(), entry.getSize());
        assertEquals(zipEntry.getCrc(), entry.getCrc());
        assertEquals(zipEntry.getTime(), entry.getTime());
        assertEquals(zipEntry.getExtra(), entry.getExtra());
    }

    @Test
    public void testCreateEntryWithDirectoryFileName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test-entry/");
        assertEquals("test-entry/", entry.getName());
        assertTrue(entry.isDirectory());
        assertEquals(-1, entry.getMethod());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getPlatform());
        assertNull(entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEntryWithNullName() {
        new ZipArchiveEntry(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEntryWithEmptyName() {
        new ZipArchiveEntry("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEntryWithNegativeMethod() {
        new ZipArchiveEntry(-1);
    }

    @Test
    public void testCreateEntryWithFile() throws Exception {
        File inputFile = new File("test-input-file.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(inputFile, "test-entry.txt");
        assertEquals("test-entry.txt", entry.getName());
        assertFalse(entry.isDirectory());
        assertEquals(-1, entry.getMethod());
        assertEquals(inputFile.length(), entry.getSize());
        assertEquals(inputFile.lastModified(), entry.getTime());
        // TODO: check extra fields
    }

    @Test
    public void testClone() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry original = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry cloned = (ZipArchiveEntry) original.clone();
        assertEquals(original, cloned);
    }

    @Test
    public void testIsSupportedCompressionMethod() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertTrue(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetMethod() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals(-1, entry.getMethod());
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipArchiveEntry.STORED, entry.getMethod());
    }

    @Test
    public void testSetMethod() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipArchiveEntry.STORED, entry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodWithInvalidValue() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test-entry.txt");
        entry.setMethod(255);
    }

    @Test
    public void testGetInternalAttributes() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals(zipEntry.getInternalFlags(), entry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        entry.setInternalAttributes(ZipConstants.FLAG_UNIX);
        assertEquals(ZipConstants.FLAG_UNIX, entry.getInternalAttributes());
    }

    @Test
    public void testGetPlatform() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals(zipEntry.getPlatform(), entry.getPlatform());
    }

    @Test
    public void testSetPlatform() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        entry.setPlatform(ZipEntry.PLATFORM_UNIX);
        assertEquals(ZipEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPlatformWithInvalidValue() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test-entry.txt");
        entry.setPlatform(5);
    }

    @Test
    public void testGetExternalAttributes() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals(zipEntry.getExternalFlags(), entry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        entry.setExternalAttributes(ZipConstants.EXT_FILE_TIME | ZipConstants.EXT_FILE_CRC);
        assertEquals(ZipConstants.EXT_FILE_TIME | ZipConstants.EXT_FILE_CRC, entry.getExternalAttributes());
    }

    @Test
    public void testGetUnixMode() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testSetUnixMode() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testSetExtraFields() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);

        byte[] customExtra = new byte[10];
        ZipExtraField customField = new CustomZipExtraField();
        customField.setData(customExtra);

        entry.setExtraFields(new ZipExtraField[] {customField});

        ZipExtraField[] field = entry.getExtraFields();
        assertEquals(1, field.length);
        assertEquals(customExtra, field[0].getData());
    }

    @Test
    public void testSetExtraFieldsWithMultipleFields() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);

        byte[] customExtra1 = new byte[10];
        ZipExtraField customField1 = new CustomZipExtraField();
        customField1.setData(customExtra1);

        byte[] customExtra2 = new byte[10];
        ZipExtraField customField2 = new CustomZipExtraField();
        customField2.setData(customExtra2);

        entry.setExtraFields(new ZipExtraField[] {customField1, customField2});

        ZipExtraField[] fields = entry.getExtraFields();
        assertEquals(2, fields.length);
        assertEquals(customExtra1, fields[0].getData());
        assertEquals(customExtra2, fields[1].getData());
    }

    @Test
    public void testAddExtraField() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);

        byte[] customExtra = new byte[10];
        ZipExtraField customField = new CustomZipExtraField();
        customField.setData(customExtra);

        entry.addExtraField(customField);

        ZipExtraField[] fields = entry.getExtraFields();
        assertEquals(1, fields.length);
        assertEquals(customExtra, fields[0].getData());
    }

    @Test
    public void testAddAsFirstExtraField() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);

        byte[] customExtra = new byte[10];
        ZipExtraField customField = new CustomZipExtraField();
        customField.setData(customExtra);

        entry.addAsFirstExtraField(customField);

        ZipExtraField[] fields = entry.getExtraFields();
        assertEquals(1, fields.length);
        assertEquals(customExtra, fields[0].getData());
    }

    @Test
    public void testRemoveExtraField() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);

        byte[] customExtra = new byte[10];
        ZipExtraField customField = new CustomZipExtraField();
        customField.setData(customExtra);
        entry.addExtraField(customField);

        entry.removeExtraField(customField.getHeaderId());

        ZipExtraField[] fields = entry.getExtraFields();
        assertEquals(0, fields.length);
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveExtraFieldWithNullHeaderId() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        entry.removeExtraField(null);
    }

    @Test
    public void testGetExtraField() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);

        byte[] customExtra = new byte[10];
        ZipExtraField customField = new CustomZipExtraField();
        customField.setData(customExtra);
        entry.addExtraField(customField);

        ZipExtraField field = entry.getExtraField(customField.getHeaderId());
        assertEquals(customField, field);
    }

    @Test
    public void testGetExtraFieldWithNullHeaderId() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);

        assertNull(entry.getExtraField(null));
    }

    @Test
    public void testSetLocalFileDataExtra() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);

        byte[] customExtra = new byte[10];
        entry.setExtra(customExtra);

        ZipExtraField[] fields = entry.getExtraFields();
        assertEquals(0, fields.length);
    }

    @Test
    public void testSetCentralDirectoryExtra() throws Exception {
        ZipEntry zipEntry = zipFile.getEntry("test-entry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);

        byte[] customExtra = new byte[10];
        entry.setCentralDirectoryExtra(customExtra);

        byte[] centralDirectoryExtra = entry.getCentralDirectoryExtra();
        assertEquals(customExtra, centralDirectoryExtra);
    }
}