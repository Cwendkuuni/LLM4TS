package org.apache.commons.compress.archivers.zip; // Import necessary packages

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.ZipUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for ZipArchiveEntry.
 */
public class ZipArchiveEntryTest {

    // Test data for various test cases
    private static final String TEST_FILE_NAME = "testFile.txt";
    private static final byte[] TEST_FILE_CONTENTS = "Test file contents".getBytes();
    private static final String TEST_DIRECTORY_NAME = "testDir/";
    private static final int TEST_UNIX_PERMISSION = 0755;

    /**
     * Initialize static variables and objects before each test.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
    }

    /**
     * Release any static resources after each test.
     */
    @AfterClass
    public static void tearDownAfterClass() {
    }

    /**
     * Test constructor with empty string name.
     */
    @Test
    public void testConstructorWithEmptyStringName() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("");
        assertNull(zipEntry.getName());
        assertEquals(ZipEntry.STORED, zipEntry.getMethod());
        assertEquals(0, zipEntry.getSize());
        assertEquals(-1L, zipEntry.getCrc());
        assertNull(zipEntry.getTime());
        assertNull(zipEntry.getExtra());
        assertNull(zipEntry.getCompressedSize());
        assertNull(zipEntry.getLocalFileName());
        assertFalse(zipEntry.isDirectory());
    }

    /**
     * Test constructor with valid name.
     */
    @Test
    public void testConstructorWithValidName() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(TEST_FILE_NAME);
        assertEquals(TEST_FILE_NAME, zipEntry.getName());
        assertEquals(ZipEntry.STORED, zipEntry.getMethod());
        assertEquals(-1, zipEntry.getSize());
        assertEquals(-1L, zipEntry.getCrc());
        assertNull(zipEntry.getTime());
        assertNull(zipEntry.getExtra());
        assertEquals(-1L, zipEntry.getCompressedSize());
        assertNull(zipEntry.getLocalFileName());
        assertFalse(zipEntry.isDirectory());
    }

    /**
     * Test constructor with Java ZipEntry.
     */
    @Test
    public void testConstructorWithJavaZipEntry() throws ZipException {
        ZipEntry zipEntry = new ZipEntry(TEST_FILE_NAME);
        ZipArchiveEntry testEntry = new ZipArchiveEntry((ZipEntry) zipEntry);
        assertEquals(TEST_FILE_NAME, testEntry.getName());
        assertEquals(ZipEntry.STORED, testEntry.getMethod());
        assertEquals(-1, testEntry.getSize());
        assertEquals(-1L, testEntry.getCrc());
        assertNull(testEntry.getTime());
        assertNull(testEntry.getExtra());
        assertEquals(-1L, testEntry.getCompressedSize());
        assertNull(testEntry.getLocalFileName());
        assertFalse(testEntry.isDirectory());
    }

    /**
     * Test constructor with Cloneable ZipArchiveEntry.
     */
    @Test
    public void testConstructorWithCloneableZipArchiveEntry() throws ZipException {
        ZipArchiveEntry originalEntry = new ZipArchiveEntry(TEST_FILE_NAME);
        ZipArchiveEntry cloneEntry = (ZipArchiveEntry) originalEntry.clone();
        assertEquals(TEST_FILE_NAME, cloneEntry.getName());
        assertEquals(ZipEntry.STORED, cloneEntry.getMethod());
        assertEquals(-1, cloneEntry.getSize());
        assertEquals(-1L, cloneEntry.getCrc());
        assertNull(cloneEntry.getTime());
        assertNull(cloneEntry.getExtra());
        assertEquals(-1L, cloneEntry.getCompressedSize());
        assertNull(cloneEntry.getLocalFileName());
        assertFalse(cloneEntry.isDirectory());
    }

    /**
     * Test constructor with File input.
     */
    @Test
    public void testConstructorWithFileInput() {
        File testFile = new File(TEST_FILE_NAME);
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(testFile, TEST_FILE_NAME);
        assertEquals(TEST_FILE_NAME, zipEntry.getName());
        assertEquals(ZipEntry.STORED, zipEntry.getMethod());
        assertEquals(testFile.length(), zipEntry.getSize());
        assertEquals(ZipUtils.crc32(TEST_FILE_CONTENTS), zipEntry.getCrc());
        assertEquals(new Date(testFile.lastModified()).getTime(), zipEntry.getTime().getTime());
        assertNull(zipEntry.getExtra());
        assertEquals(-1L, zipEntry.getCompressedSize());
        assertNull(zipEntry.getLocalFileName());
        assertFalse(zipEntry.isDirectory());
    }

    /**
     * Test getters and setters for internalAttributes, platform, and externalAttributes.
     */
    @Test
    public void testGettersAndSettersForInternalAttributesPlatformAndExternalAttributes() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(TEST_FILE_NAME);

        // Test initial values
        assertEquals(0, zipEntry.getInternalAttributes());
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipEntry.getPlatform());
        assertEquals(0L, zipEntry.getExternalAttributes());

        // Test setting values and getting them back
        zipEntry.setInternalAttributes(ZipEntry.FLAG_UNIX_USER);
        zipEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        zipEntry.setExternalAttributes(TEST_UNIX_PERMISSION);

        assertEquals(ZipEntry.FLAG_UNIX_USER, zipEntry.getInternalAttributes());
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipEntry.getPlatform());
        assertEquals(TEST_UNIX_PERMISSION, zipEntry.getExternalAttributes());
    }

    /**
     * Test getters and setters for extraFields.
     */
    @Test
    public void testGettersAndSettersForExtraFields() throws ZipException {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(TEST_FILE_NAME);

        // Test initial values
        assertNull(zipEntry.getExtraFields());

        // Test setting values and getting them back
        ZipShort testHeaderId = new ZipShort(1);
        ZipExtraField testExtraField = new MyCustomExtraField(testHeaderId, "Test Extra Field");
        zipEntry.setExtraFields(new ZipExtraField[] { testExtraField });
        ZipExtraField[] testExtraFields = zipEntry.getExtraFields();

        assertEquals(1, testExtraFields.length);
        assertEquals(testHeaderId, testExtraFields[0].getHeaderId());
        assertEquals("Test Extra Field", testExtraFields[0].toString());

        // Test setting empty extra fields and getting them back
        zipEntry.setExtraFields(new ZipExtraField[0]);
        testExtraFields = zipEntry.getExtraFields();
        assertEquals(0, testExtraFields.length);
    }

    /**
     * Test method isSupportedCompressionMethod() with valid method.
     */
    @Test
    public void testMethodIsSupportedCompressionMethodWithValidMethod() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(TEST_FILE_NAME);
        assertTrue(zipEntry.isSupportedCompressionMethod());
    }

    /**
     * Test method isSupportedCompressionMethod() with invalid method.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testMethodIsSupportedCompressionMethodWithInvalidMethod() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(TEST_FILE_NAME);
        zipEntry.setMethod(-1);
        zipEntry.isSupportedCompressionMethod();
    }

    /**
     * Test getters and setters for method.
     */
    @Test
    public void testGettersAndSettersForMethod() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(TEST_FILE_NAME);

        // Test initial value
        assertEquals(-1, zipEntry.getMethod());

        // Test setting values and getting them back
        zipEntry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, zipEntry.getMethod());

        // Test setting non-supported value and getting it back
        try {
            zipEntry.setMethod(ZipEntry.BZIP2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        assertEquals(-1, zipEntry.getMethod());
    }

    /**
     * Test equals() and hashCode() methods.
     */
    @Test
    public void testEqualsAndHashCodeMethods() {
        ZipArchiveEntry zipEntry1 = new ZipArchiveEntry(TEST_FILE_NAME);
        ZipArchiveEntry zipEntry2 = new ZipArchiveEntry(TEST_FILE_NAME);

        // Test equals() with same object
        assertTrue(zipEntry1.equals(zipEntry1));

        // Test equals() with different objects but same name
        assertTrue(zipEntry1.equals(zipEntry2));

        // Test equals() with different objects and different names
        ZipArchiveEntry zipEntry3 = new ZipArchiveEntry("otherName.txt");
        assertFalse(zipEntry1.equals(zipEntry3));

        // Test hashCode()
        int hashCode1 = zipEntry1.hashCode();
        int hashCode2 = zipEntry2.hashCode();
        assertEquals(hashCode1, hashCode2);

        // Test hashCode() with different objects but same name
        int hashCode3 = zipEntry2.hashCode();
        int hashCode4 = zipEntry1.hashCode();
        assertEquals(hashCode1, hashCode3);
        assertEquals(hashCode1, hashCode4);
    }

    /**
     * Test parseExtraData() method.
     */
    @Test
    public void testParseExtraDataMethod() throws ZipException, IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_FILE_CONTENTS);

        // Test parsing local file data
        byte[] localExtraData = new byte[10];
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(TEST_FILE_NAME);
        zipEntry.parseExtraData(localExtraData, 0, localExtraData.length, inputStream);
        assertNotNull(zipEntry.getExtraFields());
        assertEquals(1, zipEntry.getExtraFields().length);
        assertNotNull(zipEntry.getExtraFields()[0]);
        assertEquals("Test Extra Field", zipEntry.getExtraFields()[0].toString());

        // Test parsing central directory data
        byte[] centralExtraData = new byte[10];
        System.arraycopy(localExtraData, 0, centralExtraData, 0, localExtraData.length);
        zipEntry.setCentralDirectoryExtra(centralExtraData);
        zipEntry.parseExtraData(localExtraData, 0, localExtraData.length, inputStream);
        assertEquals(2, zipEntry.getExtraFields().length);
        assertNotNull(zipEntry.getExtraFields()[1]);
        assertEquals("Test Extra Field", zipEntry.getExtraFields()[1].toString());
    }

    /**
     * Custom extra field for testing.
     */
    static class MyCustomExtraField extends ZipExtraField {

        public MyCustomExtraField(ZipShort headerId, String fieldName) {
            super(headerId, fieldName);
        }

        @Override
        public String toString() {
            return "Custom Extra Field: " + getStringValue();
        }
    }
}