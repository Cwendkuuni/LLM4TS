package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ExtraFieldUtils;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

/**
 * Test cases for {@link ZipArchiveEntry}.
 */
public class ZipArchiveEntryTest {

    /**
     * Test constructor with given name.
     */
    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", zipEntry.getName());
    }

    /**
     * Test constructor with given ZipEntry.
     */
    @Test
    public void testConstructorWithZipEntry() throws IOException, ZipException {
        File file = new File("testFile.txt");
        ZipEntry zipEntry = new ZipEntry("testFile.txt");
        zipEntry.setSize(file.length());
        zipEntry.setTime(file.lastModified());
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        assertEquals("testFile.txt", zipArchiveEntry.getName());
        assertEquals(file.length(), zipArchiveEntry.getSize());
        assertEquals(new Date(file.lastModified()), zipArchiveEntry.getLastModifiedDate());
    }

    /**
     * Test constructor with given ZipArchiveEntry.
     */
    @Test
    public void testConstructorWithZipArchiveEntry() throws IOException, ZipException {
        File file = new File("testFile.txt");
        ZipEntry zipEntry = new ZipEntry("testFile.txt");
        zipEntry.setSize(file.length());
        zipEntry.setTime(file.lastModified());
        ZipArchiveEntry originalZipArchiveEntry = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry clonedZipArchiveEntry = (ZipArchiveEntry) originalZipArchiveEntry.clone();
        assertEquals("testFile.txt", clonedZipArchiveEntry.getName());
        assertEquals(file.length(), clonedZipArchiveEntry.getSize());
        assertEquals(new Date(file.lastModified()), clonedZipArchiveEntry.getLastModifiedDate());
    }

    /**
     * Test isSupportedCompressionMethod() with STORED compression method.
     */
    @Test
    public void testIsSupportedCompressionMethodWithStored() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
    }

    /**
     * Test isSupportedCompressionMethod() with DEFLATED compression method.
     */
    @Test
    public void testIsSupportedCompressionMethodWithDeflated() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(ZipArchiveEntry.DEFLATED);
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
    }

    /**
     * Test isSupportedCompressionMethod() with unsupported compression method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsSupportedCompressionMethodWithUnsupported() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(-1);
        zipArchiveEntry.isSupportedCompressionMethod();
    }

    /**
     * Test getMethod().
     */
    @Test
    public void testGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals(-1, zipArchiveEntry.getMethod());

        zipArchiveEntry.setMethod(ZipArchiveEntry.DEFLATED);
        assertEquals(ZipArchiveEntry.DEFLATED, zipArchiveEntry.getMethod());
    }

    /**
     * Test setMethod().
     */
    @Test
    public void testSetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(-1);
        zipArchiveEntry.setMethod(ZipArchiveEntry.DEFLATED);
        assertEquals(ZipArchiveEntry.DEFLATED, zipArchiveEntry.getMethod());
    }

    /**
     * Test getInternalAttributes().
     */
    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals(0, zipArchiveEntry.getInternalAttributes());

        zipArchiveEntry.setInternalAttributes(0xFFFF);
        assertEquals(0xFFFF, zipArchiveEntry.getInternalAttributes());
    }

    /**
     * Test setInternalAttributes().
     */
    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setInternalAttributes(0xFFFF);
        assertEquals(0xFFFF, zipArchiveEntry.getInternalAttributes());
    }

    /**
     * Test getExternalAttributes().
     */
    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals(0, zipArchiveEntry.getExternalAttributes());

        zipArchiveEntry.setExternalAttributes(0xFFFFFFFFL);
        assertEquals(0xFFFFFFFFL, zipArchiveEntry.getExternalAttributes());
    }

    /**
     * Test setExternalAttributes().
     */
    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExternalAttributes(0xFFFFFFFFL);
        assertEquals(0xFFFFFFFFL, zipArchiveEntry.getExternalAttributes());
    }

    /**
     * Test setUnixMode().
     */
    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(0755);
        assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    /**
     * Test getPlatform().
     */
    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());

        zipArchiveEntry.setUnixMode(0755);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    /**
     * Test getExtraFields().
     */
    @Test
    public void testGetExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertArrayEquals(new byte[0], zipArchiveEntry.getLocalFileDataExtra());
        assertArrayEquals(new byte[0], zipArchiveEntry.getCentralDirectoryExtra());
        assertEquals(0, zipArchiveEntry.getExtraFields().length);

        ZipExtraField zipExtraField = new ZipExtraField("TestExtraField", "TestData", 5);
        zipArchiveEntry.addExtraField(zipExtraField);
        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals("TestData", new String(Arrays.copyOfRange(extraFields[0].getLocalFileDataData(), 0, 5)));

        byte[] bytes = new byte[5];
        Arrays.fill(bytes, (byte) 0xFF);
        byte[] localData = new byte[10];
        System.arraycopy(bytes, 0, localData, 0, bytes.length);
        zipArchiveEntry.setLocalFileDataExtra(localData);
        extraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals("TestData", new String(Arrays.copyOfRange(extraFields[0].getLocalFileDataData(), 0, 5)));

        byte[] centralDirectoryData = new byte[5];
        System.arraycopy(bytes, 0, centralDirectoryData, 0, bytes.length);
        zipArchiveEntry.setCentralDirectoryExtra(centralDirectoryData);
        extraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals("TestData", new String(Arrays.copyOfRange(extraFields[0].getLocalFileDataData(), 0, 5)));
        assertEquals("TestData", new String(Arrays.copyOfRange(extraFields[0].getCentralDirectoryData(), 0, 5)));
    }

    /**
     * Test setExtra().
     */
    @Test
    public void testSetExtra() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] localData = new byte[5];
        Arrays.fill(localData, (byte) 0xFF);
        zipArchiveEntry.setExtra(localData);

        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals("TestExtraField", extraFields[0].getName());
        assertEquals("TestData", new String(Arrays.copyOfRange(extraFields[0].getLocalFileDataData(), 0, 5)));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(localData);
        ZipArchiveEntry cloneZipArchiveEntry = new ZipArchiveEntry("test.txt", inputStream);
        extraFields = cloneZipArchiveEntry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals("TestData", new String(Arrays.copyOfRange(extraFields[0].getLocalFileDataData(), 0, 5)));
    }

    /**
     * Test setCentralDirectoryExtra().
     */
    @Test
    public void testSetCentralDirectoryExtra() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] centralDirectoryData = new byte[5];
        Arrays.fill(centralDirectoryData, (byte) 0xFF);
        zipArchiveEntry.setCentralDirectoryExtra(centralDirectoryData);

        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals("TestExtraField", extraFields[0].getName());
        assertEquals("TestData", new String(Arrays.copyOfRange(extraFields[0].getCentralDirectoryData(), 0, 5)));

        byte[] cloneCentralDirectoryData = new byte[5];
        System.arraycopy(centralDirectoryData, 0, cloneCentralDirectoryData, 0, centralDirectoryData.length);
        zipArchiveEntry = new ZipArchiveEntry("test.txt", null, null, null, null, null, null, null, null, cloneCentralDirectoryData);
        extraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals("TestData", new String(Arrays.copyOfRange(extraFields[0].getCentralDirectoryData(), 0, 5)));
    }

    /**
     * Test getName().
     */
    @Test
    public void testGetName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", zipArchiveEntry.getName());

        zipArchiveEntry.setName("test.zip");
        assertEquals("test.zip", zipArchiveEntry.getName());
    }

    /**
     * Test isDirectory().
     */
    @Test
    public void testIsDirectory() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertFalse(zipArchiveEntry.isDirectory());

        zipArchiveEntry = new ZipArchiveEntry("test/");
        assertTrue(zipArchiveEntry.isDirectory());
    }

    /**
     * Test hashCode().
     */
    @Test
    public void testHashCode() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test.txt");
        assertEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry2.hashCode());

        zipArchiveEntry1.setName("test2.txt");
        assertNotEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry2.hashCode());
    }

    /**
     * Test equals().
     */
    @Test
    public void testEquals() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test.txt");
        assertTrue(zipArchiveEntry1.equals(zipArchiveEntry2));

        zipArchiveEntry1.setName("test2.txt");
        assertFalse(zipArchiveEntry1.equals(zipArchiveEntry2));

        ZipArchiveEntry zipArchiveEntry3 = new ZipArchiveEntry("test.txt");
        assertTrue(zipArchiveEntry1.equals(zipArchiveEntry3));
    }

    /**
     * Test getLastModifiedDate().
     */
    @Test
    public void testGetLastModifiedDate() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        zipArchiveEntry.setTime(time);
        assertEquals(date, zipArchiveEntry.getLastModifiedDate());
    }
}