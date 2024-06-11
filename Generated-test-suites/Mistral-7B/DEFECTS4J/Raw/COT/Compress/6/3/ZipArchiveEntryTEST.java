import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

@RunWith(JUnit4.class)
public class ZipArchiveEntryTest {

    private ZipArchiveEntry entry;
    private ZipArchiveEntry clone;

    @Before
    public void setUp() {
        String name = "test.txt";
        entry = new ZipArchiveEntry(name);
        clone = null;
    }

    @Test
    public void testConstructorWithName() {
        Assert.assertNotNull(entry);
        Assert.assertEquals(name, entry.getName());
    }

    @Test
    public void testConstructorWithZipEntry() throws ZipException {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        entry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals(zipEntry.getName(), entry.getName());
    }

    @Test
    public void testConstructorWithAnotherZipArchiveEntry() throws ZipException {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(new ZipEntry("test.txt"));
        entry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals(zipEntry.getName(), entry.getName());
    }

    @Test
    public void testEmptyConstructor() {
        entry = new ZipArchiveEntry();
        Assert.assertNull(entry.getName());
    }

    @Test
    public void testFileConstructor() {
        File file = new File("test.txt");
        entry = new ZipArchiveEntry(file, "test.txt");
        Assert.assertEquals(file.getName(), entry.getName());
    }

