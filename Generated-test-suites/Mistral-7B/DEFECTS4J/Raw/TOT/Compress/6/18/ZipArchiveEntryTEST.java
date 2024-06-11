package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testCreation() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getMethod());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
    }

    @Test
    public void testCreationFromZipEntry() throws ZipException {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals("test.txt", entry.getName());
        assertEquals(ZipEntry.STORED, entry.getMethod());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
    }

    @Test
    public void testCreationFromZipArchiveEntry() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry(new ZipArchiveEntry("test.txt"));
        assertEquals("test.txt", entry.getName());
        assertEquals(ZipEntry.STORED, entry.getMethod());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
    }

    @Test
    public void testCreationFromFile() {
        File file = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(file, "test.txt");
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getMethod());
        assertNotNull(entry.getSize());
        assertNotNull(entry.getTime());
        assertNull(entry.getExtraFields());
    }

    @Test
    public void testClone() throws Exception {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
        ZipArchiveEntry original = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry cloned = (ZipArchiveEntry) original.clone();

        assertEquals(original.getName(), cloned.getName());
        assertEquals(original.getMethod(), cloned.getMethod());
        assertEquals(original.getInternalAttributes(), cloned.getInternalAttributes());
        assertEquals(original.getExternalAttributes(), cloned.getExternalAttributes());
        assertArrayEquals(original.getExtraFields(), cloned.getExtraFields());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertTrue(entry.isSupportedCompressionMethod());

        entry.setMethod(ZipEntry.DEFLATED);
        assertTrue(entry.isSupportedCompressionMethod());

        entry.setMethod(-1);
        assertTrue(entry.isSupportedCompressionMethod());

        entry.setMethod(ZipEntry.STORED);
        assertFalse(entry.isSupportedCompressionMethod());

        entry.setMethod(ZipEntry.UNKNOWN);
        assertFalse(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertNull(entry.getMethod());

        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());

        entry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());

        entry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, entry.getMethod());

        entry.setMethod(-1);
        assertNull(entry.getMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertNull(entry.getInternalAttributes());

        entry.setInternalAttributes(0x1234);
        assertEquals(0x1234, entry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(0x1234);
        assertEquals(0x1234, entry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertNull(entry.getExternalAttributes());

        entry.setExternalAttributes(0x1234);
        assertEquals(0x1234, entry.getExternalAttributes());

        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(0x1234 | 0x1000, entry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_FAT);
        entry.setUnixMode(0755);
        assertEquals(0x2020, entry.getExternalAttributes());

        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        entry.setUnixMode(0755);
        assertEquals(0x1777, entry.getExternalAttributes());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());

        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_FAT);
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());

        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField[] fields = new ZipExtraField[1];
        fields[0] = new ZipExtendedInformation();
        entry.setExtraFields(fields);

        assertEquals(1, entry.getExtraFields().length);
        assertTrue(entry.hasExtraField(ZipShort.XFL_EXTERNAL_FILE_ATTRIBUTES));
        assertNotNull(entry.getExtraField(ZipShort.XFL_EXTERNAL_FILE_ATTRIBUTES));
    }

    @Test
    public void testGetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField[] fields = new ZipExtraField[1];
        fields[0] = new ZipExtendedInformation();
        entry.setExtraFields(fields);

        ZipExtraField[] result = entry.getExtraFields();
        assertEquals(1, result.length);
        assertTrue(result[0] instanceof ZipExtendedInformation);
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField[] fields = new ZipExtraField[1];
        fields[0] = new ZipExtendedInformation();
        entry.addExtraField(fields[0]);

        ZipExtraField[] result = entry.getExtraFields();
        assertEquals(1, result.length);
        assertTrue(result[0] instanceof ZipExtendedInformation);
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField[] fields = new ZipExtraField[1];
        fields[0] = new ZipExtendedInformation();
        entry.addAsFirstExtraField(fields[0]);

        ZipExtraField[] result = entry.getExtraFields();
        assertEquals(1, result.length);
        assertTrue(result[0] instanceof ZipExtendedInformation);
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField[] fields = new ZipExtraField[1];
        fields[0] = new ZipExtendedInformation();
        entry.setExtraFields(fields);

        entry.removeExtraField(ZipShort.XFL_EXTERNAL_FILE_ATTRIBUTES);
        ZipExtraField[] result = entry.getExtraFields();
        assertEquals(0, result.length);
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testGetNonExistentExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.getExtraField(ZipShort.UNDEFINED);
    }
}