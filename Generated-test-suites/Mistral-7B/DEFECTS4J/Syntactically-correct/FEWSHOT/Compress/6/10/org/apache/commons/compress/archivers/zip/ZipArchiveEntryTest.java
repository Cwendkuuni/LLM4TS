package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Test;

/**
 * Test cases for {@link ZipArchiveEntry}.
 */
public class ZipArchiveEntryTest {

    private static final String TEST_FILE_NAME = "testFile.txt";

    /**
     * Test constructor.
     */
    @Test
    public void testConstructors() {
        // Test default constructor
        ZipArchiveEntry entry = new ZipArchiveEntry();
        assertNull(entry.getName());
        assertNull(entry.getExtraFields());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getExternalAttributes());

        // Test string constructor
        entry = new ZipArchiveEntry(TEST_FILE_NAME);
        assertEquals(TEST_FILE_NAME, entry.getName());
        assertNull(entry.getExtraFields());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getExternalAttributes());

        // Test zip entry constructor
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(TEST_FILE_NAME);
        entry = new ZipArchiveEntry(zipEntry);
        assertEquals(TEST_FILE_NAME, entry.getName());
        assertNull(entry.getExtraFields());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getExternalAttributes());

        // Test clone constructor
        entry = new ZipArchiveEntry();
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) entry.clone();
        assertSame(entry, clonedEntry);

        // Test file constructor
        File testFile = File.createTempFile("test", null);
        entry = new ZipArchiveEntry(testFile, TEST_FILE_NAME);
        assertEquals(TEST_FILE_NAME, entry.getName());
        assertNull(entry.getExtraFields());
        assertEquals(testFile.length(), entry.getSize());
        assertEquals(testFile.lastModified(), entry.getTime());
    }

    /**
     * Test {@link ZipArchiveEntry#clone()} method.
     *
     * @throws IOException on error
     */
    @Test
    public void testClone() throws IOException {
        // Test empty entry
        ZipArchiveEntry emptyEntry = new ZipArchiveEntry();
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) emptyEntry.clone();
        assertSame(emptyEntry, clonedEntry);

        // Test entry with extra fields
        byte[] extraData = TestUtils.getTestData("extra_fields.bin");
        ZipArchiveEntry entry = new ZipArchiveEntry();
        entry.setExtraFields(ExtraFieldUtils.parse(extraData));
        clonedEntry = (ZipArchiveEntry) entry.clone();
        assertNotSame(entry, clonedEntry);
        assertArrayEquals(entry.getLocalFileDataExtra(), clonedEntry.getLocalFileDataExtra());
        assertArrayEquals(entry.getCentralDirectoryExtra(), clonedEntry.getCentralDirectoryExtra());
    }

    /**
     * Test {@link ZipArchiveEntry#equals(Object)} method.
     */
    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry();
        ZipArchiveEntry entry2 = new ZipArchiveEntry();
        ZipArchiveEntry equalEntry = new ZipArchiveEntry();

        // Test null object
        assertFalse(entry1.equals(null));

        // Test self-equality
        assertTrue(entry1.equals(entry1));

        // Test equality with another entry having the same name
        equalEntry.setName(entry1.getName());
        assertTrue(entry1.equals(equalEntry));
        assertTrue(equalEntry.equals(entry1));

        // Test inequality with another entry having a different name
        ZipArchiveEntry differentEntry = new ZipArchiveEntry();
        differentEntry.setName("differentName");
        assertFalse(entry1.equals(differentEntry));
        assertFalse(differentEntry.equals(entry1));
    }

    /**
     * Test methods for {@link ZipArchiveEntry#getMethod()},
     * {@link ZipArchiveEntry#setMethod(int)}, and
     * {@link ZipArchiveEntry#isSupportedCompressionMethod()}.
     */
    @Test
    public void testMethodsForGetMethodSetMethodAndIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry();

        // Test default value
        assertEquals(-1, entry.getMethod());
        assertFalse(entry.isSupportedCompressionMethod());

        // Test setting compression method
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());
        assertTrue(entry.isSupportedCompressionMethod());

        // Test setting unsupported compression method
        entry.setMethod(ZipEntry.UNKNOWN);
        assertEquals(ZipEntry.UNKNOWN, entry.getMethod());
        assertFalse(entry.isSupportedCompressionMethod());

        // Test setting unsupported negative value
        entry.setMethod(-1);
        assertEquals(-1, entry.getMethod());
        assertFalse(entry.isSupportedCompressionMethod());
    }

    /**
     * Test {@link ZipArchiveEntry#getPlatform()},
     * {@link ZipArchiveEntry#setPlatform(int)}, and
     * {@link ZipArchiveEntry#getUnixMode()}.
     */
    @Test
    public void testMethodsForGetPlatformSetPlatformAndGetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry();

        // Test default value
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        assertEquals(0, entry.getUnixMode());

        // Test setting UNIX platform
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
        assertEquals(0755, entry.getUnixMode());

        // Test setting FAT platform
        entry.setPlatform(ZipArchiveEntry.PLATFORM_FAT);
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        assertEquals(0, entry.getUnixMode());
    }

    /**
     * Test methods for {@link ZipArchiveEntry#setInternalAttributes(int)}
     * and {@link ZipArchiveEntry#getInternalAttributes()}.
     */
    @Test
    public void testMethodsForSetInternalAttributesAndGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry();

        // Test default value
        assertEquals(0, entry.getInternalAttributes());

        // Test setting internal attributes
        entry.setInternalAttributes(ZipEntry.FILE_ATTRIBUTES);
        assertEquals(ZipEntry.FILE_ATTRIBUTES, entry.getInternalAttributes());

        // Test setting invalid value
        entry.setInternalAttributes(-1);
        assertEquals(0, entry.getInternalAttributes());
    }

    /**
     * Test methods for {@link ZipArchiveEntry#setExternalAttributes(long)}
     * and {@link ZipArchiveEntry#getExternalAttributes()}.
     */
    @Test
    public void testMethodsForSetExternalAttributesAndGetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry();

        // Test default value
        assertEquals(0, entry.getExternalAttributes());

        // Test setting external attributes
        entry.setExternalAttributes(0200 | 0100L << 16);
        assertEquals(0200 | 0100L << 16, entry.getExternalAttributes());

        // Test setting invalid value
        entry.setExternalAttributes(-1);
        assertEquals(0, entry.getExternalAttributes());
    }

    /**
     * Test methods for {@link ZipArchiveEntry#addExtraField(ZipExtraField)}
     * and {@link ZipArchiveEntry#getExtraFields()}.
     */
    @Test
    public void testMethodsForAddExtraFieldAndGetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry();

        // Test empty entry
        assertArrayEquals(new ZipExtraField[0], entry.getExtraFields());

        // Test adding extra fields
        ZipShort testHeaderId = new ZipShort(1);
        byte[] testData = new byte[10];
        Arrays.fill(testData, (byte) 0xAA);
        ZipExtraField testExtraField = new ZipGenericExtraField(testHeaderId, testData);
        entry.addExtraField(testExtraField);
        ZipExtraField[] expectedExtraFields = {testExtraField};
        assertArrayEquals(expectedExtraFields, entry.getExtraFields());

        // Test adding multiple extra fields
        testExtraField = new ZipGenericExtraField(new ZipShort(2), testData);
        entry.addExtraField(testExtraField);
        expectedExtraFields = new ZipExtraField[] {testExtraField, testExtraField};
        assertArrayEquals(expectedExtraFields, entry.getExtraFields());

        // Test adding duplicate extra field
        entry.addExtraField(testExtraField);
        assertEquals(2, entry.getExtraFields().length);
    }

    /**
     * Test methods for {@link ZipArchiveEntry#setExtra(byte[])}.
     */
    @Test
    public void testMethodsForSetExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry();

        // Test empty entry
        assertEquals(new byte[0][], entry.getLocalFileDataExtra());
        assertEquals(new byte[0][], entry.getCentralDirectoryExtra());

        // Test setting extra data for local file
        byte[] extraData = TestUtils.getTestData("extra_fields.bin");
        entry.setExtra(extraData);
        byte[] expectedLocalFileDataExtra = TestUtils.getTestData("local_file_extra_data.bin");
        byte[] expectedCentralDirectoryExtra = TestUtils.getTestData("central_directory_extra_data.bin");
        assertArrayEquals(expectedLocalFileDataExtra, entry.getLocalFileDataExtra());
        assertArrayEquals(expectedCentralDirectoryExtra, entry.getCentralDirectoryExtra());

        // Test setting extra data for central directory
        ByteArrayOutputStream centralDirectoryExtraDataOutputStream = new ByteArrayOutputStream();
        entry.setCentralDirectoryExtra(centralDirectoryExtraDataOutputStream.toByteArray());
        expectedCentralDirectoryExtra = centralDirectoryExtraDataOutputStream.toByteArray();
        assertArrayEquals(expectedCentralDirectoryExtra, entry.getCentralDirectoryExtra());
    }

    /**
     * Test methods for {@link ZipArchiveEntry#getSize()}.
     */
    @Test
    public void testMethodsForGetSize() {
        ZipArchiveEntry entry = new ZipArchiveEntry();

        // Test default value
        assertEquals(-1, entry.getSize());

        // Test setting size
        entry.setSize(1024);
        assertEquals(1024, entry.getSize());
    }

    /**
     * Test methods for {@link ZipArchiveEntry#getTime()}.
     */
    @Test
    public void testMethodsForGetTime() {
        ZipArchiveEntry entry = new ZipArchiveEntry();

        // Test default value
        assertEquals(0, entry.getTime());

        // Test setting time
        entry.setTime(System.currentTimeMillis());
        Date expectedTime = new Date(System.currentTimeMillis());
        assertEquals(expectedTime.getTime(), entry.getTime());
    }

    /**
     * Test method for {@link ZipArchiveEntry#getLocalFileDataExtra()}.
     */
    @Test
    public void testGetLocalFileDataExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry();

        // Test empty entry
        assertEquals(new byte[0][], entry.getLocalFileDataExtra());

        // Test setting and getting local file extra data
        byte[] extraData = TestUtils.getTestData("extra_fields.bin");
        entry.setExtra(extraData);
        byte[] expectedLocalFileDataExtra = TestUtils.getTestData("local_file_extra_data.bin");
        assertEquals(expectedLocalFileDataExtra, entry.getLocalFileDataExtra());
    }

    /**
     * Test method for {@link ZipArchiveEntry#getCentralDirectoryExtra()}.
     */
    @Test
    public void testGetCentralDirectoryExtra() {
        ZipArchiveEntry entry = new ZipArchiveEntry();

        // Test empty entry
        assertEquals(new byte[0][], entry.getCentralDirectoryExtra());

        // Test setting and getting central directory extra data
        ByteArrayOutputStream centralDirectoryExtraDataOutputStream = new ByteArrayOutputStream();
        entry.setCentralDirectoryExtra(centralDirectoryExtraDataOutputStream.toByteArray());
        byte[] expectedCentralDirectoryExtra = centralDirectoryExtraDataOutputStream.toByteArray();
        assertEquals(expectedCentralDirectoryExtra, entry.getCentralDirectoryExtra());
    }
}