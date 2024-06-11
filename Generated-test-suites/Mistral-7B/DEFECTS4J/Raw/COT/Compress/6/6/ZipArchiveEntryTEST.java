import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.io.ByteArrayInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructor_String() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
        Assert.assertNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());
        Assert.assertEquals(0, zipArchiveEntry.getInternalAttributes());
        Assert.assertEquals(0, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testConstructor_ZipEntry() {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
        Assert.assertNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());
        Assert.assertEquals(zipEntry.getExtra(), zipArchiveEntry.getExtra());
        Assert.assertEquals(zipEntry.getInternalAttributes(), zipArchiveEntry.getInternalAttributes());
        Assert.assertEquals(zipEntry.getExternalAttributes(), zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testConstructor_ZipArchiveEntry() {
        ZipArchiveEntry original = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry copy = new ZipArchiveEntry(original);
        Assert.assertEquals(original.getName(), copy.getName());
        Assert.assertArrayEquals(original.getExtra(), copy.getExtra());
        Assert.assertEquals(original.getInternalAttributes(), copy.getInternalAttributes());
        Assert.assertEquals(original.getExternalAttributes(), copy.getExternalAttributes());
        Assert.assertEquals(original.getExtraFields(), copy.getExtraFields());
    }

    @Test
    public void testConstructor_File_String() {
        File file = new File("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, "test.txt");
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
        Assert.assertNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());
        Assert.assertEquals(file.length(), zipArchiveEntry.getSize());
        Assert.assertEquals(file.lastModified(), zipArchiveEntry.getTime());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(0777);
        Assert.assertEquals(0777, zipArchiveEntry.getUnixMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUnixMode_NegativeValue() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(-1);
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());

        zipArchiveEntry.setUnixMode(0777);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        ZipExtraField testExtraField = new ZipGlobalFileCommentField();
        testExtraField.setContent("Test Comment");

        ZipExtraField[] extraFields = {testExtraField};
        zipArchiveEntry.setExtraFields(extraFields);

        Assert.assertNotNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals("Test Comment", zipArchiveEntry.getExtraField(ZipShort.GLOBAL_FILE_COMMENT).getContent());
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        ZipExtraField testExtraField = new ZipGlobalFileCommentField();
        testExtraField.setContent("Test Comment 1");

        zipArchiveEntry.addExtraField(testExtraField);

        Assert.assertNotNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals("Test Comment 1", zipArchiveEntry.getExtraField(ZipShort.GLOBAL_FILE_COMMENT).getContent());

        ZipExtraField testExtraField2 = new ZipLocalFileCommentField();
        testExtraField2.setContent("Test Comment 2");

        zipArchiveEntry.addExtraField(testExtraField2);

        Assert.assertNotNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(2, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals("Test Comment 2", zipArchiveEntry.getExtraField(ZipShort.LOCAL_FILE_COMMENT).getContent());
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        ZipExtraField testExtraField1 = new ZipGlobalFileCommentField();
        testExtraField1.setContent("Test Comment 1");

        zipArchiveEntry.addAsFirstExtraField(testExtraField1);

        Assert.assertNotNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals("Test Comment 1", zipArchiveEntry.getExtraField(ZipShort.GLOBAL_FILE_COMMENT).getContent());

        ZipExtraField testExtraField2 = new ZipLocalFileCommentField();
        testExtraField2.setContent("Test Comment 2");

        zipArchiveEntry.addExtraField(testExtraField2);

        Assert.assertNotNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(2, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(1, zipArchiveEntry.getExtraField(ZipShort.LOCAL_FILE_COMMENT).getPosition());
        Assert.assertEquals("Test Comment 2", zipArchiveEntry.getExtraField(ZipShort.LOCAL_FILE_COMMENT).getContent());
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        ZipExtraField testExtraField1 = new ZipGlobalFileCommentField();
        testExtraField1.setContent("Test Comment 1");

        zipArchiveEntry.addExtraField(testExtraField1);

        Assert.assertNotNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);

        zipArchiveEntry.removeExtraField(ZipShort.GLOBAL_FILE_COMMENT);

        Assert.assertNull(zipArchiveEntry.getExtraFields());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertTrue(zipArchiveEntry.isSupportedCompressionMethod());

        zipArchiveEntry.setMethod(ZipEntry.STORED);
        Assert.assertTrue(zipArchiveEntry.isSupportedCompressionMethod());

        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        Assert.assertTrue(zipArchiveEntry.isSupportedCompressionMethod());

        zipArchiveEntry.setMethod(ZipEntry.FILTERED);
        Assert.assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());

        zipArchiveEntry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());

        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        Assert.assertEquals(ZipEntry.DEFLATED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        Assert.assertEquals(-1, zipArchiveEntry.getMethod());

        zipArchiveEntry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());

        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        Assert.assertEquals(ZipEntry.DEFLATED, zipArchiveEntry.getMethod());

        byte[] methodData = new byte[]{ZipEntry.DEFLATED};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(methodData);
        zipArchiveEntry.setMethod(byteArrayInputStream);
        Assert.assertEquals(ZipEntry.DEFLATED, zipArchiveEntry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethod_NegativeValue() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(-1);
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(0, zipArchiveEntry.getInternalAttributes());

        zipArchiveEntry.setInternalAttributes(ZipEntry.FLAG_UNCHANGED | ZipEntry.FLAG_COMPRESSION_METHOD);
        Assert.assertEquals(ZipEntry.FLAG_UNCHANGED | ZipEntry.FLAG_COMPRESSION_METHOD, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        Assert.assertEquals(0, zipArchiveEntry.getInternalAttributes());

        zipArchiveEntry.setInternalAttributes(ZipEntry.FLAG_UNCHANGED | ZipEntry.FLAG_COMPRESSION_METHOD);
        Assert.assertEquals(ZipEntry.FLAG_UNCHANGED | ZipEntry.FLAG_COMPRESSION_METHOD, zipArchiveEntry.getInternalAttributes());

        zipArchiveEntry.setInternalAttributes(0xFFFF & ~(ZipEntry.FLAG_UNCHANGED | ZipEntry.FLAG_COMPRESSION_METHOD));
        Assert.assertEquals(0, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(0, zipArchiveEntry.getExternalAttributes());

        zipArchiveEntry.setExternalAttributes(ZipEntry.FLAG_UNCHANGED | ZipEntry.FLAG_UNIX_GROUP);
        Assert.assertEquals(ZipEntry.FLAG_UNCHANGED | ZipEntry.FLAG_UNIX_GROUP, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        Assert.assertEquals(0, zipArchiveEntry.getExternalAttributes());

        zipArchiveEntry.setExternalAttributes(ZipEntry.FLAG_UNCHANGED | ZipEntry.FLAG_UNIX_GROUP);
        Assert.assertEquals(ZipEntry.FLAG_UNCHANGED | ZipEntry.FLAG_UNIX_GROUP, zipArchiveEntry.getExternalAttributes());

        zipArchiveEntry.setExternalAttributes(0xFFFFFFFFFFFFL);
        Assert.assertEquals(0, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetExtra() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        byte[] extraData = new byte[0];
        zipArchiveEntry.setExtra(extraData);

        Assert.assertArrayEquals(extraData, zipArchiveEntry.getExtra());
    }

    @Test(expected = RuntimeException.class)
    public void testSetExtra_InvalidData() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        byte[] extraData = new byte[10];

        zipArchiveEntry.setExtra(extraData);
    }

    @Test
    public void testSetCentralDirectoryExtra() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        byte[] centralDirectoryExtraData = new byte[0];
        zipArchiveEntry.setCentralDirectoryExtra(centralDirectoryExtraData);

        Assert.assertArrayEquals(centralDirectoryExtraData, zipArchiveEntry.getCentralDirectoryExtra());
    }

    @Test
    public void testSetName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        zipArchiveEntry.setName("test2.txt");

        Assert.assertEquals("test2.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test.txt");

        Assert.assertEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry2.hashCode());

        zipArchiveEntry1.setName("test2.txt");

        Assert.assertNotEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry2.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test.txt");

        Assert.assertTrue(zipArchiveEntry1.equals(zipArchiveEntry2));

        zipArchiveEntry1.setName("test2.txt");

        Assert.assertFalse(zipArchiveEntry1.equals(zipArchiveEntry2));

        ZipArchiveEntry zipArchiveEntry3 = new ZipArchiveEntry("test.txt");
        zipArchiveEntry3.setName("test2.txt");

        Assert.assertTrue(zipArchiveEntry1.equals(zipArchiveEntry3));

        Assert.assertFalse(zipArchiveEntry1.equals(null));

        Assert.assertFalse(zipArchiveEntry1.equals(new Object()));
    }

    @Test
    public void testGetLastModifiedDate() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        Date date = new Date();
        zipArchiveEntry.setTime(date.getTime());

        Assert.assertEquals(date, zipArchiveEntry.getLastModifiedDate());
    }
}