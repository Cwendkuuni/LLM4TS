package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.ExtraField;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.util.IOUtils;

public class ZipArchiveEntryTest {

    @Test
    public void testNewInstance() throws ZipException, IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertNull(entry.getMethod());
        assertNull(entry.getExtraFields());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getExternalAttributes());
        assertEquals("test.txt", entry.getName());
        assertTrue(entry.isSupportedCompressionMethod());
        assertEquals(0, entry.getPlatform());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
    }

    @Test
    public void testNewInstanceWithFile() throws ZipException, IOException {
        File file = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(file, "test.txt");
        assertNotNull(entry.getMethod());
        assertTrue(entry.isSupportedCompressionMethod());
        assertEquals(0, entry.getPlatform());
        assertEquals(file.length(), entry.getSize());
        assertEquals(file.lastModified(), entry.getTime());
    }

    @Test
    public void testNewInstanceWithZipEntry() throws ZipException, IOException {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertNotNull(entry.getMethod());
        assertTrue(entry.isSupportedCompressionMethod());
        assertEquals(0, entry.getPlatform());
        assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testNewInstanceWithZipArchiveEntry() throws ZipException, IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        ZipArchiveEntry clone = (ZipArchiveEntry) entry.clone();
        assertNotNull(clone);
        assertNotSame(entry, clone);
        assertEquals(entry.getMethod(), clone.getMethod());
        assertEquals(entry.getExtraFields(), clone.getExtraFields());
        assertEquals(entry.getInternalAttributes(), clone.getInternalAttributes());
        assertEquals(entry.getExternalAttributes(), clone.getExternalAttributes());
        assertEquals(entry.getName(), clone.getName());
    }

    @Test
    public void testSetPlatform() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetUnixMode() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testGetMethod() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());
    }

    @Test
    public void testSetMethod() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        assertEquals(-1, entry.getMethod());
        entry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodNegative() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        entry.setMethod(-1);
    }

    @Test
    public void testGetInternalAttributes() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        entry.setInternalAttributes(ZipEntry.FLAG_UNIX_GROUP);
        assertEquals(ZipEntry.FLAG_UNIX_GROUP, entry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        entry.setInternalAttributes(ZipEntry.FLAG_UNIX_GROUP | ZipEntry.FLAG_UNIX_USER);
        assertEquals(ZipEntry.FLAG_UNIX_GROUP | ZipEntry.FLAG_UNIX_USER, entry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        entry.setExternalAttributes(ZipEntry.FLAG_COMPRESSION
                | ZipEntry.FLAG_ENCRYPTED);
        assertEquals(ZipEntry.FLAG_COMPRESSION | ZipEntry.FLAG_ENCRYPTED, entry.getExternalAttributes());
    }

    @Test
    public void testSetExtraFieldsEmpty() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        byte[] extra = new byte[0];
        entry.setExtra(extra);
        assertArrayEquals(new byte[0], entry.getLocalFileDataExtra());
        assertArrayEquals(new byte[0], entry.getCentralDirectoryExtra());
        assertNull(entry.getExtraFields());
    }

    @Test
    public void testSetExtraFields() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        List<ZipExtraField> fields = Arrays.asList(
                new ZipExtraField("test1", "value1".getBytes()),
                new ZipExtraField("test2", "value2".getBytes()),
                new ZipExtraField("test3", "value3".getBytes())
        );
        byte[] extra = ExtraFieldUtils.mergeLocalFileDataData(fields.toArray(new ZipExtraField[0]));
        entry.setExtra(extra);
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertArrayEquals(fields.toArray(new ZipExtraField[0]), extraFields);
    }

    @Test
    public void testSetExtraFieldsMerge() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        List<ZipExtraField> fields1 = Arrays.asList(
                new ZipExtraField("test1", "value1".getBytes()),
                new ZipExtraField("test2", "value2".getBytes())
        );
        List<ZipExtraField> fields2 = Arrays.asList(
                new ZipExtraField("test2", "value3".getBytes()),
                new ZipExtraField("test3", "value3".getBytes())
        );
        byte[] extra1 = ExtraFieldUtils.mergeLocalFileDataData(fields1.toArray(new ZipExtraField[0]));
        byte[] extra2 = ExtraFieldUtils.mergeLocalFileDataData(fields2.toArray(new ZipExtraField[0]));
        entry.setExtra(extra1);
        entry.addExtraField(new ZipExtraField("test2", "value3".getBytes()));
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertArrayEquals(Arrays.asList(
                fields1.get(0), fields1.get(1), fields2.get(1), fields2.get(1)
        ).toArray(new ZipExtraField[0]), extraFields);
    }

    @Test
    public void testSetExtraFieldsReplace() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        List<ZipExtraField> fields1 = Arrays.asList(
                new ZipExtraField("test1", "value1".getBytes()),
                new ZipExtraField("test2", "value2".getBytes())
        );
        List<ZipExtraField> fields2 = Arrays.asList(
                new ZipExtraField("test1", "value4".getBytes()),
                new ZipExtraField("test3", "value3".getBytes())
        );
        byte[] extra1 = ExtraFieldUtils.mergeLocalFileDataData(fields1.toArray(new ZipExtraField[0]));
        byte[] extra2 = ExtraFieldUtils.mergeLocalFileDataData(fields2.toArray(new ZipExtraField[0]));
        entry.setExtra(extra1);
        entry.setExtraFields(fields2.toArray(new ZipExtraField[0]));
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertArrayEquals(fields2.toArray(new ZipExtraField[0]), extraFields);
    }

    @Test
    public void testSetExtraFieldsReplaceFirst() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        List<ZipExtraField> fields1 = Arrays.asList(
                new ZipExtraField("test1", "value1".getBytes()),
                new ZipExtraField("test2", "value2".getBytes())
        );
        List<ZipExtraField> fields2 = Arrays.asList(
                new ZipExtraField("test0", "value3".getBytes()),
                new ZipExtraField("test2", "value2".getBytes()),
                new ZipExtraField("test3", "value3".getBytes())
        );
        byte[] extra1 = ExtraFieldUtils.mergeLocalFileDataData(fields1.toArray(new ZipExtraField[0]));
        byte[] extra2 = ExtraFieldUtils.mergeLocalFileDataData(fields2.toArray(new ZipExtraField[0]));
        entry.setExtra(extra1);
        entry.addAsFirstExtraField(new ZipExtraField("test0", "value3".getBytes()));
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertArrayEquals(fields2.subList(1, fields2.size()).toArray(new ZipExtraField[0]), extraFields);
    }

    @Test
    public void testSetCentralDirectoryExtra() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        List<ZipExtraField> fields = Arrays.asList(
                new ZipExtraField("test1", "value1".getBytes()),
                new ZipExtraField("test2", "value2".getBytes())
        );
        byte[] extra = ExtraFieldUtils.mergeCentralDirectoryData(fields.toArray(new ZipExtraField[0]));
        entry.setCentralDirectoryExtra(extra);
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertArrayEquals(fields.toArray(new ZipExtraField[0]), extraFields);
    }

    @Test
    public void testSetLocalFileDataExtra() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        List<ZipExtraField> fields = Arrays.asList(
                new ZipExtraField("test1", "value1".getBytes()),
                new ZipExtraField("test2", "value2".getBytes())
        );
        byte[] extra = ExtraFieldUtils.mergeLocalFileDataData(fields.toArray(new ZipExtraField[0]));
        entry.setExtra(extra);
        byte[] localExtra = entry.getLocalFileDataExtra();
        assertArrayEquals(extra, localExtra);
    }

    @Test
    public void testSetCentralDirectoryExtraParse() throws ZipException {
        List<ZipExtraField> fields = Arrays.asList(
                new ZipExtraField("test1", "value1".getBytes()),
                new ZipExtraField("test2", "value2".getBytes())
        );
        byte[] extra = ExtraFieldUtils.mergeCentralDirectoryData(fields.toArray(new ZipExtraField[0]));
        ZipArchiveEntry entry = new ZipArchiveEntry();
        entry.setCentralDirectoryExtra(extra);
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertArrayEquals(fields.toArray(new ZipExtraField[0]), extraFields);
    }

    @Test
    public void testClone() throws ZipException {
        List<ZipExtraField> fields = Arrays.asList(
                new ZipExtraField("test1", "value1".getBytes()),
                new ZipExtraField("test2", "value2".getBytes())
        );
        byte[] extra = ExtraFieldUtils.mergeLocalFileDataData(fields.toArray(new ZipExtraField[0]));
        ZipArchiveEntry original = new ZipArchiveEntry();
        original.setExtra(extra);
        ZipArchiveEntry clone = (ZipArchiveEntry) original.clone();
        assertArrayEquals(fields.toArray(new ZipExtraField[0]), clone.getExtraFields());
    }

    @Test
    public void testCloneWithFile() throws ZipException, IOException {
        File file = new File("test.txt");
        ZipArchiveEntry original = new ZipArchiveEntry(file, "test.txt");
        ZipArchiveEntry clone = (ZipArchiveEntry) original.clone();
        assertEquals(original.getName(), clone.getName());
        assertArrayEquals(original.getLocalFileDataExtra(), clone.getLocalFileDataExtra());
        assertArrayEquals(original.getCentralDirectoryExtra(), clone.getCentralDirectoryExtra());
        assertEquals(original.getMethod(), clone.getMethod());
        assertEquals(original.getPlatform(), clone.getPlatform());
        assertEquals(original.getSize(), clone.getSize());
        assertEquals(original.getTime(), clone.getTime());
    }

    @Test
    public void testEquals() throws ZipException, IOException {
        File file = new File("test.txt");
        ZipArchiveEntry original1 = new ZipArchiveEntry(file, "test.txt");
        ZipArchiveEntry original2 = new ZipArchiveEntry(file, "test.txt");
        ZipArchiveEntry clone = (ZipArchiveEntry) original1.clone();
        ZipArchiveEntry other = new ZipArchiveEntry();
        assertTrue(original1.equals(original1));
        assertTrue(original1.equals(original2));
        assertTrue(original1.equals(clone));
        assertFalse(original1.equals(other));
    }

    @Test
    public void testGetName() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        entry.setName("test.txt");
        assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testIsDirectory() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        assertTrue(entry.isDirectory());
        entry.setName("test.txt");
        assertFalse(entry.isDirectory());
    }

    @Test
    public void testHashCode() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        String name = "test.txt";
        entry.setName(name);
        int hashcode = entry.hashCode();
        assertEquals(name.hashCode(), hashcode);
    }
}