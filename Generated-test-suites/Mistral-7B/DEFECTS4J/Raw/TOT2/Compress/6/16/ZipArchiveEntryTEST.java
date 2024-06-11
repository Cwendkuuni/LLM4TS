import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.ZipExtraField;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    @Test
    public void testCreateWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testCreateWithFile() {
        File file = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(file, "test.txt");
        Assert.assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testClone() throws cloneable.CloneNotSupportedException {
        ZipArchiveEntry original = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry cloned = (ZipArchiveEntry) original.clone();
        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original, cloned);
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        Assert.assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(-1, entry.getMethod());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, entry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodNegative() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setMethod(-1);
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setInternalAttributes(0x02);
        Assert.assertEquals(0x02, entry.getInternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setExternalAttributes(0x1234567890L);
        Assert.assertEquals(0x1234567890L, entry.getExternalAttributes());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ZipExtraField customField = new ZipCustomField("Test", new byte[0]);
        entry.setExtraFields(new ZipExtraField[]{customField});
        Assert.assertArrayEquals(new byte[0], entry.getLocalFileDataExtra());
        Assert.assertEquals(1, entry.getExtraFields().length);
        Assert.assertEquals(customField, entry.getExtraField(customField.getHeaderId()));
    }

    @Test
    public void testSetExtra() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] extraData = new byte[]{0x01, 0x02, 0x03};
        Os.write(os, extraData, 0, extraData.length);
        entry.setExtra(extraData);
        Assert.assertArrayEquals(extraData, entry.getLocalFileDataExtra());
    }

    @Test
    public void testGetName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testIsDirectory() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Assert.assertFalse(entry.isDirectory());
        entry = new ZipArchiveEntry("test/");
        Assert.assertTrue(entry.isDirectory());
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(entry1.hashCode(), entry2.hashCode());
        entry1.setName("test2.txt");
        Assert.assertNotEquals(entry1.hashCode(), entry2.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");
        Assert.assertTrue(entry1.equals(entry2));
        entry1.setName("test2.txt");
        Assert.assertFalse(entry1.equals(entry2));
    }

    @Test
    public void testGetLastModifiedDate() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        Date expected = new Date(System.currentTimeMillis());
        entry.setTime(expected.getTime());
        Assert.assertEquals(expected, entry.getLastModifiedDate());
    }
}