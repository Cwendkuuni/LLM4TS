import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructor() {
        String name = "test.txt";
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name);
        Assert.assertEquals(name, zipArchiveEntry.getName());
        Assert.assertNull(zipArchiveEntry.getSize());
        Assert.assertNull(zipArchiveEntry.getTime());
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());
        Assert.assertEquals(0, zipArchiveEntry.getInternalAttributes());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
        Assert.assertNull(zipArchiveEntry.getExtraFields());
        Assert.assertFalse(zipArchiveEntry.isDirectory());

        ZipEntry zipEntry = new ZipEntry(name);
        ZipArchiveEntry cloneZipArchiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals(name, cloneZipArchiveEntry.getName());
        Assert.assertNull(cloneZipArchiveEntry.getSize());
        Assert.assertNull(cloneZipArchiveEntry.getTime());
        Assert.assertEquals(-1, cloneZipArchiveEntry.getMethod());
        Assert.assertEquals(0, cloneZipArchiveEntry.getInternalAttributes());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, cloneZipArchiveEntry.getPlatform());
        Assert.assertNull(cloneZipArchiveEntry.getExtraFields());
        Assert.assertFalse(cloneZipArchiveEntry.isDirectory());
    }

    @Test
    public void testZipArchiveEntryConstructorWithFile() {
        String name = "test.txt";
        File file = new File(name);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, name);
        Assert.assertEquals(name, zipArchiveEntry.getName());
        Assert.assertEquals(file.length(), zipArchiveEntry.getSize());
        Assert.assertEquals(file.lastModified(), zipArchiveEntry.getTime());
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());
        Assert.assertEquals(0, zipArchiveEntry.getInternalAttributes());
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
        Assert.assertNull(zipArchiveEntry.getExtraFields());
        Assert.assertFalse(zipArchiveEntry.isDirectory());
    }

    @Test
    public void testZipArchiveEntryCloneable() {
        String name = "test.txt";
        ZipArchiveEntry originalZipArchiveEntry = new ZipArchiveEntry(name);
        ZipArchiveEntry clonedZipArchiveEntry = (ZipArchiveEntry) originalZipArchiveEntry.clone();
        Assert.assertEquals(originalZipArchiveEntry.getName(), clonedZipArchiveEntry.getName());
        Assert.assertNotSame(originalZipArchiveEntry.getSize(), clonedZipArchiveEntry.getSize());
        Assert.assertNotSame(originalZipArchiveEntry.getTime(), clonedZipArchiveEntry.getTime());
        Assert.assertEquals(originalZipArchiveEntry.getMethod(), clonedZipArchiveEntry.getMethod());
        Assert.assertEquals(originalZipArchiveEntry.getInternalAttributes(), clonedZipArchiveEntry.getInternalAttributes());
        Assert.assertEquals(originalZipArchiveEntry.getPlatform(), clonedZipArchiveEntry.getPlatform());
        Assert.assertNotSame(originalZipArchiveEntry.getExtraFields(), clonedZipArchiveEntry.getExtraFields());
        Assert.assertFalse(originalZipArchiveEntry.isDirectory());
        Assert.assertFalse(clonedZipArchiveEntry.isDirectory());
    }

    @Test
    public void testZipArchiveEntryMethods() {
        String name = "test.txt";
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name);

        zipArchiveEntry.setSize(1024);
        Assert.assertEquals(1024, zipArchiveEntry.getSize());

        zipArchiveEntry.setTime(System.currentTimeMillis());
        Assert.assertEquals(new Date(System.currentTimeMillis()), zipArchiveEntry.getLastModifiedDate());

        zipArchiveEntry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());

        zipArchiveEntry.setInternalAttributes(0x0201);
        Assert.assertEquals(0x0201, zipArchiveEntry.getInternalAttributes());

        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());

        zipArchiveEntry.setUnixMode(0755);
        Assert.assertEquals(0755, zipArchiveEntry.getUnixMode());

        ZipExtraField zipExtraField = new ZipExtraField("Test", "Value".getBytes());
        zipArchiveEntry.addExtraField(zipExtraField);
        Assert.assertArrayEquals("Value".getBytes(), zipArchiveEntry.getExtraField(new ZipShort(ZipShort.TEST)).getData());

        byte[] extraData = new byte[]{0x01, 0x02, 0x03};
        zipArchiveEntry.setExtra(extraData);
        Assert.assertArrayEquals(extraData, zipArchiveEntry.getExtraData());

        zipArchiveEntry.removeExtraField(ZipShort.TEST);
        Assert.assertNull(zipArchiveEntry.getExtraField(ZipShort.TEST));
    }

    @Test
    public void testZipArchiveEntryToString() {
        String name = "test.txt";
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name);
        String expectedToString = String.format("%s; size=%d; comp=UNCOMPRESSED; modtime=%s%n", name, 0, DateUtils.formatDate(new Date()));
        Assert.assertEquals(expectedToString, zipArchiveEntry.toString());

        zipArchiveEntry.setSize(1024);
        expectedToString = String.format("%s; size=%d; comp=UNCOMPRESSED; modtime=%s%n", name, 1024, DateUtils.formatDate(new Date()));
        Assert.assertEquals(expectedToString, zipArchiveEntry.toString());

        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        expectedToString = String.format("%s; size=%d; comp=DEFLATED; modtime=%s%n", name, 1024, DateUtils.formatDate(new Date()));
        Assert.assertEquals(expectedToString, zipArchiveEntry.toString());

        zipArchiveEntry.setMethod(ZipEntry.STORED);
        zipArchiveEntry.setSize(0);
        expectedToString = String.format("%s; size=0; comp=STORED; modtime=%s%n", name, DateUtils.formatDate(new Date()));
        Assert.assertEquals(expectedToString, zipArchiveEntry.toString());

        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        expectedToString = String.format("%s; size=%d; comp=STORED; platform=UNIX; modtime=%s%n", name, 1024, DateUtils.formatDate(new Date()));
        Assert.assertEquals(expectedToString, zipArchiveEntry.toString());

        ZipExtraField zipExtraField = new ZipExtraField("Test", "Value".getBytes());
        zipArchiveEntry.addExtraField(zipExtraField);
        expectedToString = String.format("%s; size=%d; comp=UNCOMPRESSED; platform=UNIX; modtime=%s%n%s%n", name, 1024, DateUtils.formatDate(new Date()), zipExtraField.toString());
        Assert.assertEquals(expectedToString, zipArchiveEntry.toString());
    }

    @Test
    public void testZipArchiveEntryGetHashCode() {
        String name1 = "test1.txt";
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry(name1);
        String name2 = "test2.txt";
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry(name2);

        Assert.assertEquals(name1.hashCode(), zipArchiveEntry1.hashCode());
        Assert.assertEquals(name2.hashCode(), zipArchiveEntry2.hashCode());

        Assert.assertNotEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry2.hashCode());
    }

    @Test
    public void testZipArchiveEntryEquals() {
        String name1 = "test1.txt";
        String name2 = "test1.txt";
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry(name1);
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry(name2);

        Assert.assertTrue(zipArchiveEntry1.equals(zipArchiveEntry1));
        Assert.assertTrue(zipArchiveEntry1.equals(zipArchiveEntry2));
        Assert.assertTrue(zipArchiveEntry2.equals(zipArchiveEntry1));

        zipArchiveEntry1.setSize(1024);
        Assert.assertTrue(zipArchiveEntry1.equals(zipArchiveEntry2));
        Assert.assertTrue(zipArchiveEntry2.equals(zipArchiveEntry1));

        zipArchiveEntry2.setSize(2048);
        Assert.assertFalse(zipArchiveEntry1.equals(zipArchiveEntry2));
        Assert.assertFalse(zipArchiveEntry2.equals(zipArchiveEntry1));
    }

    @Test
    public void testZipArchiveEntryMergeExtraFields() throws IOException {
        String name = "test.txt";
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name);

        ZipExtraField zipExtraField1 = new ZipExtraField("Test1", "Value1".getBytes());
        zipArchiveEntry.addExtraField(zipExtraField1);

        ZipExtraField zipExtraField2 = new ZipExtraField("Test2", "Value2".getBytes());
        zipArchiveEntry.addExtraField(zipExtraField2);

        byte[] extraData = new byte[1024];
        Arrays.fill(extraData, (byte) 0x01);
        zipArchiveEntry.setExtra(extraData);

        byte[] extraDataCentral = new byte[512];
        Arrays.fill(extraDataCentral, (byte) 0x02);

        ZipExtraField[] extraFields = {zipExtraField1, zipExtraField2};
        zipArchiveEntry.setCentralDirectoryExtra(extraDataCentral);

        zipArchiveEntry.mergeExtraFields(extraFields, true);

        Assert.assertArrayEquals(Arrays.concat(zipExtraField1.getData(), zipExtraField2.getData()), zipArchiveEntry.getExtraData());
        Assert.assertEquals(extraFields.length, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(2, zipArchiveEntry.getExtraFields().length);
    }

    @Test
    public void testZipArchiveEntryMergeExtraFieldsCentral() throws IOException {
        String name = "test.txt";
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(name);

        ZipExtraField zipExtraField1 = new ZipExtraField("Test1", "Value1".getBytes());
        zipArchiveEntry.addExtraField(zipExtraField1);

        ZipExtraField zipExtraField2 = new ZipExtraField("Test2", "Value2".getBytes());
        zipArchiveEntry.addExtraField(zipExtraField2);

        byte[] extraData = new byte[1024];
        Arrays.fill(extraData, (byte) 0x01);
        zipArchiveEntry.setLocalFileDataExtra(extraData);

        byte[] extraDataLocal = new byte[512];
        Arrays.fill(extraDataLocal, (byte) 0x03);

        ZipExtraField[] extraFields = {zipExtraField1, zipExtraField2};
        zipArchiveEntry.setExtraFields(extraFields);

        zipArchiveEntry.mergeExtraFields(extraFields, false);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        zipArchiveEntry.writeCentralDirectoryData(byteArrayOutputStream);
        byte[] extraDataCentral = byteArrayOutputStream.toByteArray();

        Assert.assertArrayEquals(Arrays.concat(zipExtraField1.getCentralDirectoryData(), zipExtraField2.getCentralDirectoryData()), extraDataCentral);
        Assert.assertEquals(2, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(2, zipArchiveEntry.getLocalFileDataExtra().length);
    }
}