    @Test
    public void testClone() throws CloneNotSupportedException, ZipException {
        byte[] extraData = new byte[10];
        entry.setExtra(extraData);
        entry.setInternalAttributes(1);
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        entry.setExternalAttributes(2L);
        entry.setName("test.txt");

        clone = (ZipArchiveEntry) entry.clone();

        Assert.assertNotSame(entry, clone);
        Assert.assertEquals(entry.getName(), clone.getName());
        Assert.assertArrayEquals(extraData, clone.getExtra());
        Assert.assertEquals(entry.getInternalAttributes(), clone.getInternalAttributes());
        Assert.assertEquals(entry.getPlatform(), clone.getPlatform());
        Assert.assertEquals(entry.getExternalAttributes(), clone.getExternalAttributes());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        Assert.assertTrue(entry.isSupportedCompressionMethod());
        entry.setMethod(-1);
        Assert.assertFalse(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetMethod() {
        Assert.assertEquals(-1, entry.getMethod());
        entry.setMethod(ZipConstant.STORED);
        Assert.assertEquals(ZipConstant.STORED, entry.getMethod());
    }

    @Test
    public void testSetMethod() {
        entry.setMethod(ZipConstant.STORED);
        Assert.assertEquals(ZipConstant.STORED, entry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodNegativeNumber() {
        entry.setMethod(-1);
    }

    @Test
    public void testGetInternalAttributes() {
        Assert.assertEquals(0, entry.getInternalAttributes());
        entry.setInternalAttributes(5);
        Assert.assertEquals(5, entry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        entry.setInternalAttributes(5);
        Assert.assertEquals(5, entry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        Assert.assertEquals(0, entry.getExternalAttributes());
        entry.setExternalAttributes(10L);
        Assert.assertEquals(10, (int) entry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        entry.setExternalAttributes(10L);
        Assert.assertEquals(10, (int) entry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        entry.setUnixMode(0755);
        Assert.assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testGetUnixMode() {
        Assert.assertEquals(0, entry.getUnixMode());
        entry.setUnixMode(0755);
        Assert.assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipExtraField extraField = new ZipGeneralPurposeExtraField("Test", new byte[10]);
        entry.setExtraFields(new ZipExtraField[]{extraField});
        Assert.assertEquals(1, entry.getExtraFields().length);
        Assert.assertEquals(extraField, entry.getExtraFields()[0]);
    }

    @Test
    public void testGetExtraFields() {
        ZipExtraField extraField = new ZipGeneralPurposeExtraField("Test", new byte[10]);
        entry.setExtraFields(new ZipExtraField[]{extraField});
        Assert.assertArrayEquals(new ZipExtraField[]{extraField}, entry.getExtraFields());
    }

    @Test
    public void testAddExtraField() {
        ZipExtraField extraField1 = new ZipGeneralPurposeExtraField("Test1", new byte[10]);
        entry.addExtraField(extraField1);
        Assert.assertEquals(1, entry.getExtraFields().length);
        Assert.assertEquals(extraField1, entry.getExtraFields()[0]);

        ZipExtraField extraField2 = new ZipGeneralPurposeExtraField("Test2", new byte[10]);
        entry.addExtraField(extraField2);
        Assert.assertEquals(2, entry.getExtraFields().length);
        Assert.assertArrayEquals(new ZipExtraField[]{extraField1, extraField2}, entry.getExtraFields());
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipExtraField extraField1 = new ZipGeneralPurposeExtraField("Test1", new byte[10]);
        entry.addAsFirstExtraField(extraField1);
        Assert.assertEquals(1, entry.getExtraFields().length);
        Assert.assertEquals(extraField1, entry.getExtraFields()[0]);

        ZipExtraField extraField2 = new ZipGeneralPurposeExtraField("Test2", new byte[10]);
        entry.addExtraField(extraField2);
        Assert.assertEquals(2, entry.getExtraFields().length);
        Assert.assertArrayEquals(new ZipExtraField[]{extraField2, extraField1}, entry.getExtraFields());
    }

    @Test
    public void testRemoveExtraField() {
        ZipExtraField extraField1 = new ZipGeneralPurposeExtraField("Test1", new byte[10]);
        entry.addExtraField(extraField1);
        Assert.assertEquals(1, entry.getExtraFields().length);

        entry.removeExtraField(ZipShort.LOCAL_FILE_NAME);
        Assert.assertArrayEquals(new ZipExtraField[0], entry.getExtraFields());
    }

    @Test
    public void testGetExtraField() {
        ZipExtraField extraField = new ZipGeneralPurposeExtraField("Test", new byte[10]);
        entry.setExtraFields(new ZipExtraField[]{extraField});
        Assert.assertEquals(extraField, entry.getExtraField(ZipShort.LOCAL_FILE_NAME));
        Assert.assertNull(entry.getExtraField(ZipShort.COMMENT));
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testGetExtraFieldNonexistentId() {
        Assert.assertArrayEquals(new ZipExtraField[0], entry.getExtraFields());
        Assert.assertNull(entry.getExtraField(ZipShort.COMMENT));
    }

    @Test
    public void testSetExtra() throws ZipException {
        byte[] extraData = new byte[10];
        ZipExtraField extraField = new ZipGeneralPurposeExtraField("Test", extraData);
        entry.setExtraFields(new ZipExtraField[]{extraField});

        InputStream inputStream = new ByteArrayInputStream(extraData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        entry.setExtra(outputStream.toByteArray());
        byte[] expected = ExtraFieldUtils.parse(extraData)[0].getLocalFileDataData();
        byte[] actual = entry.getExtra();

        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = RuntimeException.class)
    public void testSetExtraInvalidData() throws ZipException {
        byte[] extraData = new byte[10];

        entry.setExtra(extraData);
    }

    @Test
    public void testSetCentralDirectoryExtra() throws ZipException {
        byte[] extraData = new byte[10];
        ZipExtraField extraField = new ZipGeneralPurposeExtraField("Test", extraData);
        entry.setCentralDirectoryExtra(extraData);
        Assert.assertArrayEquals(extraData, entry.getLocalFileDataExtra());
    }

    @Test
    public void testGetName() {
        Assert.assertNull(entry.getName());
        entry.setName("test.txt");
        Assert.assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testIsDirectory() {
        Assert.assertFalse(entry.isDirectory());
        entry.setName("test.txt/");
        Assert.assertTrue(entry.isDirectory());
    }

    @Test
    public void testHashCode() {
        String name1 = "test1.txt";
        String name2 = "test2.txt";
        String name3 = "test3.txt";

        ZipArchiveEntry entry1 = new ZipArchiveEntry(name1);
        ZipArchiveEntry entry2 = new ZipArchiveEntry(name2);
        ZipArchiveEntry entry3 = new ZipArchiveEntry(name3);

        int hashCode1 = entry1.hashCode();
        int hashCode2 = entry2.hashCode();
        int hashCode3 = entry3.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
        Assert.assertNotEquals(hashCode1, hashCode3);
        Assert.assertNotEquals(hashCode2, hashCode3);

        entry1.setName(name3);
        Assert.assertEquals(hashCode3, entry1.hashCode());
    }

    @Test
    public void testEquals() {
        String name1 = "test1.txt";
        String name2 = "test2.txt";
        String name3 = "test3.txt";

        ZipArchiveEntry entry1 = new ZipArchiveEntry(name1);
        ZipArchiveEntry entry2 = new ZipArchiveEntry(name2);
        ZipArchiveEntry entry3 = new ZipArchiveEntry(name3);
        ZipArchiveEntry entry4 = new ZipArchiveEntry(name1);

        Assert.assertNotEquals(entry1, null);
        Assert.assertNotEquals(entry1, entry2);
        Assert.assertEquals(entry1, entry4);
    }

    @Test
    public void testGetLastModifiedDate() {
        long time = 1234567890123L;
        entry.setTime(time);
        Assert.assertEquals(new Date(time), entry.getLastModifiedDate());
    }
}