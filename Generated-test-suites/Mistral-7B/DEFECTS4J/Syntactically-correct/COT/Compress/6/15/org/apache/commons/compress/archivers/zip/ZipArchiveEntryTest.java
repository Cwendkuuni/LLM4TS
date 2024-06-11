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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
        Assert.assertNull(zipArchiveEntry.getSize());
        Assert.assertNull(zipArchiveEntry.getTime());
    }

    @Test
    public void testConstructorWithZipEntry() throws IOException {
        String inputFileName = "testInputFile.txt";
        File inputFile = new File(inputFileName);
        ZipEntry zipEntry = new ZipEntry("test.txt");
        zipEntry.setSize(inputFile.length());
        zipEntry.setTime(inputFile.lastModified());
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);

        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
        Assert.assertEquals(inputFile.length(), zipArchiveEntry.getSize().longValue());
        Assert.assertEquals(new Date(inputFile.lastModified()), zipArchiveEntry.getLastModifiedDate());
    }

    @Test
    public void testConstructorWithFile() throws IOException {
        String inputFileName = "testInputFile.txt";
        File inputFile = new File(inputFileName);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(inputFile, "test.txt");

        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
        Assert.assertEquals(inputFile.length(), zipArchiveEntry.getSize().longValue());
        Assert.assertEquals(new Date(inputFile.lastModified()), zipArchiveEntry.getLastModifiedDate());
    }

    @Test
    public void testClone() throws IOException, CloneNotSupportedException {
        String inputFileName = "testInputFile.txt";
        File inputFile = new File(inputFileName);
        ZipArchiveEntry original = new ZipArchiveEntry(inputFile, "test.txt");
        ZipArchiveEntry clone = (ZipArchiveEntry) original.clone();

        Assert.assertEquals(original.getName(), clone.getName());
        Assert.assertEquals(original.getSize().longValue(), clone.getSize().longValue());
        Assert.assertEquals(original.getLastModifiedDate(), clone.getLastModifiedDate());
    }

    @Test
    public void testGetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setUnixMode(0755);
        Assert.assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        zipArchiveEntry.setUnixMode(0644);
        Assert.assertEquals(0644, zipArchiveEntry.getUnixMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUnixModeWithInvalidPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setUnixMode(1);
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_FAT);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetPlatformWithInvalidValue() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        Assert.assertThrows(IllegalArgumentException.class, () -> zipArchiveEntry.setPlatform(2));
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testSetMethodWithInvalidValue() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        Assert.assertThrows(IllegalArgumentException.class, () -> zipArchiveEntry.setMethod(3));
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        Assert.assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test
    public void testIsSupportedCompressionMethodWithUnsupportedMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        Assert.assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setInternalAttributes(0x0201);
        Assert.assertEquals(0x0201, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setInternalAttributes(0x0201);
        Assert.assertEquals(0x0201, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setExternalAttributes(0x0403);
        Assert.assertEquals(0x0403, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setExternalAttributes(0x0403);
        Assert.assertEquals(0x0403, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetAndGetExtraFields() throws IOException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        ZipExtraField extraField1 = new ZipLocalFileData();
        ZipExtraField extraField2 = new ZipLocalFileData();

        extraField1.setIntValue(1);
        extraField2.setIntValue(2);

        zipArchiveEntry.setExtraFields(new ZipExtraField[] {extraField1, extraField2});
        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();

        Assert.assertEquals(2, extraFields.length);
        Assert.assertEquals(extraField1, extraFields[0]);
        Assert.assertEquals(extraField2, extraFields[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetExtraFieldsWithNullArray() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        zipArchiveEntry.setExtraFields(null);
    }

    @Test
    public void testSetAndGetExtraFieldByType() throws IOException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        ZipLocalFileData extraField = new ZipLocalFileData();

        extraField.setIntValue(1);
        zipArchiveEntry.setExtraField(ZipShort.LOCAL_FILE_COMMENT, extraField);

        ZipExtraField extractedExtraField = zipArchiveEntry.getExtraField(ZipShort.LOCAL_FILE_COMMENT);
        Assert.assertEquals(extraField, extractedExtraField);
    }

    @Test(expected = java.util.NoSuchElementException.class)
    public void testGetExtraFieldByTypeWithNonExistentType() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        ZipExtraField extractedExtraField = zipArchiveEntry.getExtraField(ZipShort.LOCAL_FILE_COMMENT);
        Assert.assertNull(extractedExtraField);
    }

    @Test
    public void testSetAndGetExtraFieldsWithMerge() throws IOException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        Map<ZipShort, ZipExtraField> originalExtraFields = new LinkedHashMap<>();
        Map<ZipShort, ZipExtraField> newExtraFields = new LinkedHashMap<>();

        ZipLocalFileData originalExtraField1 = new ZipLocalFileData();
        originalExtraField1.setIntValue(1);
        originalExtraFields.put(ZipShort.LOCAL_FILE_COMMENT, originalExtraField1);

        ZipLocalFileData newExtraField1 = new ZipLocalFileData();
        newExtraField1.setIntValue(2);
        newExtraFields.put(ZipShort.LOCAL_FILE_COMMENT, newExtraField1);

        zipArchiveEntry.setExtraFields(originalExtraFields.values().toArray(new ZipExtraField[0]));
        zipArchiveEntry.addExtraField(newExtraField1);

        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        Assert.assertEquals(2, extraFields.length);

        ZipLocalFileData mergedExtraField = (ZipLocalFileData) extraFields[0];
        Assert.assertEquals(3, mergedExtraField.getIntValue());
    }

    @Test
    public void testSetAndGetLocalFileDataExtra() throws IOException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        byte[] localFileDataExtra = new byte[]{1, 2, 3, 4};

        zipArchiveEntry.setLocalFileDataExtra(localFileDataExtra);
        byte[] extractedLocalFileDataExtra = zipArchiveEntry.getLocalFileDataExtra();

        Assert.assertArrayEquals(localFileDataExtra, extractedLocalFileDataExtra);
    }

    @Test(expected = RuntimeException.class)
    public void testSetExtraWithInvalidData() throws IOException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        byte[] invalidData = new byte[]{1, 2, 3};

        try {
            zipArchiveEntry.setExtra(invalidData);
        } catch (RuntimeException e) {
            Assert.assertEquals("Unable to parse extra data", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testSetAndGetCentralDirectoryExtra() throws IOException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        byte[] centralDirectoryExtra = new byte[]{1, 2, 3, 4};

        zipArchiveEntry.setCentralDirectoryExtra(centralDirectoryExtra);
        byte[] extractedCentralDirectoryExtra = zipArchiveEntry.getCentralDirectoryExtra();

        Assert.assertArrayEquals(centralDirectoryExtra, extractedCentralDirectoryExtra);
    }

    @Test
    public void testGetSize() throws IOException {
        String inputFileName = "testInputFile.txt";
        File inputFile = new File(inputFileName);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(inputFile, "test.txt");

        Assert.assertEquals(inputFile.length(), zipArchiveEntry.getSize().longValue());
    }

    @Test
    public void testGetLastModifiedDate() throws IOException {
        String inputFileName = "testInputFile.txt";
        File inputFile = new File(inputFileName);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(inputFile, "test.txt");

        Assert.assertEquals(new Date(inputFile.lastModified()), zipArchiveEntry.getLastModifiedDate());
    }

    @Test
    public void testEquals() {
        String name1 = "test1.txt";
        String name2 = "test2.txt";
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry(name1);
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry(name2);

        Assert.assertFalse(zipArchiveEntry1.equals(zipArchiveEntry2));

        String sameName = name1;
        ZipArchiveEntry sameNameZipArchiveEntry = new ZipArchiveEntry(sameName);

        Assert.assertTrue(zipArchiveEntry1.equals(sameNameZipArchiveEntry));
    }
}