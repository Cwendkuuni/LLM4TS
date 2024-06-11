package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test file for ZipArchiveEntry class.
 */
public class ZipArchiveEntryTest {

    private static final byte[] STORED_DATA = "Test data for stored method.".getBytes();
    private static final byte[] DEFLATED_DATA = "Test data for deflated method.".getBytes();

    @SuppressWarnings("unchecked")
    private static final ZipExtraField[] EMPTY_EXTRA_FIELDS = new ZipExtraField[0];

    @SuppressWarnings("unchecked")
    private static final ZipExtraField[] EXTRA_FIELDS = new ZipExtraField[] {
            new ZipExtendedInfo("local extra field"),
            new ZipExtendedInfo("central directory extra field")
    };

    /**
     * Test for default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        assertNull(zipArchiveEntry.getName());
        assertEquals(0, zipArchiveEntry.getSize());
        assertEquals(0, zipArchiveEntry.getMethod());
        assertEquals(0, zipArchiveEntry.getInternalAttributes());
        assertEquals(0, zipArchiveEntry.getExternalAttributes());
        assertNull(zipArchiveEntry.getExtraFields());
    }

    /**
     * Test for constructor with name.
     */
    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("name");
        assertEquals("name", zipArchiveEntry.getName());
        assertEquals(0, zipArchiveEntry.getSize());
        assertEquals(0, zipArchiveEntry.getMethod());
        assertEquals(0, zipArchiveEntry.getInternalAttributes());
        assertEquals(0, zipArchiveEntry.getExternalAttributes());
        assertNull(zipArchiveEntry.getExtraFields());
    }

    /**
     * Test for constructor with ZipEntry.
     */
    @Test
    public void testConstructorWithZipEntry() throws IOException {
        String entryName = "entryName";
        byte[] extraData = new byte[10];
        ZipEntry zipEntry = new ZipEntry(entryName);
        zipEntry.setExtra(extraData);
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        assertEquals(entryName, zipArchiveEntry.getName());
        assertEquals(zipEntry.getSize(), zipArchiveEntry.getSize());
        assertEquals(zipEntry.getMethod(), zipArchiveEntry.getMethod());
        assertEquals(zipEntry.getInternalAttributes(), zipArchiveEntry.getInternalAttributes());
        assertEquals(zipEntry.getExternalAttributes(), zipArchiveEntry.getExternalAttributes());
        assertEquals(EMPTY_EXTRA_FIELDS, zipArchiveEntry.getExtraFields());
    }

    /**
     * Test for constructor with extra fields.
     */
    @Test
    public void testConstructorWithExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("name");
        zipArchiveEntry.setExtraFields(EXTRA_FIELDS);
        assertEquals("name", zipArchiveEntry.getName());
        assertEquals(0, zipArchiveEntry.getSize());
        assertEquals(0, zipArchiveEntry.getMethod());
        assertEquals(0, zipArchiveEntry.getInternalAttributes());
        assertEquals(0, zipArchiveEntry.getExternalAttributes());
        assertEquals(EXTRA_FIELDS, zipArchiveEntry.getExtraFields());
    }

    /**
     * Test for constructor with File.
     */
    @Test
    public void testConstructorWithFile() {
        File file = new File("testFile");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, "entryName");
        assertEquals("entryName", zipArchiveEntry.getName());
        assertEquals(file.length(), zipArchiveEntry.getSize());
        assertEquals(0, zipArchiveEntry.getMethod());
        assertEquals(0, zipArchiveEntry.getInternalAttributes());
        assertEquals(0, zipArchiveEntry.getExternalAttributes());
        assertNull(zipArchiveEntry.getExtraFields());
    }

    /**
     * Test for setting and getting the compression method.
     */
    @Test
    public void testSettingAndGettingTheCompressionMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("name");
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, zipArchiveEntry.getMethod());
    }

    /**
     * Test for setting and getting the internal attributes.
     */
    @Test
    public void testSettingAndGettingTheInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("name");
        zipArchiveEntry.setInternalAttributes(ZipEntry.FILE_FLAG);
        assertEquals(ZipEntry.FILE_FLAG, zipArchiveEntry.getInternalAttributes());
    }

    /**
     * Test for setting and getting the external attributes.
     */
    @Test
    public void testSettingAndGettingTheExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("name");
        zipArchiveEntry.setExternalAttributes(ZipEntry.FILE_UNCHANGED);
        assertEquals(ZipEntry.FILE_UNCHANGED, zipArchiveEntry.getExternalAttributes());
    }

    /**
     * Test for setting and getting the name.
     */
    @Test
    public void testSettingAndGettingTheName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("name1");
        zipArchiveEntry.setName("name2");
        assertEquals("name2", zipArchiveEntry.getName());
    }

    /**
     * Test for setting and getting the platform.
     */
    @Test
    public void testSettingAndGettingThePlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("name");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    /**
     * Test for setting and getting the extra fields.
     */
    @Test
    public void testSettingAndGettingTheExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("name");
        ZipExtraField extraField = new ZipExtendedInfo("extraFieldName");
        zipArchiveEntry.addExtraField(extraField);
        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals("extraFieldName", extraFields[0].getName());
    }

    /**
     * Test for setting and getting the local file data extra.
     */
    @Test
    public void testSettingAndGettingTheLocalFileDataExtra() throws IOException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("name");
        byte[] extraData = new byte[10];
        zipArchiveEntry.setExtra(extraData);
        byte[] localExtraData = zipArchiveEntry.getLocalFileDataExtra();
        assertEquals(extraData, localExtraData);
    }

    /**
     * Test for setting and getting the central directory extra.
     */
    @Test
    public void testSettingAndGettingTheCentralDirectoryExtra() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("name");
        ZipExtraField[] extraFields = new ZipExtraField[] {
                new ZipExtendedInfo("centralDirectoryExtraFieldName")
        };
        zipArchiveEntry.setExtraFields(extraFields);
        byte[] centralDirectoryExtraData = zipArchiveEntry.getCentralDirectoryExtra();
        assertEquals(ExtraFieldUtils.mergeCentralDirectoryData(extraFields), centralDirectoryExtraData);
    }

    /**
     * Test for getting the hashCode.
     */
    @Test
    public void testGettingTheHashCode() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("name1");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("name2");
        assertEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry1.hashCode());
        assertEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry1.getName().hashCode());
        assertNotEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry2.hashCode());
        assertNotEquals(zipArchiveEntry1.hashCode(), zipArchiveEntry2.getName().hashCode());
    }

    /**
     * Test for equals method.
     */
    @Test
    public void testEqualsMethod() {
        ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("name1");
        ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry("name1");
        ZipArchiveEntry zipArchiveEntry3 = new ZipArchiveEntry("name2");
        assertTrue(zipArchiveEntry1.equals(zipArchiveEntry1));
        assertTrue(zipArchiveEntry1.equals(zipArchiveEntry2));
        assertFalse(zipArchiveEntry1.equals(zipArchiveEntry3));
        assertFalse(zipArchiveEntry1.equals(null));
    }

    /**
     * Test for cloning an instance.
     */
    @Test
    public void testCloningAnInstance() throws IOException, CloneNotSupportedException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("name");
        zipArchiveEntry.setExtraFields(EXTRA_FIELDS);
        ZipArchiveEntry clonedZipArchiveEntry = (ZipArchiveEntry) zipArchiveEntry.clone();
        assertEquals("name", clonedZipArchiveEntry.getName());
        assertEquals(0, clonedZipArchiveEntry.getSize());
        assertEquals(0, clonedZipArchiveEntry.getMethod());
        assertEquals(0, clonedZipArchiveEntry.getInternalAttributes());
        assertEquals(0, clonedZipArchiveEntry.getExternalAttributes());
        assertEquals(EXTRA_FIELDS, clonedZipArchiveEntry.getExtraFields());
    }

    /**
     * Test for setting and getting compressed data with STORED method.
     */
    @Test
    public void testSettingAndGettingCompressedDataWithStoredMethod() throws IOException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("name");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.write(STORED_DATA, outputStream);
        zipArchiveEntry.setData(outputStream.toByteArray());
        byte[] storedData = zipArchiveEntry.getData();
        assertEquals(STORED_DATA, storedData);
    }

    /**
     * Test for setting and getting compressed data with DEFLATED method.
     */
    @Test
    public void testSettingAndGettingCompressedDataWithDeflatedMethod() throws IOException {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("name");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.write(DEFLATED_DATA, outputStream);
        byte[] deflatedData = IOUtils.toByteArray(new ByteArrayInputStream(DEFLATED_DATA));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        zipArchiveEntry.setData(deflatedData);
        byte[] storedData = zipArchiveEntry.getData();
        assertEquals(deflatedData, storedData);
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        zipArchiveEntry.writeData(outputStream2);
        byte[] compressedData = outputStream2.toByteArray();
        assertEquals(deflatedData, compressedData);
    }
}