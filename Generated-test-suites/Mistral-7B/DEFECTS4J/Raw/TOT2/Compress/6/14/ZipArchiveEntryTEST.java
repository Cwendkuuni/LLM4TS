package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.BytesSize;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

/**
 * Test class for {@link ZipArchiveEntry}.
 */
public class ZipArchiveEntryTest {

    /**
     * Test constructor with name.
     */
    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
    }

    /**
     * Test constructor with ZipEntry.
     */
    @Test
    public void testConstructorWithZipEntry() throws ZipException {
        String testEntryName = "test.txt";
        byte[] testExtra = new byte[] { 0x07, 0x00, 0x01, 0x01, 0x02, 0x03 };
        ZipExtraField testExtraField = new ZipExtraField("Test", "TestValue", testExtra);
        byte[] testData = new byte[] { 0x01, 0x02 };
        ZipEntry testZipEntry = new ZipEntry(testEntryName);
        ZipArchiveEntry entry = new ZipArchiveEntry(testZipEntry);
        assertNotNull(entry);
        assertEquals(testEntryName, entry.getName());
        assertEquals(0, entry.getSize());
        assertNull(entry.getSizeLimit());
        assertEquals(ZipArchiveEntry.DEFLATED, entry.getMethod());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getExternalAttributes());
        assertNull(entry.getName());
        assertNull(entry.getExtraFields());
        assertNull(entry.getLocalFileDataExtra());
        assertNull(entry.getCentralDirectoryExtra());
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());

        entry.setExtraFields(new ZipExtraField[] { testExtraField });
        assertEquals(1, entry.getExtraFields().length);
        ZipExtraField actualExtraField = entry.getExtraField(testExtraField.getHeaderId());
        assertEquals(testExtraField, actualExtraField);
        assertEquals(testExtra, actualExtraField.getLocalFileDataData());

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        IOUtils.write(testData, byteStream, null);
        InputStream inputStream = new ByteArrayInputStream(byteStream.toByteArray());
        entry.setSize(new BytesSize(testData.length));
        entry.setInputStream(inputStream);

        assertEquals(testData.length, entry.getSize().value);
    }

    /**
     * Test constructor with File.
     */
    @Test
    public void testConstructorWithFile() {
        File testFile = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(testFile, "test.txt");
        assertEquals("test.txt", entry.getName());
        assertEquals(testFile.length(), entry.getSize().value);
        assertNull(entry.getSizeLimit());
        assertEquals(ZipArchiveEntry.STORED, entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        assertNull(entry.getName());
        assertNull(entry.getExtraFields());
        assertNull(entry.getLocalFileDataExtra());
        assertNull(entry.getCentralDirectoryExtra());
    }

    /**
     * Test constructor with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullName() {
        new ZipArchiveEntry(null);
    }

    /**
     * Test setUnixMode.
     */
    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    /**
     * Test getPlatform.
     */
    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
    }

    /**
     * Test setPlatform.
     */
    @Test
    public void testSetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    /**
     * Test setExtraFields.
     */
    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField testExtraField = new ZipExtraField("Test", "TestValue", new byte[] { 0x01, 0x02 });
        Map<ZipShort, ZipExtraField> extraFields = new LinkedHashMap<>();
        extraFields.put(ZipShort.UNIX_PERMISSIONS, testExtraField);
        entry.setExtraFields(extraFields.values().toArray(new ZipExtraField[0]));
        assertEquals(1, entry.getExtraFields().length);
        assertEquals(testExtraField, entry.getExtraField(ZipShort.UNIX_PERMISSIONS));
    }

    /**
     * Test setExtra.
     */
    @Test
    public void testSetExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] testExtra = new byte[] { 0x07, 0x00, 0x01, 0x01, 0x02, 0x03 };
        ZipExtraField testExtraField = new ZipExtraField("Test", "TestValue", testExtra);
        entry.setExtra(testExtra);
        assertEquals(1, entry.getExtraFields().length);
        assertEquals(testExtraField, entry.getExtraField(testExtraField.getHeaderId()));
    }

    /**
     * Test setCentralDirectoryExtra.
     */
    @Test
    public void testSetCentralDirectoryExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] testExtra = new byte[] { 0x07, 0x00, 0x01, 0x01, 0x02, 0x03 };
        ZipExtraField testExtraField = new ZipExtraField("Test", "TestValue", testExtra);
        entry.setCentralDirectoryExtra(testExtra);
        assertEquals(1, entry.getExtraFields().length);
        assertEquals(testExtraField, entry.getExtraField(testExtraField.getHeaderId()));
    }

    /**
     * Test getName.
     */
    @Test
    public void testGetName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
    }

    /**
     * Test isDirectory.
     */
    @Test
    public void testIsDirectory() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertFalse(entry.isDirectory());

        entry = new ZipArchiveEntry("test/");
        assertTrue(entry.isDirectory());
    }

    /**
     * Test hashCode.
     */
    @Test
    public void testHashCode() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
        assertEquals(entry1.hashCode(), entry2.hashCode());

        entry1 = new ZipArchiveEntry("test.txt");
        entry2 = new ZipArchiveEntry("test2.txt");
        assertNotEquals(entry1.hashCode(), entry2.hashCode());
    }

    /**
     * Test equals.
     */
    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
        assertTrue(entry1.equals(entry2));

        entry1 = new ZipArchiveEntry("test.txt");
        entry2 = new ZipArchiveEntry("test2.txt");
        assertFalse(entry1.equals(entry2));

        entry1 = new ZipArchiveEntry("test.txt");
        entry2 = new ZipArchiveEntry("test.txt");
        assertEquals(entry1.toString(), entry2.toString());
    }

    /**
     * Test getMethod.
     */
    @Test
    public void testGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(-1, entry.getMethod());

        entry = new ZipArchiveEntry("test.txt", new ZipEntry("test.txt"));
        assertEquals(ZipArchiveEntry.STORED, entry.getMethod());

        entry = new ZipArchiveEntry("test.txt", new ZipEntry("test.txt"));
        entry.setMethod(ZipArchiveEntry.DEFLATED);
        assertEquals(ZipArchiveEntry.DEFLATED, entry.getMethod());
    }

    /**
     * Test setMethod.
     */
    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertThrows(IllegalArgumentException.class, () -> {
            entry.setMethod(-1);
        });
        entry.setMethod(ZipArchiveEntry.STORED);
        assertEquals(ZipArchiveEntry.STORED, entry.getMethod());
        entry.setMethod(ZipArchiveEntry.DEFLATED);
        assertEquals(ZipArchiveEntry.DEFLATED, entry.getMethod());
    }

    /**
     * Test getInternalAttributes.
     */
    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getInternalAttributes());

        entry.setInternalAttributes(ZipConstants.FILE_ATTRIBUTE_HIDDEN);
        assertEquals(ZipConstants.FILE_ATTRIBUTE_HIDDEN, entry.getInternalAttributes());

        entry.setInternalAttributes(ZipConstants.FILE_ATTRIBUTE_DIRECTORY);
        assertEquals(ZipConstants.FILE_ATTRIBUTE_DIRECTORY, entry.getInternalAttributes());

        entry.setInternalAttributes(ZipConstants.FILE_ATTRIBUTE_READ_ONLY);
        assertEquals(ZipConstants.FILE_ATTRIBUTE_READ_ONLY, entry.getInternalAttributes());

        entry.setInternalAttributes(ZipConstants.FILE_ATTRIBUTE_ARCHIVE);
        assertEquals(ZipConstants.FILE_ATTRIBUTE_ARCHIVE, entry.getInternalAttributes());

        entry.setInternalAttributes(ZipConstants.FILE_ATTRIBUTE_NORMAL);
        assertEquals(ZipConstants.FILE_ATTRIBUTE_NORMAL, entry.getInternalAttributes());
    }

    /**
     * Test getExternalAttributes.
     */
    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getExternalAttributes());

        entry.setExternalAttributes(ZipConstants.CR_FILE_SYMLINK);
        assertEquals(ZipConstants.CR_FILE_SYMLINK, entry.getExternalAttributes());

        entry.setExternalAttributes(ZipConstants.CR_FILE_HIDDEN);
        assertEquals(ZipConstants.CR_FILE_HIDDEN, entry.getExternalAttributes());

        entry.setExternalAttributes(ZipConstants.CR_FILE_DIRECTORY);
        assertEquals(ZipConstants.CR_FILE_DIRECTORY, entry.getExternalAttributes());

        entry.setExternalAttributes(ZipConstants.CR_FILE_READ_ONLY);
        assertEquals(ZipConstants.CR_FILE_READ_ONLY, entry.getExternalAttributes());

        entry.setExternalAttributes(ZipConstants.CR_FILE_NORMAL);
        assertEquals(ZipConstants.CR_FILE_NORMAL, entry.getExternalAttributes());
    }

    /**
     * Test getTime.
     */
    @Test
    public void testGetTime() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(0, entry.getTime());

        Date testDate = new Date();
        entry.setTime(testDate.getTime());
        assertEquals(testDate, new Date(entry.getTime()));
    }

    /**
     * Test equals with null.
     */
    @Test
    public void testEqualsWithNull() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertFalse(entry.equals(null));
    }
}