package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.util.ExtraField;
import org.apache.commons.compress.archivers.util.ExtraFieldUtils;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

/**
 * JUnit test cases for {@link ZipArchiveEntry} class.
 */
public class ZipArchiveEntryTest {

    /**
     * Test constructor with name.
     */
    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
        assertFalse(entry.isSupportedCompressionMethod());
        assertEquals(-1, entry.getMethod());
    }

    /**
     * Test constructor with ZipEntry.
     */
    @Test
    public void testConstructorWithZipEntry() throws ZipException {
        String testEntryName = "test.txt";
        ZipEntry zipEntry = new ZipEntry(testEntryName);
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);

        assertEquals(testEntryName, entry.getName());
        assertTrue(entry.isSupportedCompressionMethod());
        assertEquals(zipEntry.getMethod(), entry.getMethod());

        // Test platform and attributes
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(zipEntry.getExtra(), entry.getLocalFileDataExtra());
        assertEquals(zipEntry.getExtra(), entry.getCentralDirectoryExtra());
        assertEquals(zipEntry.getSize(), entry.getSize());
        assertEquals(zipEntry.getTime(), entry.getLastModifiedDate().getTime());

        // Test extraFields
        assertNull(entry.getExtraFields());
        byte[] extraData = zipEntry.getExtra();
        int numExtraFields = ExtraFieldUtils.countFields(extraData);
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) entry.clone();
        ZipArchiveEntry deepClone = new ZipArchiveEntry(clonedEntry);
        ZipArchiveEntry entryWithExtra = new ZipArchiveEntry(zipEntry);
        List<ZipExtraField> expectedExtraFields = new ArrayList<>();

        for (int i = 0; i < numExtraFields; i++) {
            ZipExtraField extraField = ExtraFieldUtils.parse(extraData)[i];
            expectedExtraFields.add(extraField);
            entryWithExtra.addExtraField(extraField);
        }

        assertEquals(numExtraFields, entryWithExtra.getExtraFields().length);
        assertEquals(numExtraFields, clonedEntry.getExtraFields().length);
        assertEquals(numExtraFields, deepClone.getExtraFields().length);
        assertArrayEquals(expectedExtraFields.toArray(new ZipExtraField[0]),
                entryWithExtra.getExtraFields());
        assertArrayEquals(expectedExtraFields.toArray(new ZipExtraField[0]),
                clonedEntry.getExtraFields());
        assertArrayEquals(expectedExtraFields.toArray(new ZipExtraField[0]),
                deepClone.getExtraFields());
    }

    /**
     * Test constructor with another ZipArchiveEntry.
     */
    @Test
    public void testConstructorWithZipArchiveEntry() throws ZipException, CloneNotSupportedException {
        String testEntryName = "test.txt";
        ZipEntry zipEntry = new ZipEntry(testEntryName);
        ZipArchiveEntry originalEntry = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) originalEntry.clone();
        ZipArchiveEntry deepClone = new ZipArchiveEntry(clonedEntry);

        // Test platform and attributes
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, originalEntry.getPlatform());
        assertEquals(0, originalEntry.getInternalAttributes());
        assertEquals(originalEntry.getExternalAttributes(), deepClone.getExternalAttributes());
        assertEquals(originalEntry.getExtra(), deepClone.getLocalFileDataExtra());
        assertEquals(originalEntry.getExtra(), deepClone.getCentralDirectoryExtra());
        assertEquals(originalEntry.getSize(), deepClone.getSize());
        assertEquals(originalEntry.getLastModifiedDate().getTime(),
                deepClone.getLastModifiedDate().getTime());

        // Test extraFields
        assertNull(originalEntry.getExtraFields());
        assertNull(clonedEntry.getExtraFields());
        assertNull(deepClone.getExtraFields());

        // Test equality
        assertEquals(originalEntry, deepClone);
        assertEquals(deepClone, originalEntry);
        assertNotEquals(originalEntry, clonedEntry);
        assertNotEquals(clonedEntry, originalEntry);
    }

    /**
     * Test constructor with File.
     */
    @Test
    public void testConstructorWithFile() {
        String testFileName = "test.txt";
        File testFile = new File(testFileName);
        ZipArchiveEntry entry = new ZipArchiveEntry(testFile, "test.txt");

        assertEquals("test.txt", entry.getName());
        assertFalse(entry.isSupportedCompressionMethod());
        assertEquals(-1, entry.getMethod());

        // Test platform and attributes
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(testFile.lastModified(), entry.getLastModifiedDate().getTime());
        assertNull(entry.getExtraFields());

        // Test size
        assertEquals(testFile.length(), entry.getSize());
    }

    /**
     * Test setUnixMode.
     */
    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        // Test setting Unix permissions
        entry.setUnixMode(0777);
        assertEquals(0777, entry.getUnixMode());

        // Test setting platform
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    /**
     * Test equals.
     */
    @Test
    public void testEquals() {
        String testEntryName = "test.txt";
        ZipEntry zipEntry = new ZipEntry(testEntryName);
        ZipArchiveEntry originalEntry = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) originalEntry.clone();
        ZipArchiveEntry deepClone = new ZipArchiveEntry(clonedEntry);
        ZipArchiveEntry anotherEntry = new ZipArchiveEntry("another.txt");
        ZipArchiveEntry nullEntry = null;

        // Test equality with same entry
        assertTrue(originalEntry.equals(originalEntry));

        // Test equality with cloned entry
        assertTrue(originalEntry.equals(clonedEntry));
        assertTrue(clonedEntry.equals(originalEntry));

        // Test equality with deep cloned entry
        assertTrue(originalEntry.equals(deepClone));
        assertTrue(deepClone.equals(originalEntry));

        // Test inequality with another entry
        assertFalse(originalEntry.equals(anotherEntry));
        assertFalse(anotherEntry.equals(originalEntry));

        // Test inequality with null entry
        assertFalse(originalEntry.equals(nullEntry));
        assertFalse(nullEntry.equals(originalEntry));
    }

    /**
     * Test hashCode.
     */
    @Test
    public void testHashCode() {
        String testEntryName = "test.txt";
        ZipEntry zipEntry = new ZipEntry(testEntryName);
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);

        // Test name hashcode
        int nameHashCode = testEntryName.hashCode();
        assertEquals(nameHashCode, entry.hashCode());

        // Test different name hashcode
        String anotherName = "another.txt";
        ZipArchiveEntry anotherEntry = new ZipArchiveEntry(zipEntry);
        anotherEntry.setName(anotherName);
        assertNotEquals(nameHashCode, anotherEntry.hashCode());
    }
}