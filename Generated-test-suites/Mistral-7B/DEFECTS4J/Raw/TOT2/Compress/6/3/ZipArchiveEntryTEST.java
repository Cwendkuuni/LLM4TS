import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testCreateEntryWithName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testCreateEntryWithFile() {
        File testFile = new File("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(testFile, "test.txt");
        assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testClone() throws CloneNotSupportedException, ZipException {
        ZipArchiveEntry original = new ZipArchiveEntry("test.txt");
        original.setMethod(ZipEntry.STORED);
        original.setUnixMode(0755);
        original.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        ZipArchiveEntry clone = (ZipArchiveEntry) original.clone();
        assertEquals(original.getName(), clone.getName());
        assertEquals(original.getMethod(), clone.getMethod());
        assertEquals(original.getUnixMode(), clone.getUnixMode());
        assertEquals(original.getPlatform(), clone.getPlatform());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodNegative() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(-1);
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(0755);
        assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUnixModeInvalid() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(-1);
    }

    @Test
    public void testSetMethodAndPlatform() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetMethodAndPlatformUnsupported() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        zipArchiveEntry.setMethod(ZipEntry.STORED);
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setInternalAttributes(1234);
        assertEquals(1234, zipArchiveEntry.getInternalAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInternalAttributesNegative() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setInternalAttributes(-1);
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExternalAttributes(123456789L);
        assertEquals(123456789L, zipArchiveEntry.getExternalAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetExternalAttributesNegative() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExternalAttributes(-1);
    }

    @Test
    public void testSetExtraFieldsEmpty() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] extraDataEmpty = new byte[0];
        zipArchiveEntry.setExtra(extraDataEmpty);
        assertTrue(zipArchiveEntry.getExtraFields().length == 0);
    }

    @Test
    public void testSetExtraFields() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField zipExtraField1 = new ZipExtendedInformation("USER_comment", "Test comment 1");
        ZipExtraField zipExtraField2 = new ZipExtendedInformation("CREATION_DATE", new Date());
        ZipExtraField[] zipExtraFields = {zipExtraField1, zipExtraField2};
        byte[] extraData = ExtraFieldUtils.toBytes(zipExtraFields);
        zipArchiveEntry.setExtra(extraData);
        ZipExtraField[] actualExtraFields = zipArchiveEntry.getExtraFields();
        assertArrayEquals(zipExtraFields, actualExtraFields);
    }

    @Test(expected = ZipException.class)
    public void testSetExtraFieldsInvalidData() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] invalidData = new byte[]{'A', 'B', 'C'};
        zipArchiveEntry.setExtra(invalidData);
    }

    @Test
    public void testAddExtraField() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField zipExtraField1 = new ZipExtendedInformation("USER_comment", "Test comment 1");
        zipArchiveEntry.addExtraField(zipExtraField1);
        ZipExtraField[] actualExtraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, actualExtraFields.length);
        assertEquals(zipExtraField1, actualExtraFields[0]);
    }

    @Test
    public void testAddExtraFieldWithSameType() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField zipExtraField1 = new ZipExtendedInformation("USER_comment", "Test comment 1");
        ZipExtraField zipExtraField2 = new ZipExtendedInformation("USER_comment", "Test comment 2");
        zipArchiveEntry.addExtraField(zipExtraField1);
        zipArchiveEntry.addExtraField(zipExtraField2);
        ZipExtraField[] actualExtraFields = zipArchiveEntry.getExtraFields();
        assertEquals(2, actualExtraFields.length);
        assertEquals(zipExtraField1, actualExtraFields[0]);
        assertEquals(zipExtraField2, actualExtraFields[1]);
    }

    @Test
    public void testRemoveExtraField() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField zipExtraField1 = new ZipExtendedInformation("USER_comment", "Test comment 1");
        ZipExtraField zipExtraField2 = new ZipExtendedInformation("USER_comment", "Test comment 2");
        zipArchiveEntry.addExtraField(zipExtraField1);
        zipArchiveEntry.addExtraField(zipExtraField2);
        zipArchiveEntry.removeExtraField(ZipShort.USR_NAME);
        ZipExtraField[] actualExtraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, actualExtraFields.length);
        assertEquals(zipExtraField2, actualExtraFields[0]);
    }

    @Test
    public void testMergeExtraFields() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField zipExtraField1 = new ZipExtendedInformation("USR_NAME", "Test comment 1");
        ZipExtraField zipExtraField2 = new ZipExtendedInformation("CREATION_DATE", new Date());
        ZipExtraField zipExtraField3 = new ZipExtendedInformation("USR_NAME", "Test comment 2");
        byte[] extraData = ExtraFieldUtils.toBytes(new ZipExtraField[]{zipExtraField1, zipExtraField3});
        zipArchiveEntry.setExtra(extraData);
        ZipExtraField[] actualExtraFields = zipArchiveEntry.getExtraFields();
        assertEquals(2, actualExtraFields.length);
        assertEquals(zipExtraField1, actualExtraFields[0]);
        assertEquals(zipExtraField3, actualExtraFields[1]);
    }

    @Test(expected = ZipException.class)
    public void testMergeExtraFieldsNegativeIndex() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField zipExtraField1 = new ZipExtendedInformation("USR_NAME", "Test comment 1");
        ZipExtraField zipExtraField2 = new ZipExtendedInformation("CREATION_DATE", new Date());
        byte[] extraData = ExtraFieldUtils.toBytes(new ZipExtraField[]{zipExtraField1, zipExtraField2});
        zipArchiveEntry.setExtra(extraData);
        zipArchiveEntry.mergeExtraFields(new ZipExtraField[]{}, -1);
    }

    @Test
    public void testMergeExtraFieldsOverwriteSameType() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField zipExtraField1 = new ZipExtendedInformation("USR_NAME", "Test comment 1");
        ZipExtraField zipExtraField2 = new ZipExtendedInformation("USR_NAME", "Test comment 2");
        byte[] extraData = ExtraFieldUtils.toBytes(new ZipExtraField[]{zipExtraField1, zipExtraField2});
        zipArchiveEntry.setExtra(extraData);
        ZipExtraField[] actualExtraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, actualExtraFields.length);
        assertEquals(zipExtraField2, actualExtraFields[0]);
    }

    @Test
    public void testMergeLocalFileDataData() throws IOException, ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[0];
        ZipExtraField zipExtraField1 = new ZipExtendedInformation("USR_NAME", "Test comment 1");
        byte[] localExtraData1 = zipExtraField1.getLocalFileDataData();
        zipArchiveEntry.setExtra(extraData);
        zipArchiveEntry.mergeExtraFields(new ZipExtraField[]{zipExtraField1}, true);
        byte[] mergedExtraData = zipArchiveEntry.getLocalFileDataExtra();
        byte[] expectedMergedExtraData = new byte[localExtraData1.length];
        System.arraycopy(localExtraData1, 0, expectedMergedExtraData, 0, localExtraData1.length);
        assertArrayEquals(expectedMergedExtraData, mergedExtraData);
    }

    @Test
    public void testMergeCentralDirectoryData() throws IOException, ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] extraData = new byte[0];
        ZipExtraField zipExtraField1 = new ZipExtendedInformation("USR_NAME", "Test comment 1");
        byte[] centralDirectoryExtraData1 = zipExtraField1.getCentralDirectoryData();
        zipArchiveEntry.setCentralDirectoryExtra(centralDirectoryExtraData1);
        zipArchiveEntry.mergeExtraFields(new ZipExtraField[]{zipExtraField1}, false);
        byte[] mergedExtraData = zipArchiveEntry.getLocalFileDataExtra();
        byte[] expectedMergedExtraData = new byte[centralDirectoryExtraData1.length];
        System.arraycopy(centralDirectoryExtraData1, 0, expectedMergedExtraData, 0, centralDirectoryExtraData1.length);
        assertArrayEquals(expectedMergedExtraData, mergedExtraData);
    }

    @Test
    public void testGetName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", zipArchiveEntry.getName());
        zipArchiveEntry.setName("test2.txt");
        assertEquals("test2.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testIsDirectory() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertFalse(zipArchiveEntry.isDirectory());
        zipArchiveEntry.setName("test/");
        assertTrue(zipArchiveEntry.isDirectory());
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test.txt");
        assertEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry2.hashCode());
        zipArchiveEntry2.setMethod(ZipEntry.DEFLATED);
        assertNotEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry2.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("test.txt");
        assertTrue(zipArchiveEntry1.equals(zipArchiveEntry2));
        zipArchiveEntry2.setMethod(ZipEntry.DEFLATED);
        assertFalse(zipArchiveEntry1.equals(zipArchiveEntry2));
        zipArchiveEntry2 = new ZipArchiveEntry("test.txt");
        zipArchiveEntry2.setName("test2.txt");
        assertFalse(zipArchiveEntry1.equals(zipArchiveEntry2));
    }

    @Test
    public void testToString() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals("ZipArchiveEntry[name=test.txt]", zipArchiveEntry.toString());
    }

    @Test
    public void testGetLastModifiedDate() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Date lastModifiedDate = new Date();
        zipArchiveEntry.setTime(lastModifiedDate.getTime());
        assertEquals(lastModifiedDate, zipArchiveEntry.getLastModifiedDate());
    }
}