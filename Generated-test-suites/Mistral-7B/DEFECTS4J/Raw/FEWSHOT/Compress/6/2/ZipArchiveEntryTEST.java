package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.ExtraField;
import org.apache.commons.compress.archivers.zip.ExtraFieldUtils;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.ZipShort;

/**
 * Tests for {@link ZipArchiveEntry}.
 */
public class ZipArchiveEntryTest {

    /**
     * Test creating new ZipArchiveEntry instances.
     */
    @Test
    public void testNewInstance() {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        assertEquals(name, entry.getName());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getPlatform());
        assertEquals(0, entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
        assertEquals(-1, entry.getMethod());
        assertEquals(false, entry.isSupportedCompressionMethod());
    }

    /**
     * Test creating new ZipArchiveEntry instances from ZipEntry instances.
     * @throws IOException on error
     */
    @Test
    public void testNewInstanceFromZipEntry() throws IOException {
        String name = "test.txt";
        byte[] extra = new byte[0];
        ZipEntry zipEntry = new ZipEntry(name);
        zipEntry.setExtra(extra);
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals(name, entry.getName());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getPlatform());
        assertEquals(0, entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
        assertEquals(-1, entry.getMethod());
        assertEquals(false, entry.isSupportedCompressionMethod());
    }

    /**
     * Test creating new ZipArchiveEntry instances from other ZipArchiveEntry instances.
     * @throws IOException on error
     * @throws ZipException on error
     */
    @Test
    public void testNewInstanceFromZipArchiveEntry() throws IOException, ZipException {
        String name = "test.txt";
        int internalAttributes = 0x20;
        long externalAttributes = 0x100;
        LinkedHashMap<ZipShort, ZipExtraField> extraFields = new LinkedHashMap<>();
        ZipArchiveEntry source = new ZipArchiveEntry(name);
        source.setInternalAttributes(internalAttributes);
        source.setExternalAttributes(externalAttributes);
        source.setExtraFields(extraFields.toArray(new ZipExtraField[0]));
        ZipArchiveEntry entry = new ZipArchiveEntry(source);
        assertEquals(name, entry.getName());
        assertEquals(internalAttributes, entry.getInternalAttributes());
        assertEquals(source.getPlatform(), entry.getPlatform());
        assertEquals(externalAttributes, entry.getExternalAttributes());
        assertArrayEquals(extraFields.toArray(new ZipExtraField[0]), entry.getExtraFields());
        assertEquals(-1, entry.getMethod());
        assertEquals(false, entry.isSupportedCompressionMethod());
    }

