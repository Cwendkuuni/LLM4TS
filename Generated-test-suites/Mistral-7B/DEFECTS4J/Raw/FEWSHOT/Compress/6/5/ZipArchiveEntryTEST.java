package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ExtraField;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.ZipShort;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructor() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
        assertFalse(entry.isDirectory());
    }

    @Test
    public void testConstructorFromZipEntry() throws ZipException {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals("test.txt", entry.getName());
        assertFalse(entry.isDirectory());
    }

    @Test
    public void testConstructorFromZipArchiveEntry() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry(new ZipArchiveEntry("test.txt"));
        assertEquals("test.txt", entry.getName());
        assertFalse(entry.isDirectory());
    }

    @Test
    public void testConstructorFromFile() {
        File file = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(file, "test.txt");
        assertEquals("test.txt", entry.getName());
        assertFalse(entry.isDirectory());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        ZipArchiveEntry original = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry clone = (ZipArchiveEntry) original.clone();
        assertEquals("test.txt", clone.getName());
        assertFalse(clone.isDirectory());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertTrue(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(-1, entry.getMethod());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(0xFFFF);
        assertEquals(0xFFFF, entry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(0xFFFFFFFFL);
        assertEquals(0xFFFFFFFFL, entry.getExternalAttributes());
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
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipExtraField extraField1 = new ZipExtendedInfo();
        ZipExtraField extraField2 = new ZipExtendedInfo();
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExtraFields(new ZipExtraField[] { extraField1, extraField2 });
        assertEquals(2, entry.getExtraFields().length);
    }

    @Test
    public void testGetExtraFields() {
        ZipExtraField extraField1 = new ZipExtendedInfo();
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.addExtraField(extraField1);
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals(extraField1, extraFields[0]);
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtendedInfo();
        entry.addExtraField(extraField);
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals(extraField, extraFields[0]);
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtendedInfo();
        entry.addAsFirstExtraField(extraField);
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals(extraField, extraFields[0]);
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtendedInfo();
        entry.addExtraField(extraField);
        entry.removeExtraField(new ZipShort(ZipShort.XFL));
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertNull(extraFields[0]);
    }

    @Test
    public void testGetExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtendedInfo();
        entry.addExtraField(extraField);
        ZipExtraField extraFieldFromEntry = entry.getExtraField(new ZipShort(ZipShort.XFL));
        assertEquals(extraField, extraFieldFromEntry);
    }

    @Test
    public void testSetExtra() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipExtraField[] extraFields = { new ZipExtendedInfo(), new ZipExtendedInfo() };
        entry.setExtra(ExtraFieldUtils.parse(new byte[0], true)[0]);
        entry.setExtraFields(extraFields);
        entry.setExtra(outputStream.toByteArray());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipEntry zipEntry = new ZipEntry("test.txt");
        zipEntry.setExtra(inputStream);
        ZipArchiveEntry entryFromExtra = new ZipArchiveEntry(zipEntry);
        assertEquals(extraFields[0], entryFromExtra.getExtraField(new ZipShort(ZipShort.XFL)));
        assertEquals(extraFields[1], entryFromExtra.getExtraField(new ZipShort(ZipShort.COMMENT)));
    }

    @Test
    public void testSetCentralDirectoryExtra() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipExtraField[] extraFields = { new ZipExtendedInfo(), new ZipExtendedInfo() };
        entry.setExtra(ExtraFieldUtils.parse(new byte[0], true)[0]);
        entry.setCentralDirectoryExtra(outputStream.toByteArray());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ZipEntry zipEntry = new ZipEntry("test.txt");
        zipEntry.setExtra(inputStream);
        ZipArchiveEntry entryFromExtra = new ZipArchiveEntry(zipEntry);
        assertEquals(extraFields[0], entryFromExtra.getExtraField(new ZipShort(ZipShort.XFL)));
        assertEquals(extraFields[1], entryFromExtra.getExtraField(new ZipShort(ZipShort.COMMENT)));
    }

    @Test
    public void testGetLocalFileDataExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[0];
        byte[] extraDataFromEntry = entry.getLocalFileDataExtra();
        assertEquals(extraData, extraDataFromEntry);
    }

    @Test
    public void testGetCentralDirectoryExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[0];
        byte[] extraDataFromEntry = entry.getCentralDirectoryExtra();
        assertEquals(extraData, extraDataFromEntry);
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
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test2.txt");
        int hashCode1 = entry1.hashCode();
        int hashCode2 = entry2.hashCode();
        assertEquals(hashCode1, hashCode1); // hashCode must be consistent
        assertNotEquals(hashCode1, hashCode2); // hashCode must be different for different entries
    }
}