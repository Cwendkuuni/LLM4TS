package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstruction() throws IOException {
        String name = "test.txt";
        ZipArchiveEntry entry = new ZipArchiveEntry(name);
        Assert.assertEquals(name, entry.getName());
        Assert.assertEquals(-1, entry.getMethod());
        Assert.assertEquals(0, entry.getInternalAttributes());
        Assert.assertEquals(0, entry.getPlatform());
        Assert.assertEquals(0, entry.getExternalAttributes());
        Assert.assertNull(entry.getExtraFields());
        Assert.assertNull(entry.getName());
    }

    @Test
    public void testZipArchiveEntryConstructorFromZipEntry() throws IOException {
        String name = "test.txt";
        byte[] extraData = new byte[]{(byte) 0x50, (byte) 0x4b, (byte) 0x03, (byte) 0x04};
        ZipArchiveEntry originalEntry = new ZipArchiveEntry(new TestZipEntry(name, extraData));

        ZipArchiveEntry testEntry = new ZipArchiveEntry(originalEntry);

        Assert.assertEquals(name, testEntry.getName());
        Assert.assertEquals(-1, testEntry.getMethod());
        Assert.assertEquals(originalEntry.getInternalAttributes(), testEntry.getInternalAttributes());
        Assert.assertEquals(originalEntry.getPlatform(), testEntry.getPlatform());
        Assert.assertEquals(originalEntry.getExternalAttributes(), testEntry.getExternalAttributes());
        Assert.assertEquals(originalEntry.getExtraFields(), testEntry.getExtraFields());
    }

    @Test
    public void testZipArchiveEntryClone() throws IOException {
        String name = "test.txt";
        byte[] extraData = new byte[]{(byte) 0x50, (byte) 0x4b, (byte) 0x03, (byte) 0x04};
        ZipArchiveEntry originalEntry = new ZipArchiveEntry(name);
        originalEntry.setMethod(DEFLATED);
        originalEntry.setInternalAttributes(0x1234);
        originalEntry.setPlatform(PLATFORM_UNIX);
        originalEntry.setExternalAttributes(0x5678);
        originalEntry.setExtraFields(new ZipExtraField[]{new TestZipExtraField(), new TestZipExtraField()});

        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) originalEntry.clone();

        Assert.assertEquals(name, clonedEntry.getName());
        Assert.assertEquals(DEFLATED, clonedEntry.getMethod());
        Assert.assertEquals(originalEntry.getInternalAttributes(), clonedEntry.getInternalAttributes());
        Assert.assertEquals(originalEntry.getPlatform(), clonedEntry.getPlatform());
        Assert.assertEquals(originalEntry.getExternalAttributes(), clonedEntry.getExternalAttributes());
        Assert.assertEquals(originalEntry.getExtraFields(), clonedEntry.getExtraFields());
    }

    @Test
    public void testZipArchiveEntryConstructionFromFile() throws IOException {
        String name = "test.txt";
        byte[] fileData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06};

        ZipArchiveEntry entry = new ZipArchiveEntry(new File(name), name);

        Assert.assertEquals(name, entry.getName());
        Assert.assertEquals(-1, entry.getMethod());
        Assert.assertEquals(0, entry.getInternalAttributes());
        Assert.assertEquals(0, entry.getPlatform());
        Assert.assertEquals(new Date(System.currentTimeMillis()).getTime(), entry.getExternalAttributes());
        Assert.assertNull(entry.getExtraFields());
        Assert.assertEquals(new Date(), entry.getLastModifiedDate());
        Assert.assertEquals(fileData.length, entry.getSize());
    }

    @Test
    public void testGetIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        Assert.assertFalse(entry.isSupportedCompressionMethod());

        entry.setMethod(DEFLATED);
        Assert.assertTrue(entry.isSupportedCompressionMethod());

        entry.setMethod(STORED);
        Assert.assertTrue(entry.isSupportedCompressionMethod());

        entry.setMethod(-1);
        Assert.assertFalse(entry.isSupportedCompressionMethod());
    }

    @Test
    public void testSetGetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");

        entry.setMethod(DEFLATED);
        Assert.assertEquals(DEFLATED, entry.getMethod());

        entry.setMethod(STORED);
        Assert.assertEquals(STORED, entry.getMethod());

        entry.setMethod(-1);
        Assert.assertEquals(-1, entry.getMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setMethod(-1);
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        Assert.assertEquals(0, entry.getInternalAttributes());

        entry.setInternalAttributes(1234);
        Assert.assertEquals(1234, entry.getInternalAttributes());
    }

    @Test
    public void testSetGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        Assert.assertEquals(0, entry.getPlatform());

        entry.setPlatform(PLATFORM_UNIX);
        Assert.assertEquals(PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setUnixMode(0777);

        Assert.assertEquals(0777, entry.getUnixMode());
    }

    @Test
    public void testGetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setPlatform(PLATFORM_UNIX);
        entry.setExternalAttributes(0x12345678);

        Assert.assertEquals(0x12, entry.getUnixMode());
    }

    @Test
    public void testSetGetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");

        Assert.assertEquals(0, entry.getExternalAttributes());

        long externalAttributes = 0x12345678;
        entry.setExternalAttributes(externalAttributes);
        Assert.assertEquals(externalAttributes, entry.getExternalAttributes());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");

        ZipExtraField[] extraFields = new ZipExtraField[]{new TestZipExtraField()};
        entry.setExtraFields(extraFields);

        Assert.assertEquals(extraFields, entry.getExtraFields());
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipExtraField extraField = new TestZipExtraField();

        entry.addExtraField(extraField);

        Assert.assertEquals(new ZipExtraField[]{extraField}, entry.getExtraFields());
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipExtraField extraField = new TestZipExtraField();

        entry.addAsFirstExtraField(extraField);

        Assert.assertEquals(new ZipExtraField[]{extraField}, entry.getExtraFields());
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");

        ZipExtraField extraField1 = new TestZipExtraField();
        ZipExtraField extraField2 = new TestZipExtraField();

        entry.setExtraFields(new ZipExtraField[]{extraField1, extraField2});
        entry.removeExtraField(TestZipShort.TEST_HEADER_ID);

        Assert.assertEquals(new ZipExtraField[]{extraField2}, entry.getExtraFields());
    }

    @Test
    public void testGetExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");

        ZipExtraField extraField = new TestZipExtraField();
        entry.setExtraFields(new ZipExtraField[]{extraField});

        Assert.assertEquals(extraField, entry.getExtraField(TestZipShort.TEST_HEADER_ID));
    }

    @Test
    public void testGetExtraDataAndSetExtra() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("");

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] extraData = new byte[]{(byte) 0x01, (byte) 0x23, (byte) 0x45};
        os.write(extraData, 0, extraData.length);
        InputStream is = new ByteArrayInputStream(os.toByteArray());

        entry.setExtra(is);
        Assert.assertEquals(extraData, entry.getLocalFileDataExtra());
    }

    @Test
    public void testSetCentralDirectoryExtra() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("");

        byte[] extraData = new byte[]{(byte) 0x01, (byte) 0x23, (byte) 0x45};

        entry.setCentralDirectoryExtra(extraData);
        Assert.assertEquals(extraData, entry.getCentralDirectoryExtra());
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");

        Assert.assertEquals(entry1.hashCode(), entry2.hashCode());

        entry1.setName("test2.txt");
        Assert.assertNotEquals(entry1.hashCode(), entry2.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("test.txt");
        ZipArchiveEntry entry2 = new ZipArchiveEntry("test.txt");

        Assert.assertTrue(entry1.equals(entry2));

        entry1.setName("test2.txt");
        Assert.assertFalse(entry1.equals(entry2));
    }

    @Test
    public void testToByteArray() throws IOException {
        ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        entry.write(os);

        byte[] expected = new byte[100]; // large enough for all fields
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        byte[] actual = new byte[100];
        int read = 0;
        while ((read = is.read(actual, 0, actual.length)) > 0) {
            System.arraycopy(actual, 0, expected, 0, read);
        }

        ZipArchiveEntry testEntry = new ZipArchiveEntry(new ByteArrayInputStream(expected));

        Assert.assertEquals(entry.getName(), testEntry.getName());
        Assert.assertEquals(entry.getMethod(), testEntry.getMethod());
        Assert.assertEquals(entry.getInternalAttributes(), testEntry.getInternalAttributes());
        Assert.assertEquals(entry.getPlatform(), testEntry.getPlatform());
        Assert.assertEquals(entry.getExternalAttributes(), testEntry.getExternalAttributes());
        Assert.assertEquals(entry.getExtraFields(), testEntry.getExtraFields());
    }

    private static final int STORED = 0;
    private static final int DEFLATED = 8;

    private static class TestZipEntry implements ZipEntry {
        private String name;
        private byte[] extraData;

        public TestZipEntry(String name, byte[] extraData) {
            this.name = name;
            this.extraData = extraData;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getSize() {
            return 0;
        }

        @Override
        public long getTime() {
            return 0;
        }

        @Override
        public void setTime(long time) {
            // not used
        }

        @Override
        public int getMethod() {
            return STORED;
        }

        @Override
        public byte[] getExtra() {
            return extraData;
        }

        @Override
        public Big5Entry getBig5Entry() {
            return null;
        }

        @Override
        public UTF8Entry getUTF8Entry() {
            return null;
        }
    }

    private static class TestZipExtraField implements ZipExtraField {
        private short headerId;
        private String fieldName;

        public TestZipExtraField() {
            this.headerId = TestZipShort.TEST_HEADER_ID;
            this.fieldName = "TestExtraField";
        }

        @Override
        public short getHeaderId() {
            return headerId;
        }

        @Override
        public String getFieldName() {
            return fieldName;
        }

        @Override
        public byte[] getLocalFileDataData() {
            return new byte[0];
        }

        @Override
        public byte[] getCentralDirectoryData() {
            return new byte[0];
        }

        @Override
        public void parseFromLocalFileData(byte[] data, int offset, int length) throws IOException {
            // not used
        }

        @Override
        public void parseFromCentralDirectoryData(byte[] data, int offset, int length) throws IOException {
            // not used
        }

        @Override
        public void writeToDataOutputStream(OutputStream dataStream) throws IOException {
            // not used
        }

        @Override
        public void writeToDataOutputStream(OutputStream dataStream, int offset) throws IOException {
            // not used
        }
    }

    private static class TestZipShort extends Short {
        public static final short TEST_HEADER_ID = (short) 0x504b;

        public TestZipShort(int value) {
            super(value & SHORT_MASK);
        }

        public static TestZipShort valueOf(short value) {
            return new TestZipShort(value);
        }
    }

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        DateUtils.setDefaultLocale(Locale.ENGLISH);
    }
}