    /**
     * Test creating new ZipArchiveEntry instances with a File instance.
     * @throws IOException on error
     */
    @Test
    public void testNewInstanceFromFile() throws IOException {
        String name = "test.txt";
        File file = new File(name);
        ZipArchiveEntry entry = new ZipArchiveEntry(file, name);
        assertEquals(name, entry.getName());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getPlatform());
        assertEquals(file.length(), entry.getSize());
        long time = file.lastModified();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        assertEquals(date.getTime(), entry.getTime());
        assertNull(entry.getExtraFields());
        assertEquals(-1, entry.getMethod());
        assertEquals(false, entry.isSupportedCompressionMethod());
    }

    /**
     * Test setting and getting the compression method of a ZipArchiveEntry instance.
     */
    @Test
    public void testSetAndGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipArchiveEntry.STORED);
        assertEquals(ZipArchiveEntry.STORED, entry.getMethod());
        entry.setMethod(ZipArchiveEntry.DEFLATED);
        assertEquals(ZipArchiveEntry.DEFLATED, entry.getMethod());
        try {
            entry.setMethod(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertEquals(false, entry.isSupportedCompressionMethod());
        entry.setMethod(ZipArchiveEntry.STORED);
        assertTrue(entry.isSupportedCompressionMethod());
    }

    /**
     * Test getting the internal attributes of a ZipArchiveEntry instance.
     */
    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(0x20);
        assertEquals(0x20, entry.getInternalAttributes());
    }

    /**
     * Test setting and getting the external attributes of a ZipArchiveEntry instance.
     */
    @Test
    public void testSetAndGetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(0x100);
        assertEquals(0x100, entry.getExternalAttributes());
    }

    /**
     * Test setting and getting the extra fields of a ZipArchiveEntry instance.
     */
    @Test
    public void testSetAndGetExtraFields() {
        String name = "test.txt";
        byte[] extra = new byte[10];
        ZipExtraField field = new ZipExtraField(ZipShort.UNIX_USER, new byte[4]);
        LinkedHashMap<ZipShort, ZipExtraField> fields = new LinkedHashMap<>();
        fields.put(ZipShort.UNIX_USER, field);
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        entry.setExtraFields(fields.toArray(new ZipExtraField[0]));
        assertArrayEquals(fields.toArray(new ZipExtraField[0]), entry.getExtraFields());

        byte[] data = new byte[10];
        System.arraycopy(extra, 0, data, 0, extra.length);
        entry.setExtra(data);
        assertArrayEquals(data, entry.getExtra());
        assertSame(fields, entry.getExtraFields());
    }

    /**
     * Test creating a ZipArchiveEntry instance from a byte array.
     * @throws IOException on error
     * @throws ZipException on error
     */
    @Test
    public void testNewInstanceFromByteArray() throws IOException, ZipException {
        String name = "test.txt";
        byte[] extra = new byte[10];
        ZipExtraField field = new ZipExtraField(ZipShort.UNIX_USER, new byte[4]);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ExtraFieldUtils.writeFields(byteStream, field);
        byte[] localFileData = byteStream.toByteArray();

        byte[] centralDirectoryData = new byte[localFileData.length + 2];
        System.arraycopy(localFileData, 0, centralDirectoryData, 0, localFileData.length);
        centralDirectoryData[localFileData.length] = (byte) 0x50;
        centralDirectoryData[localFileData.length + 1] = (byte) 0x4B;

        List<ZipExtraField> extraFields = new ArrayList<>();
        extraFields.add(field);

        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        entry.setExtraFields(extraFields.toArray(new ZipExtraField[0]));
        entry.setLocalFileDataExtra(localFileData);
        entry.setCentralDirectoryExtra(centralDirectoryData);

        ZipArchiveEntry parsedEntry = new ZipArchiveEntry(entry);
        assertArrayEquals(extra, parsedEntry.getExtra());
        assertSame(field, parsedEntry.getExtraFields().get(ZipShort.UNIX_USER));
        assertEquals(name, parsedEntry.getName());
        assertEquals(0, parsedEntry.getInternalAttributes());
        assertEquals(0, parsedEntry.getPlatform());
        assertEquals(0, parsedEntry.getExternalAttributes());
        assertEquals(localFileData.length + 2, parsedEntry.getCentralDirectorySize());
        assertEquals(localFileData.length, parsedEntry.getLocalFileDataSize());
        assertEquals(-1, parsedEntry.getMethod());
        assertEquals(false, parsedEntry.isSupportedCompressionMethod());
    }

    /**
     * Tests the isSupportedCompressionMethod() method.
     * @throws IOException on error
     */
    @Test
    public void testIsSupportedCompressionMethod() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertFalse(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipArchiveEntry.STORED);
        assertTrue(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipArchiveEntry.DEFLATED);
        assertTrue(entry.isSupportedCompressionMethod());
        entry.setMethod(-1);
        assertFalse(entry.isSupportedCompressionMethod());
        entry.setMethod(ZipArchiveEntry.FILTERED);
        assertFalse(entry.isSupportedCompressionMethod());
    }

    /**
     * Test parsing a name from a ZipArchiveEntry instance.
     */
    @Test
    public void testParseName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", entry.getName());
        entry.setName("test/subdir/file.txt");
        assertEquals("test/subdir/file.txt", entry.getName());
    }

    /**
     * Test parsing a hashCode from a ZipArchiveEntry instance.
     */
    @Test
    public void testParseHashCode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt".hashCode(), entry.hashCode());
        entry.setName("test/subdir/file.txt");
        assertEquals("test/subdir/file.txt".hashCode(), entry.hashCode());
    }

    /**
     * Test parsing equals from a ZipArchiveEntry instance.
     */
    @Test
    public void testParseEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
        assertTrue(entry1.equals(entry2));
        entry2.setName("test/subdir/file.txt");
        assertFalse(entry1.equals(entry2));
        entry1.setName("test/subdir/file.txt");
        assertTrue(entry1.equals(entry2));
    }

    /**
     * Test setting and getting the name of a ZipArchiveEntry instance.
     */
    @Test
    public void testSetAndGetName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setName("test/subdir/file.txt");
        assertEquals("test/subdir/file.txt", entry.getName());
    }

    /**
     * Test getting the size of a ZipArchiveEntry instance.
     */
    @Test
    public void testGetSize() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setSize(10);
        assertEquals(10, entry.getSize());
    }

    /**
     * Test parsing the last modified date from a ZipArchiveEntry instance.
     */
    @Test
    public void testParseLastModifiedDate() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(1234567890L);
        Date date = calendar.getTime();
        entry.setTime(date.getTime());
        assertEquals(date, entry.getLastModifiedDate());
    }
}