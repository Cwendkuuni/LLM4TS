package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.util.bytes.BigEndianDataInputStream;
import org.apache.commons.compress.util.bytes.BigEndianDataOutputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link ZipArchiveEntry}.
 */
public class ZipArchiveEntryTest {

    private static final byte[] BAD_HEADER = new byte[3];
    private static final byte[] EMPTY_EXTRA_DATA = new byte[0];
    private static final byte[] LOCAL_EXTRA_DATA = {(byte) 0x50, (byte) 0x4b, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09};
    private static final byte[] CENTRAL_EXTRA_DATA = {(byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f, (byte) 0x10};
    private static final int TEST_NAME = 1;
    private static final int TEST_METHOD = 2;
    private static final int TEST_INTERNAL_ATTRIBUTES = 3;
    private static final int TEST_PLATFORM = 4;
    private static final long TEST_EXTERNAL_ATTRIBUTES = 5;
    private static final int TEST_UNIX_MODE = 6;
    private static final int TEST_INTERNAL_ATTRIBUTES_WITH_UNIX_PERMS = 7;
    private static final int TEST_INVALID_INTERNAL_ATTRIBUTES = 8;
    private ZipArchiveEntry testEntry;

    @Before
    public void setup() {
        testEntry = new ZipArchiveEntry("testEntry.txt");
    }

    @Test
    public void testCreateEmpty() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        assertNull(entry.getName());
        assertNull(entry.getExtraFields());
        assertNull(entry.getExtra());
        assertNull(entry.getLocalFileDataExtra());
        assertNull(entry.getCentralDirectoryExtra());
    }

    @Test
    public void testCreateWithName() {
        assertEquals("testEntry.txt", testEntry.getName());
        assertEquals(new byte[0][], testEntry.getExtra());
        assertNull(testEntry.getExtraFields());
        assertNull(testEntry.getLocalFileDataExtra());
        assertNull(testEntry.getCentralDirectoryExtra());
        assertFalse(testEntry.isDirectory());
    }

    @Test
    public void testCreateWithFile() throws IOException {
        File testFile = new File("testFile.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(testFile, "testEntry.txt");
        assertEquals("testEntry.txt", entry.getName());
        assertArrayEquals(EMPTY_EXTRA_DATA, entry.getExtra());
        assertNull(entry.getExtraFields());
        assertEquals(testFile.length(), entry.getSize());
        assertEquals(testFile.lastModified(), entry.getTime());
    }

    @Test
    public void testCreateWithZipEntry() throws IOException {
        java.util.zip.ZipEntry testEntry = new java.util.zip.ZipEntry("testEntry.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(testEntry);
        assertEquals("testEntry.txt", entry.getName());
        assertArrayEquals(EMPTY_EXTRA_DATA, entry.getExtra());
        assertNull(entry.getExtraFields());
        assertEquals(testEntry.getSize(), entry.getSize());
        assertEquals(testEntry.getTime(), entry.getTime());
    }

    @Test
    public void testCreateWithZipEntryClone() throws IOException {
        java.util.zip.ZipEntry testEntry = new java.util.zip.ZipEntry("testEntry.txt");
        ZipArchiveEntry original = new ZipArchiveEntry(testEntry);
        ZipArchiveEntry clone = new ZipArchiveEntry(original);
        assertEquals("testEntry.txt", clone.getName());
        assertArrayEquals(EMPTY_EXTRA_DATA, clone.getExtra());
        assertNull(clone.getExtraFields());
        assertEquals(original.getSize(), clone.getSize());
        assertEquals(original.getTime(), clone.getTime());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        assertFalse(testEntry.isSupportedCompressionMethod());
        testEntry.setMethod(ZipEntry.STORED);
        assertTrue(testEntry.isSupportedCompressionMethod());
        testEntry.setMethod(ZipEntry.DEFLATED);
        assertTrue(testEntry.isSupportedCompressionMethod());
        testEntry.setMethod(ZipEntry.UNKNOWN);
        assertFalse(testEntry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetMethod() {
        testEntry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, testEntry.getMethod());
        testEntry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, testEntry.getMethod());
        testEntry.setMethod(-1);
        assertEquals(-1, testEntry.getMethod());
    }

    @Test
    public void testSetMethod() {
        testEntry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, testEntry.getMethod());
        testEntry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, testEntry.getMethod());
        testEntry.setMethod(3);
        assertEquals(ZipEntry.DEFLATED, testEntry.getMethod()); // falls back to default
    }

