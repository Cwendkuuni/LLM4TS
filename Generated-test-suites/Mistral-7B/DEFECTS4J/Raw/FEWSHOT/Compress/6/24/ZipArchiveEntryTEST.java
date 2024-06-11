package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.PublicSuffixMatcherUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntry() {
        // Test default constructor
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("");
        assertNull(zipEntry.getName());
        assertEquals(0, zipEntry.getSize());
        assertEquals("", zipEntry.getComment());
        assertNull(zipEntry.getExtra());
        assertEquals(ZipEntry.DEF_COMPRESSION_METHOD, zipEntry.getMethod());
        assertEquals(0, zipEntry.getInternalAttributes());
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipEntry.getPlatform());
        assertEquals(0, zipEntry.getExternalAttributes());
        assertNull(zipEntry.getExtraFields());

        // Test constructor with name
        zipEntry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", zipEntry.getName());
        assertEquals(0, zipEntry.getSize());
        assertEquals("", zipEntry.getComment());
        assertNull(zipEntry.getExtra());
        assertEquals(ZipEntry.DEF_COMPRESSION_METHOD, zipEntry.getMethod());
        assertEquals(0, zipEntry.getInternalAttributes());
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipEntry.getPlatform());
        assertEquals(0, zipEntry.getExternalAttributes());
        assertNull(zipEntry.getExtraFields());

        // Test constructor from ZipEntry
        ZipEntry zipEntryObj = new ZipEntry("test.txt");
        zipEntry = new ZipArchiveEntry(zipEntryObj);
        assertEquals("test.txt", zipEntry.getName());
        assertEquals(0, zipEntry.getSize());
        assertEquals(zipEntryObj.getComment(), zipEntry.getComment());
        assertArrayEquals(zipEntryObj.getExtra(), zipEntry.getExtra());
        assertEquals(zipEntryObj.getMethod(), zipEntry.getMethod());
        assertEquals(zipEntryObj.getInternalAttributes(), zipEntry.getInternalAttributes());
        assertEquals(zipEntryObj.getPlatform(), zipEntry.getPlatform());
        assertEquals(zipEntryObj.getExternalAttributes(), zipEntry.getExternalAttributes());
        assertArrayEquals(zipEntryObj.getExtraFields(), zipEntry.getExtraFields().toArray());

        // Test constructor from ZipArchiveEntry
        ZipArchiveEntry zipEntry2 = new ZipArchiveEntry(zipEntry);
        assertEquals(zipEntry, zipEntry2);

        // Test clone method
        ZipArchiveEntry clonedEntry = zipEntry.clone();
        assertEquals(zipEntry, clonedEntry);

        // Test constructor with File and entryName
        File file = new File("test.txt");
        zipEntry = new ZipArchiveEntry(file, "test.txt");
        assertEquals("test.txt", zipEntry.getName());
        assertEquals(file.length(), zipEntry.getSize());
        assertEquals("", zipEntry.getComment());
        assertNull(zipEntry.getExtra());
        assertEquals(ZipEntry.DEF_COMPRESSION_METHOD, zipEntry.getMethod());
        assertEquals(0, zipEntry.getInternalAttributes());
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipEntry.getPlatform());
        assertEquals(file.lastModified(), zipEntry.getTime());
        assertNull(zipEntry.getExtraFields());

        // Test setting and getting method
        zipEntry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, zipEntry.getMethod());

        // Test setting and getting internal attributes
        zipEntry.setInternalAttributes(ZipEntry.FLAG_UNCHANGED);
        assertEquals(ZipEntry.FLAG_UNCHANGED, zipEntry.getInternalAttributes());

        // Test setting and getting external attributes
        zipEntry.setUnixMode(0775);
        assertEquals(0775, zipEntry.getUnixMode());
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipEntry.getPlatform());

        // Test setting and getting platform
        zipEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipEntry.getPlatform());

        // Test setting and getting extra fields
        ZipExtraField extraField1 = new ZipExtraField(new byte[0], ZipShort.createShort(1), "Test1");
        ZipExtraField extraField2 = new ZipExtraField(new byte[0], ZipShort.createShort(2), "Test2");
        List<ZipExtraField> extraFields = new ArrayList<>();
        extraFields.add(extraField1);
        extraFields.add(extraField2);
        zipEntry.setExtraFields(extraFields.toArray(new ZipExtraField[0]));
        assertEquals(2, zipEntry.getExtraFields().length);
        assertEquals(extraField1, zipEntry.getExtraField(ZipShort.createShort(1)));
        assertEquals(extraField2, zipEntry.getExtraField(ZipShort.createShort(2)));

        // Test setting and getting name
        zipEntry.setName("newTest.txt");
        assertEquals("newTest.txt", zipEntry.getName());

        // Test hashCode method
        ZipArchiveEntry zipEntry3 = new ZipArchiveEntry("newTest.txt");
        assertEquals(zipEntry3.hashCode(), zipEntry.hashCode());

        // Test equals method
        assertTrue(zipEntry.equals(zipEntry));
        assertFalse(zipEntry.equals(new Object()));
        assertFalse(zipEntry.equals(new Integer(0)));
        assertFalse(zipEntry.equals(new String("test.txt")));
        assertTrue(zipEntry.equals(zipEntry3));

        // Test toString method
        String expectedToString = "ZipArchiveEntry{name='test.txt', size=0, method="
                + ZipEntry.DEF_COMPRESSION_METHOD + ", intAttrs=" + ZipEntry.FLAG_UNCHANGED
                + ", platform=" + ZipArchiveEntry.PLATFORM_FAT + ", extAttrs=" + 0L
                + ", extraFields={}}";
        String actualToString = zipEntry.toString();
        assertEquals(expectedToString, actualToString);

        // Test getName method with public suffix
        String fileName = "test.txt.gz";
        String expectedPublicSuffix = PublicSuffixMatcherUtil.getPublicSuffix(fileName);
        String suffix = null;
        if (expectedPublicSuffix != null) {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }
        String name = zipEntry.getName();
        if (suffix != null) {
            String expectedName = fileName + "." + suffix;
            assertEquals(expectedName, name);
        } else {
            assertEquals(fileName, name);
        }

        // Test setExtra method
        byte[] extraData = new byte[]{0x01, 0x02, 0x03};
        try {
            zipEntry.setExtra(extraData);
            byte[] actualExtraData = zipEntry.getExtra();
            assertArrayEquals(extraData, actualExtraData);
        } catch (RuntimeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test setCentralDirectoryExtra method
        byte[] centralDirectoryExtraData = new byte[]{0x04, 0x05, 0x06};
        try {
            zipEntry.setCentralDirectoryExtra(centralDirectoryExtraData);
            byte[] actualCentralDirectoryExtraData = zipEntry.getCentralDirectoryExtra();
            assertArrayEquals(centralDirectoryExtraData, actualCentralDirectoryExtraData);
        } catch (RuntimeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test getLocalFileDataExtra method
        InputStream inputStream = new ByteArrayInputStream(extraData);
        try {
            zipEntry.setExtra(inputStream);
            byte[] actualLocalFileDataExtraData = zipEntry.getLocalFileDataExtra();
            assertArrayEquals(extraData, actualLocalFileDataExtraData);
        } catch (RuntimeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test setUnixMode method
        zipEntry.setUnixMode(0664);
        assertEquals(0664, zipEntry.getUnixMode());

        // Test getUnixMode method
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipEntry.getPlatform());

        // Test getPlatform method
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipEntry.getPlatform());

        // Test setPlatform method
        zipEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipEntry.getPlatform());

        // Test addExtraField method
        ZipExtraField newExtraField = new ZipExtraField(new byte[0], ZipShort.createShort(3), "Test3");
        zipEntry.addExtraField(newExtraField);
        assertEquals(3, zipEntry.getExtraFields().length);

        // Test addAsFirstExtraField method
        zipEntry.addAsFirstExtraField(newExtraField);
        assertEquals(1, zipEntry.getExtraFields().length);

        // Test removeExtraField method
        zipEntry.removeExtraField(ZipShort.createShort(1));
        assertEquals(0, zipEntry.getExtraFields().length);

        // Test getExtraField method
        ZipExtraField extraField = zipEntry.getExtraField(ZipShort.createShort(2));
        assertNotNull(extraField);
        assertEquals("Test2", extraField.getName());

        // Test setExtra method with ZipException
        try {
            byte[] invalidExtraData = new byte[]{0x07, 0x08, 0x09};
            zipEntry.setExtra(invalidExtraData);
            fail("Unexpected success");
        } catch (RuntimeException e) {
            assertEquals("Invalid extra field data", e.getMessage());
        }

        // Test constructor with File and entryName, invalid file
        File invalidFile = new File("does_not_exist.txt");
        try {
            zipEntry = new ZipArchiveEntry(invalidFile, "test.txt");
            fail("Unexpected success");
        } catch (IOException e) {
            // Expected
        }

        // Test constructor from ZipEntry, null entry
        try {
            zipEntry = new ZipArchiveEntry(null);
            fail("Unexpected success");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test constructor from ZipEntry, invalid entry
        try {
            ZipEntry invalidEntry = new ZipEntry("invalid_entry");
            zipEntry = new ZipArchiveEntry(invalidEntry);
            fail("Unexpected success");
        } catch (IOException e) {
            // Expected
        }

        // Test constructor from ZipArchiveEntry, null entry
        try {
            zipEntry = new ZipArchiveEntry(null);
            fail("Unexpected success");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}