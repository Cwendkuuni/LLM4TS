package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.extra.ZipExtraField;
import org.apache.commons.compress.archivers.zip.extra.ZipShort;
import org.apache.commons.compress.utils.ByteUtils;

public class ZipArchiveEntryTest {

    @org.junit.Test
    public void testNewEntryWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getMethod());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
        assertEquals(0, entry.getSize());
        assertNull(entry.getTime());
    }

    @org.junit.Test
    public void testNewEntryWithFile() {
        File file = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(file, "test.txt");
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getMethod());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
        assertEquals(file.length(), entry.getSize());
        assertEquals(new Date(file.lastModified()), entry.getTime());
    }

    @org.junit.Test
    public void testCloneEntry() throws IOException, ZipException {
        ZipArchiveEntry original = new ZipArchiveEntry("test.txt");
        original.setMethod(ZipEntry.STORED);
        original.setInternalAttributes(0x2000);
        original.setExternalAttributes(0x800001L);
        ZipExtraField extraField = new ZipExtraField(new ZipShort(1), "test");
        LinkedHashMap<ZipShort, ZipExtraField> extraFields = new LinkedHashMap<>();
        extraFields.put(new ZipShort(1), extraField);
        original.setExtraFields(extraFields.values().toArray(new ZipExtraField[0]));
        ZipArchiveEntry cloned = (ZipArchiveEntry) original.clone();

        assertEquals(original.getName(), cloned.getName());
        assertEquals(original.getMethod(), cloned.getMethod());
        assertEquals(original.getInternalAttributes(), cloned.getInternalAttributes());
        assertEquals(original.getExternalAttributes(), cloned.getExternalAttributes());
        assertArrayEquals(original.getExtraFields(), cloned.getExtraFields());
    }

    @org.junit.Test
    public void testSetAndGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());

        entry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, entry.getMethod());

        try {
            entry.setMethod(ZipEntry.UNKNOWN);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    @org.junit.Test
    public void testSetAndGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @org.junit.Test
    public void testSetExtraFields() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtraField(new ZipShort(1), "test");
        LinkedHashMap<ZipShort, ZipExtraField> extraFields = new LinkedHashMap<>();
        extraFields.put(new ZipShort(1), extraField);
        byte[] extraData = new ByteArrayOutputStream().toByteArray();
        ByteUtils.writeByteArrayToByteBuffer(extraData, extraFields.values().toArray(new ZipExtraField[0]));
        entry.setExtra(extraData);

        ZipExtraField[] result = entry.getExtraFields();
        assertEquals(1, result.length);
        assertEquals(new ZipShort(1), result[0].getHeaderId());
        assertEquals("test", result[0].getRawData().toString());
    }

    @org.junit.Test
    public void testSetAndGetLocalFileExtraFields() throws IOException, ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtraField(new ZipShort(1), "test");
        LinkedHashMap<ZipShort, ZipExtraField> extraFields = new LinkedHashMap<>();
        extraFields.put(new ZipShort(1), extraField);
        byte[] extraData = new ByteArrayOutputStream().toByteArray();
        ByteUtils.writeByteArrayToByteBuffer(extraData, extraFields.values().toArray(new ZipExtraField[0]));
        entry.setExtra(extraData);

        byte[] localExtraData = entry.getLocalFileDataExtra();
        ZipExtraField[] localExtraFields = ExtraFieldUtils.parse(localExtraData, true);
        assertEquals(1, localExtraFields.length);
        assertEquals(new ZipShort(1), localExtraFields[0].getHeaderId());
        assertEquals("test", localExtraFields[0].getRawData().toString());
    }

    @org.junit.Test
    public void testSetAndGetCentralDirectoryExtraFields() throws IOException, ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField extraField = new ZipExtraField(new ZipShort(1), "test");
        LinkedHashMap<ZipShort, ZipExtraField> extraFields = new LinkedHashMap<>();
        extraFields.put(new ZipShort(1), extraField);
        byte[] extraData = new ByteArrayOutputStream().toByteArray();
        ByteUtils.writeByteArrayToByteBuffer(extraData, extraFields.values().toArray(new ZipExtraField[0]));
        entry.setCentralDirectoryExtra(extraData);

        byte[] centralExtraData = entry.getCentralDirectoryExtra();
        ZipExtraField[] centralExtraFields = ExtraFieldUtils.parse(centralExtraData, false);
        assertEquals(1, centralExtraFields.length);
        assertEquals(new ZipShort(1), centralExtraFields[0].getHeaderId());
        assertEquals("test", centralExtraFields[0].getRawData().toString());
    }

    @org.junit.Test
    public void testSetAndGetName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setName("test2.txt");
        assertEquals("test2.txt", entry.getName());
    }

    @org.junit.Test
    public void testHashCode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(entry.hashCode(), entry.getName().hashCode());
    }

    @org.junit.Test
    public void testIsDirectory() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertFalse(entry.isDirectory());

        entry = new ZipArchiveEntry("test/");
        assertTrue(entry.isDirectory());
    }

    @org.junit.Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
        assertTrue(entry1.equals(entry2));

        entry2 = new ZipArchiveEntry("test2.txt");
        assertFalse(entry1.equals(entry2));

        entry1.setName("test.txt");
        entry2.setName("test.txt");
        assertTrue(entry1.equals(entry2));

        entry1.setName("test2.txt");
        assertFalse(entry1.equals(entry2));
    }
}