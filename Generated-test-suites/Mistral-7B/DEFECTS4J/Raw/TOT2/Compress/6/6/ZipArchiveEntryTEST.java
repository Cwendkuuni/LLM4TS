import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructorWithName() {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        Assert.assertEquals(name, entry.getName());
        Assert.assertEquals(-1, entry.getMethod());
        Assert.assertEquals(0, entry.getInternalAttributes());
        Assert.assertEquals(0, entry.getExternalAttributes());
        Assert.assertNull(entry.getExtraFields());
    }

    @Test
    public void testConstructorWithZipEntry() throws IOException {
        String filePath = "testFile.zip";
        File zipFile = new File(filePath);
        ZipFile zip = new ZipFile(zipFile);
        ZipEntry entry = zip.getEntry("test.txt");
        ZipArchiveEntry archiveEntry = new ZipArchiveEntry(entry);
        Assert.assertEquals("test.txt", archiveEntry.getName());
        Assert.assertEquals(-1, archiveEntry.getMethod());
        Assert.assertEquals(entry.getMethod(), archiveEntry.getMethod());
        Assert.assertEquals(entry.getSize(), archiveEntry.getSize());
        Assert.assertEquals(entry.getTime(), archiveEntry.getTime());
        Assert.assertEquals(entry.getComment(), archiveEntry.getComment());
        Assert.assertNotNull(archiveEntry.getExtraFields());
    }

    @Test
    public void testConstructorWithZipArchiveEntry() throws IOException {
        String filePath = "testFile.zip";
        File zipFile = new File(filePath);
        ZipFile zip = new ZipFile(zipFile);
        ZipEntry entry = zip.getEntry("test.txt");
        ZipArchiveEntry originalEntry = new ZipArchiveEntry(entry);
        ZipArchiveEntry cloneEntry = (ZipArchiveEntry) originalEntry.clone();
        Assert.assertNotSame(originalEntry, cloneEntry);
        Assert.assertEquals(originalEntry.getName(), cloneEntry.getName());
        Assert.assertEquals(originalEntry.getMethod(), cloneEntry.getMethod());
        Assert.assertEquals(originalEntry.getInternalAttributes(), cloneEntry.getInternalAttributes());
        Assert.assertEquals(originalEntry.getExternalAttributes(), cloneEntry.getExternalAttributes());
        Assert.assertArrayEquals(originalEntry.getExtra(), cloneEntry.getExtra());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, entry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodWithNegativeNumber() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(-1);
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(0xFF00);
        Assert.assertEquals(0xFF00, entry.getInternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        Assert.assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField field = new ZipExtendedTimestamps();
        entry.setExtraFields(new ZipExtraField[]{field});
        Assert.assertNotNull(entry.getExtraFields());
        Assert.assertEquals(1, entry.getExtraFields().length);
        Assert.assertEquals(field, entry.getExtraField(new ZipShort(field.getHeaderId())));
    }

    @Test
    public void testSetExtra() throws IOException {
        String filePath = "testFile.zip";
        File zipFile = new File(filePath);
        ZipFile zip = new ZipFile(zipFile);
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[10];
        entry.setExtra(extraData);
        Assert.assertEquals(extraData.length, entry.getLocalFileDataExtra().length);
        Assert.assertEquals(extraData, entry.getLocalFileDataExtra());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertTrue(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetName() {
        ZipArchiveEntry
            entry1 = new ZipArchiveEntry("test1.txt"),
            entry2 = new ZipArchiveEntry("test2/folder/file.txt");
        Assert.assertEquals("test1.txt", entry1.getName());
        Assert.assertEquals("test2/folder/file.txt", entry2.getName());
    }

    @Test
    public void testIsDirectory() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test1.txt");
        Assert.assertFalse(entry1.isDirectory());
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test2/folder/");
        Assert.assertTrue(entry2.isDirectory());
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test1.txt");
        Assert.assertEquals(entry1.hashCode(), entry2.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test1.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test1.txt");
        Assert.assertTrue(entry1.equals(entry2));

        ZipArchiveEntry entry3 = new ZipArchiveEntry("test2.txt");
        Assert.assertFalse(entry1.equals(entry3));
    }

    @Test
    public void testGetLastModifiedDate() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Date date = new Date(System.currentTimeMillis());
        entry.setTime(date.getTime());
        Assert.assertEquals(date, entry.getLastModifiedDate());
    }
}