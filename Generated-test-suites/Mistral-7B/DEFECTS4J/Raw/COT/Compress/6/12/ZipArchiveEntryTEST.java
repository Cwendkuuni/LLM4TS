import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.ExtraField;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithZipEntry() throws ZipException {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        assertEquals("test.txt", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithZipArchiveEntry() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        ZipArchiveEntry clone = zipArchiveEntry.clone();
        assertNotSame(zipArchiveEntry, clone);
    }

    @Test
    public void testConstructorWithFile() {
        File file = new File("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, "test.txt");
        assertEquals("test.txt", zipArchiveEntry.getName());
        assertEquals(-1, zipArchiveEntry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodNegativeValue() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        zipArchiveEntry.setMethod(-1);
    }

    @Test
    public void testIsSupportedCompressionMethodWithStored() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test
    public void testIsSupportedCompressionMethodWithDeflated() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testSetMethodAndGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        assertEquals(0, zipArchiveEntry.getInternalAttributes());

        ZipArchiveEntry clonedEntry = new ZipArchiveEntry(zipArchiveEntry);
        clonedEntry.setInternalAttributes(12345);
        assertEquals(12345, clonedEntry.getInternalAttributes());
        assertEquals(0, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributesAndGetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        zipArchiveEntry.setInternalAttributes(12345);
        assertEquals(12345, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        assertEquals(0, zipArchiveEntry.getExternalAttributes());

        ZipArchiveEntry clonedEntry = new ZipArchiveEntry(zipArchiveEntry);
        clonedEntry.setExternalAttributes(12345L);
        assertEquals(12345L, clonedEntry.getExternalAttributes());
        assertEquals(0, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributesAndGetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        zipArchiveEntry.setExternalAttributes(12345L);
        assertEquals(12345L, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        zipArchiveEntry.setUnixMode(0755);
        assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());

        ZipArchiveEntry clonedEntry = new ZipArchiveEntry(zipArchiveEntry);
        clonedEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, clonedEntry.getPlatform());
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetPlatformAndGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        ZipExtraField zipExtraField1 = new ExtraField(ZipShort.STANDARD_INFO_TRAILER, "Test1".getBytes());
        ZipExtraField zipExtraField2 = new ExtraField(ZipShort.COMMENT, "Test2".getBytes());

        zipArchiveEntry.setExtraFields(new ZipExtraField[]{zipExtraField1, zipExtraField2});

        ZipExtraField[] actualExtraFields = zipArchiveEntry.getExtraFields();
        assertEquals(2, actualExtraFields.length);
        assertEquals(ZipShort.STANDARD_INFO_TRAILER, actualExtraFields[0].getHeaderId());
        assertEquals(ZipShort.COMMENT, actualExtraFields[1].getHeaderId());
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        ZipExtraField zipExtraField = new ExtraField(ZipShort.COMMENT, "Test".getBytes());

        zipArchiveEntry.addExtraField(zipExtraField);

        ZipExtraField[] actualExtraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, actualExtraFields.length);
        assertEquals(ZipShort.COMMENT, actualExtraFields[0].getHeaderId());
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        ZipExtraField zipExtraField = new ExtraField(ZipShort.COMMENT, "Test".getBytes());

        zipArchiveEntry.addAsFirstExtraField(zipExtraField);

        ZipExtraField[] actualExtraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, actualExtraFields.length);
        assertEquals(ZipShort.COMMENT, actualExtraFields[0].getHeaderId());
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        ZipExtraField zipExtraField = new ExtraField(ZipShort.COMMENT, "Test".getBytes());
        zipArchiveEntry.addExtraField(zipExtraField);
        zipArchiveEntry.removeExtraField(ZipShort.COMMENT);

        ZipExtraField[] actualExtraFields = zipArchiveEntry.getExtraFields();
        assertEquals(0, actualExtraFields.length);
    }

    @Test
    public void testGetExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        ZipExtraField zipExtraField = new ExtraField(ZipShort.COMMENT, "Test".getBytes());
        zipArchiveEntry.addExtraField(zipExtraField);

        ZipExtraField actualExtraField = zipArchiveEntry.getExtraField(ZipShort.COMMENT);
        assertEquals(zipExtraField, actualExtraField);
    }

    @Test
    public void testSetLocalFileDataExtra() throws ZipException, IOException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        byte[] data = "TestData".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);

        zipArchiveEntry.setExtra(inputStream);

        ZipExtraField[] actualExtraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, actualExtraFields.length);
        ZipExtraField actualExtraField = actualExtraFields[0];
        assertEquals(ZipShort.LOCAL_FILE_DATA, actualExtraField.getHeaderId());
        byte[] actualData = actualExtraField.getData();
        assertArrayEquals(data, actualData);
    }

    @Test
    public void testSetCentralDirectoryExtra() throws ZipException, IOException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        byte[] data = "TestData".getBytes();

        InputStream inputStream = new ByteArrayInputStream(data);
        byte[] centralDirectoryData = IOUtils.toByteArray(inputStream);

        zipArchiveEntry.setCentralDirectoryExtra(centralDirectoryData);

        ZipExtraField[] actualExtraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, actualExtraFields.length);
        ZipExtraField actualExtraField = actualExtraFields[0];
        assertEquals(ZipShort.CENTRAL_DIRECTORY_FILE_HEADER, actualExtraField.getHeaderId());
        byte[] actualData = actualExtraField.getData();
        assertArrayEquals(data, actualData);
    }

    @Test(expected = ZipException.class)
    public void testSetExtraWithInvalidData() throws ZipException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
        byte[] invalidData = new byte[2];
        zipArchiveEntry.setExtra(new ByteArrayInputStream(invalidData));
    }
}