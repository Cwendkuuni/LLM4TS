package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.io.ByteArrayOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

public class ZipArchiveEntryTest {

    @Test
    public void testEmptyZipArchiveEntryCreation() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("");
        Assert.assertEquals("", zipArchiveEntry.getName());
        Assert.assertNull(zipArchiveEntry.getInternalAttributes());
        Assert.assertEquals(0, zipArchiveEntry.getExternalAttributes());
        Assert.assertNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());
        Assert.assertNull(zipArchiveEntry.getSize());
        Assert.assertEquals(0L, zipArchiveEntry.getTime());
    }

    @Test
    public void testZipArchiveEntryCreationWithName() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals("test.txt", zipArchiveEntry.getName());
        Assert.assertNull(zipArchiveEntry.getInternalAttributes());
        Assert.assertEquals(0, zipArchiveEntry.getExternalAttributes());
        Assert.assertNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());
        Assert.assertNull(zipArchiveEntry.getSize());
        Assert.assertEquals(0L, zipArchiveEntry.getTime());
    }

    @Test
    public void testZipArchiveEntryCreationWithFile() {
        File testFile = new File("testFile.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(testFile, "testEntry.txt");
        Assert.assertEquals("testEntry.txt", zipArchiveEntry.getName());
        Assert.assertEquals(testFile.isDirectory() ? 0755 : 0644, zipArchiveEntry.getInternalAttributes());
        Assert.assertEquals(testFile.lastModified(), zipArchiveEntry.getTime(), 1000L);
        Assert.assertNull(zipArchiveEntry.getExternalAttributes());
        Assert.assertNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
        Assert.assertEquals(testFile.length(), zipArchiveEntry.getSize());
    }

    @Test
    public void testZipArchiveEntryCreationWithZipEntry() {
        ZipEntry zipEntry = new ZipEntry("testEntry.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        Assert.assertEquals("testEntry.txt", zipArchiveEntry.getName());
        Assert.assertNull(zipArchiveEntry.getInternalAttributes());
        Assert.assertNull(zipArchiveEntry.getExternalAttributes());
        Assert.assertNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());
        Assert.assertNull(zipArchiveEntry.getSize());
        Assert.assertEquals(0L, zipArchiveEntry.getTime());
    }

    @Test
    public void testZipArchiveEntryCreationWithByteArray() {
        final byte[] testByteArray = new byte[]{'T', 'e', 's', 't', ' ', 'F', 'i', 'l', 'e', (byte) 0x0A};
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("testByteArray.txt");
        zipArchiveEntry.setData(testByteArray);
        Assert.assertEquals("testByteArray.txt", zipArchiveEntry.getName());
        Assert.assertNull(zipArchiveEntry.getInternalAttributes());
        Assert.assertNull(zipArchiveEntry.getExternalAttributes());
        Assert.assertNull(zipArchiveEntry.getExtraFields());
        Assert.assertEquals(-1, zipArchiveEntry.getMethod());
        Assert.assertArrayEquals(testByteArray, zipArchiveEntry.getBytes());
        Assert.assertEquals(testByteArray.length, zipArchiveEntry.getSize());
        Assert.assertEquals(new Date(System.currentTimeMillis()), zipArchiveEntry.getTime());
    }

    @Test
    public void testSetAndGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        Assert.assertEquals(ZipEntry.DEFLATED, zipArchiveEntry.getMethod());
    }

    @Test
    public void testGetAndSetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setInternalAttributes(0744);
        Assert.assertEquals(0744, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testGetAndSetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExternalAttributes(1234L);
        Assert.assertEquals(1234L, zipArchiveEntry.getExternalAttributes());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(0755);
        Assert.assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testGetUnixMode() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(0755);
        Assert.assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        Assert.assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testSetExtraFields() {
        List<ZipExtraField> extraFields = new ArrayList<>();
        extraFields.add(new CustomByteExtraField(23, new byte[10]));
        extraFields.add(new CustomUInt16ExtraField(34, 32765));

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setExtraFields(extraFields.toArray(new ZipExtraField[0]));

        ZipExtraField[] extraFieldsArray = zipArchiveEntry.getExtraFields();
        Assert.assertArrayEquals(extraFields.toArray(new ZipExtraField[0]), extraFieldsArray);
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        List<ZipExtraField> extraFields = new ArrayList<>();
        extraFields.add(new CustomByteExtraField(23, new byte[10]));
        zipArchiveEntry.addExtraField(extraFields.remove(0));

        ZipExtraField[] extraFieldsArray = zipArchiveEntry.getExtraFields();
        Assert.assertArrayEquals(new ZipExtraField[] {extraFields.get(0)}, extraFieldsArray);
    }

    @Test
    public void testAddAsFirstExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        List<ZipExtraField> extraFields = new ArrayList<>();
        extraFields.add(new CustomByteExtraField(23, new byte[10]));
        zipArchiveEntry.addAsFirstExtraField(extraFields.remove(0));

        ZipExtraField[] extraFieldsArray = zipArchiveEntry.getExtraFields();
        Assert.assertArrayEquals(new ZipExtraField[] {extraFields.get(0)}, extraFieldsArray);
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        List<ZipExtraField> extraFields = new ArrayList<>();
        extraFields.add(new CustomByteExtraField(23, new byte[10]));
        zipArchiveEntry.addExtraField(extraFields.remove(0));

        zipArchiveEntry.removeExtraField(ZipShort.CUSTOM_BYTE_ID);

        ZipExtraField[] extraFieldsArray = zipArchiveEntry.getExtraFields();
        Assert.assertArrayEquals(new ZipExtraField[0], extraFieldsArray);
    }

    @Test
    public void testGetExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");

        List<ZipExtraField> extraFields = new ArrayList<>();
        extraFields.add(new CustomByteExtraField(23, new byte[10]));
        zipArchiveEntry.addExtraField(extraFields.remove(0));

        ZipExtraField extraField = zipArchiveEntry.getExtraField(ZipShort.CUSTOM_BYTE_ID);
        Assert.assertEquals(extraFields.get(0), extraField);
    }

    @Test(expected = RuntimeException.class)
    public void testSetInvalidExtraData() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] invalidExtraData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        zipArchiveEntry.setExtra(invalidExtraData);
    }

    @Test(expected = RuntimeException.class)
    public void testSetInvalidCentralDirectoryExtraData() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        byte[] invalidCentralDirectoryExtraData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        zipArchiveEntry.setCentralDirectoryExtra(invalidCentralDirectoryExtraData);
    }

    private static class CustomByteExtraField extends ZipExtraField {
        private static final int CUSTOM_BYTE_ID = 23;

        private final byte[] data;

        public CustomByteExtraField(int id, byte[] data) {
            super(id);
            this.data = data;
        }

        @Override
        public int getHeaderId() {
            return CUSTOM_BYTE_ID;
        }

        @Override
        public byte[] getLocalFileDataData() {
            return data;
        }

        @Override
        public byte[] getCentralDirectoryData() {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
            byteArrayOutputStream.write(data);
            return byteArrayOutputStream.toByteArray();
        }

        @Override
        public void parseFromLocalFileData(byte[] data, int offset, int length) {
            if (length != this.data.length) {
                throw new ZipException("Invalid length of local file data.");
            }
            System.arraycopy(data, offset, this.data, 0, this.data.length);
        }

        @Override
        public void parseFromCentralDirectoryData(byte[] data, int offset, int length) throws ZipException {
            int headerSize = 18;
            if (length < headerSize) {
                throw new ZipException("Invalid central directory data.");
            }
            byte[] header = new byte[headerSize];
            System.arraycopy(data, offset, header, 0, headerSize);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data, offset + headerSize, length - headerSize);
            this.data = new byte[length - headerSize];
            byte[] buffer = new byte[data.length];
            int bytesRead = 0;
            while (bytesRead < data.length) {
                int bytesAvailable = (int) byteArrayInputStream.available();
                int bytesToRead = Math.min(bytesAvailable, data.length - bytesRead);
                byteArrayInputStream.read(buffer, 0, bytesToRead);
                System.arraycopy(buffer, 0, data, bytesRead, bytesToRead);
                bytesRead += bytesToRead;
            }
        }
    }

    private static class CustomUInt16ExtraField extends ZipExtraField {
        private static final int CUSTOM_UINT16_ID = 34;
        private final int data;

        public CustomUInt16ExtraField(int id, int data) {
            super(id);
            this.data = data;
        }

        @Override
        public int getHeaderId() {
            return CUSTOM_UINT16_ID;
        }

        @Override
        public void parseFromLocalFileData(byte[] data, int offset, int length) {
            if (length != 2) {
                throw new ZipException("Invalid length of local file data.");
            }
            this.data = ((data[offset] & 0xFF) << 8) | (data[offset + 1] & 0xFF);
        }

        @Override
        public void parseFromCentralDirectoryData(byte[] data, int offset, int length) throws ZipException {
            int headerSize = 18;
            if (length < headerSize) {
                throw new ZipException("Invalid central directory data.");
            }
            byte[] header = new byte[headerSize];
            System.arraycopy(data, offset, header, 0, headerSize);
            int offsetInCentralDirectoryData = offset + headerSize;
            this.data = ((data[offsetInCentralDirectoryData] & 0xFF) << 8) | (data[offsetInCentralDirectoryData + 1] & 0xFF);
        }

        @Override
        public byte[] getLocalFileDataData() {
            byte[] data = new byte[2];
            data[0] = (byte) ((data.length >>> 8) & 0xFF);
            data[1] = (byte) (data.length & 0xFF);
            return data;
        }

        @Override
        public byte[] getCentralDirectoryData() {
            return new byte[]{CUSTOM_UINT16_ID, (byte) ((data >>> 8) & 0xFF), (byte) (data & 0xFF), (byte) 0};
        }
    }
}