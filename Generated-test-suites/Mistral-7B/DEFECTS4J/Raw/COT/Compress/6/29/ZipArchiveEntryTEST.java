package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

/**
 * Test case for {@link ZipArchiveEntry}.
 */
public class ZipArchiveEntryTest {

    private static final String TEST_NAME = "test.txt";
    private static final int TEST_SIZE = 10;
    private static final int TEST_TIME = 123456789;
    private static final int TEST_UNIX_MODE = 0755;

    private ZipArchiveEntry entry;
    private byte[] testData;

    @Before
    public void setUp() throws Exception {
        testData = new byte[TEST_SIZE];
        Arrays.fill(testData, (byte) 'a');

        entry = new ZipArchiveEntry(TEST_NAME);
        entry.setSize(TEST_SIZE);
        entry.setTime(new Date(TEST_TIME));
        entry.setUnixMode(TEST_UNIX_MODE);
    }

    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(TEST_NAME);
        Assert.assertEquals(TEST_NAME, zipEntry.getName());
        Assert.assertNull(zipEntry.getExtraFields());
        Assert.assertEquals(-1, zipEntry.getMethod());
    }

    @Test
    public void testConstructorWithZipEntry() throws ZipException {
        ZipEntry zipEntry = new ZipEntry(TEST_NAME);
        byte[] extraData = "TestExtraData".getBytes();
        zipEntry.setExtra(extraData);
        zipEntry.setMethod(ZipEntry.STORED);

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals(TEST_NAME, zipArchiveEntry.getName());
        Assert.assertEquals("TestExtraData", new String(zipArchiveEntry.getExtra()));
        Assert.assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testConstructorWithZipArchiveEntry() throws ZipException {
        byte[] extraData = "TestExtraData".getBytes();
        ZipArchiveEntry originalEntry = new ZipArchiveEntry(TEST_NAME);
        originalEntry.setExtraFields(new ZipExtraField[]{new ZipExtendedTimestamp(), new ZipNoteStream()});
        originalEntry.setExtra(extraData);
        originalEntry.setMethod(ZipEntry.STORED);

        ZipArchiveEntry copyEntry = new ZipArchiveEntry(originalEntry);
        Assert.assertEquals(TEST_NAME, copyEntry.getName());
        Assert.assertArrayEquals(extraData, copyEntry.getExtra());
        Assert.assertEquals(ZipEntry.STORED, copyEntry.getMethod());
        Assert.assertArrayEquals(originalEntry.getExtraFields().toArray(new ZipExtraField[0]), copyEntry.getExtraFields().toArray(new ZipExtraField[0]));
    }

    @Test
    public void testConstructorWithFile() {
        File file = new File("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, TEST_NAME);
        Assert.assertEquals(TEST_NAME, zipArchiveEntry.getName());
        Assert.assertEquals(file.length(), zipArchiveEntry.getSize());
        Assert.assertEquals(file.lastModified(), zipArchiveEntry.getTime().getTime());
        Assert.assertEquals(TEST_UNIX_MODE, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        Assert.assertTrue(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        Assert.assertTrue(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipEntry.UNKNOWN);
        Assert.assertFalse(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetMethod() {
        Assert.assertEquals(-1, entry.getMethod());
        entry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, entry.getMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        Assert.assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    @Test
    public void testSetMethod() {
        entry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, entry.getMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        Assert.assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodWithIllegalArgument() {
        entry.setMethod(123);
    }

    @Test
    public void testGetInternalAttributes() {
        Assert.assertEquals(0, entry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        int internalAttributes = 0x0102;
        entry.setInternalAttributes(internalAttributes);
        Assert.assertEquals(internalAttributes, entry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        Assert.assertEquals(0, entry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        long externalAttributes = 0x01020304;
        entry.setExternalAttributes(externalAttributes);
        Assert.assertEquals(externalAttributes, entry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        entry.setUnixMode(TEST_UNIX_MODE);
        Assert.assertEquals(TEST_UNIX_MODE, entry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipExtraField field1 = new ZipExtendedTimestamp();
        field1.parseFromCentralDirectoryData(new byte[16], 0, 16);

        ZipExtraField field2 = new ZipNoteStream();
        field2.parseFromCentralDirectoryData(new byte[16], 16, 16);

        entry.setExtraFields(new ZipExtraField[]{field1, field2});
        Assert.assertArrayEquals(new ZipExtraField[]{field1, field2}, entry.getExtraFields().toArray(new ZipExtraField[0]));
    }

    @Test
    public void testGetExtraFields() {
        ZipExtraField field1 = new ZipExtendedTimestamp();
        field1.parseFromCentralDirectoryData(new byte[16], 0, 16);

        ZipExtraField field2 = new ZipNoteStream();
        field2.parseFromCentralDirectoryData(new byte[16], 16, 16);

        entry.setExtraFields(new ZipExtraField[]{field1, field2});
        Assert.assertArrayEquals(new ZipExtraField[]{field1, field2}, entry.getExtraFields().toArray(new ZipExtraField[0]));
    }

    @Test
    public void testAddExtraField() {
        ZipExtraField field1 = new ZipExtendedTimestamp();
        field1.parseFromCentralDirectoryData(new byte[16], 0, 16);

        entry.addExtraField(field1);
        Assert.assertArrayEquals(new ZipExtraField[]{field1}, entry.getExtraFields().toArray(new ZipExtraField[0]));
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipExtraField field1 = new ZipExtendedTimestamp();
        field1.parseFromCentralDirectoryData(new byte[16], 0, 16);

        entry.addAsFirstExtraField(field1);
        Assert.assertArrayEquals(new ZipExtraField[]{field1}, entry.getExtraFields().toArray(new ZipExtraField[0]));
    }

    @Test
    public void testRemoveExtraField() {
        ZipExtraField field1 = new ZipExtendedTimestamp();
        field1.parseFromCentralDirectoryData(new byte[16], 0, 16);
        entry.addExtraField(field1);

        Assert.assertArrayEquals(new ZipExtraField[]{field1}, entry.getExtraFields().toArray(new ZipExtraField[0]));

        entry.removeExtraField(field1.getHeaderId());
        Assert.assertArrayEquals(new ZipExtraField[]{}, entry.getExtraFields().toArray(new ZipExtraField[0]));
    }

    @Test
    public void testGetExtraField() {
        ZipExtraField field1 = new ZipExtendedTimestamp();
        field1.parseFromCentralDirectoryData(new byte[16], 0, 16);
        entry.addExtraField(field1);

        ZipExtraField retrieved = entry.getExtraField(field1.getHeaderId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(field1.getClass(), retrieved.getClass());
    }

    @Test
    public void testSetExtra() throws ZipException {
        byte[] extraData = new byte[16];
        Arrays.fill(extraData, (byte) 0x5A);
        InputStream inputStream = new ByteArrayInputStream(extraData);
        OutputStream outputStream = new ByteArrayOutputStream();

        entry.setExtra(extraData);
        byte[] extra = entry.getExtra();
        Assert.assertArrayEquals(extraData, extra);

        entry.setExtra(inputStream);
        extra = entry.getExtra();
        Assert.assertArrayEquals(extraData, extra);

        entry.setExtra(outputStream);
        extra = entry.getExtra();
        Assert.assertArrayEquals(extraData, extra);
    }

    @Test(expected = RuntimeException.class)
    public void testSetExtraWithInvalidData() {
        byte[] extraData = new byte[16];
        Arrays.fill(extraData, (byte) 0x5A);
        byte[] invalidExtraData = new byte[17];

        entry.setExtra(extraData);
        entry.setExtra(invalidExtraData);
    }

    @Test
    public void testSetCentralDirectoryExtra() throws ZipException {
        byte[] extraData = "TestCentralDirectoryExtraData".getBytes();
        InputStream inputStream = new ByteArrayInputStream(extraData);

        entry.setCentralDirectoryExtra(extraData);
        byte[] centralExtraData = entry.getCentralDirectoryExtra();
        Assert.assertArrayEquals(extraData, centralExtraData);

        entry.setCentralDirectoryExtra(inputStream);
        centralExtraData = entry.getCentralDirectoryExtra();
        Assert.assertArrayEquals(extraData, centralExtraData);
    }

    @Test(expected = RuntimeException.class)
    public void testSetCentralDirectoryExtraWithInvalidData() {
        byte[] extraData = "TestCentralDirectoryExtraData".getBytes();
        byte[] invalidExtraData = new byte[17];

        entry.setCentralDirectoryExtra(extraData);
        entry.setCentralDirectoryExtra(invalidExtraData);
    }

    @Test
    public void testGetName() {
        Assert.assertEquals(TEST_NAME, entry.getName());
    }

    @Test
    public void testIsDirectory() {
        Assert.assertFalse(entry.isDirectory());

        entry.setName(TEST_NAME + "/");
        Assert.assertTrue(entry.isDirectory());
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry(TEST_NAME);
        ZipArchiveEntry entry2 = new ZipArchiveEntry(TEST_NAME);
        Assert.assertEquals(entry1.hashCode(), entry2.hashCode());

        entry1.setSize(TEST_SIZE);
        Assert.assertNotEquals(entry1.hashCode(), entry2.hashCode());

        entry2.setSize(TEST_SIZE);
        Assert.assertEquals(entry1.hashCode(), entry2.hashCode());

        entry1.setTime(new Date(System.currentTimeMillis() - 100));
        Assert.assertNotEquals(entry1.hashCode(), entry2.hashCode());

        entry2.setTime(new Date(System.currentTimeMillis() - 100));
        Assert.assertEquals(entry1.hashCode(), entry2.hashCode());

        entry1.setUnixMode(TEST_UNIX_MODE);
        Assert.assertNotEquals(entry1.hashCode(), entry2.hashCode());

        entry2.setUnixMode(TEST_UNIX_MODE);
        Assert.assertEquals(entry1.hashCode(), entry2.hashCode());

        entry1.setExtraFields(new ZipExtraField[]{new ZipExtendedTimestamp()});
        Assert.assertNotEquals(entry1.hashCode(), entry2.hashCode());

        entry2.setExtraFields(new ZipExtraField[]{new ZipExtendedTimestamp()});
        Assert.assertEquals(entry1.hashCode(), entry2.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry(TEST_NAME);
        ZipArchiveEntry entry2 = new ZipArchiveEntry(TEST_NAME);
        Assert.assertTrue(entry1.equals(entry2));

        entry1.setSize(TEST_SIZE);
        Assert.assertFalse(entry1.equals(entry2));

        entry2.setSize(TEST_SIZE);
        Assert.assertTrue(entry1.equals(entry2));

        entry1.setTime(new Date(System.currentTimeMillis() - 100));
        Assert.assertFalse(entry1.equals(entry2));

        entry2.setTime(new Date(System.currentTimeMillis() - 100));
        Assert.assertTrue(entry1.equals(entry2));

        entry1.setUnixMode(TEST_UNIX_MODE);
        Assert.assertFalse(entry1.equals(entry2));

        entry2.setUnixMode(TEST_UNIX_MODE);
        Assert.assertTrue(entry1.equals(entry2));

        entry1.setExtraFields(new ZipExtraField[]{new ZipExtendedTimestamp()});
        Assert.assertFalse(entry1.equals(entry2));

        entry2.setExtraFields(new ZipExtraField[]{new ZipExtendedTimestamp()});
        Assert.assertTrue(entry1.equals(entry2));

        entry1 = new ZipArchiveEntry(TEST_NAME + "/");
        Assert.assertTrue(entry1.equals(entry2));

        entry2 = new ZipArchiveEntry(TEST_NAME);
        Assert.assertFalse(entry1.equals(entry2));

        entry1 = new ZipArchiveEntry(null);
        Assert.assertFalse(entry1.equals(entry2));
    }

    @Test
    public void testGetLastModifiedDate() {
        Assert.assertEquals(new Date(TEST_TIME), entry.getLastModifiedDate());
    }
}