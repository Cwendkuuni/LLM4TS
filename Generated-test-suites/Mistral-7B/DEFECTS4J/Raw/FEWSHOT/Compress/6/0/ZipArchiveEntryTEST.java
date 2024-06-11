package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.IOUtils;

public class ZipArchiveEntryTest {

    @Test
    public void testCreateAndGetSimpleEntry() {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        assertEquals(name, entry.getName());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
        assertEquals(-1, entry.getMethod());
        assertFalse(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testCreateFromZipEntry() throws IOException {
        String name = "test.txt";
        ZipEntry zipEntry = new ZipEntry(name);
        ZipArchiveEntry archiveEntry = new ZipArchiveEntry(zipEntry);
        assertEquals(name, archiveEntry.getName());
        assertEquals(zipEntry.getSize(), archiveEntry.getSize());
        assertEquals(zipEntry.getTime(), archiveEntry.getTime());
        assertEquals(zipEntry.getMethod(), archiveEntry.getMethod());
        assertEquals(zipEntry.getCompressedSize(), archiveEntry.getCompressedSize());
        assertEquals(zipEntry.getCrc(), archiveEntry.getCrc());
        assertArrayEquals(zipEntry.getExtra(), archiveEntry.getLocalFileDataExtra());
    }

    @Test
    public void testCreateFromZipArchiveEntry() throws IOException {
        String name = "test.txt";
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(name);
        ZipArchiveEntry newEntry = new ZipArchiveEntry(zipEntry);
        assertEquals(name, newEntry.getName());
        assertEquals(zipEntry.getSize(), newEntry.getSize());
        assertEquals(zipEntry.getTime(), newEntry.getTime());
        assertEquals(zipEntry.getMethod(), newEntry.getMethod());
        assertEquals(zipEntry.getCompressedSize(), newEntry.getCompressedSize());
        assertEquals(zipEntry.getCrc(), newEntry.getCrc());
        assertArrayEquals(zipEntry.getExtra(), newEntry.getLocalFileDataExtra());
    }

    @Test(expected = ZipException.class)
    public void testCreateFromInvalidZipEntry() throws IOException {
        String name = "test.txt";
        ZipEntry zipEntry = new ZipEntry(name);
        zipEntry.setSize(-1);
        ZipArchiveEntry archiveEntry = new ZipArchiveEntry(zipEntry);
    }

    @Test
    public void testSetAndGetMethod() {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        entry.setMethod(ZipArchiveEntry.STORED);
        assertEquals(ZipArchiveEntry.STORED, entry.getMethod());
        entry.setMethod(ZipArchiveEntry.DEFLATED);
        assertEquals(ZipArchiveEntry.DEFLATED, entry.getMethod());
        entry.setMethod(-1);
        assertEquals(-1, entry.getMethod());
    }

    @Test
    public void testSetAndGetInternalAttributes() {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        int internalAttributes = 0x20;
        entry.setInternalAttributes(internalAttributes);
        assertEquals(internalAttributes, entry.getInternalAttributes());
    }

    @Test
    public void testSetAndGetExternalAttributes() throws IOException {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        long externalAttributes = 0x123;
        entry.setExternalAttributes(externalAttributes);
        assertEquals(externalAttributes, entry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        String name = "test.txt";        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        int unixMode = 0755;
        entry.setUnixMode(unixMode);
        assertEquals(unixMode, entry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetExtraFields() throws IOException {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        List<ZipExtraField> fields = new ArrayList<>();
        fields.add(new ZipGeneralPurposeCommentField("Test comment"));
        entry.setExtraFields(fields.toArray(new ZipExtraField[0]));
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertEquals(fields.size(), extraFields.length);
        for (int i = 0; i < extraFields.length; i++) {
            assertEquals(fields.get(i).getClass(), extraFields[i].getClass());
            assertEquals(fields.get(i).getData(), extraFields[i].getData());
        }
    }

    @Test
    public void testAddExtraField() throws IOException {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        List<ZipExtraField> fields = new ArrayList<>();
        fields.add(new ZipGeneralPurposeCommentField("Test comment 1"));
        entry.addExtraField(fields.get(0));
        fields.add(new ZipGeneralPurposeCommentField("Test comment 2"));
        entry.addExtraField(fields.get(1));
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertEquals(fields.size(), extraFields.length);
        for (int i = 0; i < extraFields.length; i++) {
            assertEquals(fields.get(i).getClass(), extraFields[i].getClass());
            assertEquals(fields.get(i).getData(), extraFields[i].getData());
        }
    }

    @Test
    public void testAddAsFirstExtraField() throws IOException {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        List<ZipExtraField> fields = new ArrayList<>();
        fields.add(new ZipGeneralPurposeCommentField("Test comment 2"));
        fields.add(new ZipGeneralPurposeCommentField("Test comment 1"));
        entry.addAsFirstExtraField(fields.get(0));
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertEquals(fields.size(), extraFields.length);
        for (int i = 0; i < extraFields.length; i++) {
            assertEquals(fields.get(i).getClass(), extraFields[i].getClass());
            assertEquals(fields.get(i).getData(), extraFields[i].getData());
        }
    }

    @Test
    public void testRemoveExtraField() throws IOException {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        List<ZipExtraField> fields = new ArrayList<>();
        fields.add(new ZipGeneralPurposeCommentField("Test comment 1"));
        entry.addExtraField(fields.get(0));
        fields.add(new ZipGeneralPurposeCommentField("Test comment 2"));
        entry.addExtraField(fields.get(1));
        entry.removeExtraField(ZipShort.COMMENT);
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals(fields.get(0).getClass(), extraFields[0].getClass());
        assertEquals(fields.get(0).getData(), extraFields[0].getData());
    }

    @Test
    public void testGetExtraField() throws IOException {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        List<ZipExtraField> fields = new ArrayList<>();
        fields.add(new ZipGeneralPurposeCommentField("Test comment 1"));
        entry.addExtraField(fields.get(0));
        ZipExtraField extraField = entry.getExtraField(ZipShort.COMMENT);
        assertEquals(fields.get(0).getClass(), extraField.getClass());
        assertEquals(fields.get(0).getData(), extraField.getData());
    }

    @Test
    public void testSetAndGetName() {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        String newName = "newName.txt";
        entry.setName(newName);
        assertEquals(newName, entry.getName());
    }

    @Test
    public void testHashCode() {
        String name1 = "test1.txt";
        String name2 = "test2.txt";
        ZipArchiveEntry entry1 = new ZipArchiveEntry(name1);
        ZipArchiveEntry entry2 = new ZipArchiveEntry(name2);
        assertEquals(name1.hashCode(), entry1.hashCode());
        assertEquals(name2.hashCode(), entry2.hashCode());
        assertEquals(name1.hashCode(), entry1.getName().hashCode());
        assertEquals(name2.hashCode(), entry2.getName().hashCode());
        assertEquals(entry1.hashCode(), entry1.getName().hashCode());
        assertEquals(entry2.hashCode(), entry2.getName().hashCode());
    }

    @Test
    public void testEquals() {
        String name1 = "test1.txt";
        String name2 = "test2.txt";
        ZipArchiveEntry entry1 = new ZipArchiveEntry(name1);
        ZipArchiveEntry entry2 = new ZipArchiveEntry(name2);
        ZipArchiveEntry entry3 = new ZipArchiveEntry(name1);
        assertFalse(entry1.equals(null));
        assertFalse(entry1.equals(new Object()));
        assertTrue(entry1.equals(entry3));
        assertFalse(entry1.equals(entry2));
    }

    @Test
    public void testGetLastModifiedDate() {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        long time = System.currentTimeMillis();
        entry.setTime(time);
        Date lastModifiedDate = entry.getLastModifiedDate();
        assertEquals(lastModifiedDate.getTime(), time);
    }

    @Test
    public void testParseLocalFileDataExtra() throws IOException {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        byte[] extraData = new byte[10];
        ZipExtraField extraField = new ZipGeneralPurposeCommentField("Test comment");
        extraField.setLocalFileDataData(extraData);
        entry.setExtra(extraData);
        ZipExtraField parsedExtraField = entry.getLocalFileDataExtra();
        assertEquals(extraField.getClass(), parsedExtraField.getClass());
        assertEquals(extraField.getData(), parsedExtraField.getData());
    }

    @Test
    public void testParseCentralDirectoryDataExtra() throws IOException {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        byte[] extraData = new byte[10];
        ZipExtraField extraField = new ZipGeneralPurposeCommentField("Test comment");
        extraField.setCentralDirectoryData(extraData);
        entry.setCentralDirectoryExtra(extraData);
        ZipExtraField parsedExtraField = entry.getExtraFields()[0];
        assertEquals(extraField.getClass(), parsedExtraField.getClass());
        assertEquals(extraField.getData(), parsedExtraField.getData());
    }

    @Test
    public void testMergeExtraFields() throws IOException {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        byte[] extraData1 = new byte[10];
        ZipExtraField extraField1 = new ZipGeneralPurposeCommentField("Test comment 1");
        extraField1.setLocalFileDataData(extraData1);
        entry.setExtra(extraData1);

        byte[] extraData2 = new byte[10];
        ZipExtraField extraField2 = new ZipGeneralPurposeCommentField("Test comment 2");
        extraField2.setCentralDirectoryData(extraData2);

        entry.mergeExtraFields(new ZipExtraField[] { extraField2 }, false);
        ZipExtraField parsedExtraField = entry.getExtraFields()[0];
        assertEquals(extraField1.getClass(), parsedExtraField.getClass());
        assertEquals(extraField1.getData(), parsedExtraField.getData());
        assertEquals(extraField2.getClass(), parsedExtraField.getClass());
        assertEquals(extraField2.getData(), parsedExtraField.getData());
    }

    @Test
    public void testParseFromLocalFileData() throws IOException {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        byte[] extraData = new byte[10];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(extraData);
        ZipExtraField extraField = new ZipGeneralPurposeCommentField();
        extraField.parseFromLocalFileData(inputStream, 0, extraData.length);
        assertEquals(extraField.getData(), "Test comment");
        entry.setLocalFileDataExtra(extraData);
        ZipExtraField parsedExtraField = entry.getExtraFields()[0];
        assertEquals(extraField.getClass(), parsedExtraField.getClass());
        assertEquals(extraField.getData(), parsedExtraField.getData());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws IOException {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        byte[] extraData = new byte[10];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(extraData);
        ZipExtraField extraField = new ZipGeneralPurposeCommentField();
        extraField.parseFromCentralDirectoryData(inputStream, 0, extraData.length);
        assertEquals(extraField.getData(), "Test comment");
        entry.setCentralDirectoryExtra(extraData);
        ZipExtraField parsedExtraField = entry.getExtraFields()[0];
        assertEquals(extraField.getClass(), parsedExtraField.getClass());
        assertEquals(extraField.getData(), parsedExtraField.getData());
    }
}