    @Test
    public void testGetInternalAttributes() {
        testEntry.setInternalAttributes(TEST_INTERNAL_ATTRIBUTES);
        assertEquals(TEST_INTERNAL_ATTRIBUTES, testEntry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        testEntry.setInternalAttributes(TEST_INTERNAL_ATTRIBUTES);
        assertEquals(TEST_INTERNAL_ATTRIBUTES, testEntry.getInternalAttributes());
    }

    @Test
    public void testGetPlatform() {
        testEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, testEntry.getPlatform());
        testEntry.setPlatform(ZipArchiveEntry.PLATFORM_FAT);
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, testEntry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        testEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, testEntry.getPlatform());
        testEntry.setPlatform(ZipArchiveEntry.PLATFORM_FAT);
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, testEntry.getPlatform());
    }

    @Test
    public void testGetExternalAttributes() {
        testEntry.setExternalAttributes(TEST_EXTERNAL_ATTRIBUTES);
        assertEquals(TEST_EXTERNAL_ATTRIBUTES, testEntry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        testEntry.setExternalAttributes(TEST_EXTERNAL_ATTRIBUTES);
        assertEquals(TEST_EXTERNAL_ATTRIBUTES, testEntry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        testEntry.setUnixMode(TEST_UNIX_MODE);
        assertEquals(TEST_UNIX_MODE, testEntry.getUnixMode());
    }

    @Test
    public void testGetUnixMode() {
        testEntry.setUnixMode(TEST_UNIX_MODE);
        assertEquals(TEST_UNIX_MODE, testEntry.getUnixMode());
    }

    @Test
    public void testGetIntModeWithUnixPerms() {
        testEntry.setUnixMode(TEST_INTERNAL_ATTRIBUTES_WITH_UNIX_PERMS);
        assertEquals(TEST_INTERNAL_ATTRIBUTES_WITH_UNIX_PERMS, testEntry.getInternalAttributes());
        assertEquals(TEST_UNIX_MODE, testEntry.getUnixMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetIntModeNegative() {
        testEntry.setUnixMode(-1);
    }

    @Test
    public void testSetExtraFields() {
        ZipExtraField extraField = new ZipExtendedTimestamps();
        testEntry.setExtraFields(new ZipExtraField[]{extraField});
        assertArrayEquals(EMPTY_EXTRA_DATA, testEntry.getExtra());
        assertEquals(1, testEntry.getExtraFields().size());
        ZipExtraField retrievedExtraField = testEntry.getExtraField(extraField.getHeaderId());
        assertSame(extraField, retrievedExtraField);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (BigEndianDataOutputStream dos = new BigEndianDataOutputStream(out)) {
            testEntry.writeExtraFieldData(dos);
        }
        byte[] writtenExtraData = out.toByteArray();
        assertEquals(writtenExtraData.length, ExtraFieldUtils.ESTIMATED_SIZE_FOR_ONE_FIELD);

        ByteArrayInputStream in = new ByteArrayInputStream(writtenExtraData);
        try (BigEndianDataInputStream dis = new BigEndianDataInputStream(in)) {
            ZipExtraField[] parsedExtraFields = ExtraFieldUtils.parse(dis.readNBytes(ExtraFieldUtils.ESTIMATED_SIZE_FOR_ONE_FIELD), true);
            assertEquals(1, parsedExtraFields.length);
            assertEquals(extraField.getClass(), parsedExtraFields[0].getClass());
            assertEquals(extraField.getHeaderId(), parsedExtraFields[0].getHeaderId());
        }
    }

    @Test
    public void testAddExtraField() {
        ZipExtraField extraField = new GenericZipExtendedInformation();
        testEntry.addExtraField(extraField);
        assertArrayEquals(EMPTY_EXTRA_DATA, testEntry.getExtra());
        assertEquals(1, testEntry.getExtraFields().size());
        ZipExtraField retrievedExtraField = testEntry.getExtraField(extraField.getHeaderId());
        assertSame(extraField, retrievedExtraField);
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipExtraField extraField = new GenericZipExtendedInformation();
        testEntry.addAsFirstExtraField(extraField);
        assertArrayEquals(EMPTY_EXTRA_DATA, testEntry.getExtra());
        assertEquals(1, testEntry.getExtraFields().size());
        ZipExtraField retrievedExtraField = testEntry.getExtraField(extraField.getHeaderId());
        assertSame(extraField, retrievedExtraField);
    }

    @Test
    public void testRemoveExtraField() {
        ZipExtraField extraField = new GenericZipExtendedInformation();
        testEntry.addExtraField(extraField);
        testEntry.removeExtraField(extraField.getHeaderId());
        assertArrayEquals(EMPTY_EXTRA_DATA, testEntry.getExtra());
        assertEquals(0, testEntry.getExtraFields().size());
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveExtraFieldNull() {
        testEntry.removeExtraField(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetExtraFieldNull() {
        testEntry.getExtraField(null);
    }

    @Test
    public void testSetLocalExtraData() throws IOException {
        testEntry.setExtraFields(new ZipExtraField[]{new CustomZipExtraField()});
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (BigEndianDataOutputStream dos = new BigEndianDataOutputStream(out)) {
            testEntry.writeLocalFileDataExtra(dos);
            byte[] localExtraData = out.toByteArray();
            assertEquals(localExtraData.length, ExtraFieldUtils.ESTIMATED_SIZE_FOR_ONE_FIELD);
            assertEquals(LOCAL_EXTRA_DATA, localExtraData);
        }

        ByteArrayInputStream in = new ByteArrayInputStream(LOCAL_EXTRA_DATA);
        try (BigEndianDataInputStream dis = new BigEndianDataInputStream(in)) {
            ZipExtraField[] parsedExtraFields = ExtraFieldUtils.parse(dis.readNBytes(ExtraFieldUtils.ESTIMATED_SIZE_FOR_ONE_FIELD), true);
            assertEquals(1, parsedExtraFields.length);
            assertEquals(CustomZipExtraField.class, parsedExtraFields[0].getClass());
        }
    }

    @Test
    public void testSetCentralDirectoryExtraData() throws IOException {
        testEntry.setExtraFields(new ZipExtraField[]{new CustomZipExtraField()});
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (BigEndianDataOutputStream dos = new BigEndianDataOutputStream(out)) {
            testEntry.writeCentralDirectoryExtra(dos);
            byte[] centralDirectoryExtraData = out.toByteArray();
            assertEquals(centralDirectoryExtraData.length, ExtraFieldUtils.ESTIMATED_SIZE_FOR_ONE_FIELD);
            assertEquals(CENTRAL_EXTRA_DATA, centralDirectoryExtraData);
        }

        ByteArrayInputStream in = new ByteArrayInputStream(CENTRAL_EXTRA_DATA);
        try (BigEndianDataInputStream dis = new BigEndianDataInputStream(in)) {
            ZipExtraField[] parsedExtraFields = ExtraFieldUtils.parse(dis.readNBytes(ExtraFieldUtils.ESTIMATED_SIZE_FOR_ONE_FIELD), false);
            assertEquals(1, parsedExtraFields.length);
            assertEquals(CustomZipExtraField.class, parsedExtraFields[0].getClass());
        }
    }

    @Test
    public void testClone() throws IOException, CloneNotSupportedException {
        testEntry.setExtraFields(new ZipExtraField[]{new CustomZipExtraField()});
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) testEntry.clone();
        assertArrayEquals(EMPTY_EXTRA_DATA, clonedEntry.getExtra());
        assertEquals(1, clonedEntry.getExtraFields().size());
        ZipExtraField retrievedExtraField = clonedEntry.getExtraField(CustomZipExtraField.class.getField("HEADER_ID").getInt(null));
        assertSame(testEntry.getExtraField(CustomZipExtraField.class.getField("HEADER_ID").getInt(null)), retrievedExtraField);
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneNotSupported() throws IOException {
        testEntry.setExtraFields(new ZipExtraField[]{new CustomZipExtraField()});
        ((Cloneable) new Object()).clone();
    }
}

class CustomZipExtraField extends ZipExtraField {
    public CustomZipExtraField() {
        super((short) 0, (short) 0, new byte[0]);
    }

    public static final int HEADER_ID = 0;
}