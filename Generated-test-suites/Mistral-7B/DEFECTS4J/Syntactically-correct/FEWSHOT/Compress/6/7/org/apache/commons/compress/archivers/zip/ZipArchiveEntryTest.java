package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.ByteStreams;

public class ZipArchiveEntryTest {
    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());

        entry.setUnixMode(0755);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetAndGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(-1, entry.getMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(0);
        assertEquals(0, entry.getInternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(1);
        assertEquals(1, entry.getExternalAttributes());
    }

    @Test
    public void testSetExtraFields() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField extraField = new ZipExtendedInformation("test");
        entry.setExtraFields(new ZipExtraField[] { extraField });

        try {
            entry.setExtra(extraField.getLocalFileDataData());
            byte[] extra = entry.getLocalFileDataExtra();
            assertEquals(extraField.getLocalFileDataData().length, extra.length);
            assertArrayEquals(extraField.getLocalFileDataData(), extra);
        } catch (IOException e) {
            fail("should not throw IOException: " + e.getMessage());
        }

        try {
            entry.setCentralDirectoryExtra(extraField.getCentralDirectoryData());
            byte[] extra = entry.getCentralDirectoryExtra();
            assertEquals(extraField.getCentralDirectoryData().length, extra.length);
            assertArrayEquals(extraField.getCentralDirectoryData(), extra);
        } catch (IOException e) {
            fail("should not throw IOException: " + e.getMessage());
        }

        try {
            ZipExtraField extraFieldFromEntry = entry.getExtraField(extraField.getHeaderId());
            assertNotNull(extraFieldFromEntry);
            assertEquals(extraField.getClass(), extraFieldFromEntry.getClass());
            assertArrayEquals(extraField.getLocalFileDataData(), extraFieldFromEntry.getLocalFileDataData());
            assertArrayEquals(extraField.getCentralDirectoryData(), extraFieldFromEntry.getCentralDirectoryData());
        } catch (NullPointerException e) {
            fail("should not throw NullPointerException: " + e.getMessage());
        }

        entry.removeExtraField(extraField.getHeaderId());
        assertNull(entry.getExtraField(extraField.getHeaderId()));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        ZipArchiveEntry originalEntry = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) originalEntry.clone();

        ZipExtraField extraField = new ZipExtendedInformation("test");
        originalEntry.setExtraFields(new ZipExtraField[] { extraField });

        try {
            originalEntry.setExtra(extraField.getLocalFileDataData());
        } catch (IOException e) {
            fail("should not throw IOException: " + e.getMessage());
        }

        assertEquals(originalEntry.getMethod(), clonedEntry.getMethod());
        assertNotEquals(originalEntry.getMethod(), clonedEntry.hashCode());
        assertEquals(originalEntry.getPlatform(), clonedEntry.getPlatform());
        assertNotEquals(originalEntry.getPlatform(), clonedEntry.hashCode());
        assertEquals(originalEntry.getInternalAttributes(), clonedEntry.getInternalAttributes());
        assertNotEquals(originalEntry.getInternalAttributes(), clonedEntry.hashCode());
        assertEquals(originalEntry.getExternalAttributes(), clonedEntry.getExternalAttributes());
        assertNotEquals(originalEntry.getExternalAttributes(), clonedEntry.hashCode());
        assertArrayEquals(originalEntry.getLocalFileDataExtra(), clonedEntry.getLocalFileDataExtra());
        assertNotEquals(originalEntry.getLocalFileDataExtra(), clonedEntry.hashCode());
        assertArrayEquals(originalEntry.getCentralDirectoryExtra(), clonedEntry.getCentralDirectoryExtra());
        assertNotEquals(originalEntry.getCentralDirectoryExtra(), clonedEntry.hashCode());
        assertArrayEquals(originalEntry.getName().getBytes(), clonedEntry.getName().getBytes());
        assertNotEquals(originalEntry.getName(), clonedEntry.getName());
        assertArrayEquals(originalEntry.getExtraFields(), clonedEntry.getExtraFields());
        assertNotEquals(originalEntry.getExtraFields(), clonedEntry.hashCode());
    }

    @Test
    public void testSetName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setName("test2.txt");
        assertEquals("test2.txt", entry.getName());
    }
}