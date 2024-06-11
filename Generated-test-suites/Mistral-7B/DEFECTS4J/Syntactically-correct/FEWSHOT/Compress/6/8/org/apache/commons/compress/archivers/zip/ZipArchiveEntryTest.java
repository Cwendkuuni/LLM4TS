package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.io.ByteArrayInputStream;
import org.apache.commons.compress.utils.io.ByteArrayOutputStream;
import org.apache.commons.compress.utils.io.ByteStreams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZipArchiveEntryTest {
    @Test
    public void testCreateEmptyEntry() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertNull(entry.getMethod());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getPlatform());
        assertNull(entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
        assertEquals("test.txt", entry.getName());
        assertEquals(0, entry.getSize());
        assertEquals(0L, entry.getTime());
    }

    @Test
    public void testCreateEntryWithInputStream() {
        String entryName = "test.txt";
        File inputFile = new File("test_file.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(inputFile, entryName);
        assertTrue(entry.isDirectory() ^ inputFile.isDirectory());
        assertEquals(inputFile.length(), entry.getSize());
        assertEquals(inputFile.lastModified(), entry.getTime());
        assertNull(entry.getMethod());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getPlatform());
        assertNull(entry.getExternalAttributes());
        assertNull(entry.getExtraFields());
        assertEquals(entryName, entry.getName());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUnixModeWithInvalidMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        entry.setUnixMode(4294967295);
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());

        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField testExtra = new TestZipExtraField("TestExtraField");

        entry.setExtraFields(new ZipExtraField[] { testExtra });
        assertEquals(1, entry.getExtraFields().length);
        assertTrue(entry.getExtraFields()[0] instanceof TestZipExtraField);

        entry.setExtraFields(new ZipExtraField[0]);
        assertEquals(0, entry.getExtraFields().length);
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField testExtra1 = new TestZipExtraField("TestExtraField1");
        ZipExtraField testExtra2 = new TestZipExtraField("TestExtraField2");

        entry.addExtraField(testExtra1);
        entry.addExtraField(testExtra2);

        assertEquals(2, entry.getExtraFields().length);
        assertTrue(entry.getExtraFields()[0] instanceof TestZipExtraField);
        assertTrue(entry.getExtraFields()[1] instanceof TestZipExtraField);
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField testExtra1 = new TestZipExtraField("TestExtraField1");
        ZipExtraField testExtra2 = new TestZipExtraField("TestExtraField2");

        entry.addAsFirstExtraField(testExtra2);
        entry.addAsFirstExtraField(testExtra1);

        assertEquals(2, entry.getExtraFields().length);
        assertTrue(entry.getExtraFields()[0] instanceof TestZipExtraField);
        assertTrue(entry.getExtraFields()[1] instanceof TestZipExtraField);
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField testExtra1 = new TestZipExtraField("TestExtraField1");
        ZipExtraField testExtra2 = new TestZipExtraField("TestExtraField2");

        entry.addExtraField(testExtra1);
        entry.addExtraField(testExtra2);

        assertEquals(2, entry.getExtraFields().length);

        entry.removeExtraField(new ZipShort(testExtra1.getHeaderId()));
        assertEquals(1, entry.getExtraFields().length);
        assertFalse(entry.getExtraFields()[0] instanceof TestZipExtraField);
    }

    @Test
    public void testGetExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ZipExtraField testExtra = new TestZipExtraField("TestExtraField");
        entry.addExtraField(testExtra);

        assertEquals(testExtra, entry.getExtraField(new ZipShort(testExtra.getHeaderId())));
        assertNull(entry.getExtraField(new ZipShort(42)));
    }

    @Test
    public void testSetExtra() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ZipExtraField testExtra = new TestZipExtraField("TestExtraField");
        testExtra.setExtraData(new byte[] { 1, 2, 3 });
        testExtra.writeTo(output);

        entry.setExtra(output.toByteArray());

        assertEquals(1, entry.getExtraFields().length);
        assertTrue(entry.getExtraFields()[0] instanceof TestZipExtraField);
        assertArrayEquals(new byte[] { 1, 2, 3 }, entry.getExtraFields()[0].getLocalFileDataData());
    }

    @Test
    public void testSetCentralDirectoryExtra() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ZipExtraField testExtra = new TestZipExtraField("TestExtraField");
        testExtra.setExtraData(new byte[] { 1, 2, 3 });
        testExtra.writeToCentralDirectory(output);

        entry.setCentralDirectoryExtra(output.toByteArray());

        assertEquals(1, entry.getExtraFields().length);
        assertTrue(entry.getExtraFields()[0] instanceof TestZipExtraField);
        assertArrayEquals(new byte[] { 1, 2, 3 }, entry.getExtraFields()[0].getCentralDirectoryData());
    }

    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(false, entry.isSupportedCompressionMethod());

        entry.setMethod(ZipEntry.STORED);
        assertEquals(true, entry.isSupportedCompressionMethod());

        entry.setMethod(-1);
        assertEquals(false, entry.isSupportedCompressionMethod());
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(-1, entry.getMethod());

        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());

        entry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
        assertEquals(-1, entry.getMethod());

        entry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, entry.getMethod());

        entry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, entry.getMethod());

        entry.setMethod(42);
        assertEquals(-1, entry.getMethod());
    }

    @Test
    public void testClone() throws CloneNotSupportedException, IOException {
        ZipArchiveEntry originalEntry = new ZipArchiveEntry("test.txt");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ZipExtraField testExtra = new TestZipExtraField("TestExtraField");
        testExtra.setExtraData(new byte[] { 1, 2, 3 });
        testExtra.writeTo(output);
        originalEntry.setExtraFields(new ZipExtraField[] { testExtra });

        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) originalEntry.clone();

        assertEquals(originalEntry.getSize(), clonedEntry.getSize());
        assertEquals(originalEntry.getTime(), clonedEntry.getTime());
        assertEquals(originalEntry.getMethod(), clonedEntry.getMethod());
        assertEquals(originalEntry.getPlatform(), clonedEntry.getPlatform());
        assertEquals(originalEntry.getInternalAttributes(), clonedEntry.getInternalAttributes());
        assertEquals(originalEntry.getExternalAttributes(), clonedEntry.getExternalAttributes());
        assertArrayEquals(originalEntry.getLocalFileDataExtra(), clonedEntry.getLocalFileDataExtra());

        ZipExtraField[] extraFieldsOriginal = originalEntry.getExtraFields();
        ZipExtraField[] extraFieldsCloned = clonedEntry.getExtraFields();

        for (int i = 0; i < extraFieldsOriginal.length; i++) {
            ZipExtraField extraFieldOriginal = extraFieldsOriginal[i];
            ZipExtraField extraFieldCloned = extraFieldsCloned[i];

            assertEquals(extraFieldOriginal.getHeaderId(), extraFieldCloned.getHeaderId());
            assertEquals(extraFieldOriginal.getClass().getName(), extraFieldCloned.getClass().getName());
            assertArrayEquals(extraFieldOriginal.getLocalFileDataData(), extraFieldCloned.getLocalFileDataData());
            assertArrayEquals(extraFieldOriginal.getCentralDirectoryData(), extraFieldCloned.getCentralDirectoryData());
        }
    }

    static class TestZipExtraField extends ZipExtraField {
        public TestZipExtraField(String name) {
            super(new ZipShort(1), new ZipShort(1), name.getBytes());
        }

        @Override
        public int getHeaderId() {
            return 1;
        }

        @Override
        public void parseFromLocalFileData(byte[] data, int offset, int length) throws IOException {
            // Nothing to do here
        }

        @Override
        public void parseFromCentralDirectoryData(byte[] data, int offset, int length) throws IOException {
            // Nothing to do here
        }

        @Override
        public void writeTo(OutputStream out) throws IOException {
            // Nothing to do here
        }

        @Override
        public void writeToCentralDirectory(OutputStream out) throws IOException {
            // Nothing to do here
        }
    }
}

class TestZipExtraField extends ZipExtraField {
    // ...
}