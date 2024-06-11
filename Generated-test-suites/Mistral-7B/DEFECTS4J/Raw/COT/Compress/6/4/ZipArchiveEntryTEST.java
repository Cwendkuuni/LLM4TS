import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructor_String() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
        Assert.assertNull(zipArchiveEntry.getSize());
        Assert.assertEquals(0L, zipArchiveEntry.getTime());
    }

    @Test
    public void testConstructor_ZipEntry() throws IOException {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
        Assert.assertEquals(1L, zipArchiveEntry.getSize());
        Assert.assertEquals(new Date(System.currentTimeMillis() - 1000L), zipArchiveEntry.getLastModifiedDate());
    }

    @Test
    public void testConstructor_FileAndString() {
        File testFile = new File("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(testFile, "test.txt");
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
        Assert.assertEquals(testFile.length(), zipArchiveEntry.getSize());
        Assert.assertEquals(testFile.lastModified(), zipArchiveEntry.getTime());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertTrue(zipArchiveEntry.isSupportedCompressionMethod());

        zipArchiveEntry.setMethod(ZipEntry.STORED);
        Assert.assertTrue(zipArchiveEntry.isSupportedCompressionMethod());

        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        Assert.assertTrue(zipArchiveEntry.isSupportedCompressionMethod());

        zipArchiveEntry.setMethod(123);
        Assert.assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethod_NegativeValue() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(-1);
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());

        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        Assert.assertEquals(ZipEntry.DEFLATED, zipArchiveEntry.getMethod());

        zipArchiveEntry.setMethod(123);
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setInternalAttributes(123);
        Assert.assertEquals(123, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExternalAttributes(123);
        Assert.assertEquals(123, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(0755);
        Assert.assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testGetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(0755);
        Assert.assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());

        zipArchiveEntry.setUnixMode(0755);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipShort headerId = new ZipShort((short) 1);
        ZipExtraField zipExtraField = new GeneralPurposeByteHeaderField("Test");

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExtraFields(new ZipExtraField[] { zipExtraField });
        Assert.assertNotNull(zipArchiveEntry.getExtraField(headerId));
        Assert.assertEquals("Test", zipArchiveEntry.getExtraField(headerId).getStringValue());
    }

    @Test
    public void testAddExtraField() {
        ZipShort headerId = new ZipShort((short) 1);
        ZipExtraField zipExtraField = new GeneralPurposeByteHeaderField("Test");

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.addExtraField(zipExtraField);
        Assert.assertNotNull(zipArchiveEntry.getExtraField(headerId));
        Assert.assertEquals("Test", zipArchiveEntry.getExtraField(headerId).getStringValue());
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipShort headerId = new ZipShort((short) 1);
        ZipExtraField zipExtraField = new GeneralPurposeByteHeaderField("Test");

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.addAsFirstExtraField(zipExtraField);
        Assert.assertNotNull(zipArchiveEntry.getExtraField(headerId));
        Assert.assertEquals("Test", zipArchiveEntry.getExtraField(headerId).getStringValue());
    }

    @Test
    public void testRemoveExtraField() {
        ZipShort headerId = new ZipShort((short) 1);
        ZipExtraField zipExtraField = new GeneralPurposeByteHeaderField("Test");

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.addExtraField(zipExtraField);

        zipArchiveEntry.removeExtraField(headerId);
        Assert.assertNull(zipArchiveEntry.getExtraField(headerId));
    }

    @Test
    public void testSetLocalFileDataExtra() throws IOException {
        ZipShort headerId = new ZipShort((short) 1);
        ZipExtraField zipExtraField = new GeneralPurposeByteHeaderField("Test");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = zipExtraField.getLocalFileDataData();
        byteArrayOutputStream.write(bytes, 0, bytes.length);
        byte[] extra = byteArrayOutputStream.toByteArray();

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExtra(extra);
        Assert.assertNotNull(zipArchiveEntry.getExtraField(headerId));
        Assert.assertEquals("Test", zipArchiveEntry.getExtraField(headerId).getStringValue());
    }

    @Test
    public void testSetCentralDirectoryExtra() throws IOException {
        ZipShort headerId = new ZipShort((short) 1);
        ZipExtraField zipExtraField = new GeneralPurposeByteHeaderField("Test");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = zipExtraField.getCentralDirectoryData();
        byteArrayOutputStream.write(bytes, 0, bytes.length);
        byte[] extra = byteArrayOutputStream.toByteArray();

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setCentralDirectoryExtra(extra);
        Assert.assertNotNull(zipArchiveEntry.getExtraField(headerId));
        Assert.assertEquals("Test", zipArchiveEntry.getExtraField(headerId).getStringValue());
    }

    @Test
    public void testGetName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testIsDirectory() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertFalse(zipArchiveEntry.isDirectory());

        zipArchiveEntry = new ZipArchiveEntry("test/");
        Assert.assertTrue(zipArchiveEntry.isDirectory());
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test.txt");

        Assert.assertEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry2.hashCode());

        ZipArchiveEntry zipArchiveEntry3 = new ZipArchiveEntry("test.txt");
        zipArchiveEntry3.setName("test2.txt");
        Assert.assertNotEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry3.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test.txt");

        Assert.assertTrue(zipArchiveEntry1.equals(zipArchiveEntry2));

        ZipArchiveEntry zipArchiveEntry3 = new ZipArchiveEntry("test2.txt");
        Assert.assertFalse(zipArchiveEntry1.equals(zipArchiveEntry3));

        zipArchiveEntry1.setName("test.txt");
        Assert.assertTrue(zipArchiveEntry1.equals(zipArchiveEntry2));

        zipArchiveEntry2.setName("test.txt");
        Assert.assertTrue(zipArchiveEntry1.equals(zipArchiveEntry2));
    }

    @Test
    public void testGetLastModifiedDate() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Date date = new Date(System.currentTimeMillis() - 1000L);
        zipArchiveEntry.setTime(date.getTime());
        Assert.assertEquals(date, zipArchiveEntry.getLastModifiedDate());
    }
}