package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.junit.Test;

import java.io.File;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    @Test
    public void testNewInstanceWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getSize());
        assertNull(entry.getTime());
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        assertEquals(-1, entry.getMethod());
        assertNull(entry.getExtraFields());
    }

    @Test
    public void testNewInstanceWithFile() {
        File file = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(file, "test.txt");
        assertEquals("test.txt", entry.getName());
        assertEquals(file.length(), entry.getSize());
        assertEquals(file.lastModified(), entry.getTime().getTime());
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        assertEquals(-1, entry.getMethod());
        assertNull(entry.getExtraFields());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        ZipArchiveEntry original = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry clone = (ZipArchiveEntry) original.clone();
        assertEquals(original.getName(), clone.getName());
        assertEquals(original.getSize(), clone.getSize());
        assertEquals(original.getTime().getTime(), clone.getTime().getTime());
        assertEquals(original.getPlatform(), clone.getPlatform());
        assertEquals(original.getMethod(), clone.getMethod());
        assertEquals(original.getExtraFields(), clone.getExtraFields());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertTrue(entry.isSupportedCompressionMethod());

        entry.setMethod(ZipEntry.STORED);
        assertTrue(entry.isSupportedCompressionMethod());

        entry.setMethod(ZipEntry.DEFLATED);
        assertTrue(entry.isSupportedCompressionMethod());

        entry.setMethod(ZipEntry.UNKNOWN);
        assertFalse(entry.isSupportedCompressionMethod());

        entry.setMethod(-1);
        assertFalse(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(-1, entry.getMethod());

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
        assertEquals(-1, entry.getMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getInternalAttributes());

        entry.setInternalAttributes(ZipEntry.FLAG_UNIX_GROUP);
        assertEquals(ZipEntry.FLAG_UNIX_GROUP, entry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        entry.setInternalAttributes(ZipEntry.FLAG_UNIX_GROUP);
        assertEquals(ZipEntry.FLAG_UNIX_GROUP, entry.getInternalAttributes());

        entry.setInternalAttributes(ZipEntry.FLAG_DELETED);
        assertEquals(ZipEntry.FLAG_DELETED, entry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        entry.setExternalAttributes(1);
        assertEquals(1, entry.getExternalAttributes());

        entry.setExternalAttributes(0xFFFF0101L);
        assertEquals(0xFFFF0101L, entry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testGetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());

        entry.setPlatform(ZipArchiveEntry.PLATFORM_FAT);
        assertEquals(0, entry.getUnixMode());
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
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField extraField1 = new ZipExtraField(ZipShort.LOCAL_FILE_COMMENT, "comment1".getBytes());
        ZipExtraField extraField2 = new ZipExtraField(ZipShort.GLOBAL_FILE_COMMENT, "comment2".getBytes());

        entry.setExtraFields(new ZipExtraField[]{extraField1, extraField2});
        assertEquals(2, entry.getExtraFields().length);
        assertEquals(extraField1, entry.getExtraField(ZipShort.LOCAL_FILE_COMMENT));
        assertEquals(extraField2, entry.getExtraField(ZipShort.GLOBAL_FILE_COMMENT));

        entry.setExtraFields(new ZipExtraField[0]);
        assertEquals(0, entry.getExtraFields().length);
    }

    @Test
    public void testGetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField extraField1 = new ZipExtraField(ZipShort.LOCAL_FILE_COMMENT, "comment1".getBytes());
        ZipExtraField extraField2 = new ZipExtraField(ZipShort.GLOBAL_FILE_COMMENT, "comment2".getBytes());

        entry.setExtraFields(new ZipExtraField[]{extraField1, extraField2});
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertEquals(2, extraFields.length);
        assertEquals(extraField1, extraFields[0]);
        assertEquals(extraField2, extraFields[1]);

        entry.setExtraFields(new ZipExtraField[0]);
        extraFields = entry.getExtraFields();
        assertEquals(0, extraFields.length);
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField extraField1 = new ZipExtraField(ZipShort.LOCAL_FILE_COMMENT, "comment1".getBytes());
        ZipExtraField extraField2 = new ZipExtraField(ZipShort.GLOBAL_FILE_COMMENT, "comment2".getBytes());

        entry.setExtraFields(new ZipExtraField[0]);
        entry.addExtraField(extraField1);
        assertEquals(1, entry.getExtraFields().length);
        assertEquals(extraField1, entry.getExtraField(ZipShort.LOCAL_FILE_COMMENT));

        entry.addExtraField(extraField2);
        assertEquals(2, entry.getExtraFields().length);
        assertEquals(extraField1, entry.getExtraField(ZipShort.LOCAL_FILE_COMMENT));
        assertEquals(extraField2, entry.getExtraField(ZipShort.GLOBAL_FILE_COMMENT));
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField extraField1 = new ZipExtraField(ZipShort.LOCAL_FILE_COMMENT, "comment1".getBytes());
        ZipExtraField extraField2 = new ZipExtraField(ZipShort.GLOBAL_FILE_COMMENT, "comment2".getBytes());

        entry.setExtraFields(new ZipExtraField[0]);
        entry.addAsFirstExtraField(extraField1);
        assertEquals(1, entry.getExtraFields().length);
        assertEquals(extraField1, entry.getExtraField(ZipShort.LOCAL_FILE_COMMENT));

        entry.addAsFirstExtraField(extraField2);
        assertEquals(2, entry.getExtraFields().length);
        assertEquals(extraField2, entry.getExtraField(ZipShort.GLOBAL_FILE_COMMENT));
        assertEquals(extraField1, entry.getExtraField(ZipShort.LOCAL_FILE_COMMENT));
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField extraField1 = new ZipExtraField(ZipShort.LOCAL_FILE_COMMENT, "comment1".getBytes());
        ZipExtraField extraField2 = new ZipExtraField(ZipShort.GLOBAL_FILE_COMMENT, "comment2".getBytes());

        entry.setExtraFields(new ZipExtraField[]{extraField1, extraField2});
        assertEquals(2, entry.getExtraFields().length);
        entry.removeExtraField(ZipShort.LOCAL_FILE_COMMENT);
        assertEquals(1, entry.getExtraFields().length);
        assertNull(entry.getExtraField(ZipShort.LOCAL_FILE_COMMENT));
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testGetExtraFieldNull() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertNull(entry.getExtraField(ZipShort.LOCAL_FILE_COMMENT));
    }

    @Test
    public void testSetLocalFileDataExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        byte[] extraData = "comment1".getBytes();
        entry.setExtra(extraData);

        ZipExtraField extraField = new ZipExtraField(ZipShort.LOCAL_FILE_COMMENT, extraData);
        entry.setExtraFields(new ZipExtraField[]{extraField});
        assertEquals(1, entry.getExtraFields().length);
        assertEquals(extraField, entry.getExtraField(ZipShort.LOCAL_FILE_COMMENT));

        entry.setLocalFileDataExtra(extraData);
        assertEquals(extraData, entry.getLocalFileDataExtra());
    }

    @Test(expected = java.lang.RuntimeException.class)
    public void testSetLocalFileDataExtraInvalidData() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[0];
        try {
            entry.setLocalFileDataExtra(extraData);
            fail();
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testSetCentralDirectoryExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        byte[] extraData = "comment1".getBytes();
        entry.setExtra(extraData);
        entry.setCentralDirectoryExtra(extraData);

        ZipExtraField extraField = new ZipExtraField(ZipShort.LOCAL_FILE_COMMENT, extraData);
        entry.setExtraFields(new ZipExtraField[]{extraField});
        assertEquals(1, entry.getExtraFields().length);
        assertEquals(extraField, entry.getExtraField(ZipShort.LOCAL_FILE_COMMENT));
    }

    @Test(expected = java.lang.RuntimeException.class)
    public void testSetCentralDirectoryExtraInvalidData() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[0];
        try {
            entry.setCentralDirectoryExtra(extraData);
            fail();
        } catch (RuntimeException e) {
            // Expected
        }
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
        assertEquals(entry1.hashCode(), entry2.hashCode());

        entry2.setName("test2.txt");
        assertNotEquals(entry1.hashCode(), entry2.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
        assertTrue(entry1.equals(entry2));

        entry2.setName("test2.txt");
        assertFalse(entry1.equals(entry2));
    }

    @Test
    public void testGetLastModifiedDate() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(Instant.now(), entry.getLastModifiedDate().toInstant());
    }
}