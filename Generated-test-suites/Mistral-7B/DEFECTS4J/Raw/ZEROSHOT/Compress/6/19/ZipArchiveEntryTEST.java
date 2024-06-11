import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.io.ByteArrayOutputStream;
import org.apache.commons.compress.utils.io.ByteStreams;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    @Test
    public void testCreateAndGetBasicFields() throws IOException {
        String name = "test.txt";
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name);
        Assert.assertEquals(name, zipArchiveEntry.getName());
        Assert.assertNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());
        Assert.assertEquals(0, zipArchiveEntry.getInternalAttributes());
        Assert.assertEquals(0, zipArchiveEntry.getExternalAttributes());
        Assert.assertEquals(0, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testCreateWithFile() throws IOException {
        File inputFile = new File("testFile.txt");
        String name = "testFile.txt";
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(inputFile, name);
        Assert.assertEquals(name, zipArchiveEntry.getName());
        Assert.assertNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());
        Assert.assertEquals(inputFile.length(), zipArchiveEntry.getSize());
        Assert.assertEquals(inputFile.lastModified(), zipArchiveEntry.getTime());
    }

    @Test
    public void testCreateWithZipEntry() throws IOException {
        String name = "test.txt";
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(name);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals(name, zipArchiveEntry.getName());
        Assert.assertNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());
        Assert.assertEquals(0, zipArchiveEntry.getInternalAttributes());
        Assert.assertEquals(0, zipArchiveEntry.getExternalAttributes());
        Assert.assertEquals(0, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testCreateWithExtraFields() throws IOException {
        String name = "test.txt";
        byte[] extraFieldData = new byte[]{(byte) 0x07, (byte) 0x01, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name);
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{new ZipExtendedInfo(extraFieldData)});
        Assert.assertNotNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(ZipShort.UNIX_EXTENDED_INFO, zipArchiveEntry.getExtraFields()[0].getHeaderId());
        Assert.assertArrayEquals(extraFieldData, zipArchiveEntry.getExtraFields()[0].getLocalFileDataData());
    }

    @Test
    public void testSetAndGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setUnixMode(0755);
        Assert.assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testSetInternalAttributes() throws IOException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setInternalAttributes(ZipShort.STAR);
        Assert.assertEquals(ZipShort.STAR, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() throws IOException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setExternalAttributes(0644);
        Assert.assertEquals(0644, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetExtraFields() throws IOException {
        String name = "test.txt";
        byte[] extraFieldData = new byte[]{(byte) 0x07, (byte) 0x01, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name);
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{new ZipExtendedInfo(extraFieldData)});
        Assert.assertNotNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(ZipShort.UNIX_EXTENDED_INFO, zipArchiveEntry.getExtraFields()[0].getHeaderId());
        Assert.assertArrayEquals(extraFieldData, zipArchiveEntry.getExtraFields()[0].getLocalFileDataData());
    }

    @Test
    public void testSetExtraWithByteArray() throws IOException {
        String name = "test.txt";
        byte[] extraFieldData = new byte[]{(byte) 0x07, (byte) 0x01, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        zipArchiveEntry.setExtra(extraFieldData);
        byte[] extraData = byteArrayOutputStream.toByteArray();
        Assert.assertNotNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(ZipShort.UNIX_EXTENDED_INFO, zipArchiveEntry.getExtraFields()[0].getHeaderId());
        Assert.assertArrayEquals(extraFieldData, zipArchiveEntry.getExtraFields()[0].getLocalFileDataData());
        Assert.assertArrayEquals(extraData, zipArchiveEntry.getLocalFileDataExtra());
    }

    @Test
    public void testSetAndGetCentralDirectoryExtraData() throws IOException {
        String name = "test.txt";
        byte[] extraFieldData = new byte[]{(byte) 0x07, (byte) 0x01, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name);
        byte[] centralDirectoryExtraData = new byte[]{0x07, 0x01, (byte) 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        zipArchiveEntry.setCentralDirectoryExtra(centralDirectoryExtraData);
        Assert.assertArrayEquals(centralDirectoryExtraData, zipArchiveEntry.getCentralDirectoryExtra());
    }

    @Test
    public void testSetExtraFieldsWithArray() throws IOException {
        String name = "test.txt";
        byte[] extraFieldData = new byte[]{(byte) 0x07, (byte) 0x01, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name);
        ZipExtraField[] extraFields = new ZipExtraField[]{new ZipExtendedInfo(extraFieldData)};
        zipArchiveEntry.setExtraFields(extraFields);
        Assert.assertNotNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(ZipShort.UNIX_EXTENDED_INFO, zipArchiveEntry.getExtraFields()[0].getHeaderId());
        Assert.assertArrayEquals(extraFieldData, zipArchiveEntry.getExtraFields()[0].getLocalFileDataData());
    }

    @Test
    public void testSetExtraFieldsWithMap() throws IOException {
        String name = "test.txt";
        byte[] extraFieldData = new byte[]{(byte) 0x07, (byte) 0x01, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Map<ZipShort, ZipExtraField> extraFieldsMap = new LinkedHashMap<>();
        extraFieldsMap.put(ZipShort.UNIX_EXTENDED_INFO, new ZipExtendedInfo(extraFieldData));
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name);
        zipArchiveEntry.setExtraFields(extraFieldsMap.values().toArray(new ZipExtraField[0]));
        Assert.assertNotNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(ZipShort.UNIX_EXTENDED_INFO, zipArchiveEntry.getExtraFields()[0].getHeaderId());
        Assert.assertArrayEquals(extraFieldData, zipArchiveEntry.getExtraFields()[0].getLocalFileDataData());
    }

    @Test
    public void testClone() throws IOException, CloneNotSupportedException {
        String name = "test.txt";
        byte[] extraFieldData = new byte[]{(byte) 0x07, (byte) 0x01, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ZipArchiveEntry original = new ZipArchiveEntry(name);
        original.setExtraFields(new ZipExtraField[]{new ZipExtendedInfo(extraFieldData)});
        ZipArchiveEntry cloned = (ZipArchiveEntry) original.clone();
        Assert.assertEquals(original.getName(), cloned.getName());
        Assert.assertArrayEquals(original.getLocalFileDataExtra(), cloned.getLocalFileDataExtra());
        Assert.assertArrayEquals(original.getCentralDirectoryExtra(), cloned.getCentralDirectoryExtra());
        Assert.assertArrayEquals(original.getExtraFields(), cloned.getExtraFields());
    }

    @Test
    public void testEquals() throws IOException {
        String name = "test.txt";
        byte[] extraFieldData = new byte[]{(byte) 0x07, (byte) 0x01, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry(name);
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry(name);
        zipArchiveEntry1.setExtraFields(new ZipExtraField[]{new ZipExtendedInfo(extraFieldData)});
        Assert.assertTrue(zipArchiveEntry1.equals(zipArchiveEntry2));

        zipArchiveEntry2.setName("test2.txt");
        Assert.assertFalse(zipArchiveEntry1.equals(zipArchiveEntry2));

        ZipArchiveEntry zipArchiveEntry3 = new ZipArchiveEntry(name);
        Assert.assertFalse(zipArchiveEntry1.equals(zipArchiveEntry3));

        ZipArchiveEntry zipArchiveEntry4 = new Object() {};
        Assert.assertFalse(zipArchiveEntry1.equals(zipArchiveEntry4));
    }

    @Test
    public void testToString() throws IOException {
        String name = "test.txt";
        byte[] extraFieldData = new byte[]{(byte) 0x07, (byte) 0x01, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name);
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{new ZipExtendedInfo(extraFieldData)});
        String toString = zipArchiveEntry.toString();
        Assert.assertTrue(toString.contains(name));
        Assert.assertTrue(toString.contains("method=-1"));
        Assert.assertTrue(toString.contains("internalAttributes=0"));
        Assert.assertTrue(toString.contains("externalAttributes=0"));
        Assert.assertTrue(toString.contains("platform=0"));
        Assert.assertTrue(toString.contains("extraFields=null"));
        Assert.assertTrue(toString.contains("name=" + name));
        Assert.assertTrue(toString.contains("method=-1"));
        Assert.assertTrue(toString.contains("internalAttributes=0"));
        Assert.assertTrue(toString.contains("externalAttributes=0"));
        Assert.assertTrue(toString.contains("platform=0"));
        Assert.assertTrue(toString.contains("extraFields={}"));
    }

    @Test
    public void testHashCode() throws IOException {
        String name = "test.txt";
        byte[] extraFieldData = new byte[]{(byte) 0x07, (byte) 0x01, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry(name);
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry(name);
        zipArchiveEntry1.setExtraFields(new ZipExtraField[]{new ZipExtendedInfo(extraFieldData)});
        zipArchiveEntry2.setExtraFields(new ZipExtraField[]{new ZipExtendedInfo(extraFieldData)});
        int hashCode1 = zipArchiveEntry1.hashCode();
        int hashCode2 = zipArchiveEntry2.hashCode();
        Assert.assertEquals(hashCode1, hashCode2);

        zipArchiveEntry1.setName("test2.txt");
        int hashCode3 = zipArchiveEntry1.hashCode();
        Assert.assertNotEquals(hashCode1, hashCode3);

        ZipArchiveEntry zipArchiveEntry3 = new ZipArchiveEntry(name);
        int hashCode4 = zipArchiveEntry3.hashCode();
        Assert.assertNotEquals(hashCode1, hashCode4);
    }
}