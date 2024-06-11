import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.utils.ExtraFieldUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;

public class ZipArchiveEntryTest {

    private static final String TEST_FILE = "test.txt";
    private static final String TEST_ENTRY_NAME = "test.txt";

    @Test
    public void testZipArchiveEntry() throws IOException, ZipException {
        // Create ZipArchiveEntry from ZipEntry
        ZipEntry zipEntry = new ZipEntry(TEST_ENTRY_NAME);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals(TEST_ENTRY_NAME, zipArchiveEntry.getName());
        Assert.assertNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());
        Assert.assertEquals(0, zipArchiveEntry.getInternalAttributes());
        Assert.assertEquals(0, zipArchiveEntry.getPlatform());
        Assert.assertEquals(0, zipArchiveEntry.getExternalAttributes());

        // Create ZipArchiveEntry from File
        File testFile = new File(TEST_FILE);
        zipArchiveEntry = new ZipArchiveEntry(testFile, TEST_ENTRY_NAME);
        Assert.assertEquals(TEST_ENTRY_NAME, zipArchiveEntry.getName());
        Assert.assertNotNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(0, zipArchiveEntry.getMethod());
        Assert.assertEquals(0, zipArchiveEntry.getInternalAttributes());
        Assert.assertEquals(0, zipArchiveEntry.getPlatform());
        Assert.assertEquals(testFile.length(), zipArchiveEntry.getSize());
        Assert.assertEquals(testFile.lastModified(), zipArchiveEntry.getTime());

        // Test setting methods
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());

        zipArchiveEntry.setInternalAttributes(ZipEntry.FLAG_UNIX);
        Assert.assertEquals(ZipEntry.FLAG_UNIX, zipArchiveEntry.getInternalAttributes());

        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());

        zipArchiveEntry.setExternalAttributes(1L);
        Assert.assertEquals(1L, zipArchiveEntry.getExternalAttributes());

        zipArchiveEntry.setUnixMode(0755);
        Assert.assertEquals(0755, zipArchiveEntry.getUnixMode());

        // Test cloning
        ZipArchiveEntry cloned = zipArchiveEntry.clone();
        Assert.assertEquals(zipArchiveEntry, cloned);
        Assert.assertNotSame(zipArchiveEntry, cloned);
        Assert.assertEquals(zipArchiveEntry.getName(), cloned.getName());
        Assert.assertEquals(zipArchiveEntry.getExtraFields(), cloned.getExtraFields());
        Assert.assertEquals(zipArchiveEntry.getMethod(), cloned.getMethod());
        Assert.assertEquals(zipArchiveEntry.getInternalAttributes(), cloned.getInternalAttributes());
        Assert.assertEquals(zipArchiveEntry.getPlatform(), cloned.getPlatform());
        Assert.assertEquals(zipArchiveEntry.getExternalAttributes(), cloned.getExternalAttributes());

        // Test setting extra fields
        ZipExtraField testExtraField = new ZipExtraField("TestExtraField", new byte[0]);
        zipArchiveEntry.addExtraField(testExtraField);
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        List<ZipExtraField> extraFields = Arrays.asList(zipArchiveEntry.getExtraFields());
        Assert.assertTrue(extraFields.contains(testExtraField));

        // Test setting and getting extra fields with header id
        ZipShort testHeaderId = new ZipShort("TestHeaderId");
        ZipExtraField testExtraFieldWithId = new ZipExtraField(testHeaderId, new byte[0]);
        zipArchiveEntry.addExtraField(testExtraFieldWithId);
        Assert.assertEquals(1, zipArchiveEntry.getExtraFields().length);
        Assert.assertEquals(testExtraFieldWithId, zipArchiveEntry.getExtraField(testHeaderId));

        // Test merging extra fields
        ZipExtraField testExtraFieldToMerge = new ZipExtraField("TestExtraFieldToMerge", new byte[0]);
        ZipExtraField testExtraFieldAlreadyExists = new ZipExtraField("TestExtraFieldAlreadyExists", new byte[0]);
        Map<ZipShort, ZipExtraField> originalExtraFields = new LinkedHashMap<>();
        originalExtraFields.put(testHeaderId, testExtraFieldAlreadyExists);
        zipArchiveEntry.setExtraFields(new ZipExtraField[]{testExtraFieldToMerge});
        zipArchiveEntry.mergeExtraFields(new ZipExtraField[]{testExtraFieldToMerge}, true);
        Assert.assertEquals(2, zipArchiveEntry.getExtraFields().length);
        Assert.assertTrue(Arrays.asList(zipArchiveEntry.getExtraFields()).contains(testExtraFieldToMerge));
        Assert.assertTrue(Arrays.asList(zipArchiveEntry.getExtraFields()).contains(testExtraFieldAlreadyExists));

        // Test merging extra fields with different locations
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        testExtraFieldToMerge.writeTo(byteArrayOutputStream);
        byte[] testExtraFieldToMergeBytes = byteArrayOutputStream.toByteArray();

        byteArrayOutputStream = new ByteArrayOutputStream();
        testExtraFieldAlreadyExists.writeTo(byteArrayOutputStream);
        byte[] testExtraFieldAlreadyExistsBytes = byteArrayOutputStream.toByteArray();

        zipArchiveEntry.setExtraFields(new ZipExtraField[]{testExtraFieldToMerge, testExtraFieldAlreadyExists});
        zipArchiveEntry.mergeExtraFields(new ZipExtraField[]{testExtraFieldToMerge, testExtraFieldAlreadyExists}, false);
        Assert.assertEquals(2, zipArchiveEntry.getExtraFields().length);
        Assert.assertTrue(Arrays.asList(zipArchiveEntry.getExtraFields()).contains(testExtraFieldToMerge));
        Assert.assertTrue(Arrays.asList(zipArchiveEntry.getExtraFields()).contains(testExtraFieldAlreadyExists));
        Assert.assertArrayEquals(testExtraFieldToMergeBytes, zipArchiveEntry.getLocalFileDataExtra());
        Assert.assertArrayEquals(testExtraFieldAlreadyExistsBytes, zipArchiveEntry.getCentralDirectoryExtra());

        // Test setting extra data
        byte[] testExtraData = new byte[]{"TestExtraData".getBytes()};
        zipArchiveEntry.setExtra(testExtraData);
        Assert.assertArrayEquals(testExtraData, zipArchiveEntry.getLocalFileDataExtra());
        Assert.assertArrayEquals(testExtraData, zipArchiveEntry.getCentralDirectoryExtra());

        // Test parsing extra data
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testExtraData);
        ZipExtraField parsedTestExtraField = ExtraFieldUtils.parse(byteArrayInputStream).next();
        Assert.assertEquals("TestExtraData", parsedTestExtraField.getName());

        // Test parsing extra data with header id
        ZipShort testHeaderIdToParse = new ZipShort("TestHeaderIdToParse");
        byte[] testExtraDataWithHeaderId = new byte[17];
        System.arraycopy(testHeaderIdToParse.getData(), 0, testExtraDataWithHeaderId, 0, testHeaderIdToParse.getData().length);
        System.arraycopy(testExtraData, 0, testExtraDataWithHeaderId, testHeaderIdToParse.getData().length, testExtraData.length);
        ByteArrayInputStream byteArrayInputStreamWithHeaderId = new ByteArrayInputStream(testExtraDataWithHeaderId);
        ZipExtraField parsedTestExtraFieldWithHeaderId = ExtraFieldUtils.parse(byteArrayInputStreamWithHeaderId).next();
        Assert.assertEquals(testHeaderIdToParse, parsedTestExtraFieldWithHeaderId.getHeaderId());
        Assert.assertEquals("TestExtraData", parsedTestExtraFieldWithHeaderId.getName());
    }
}