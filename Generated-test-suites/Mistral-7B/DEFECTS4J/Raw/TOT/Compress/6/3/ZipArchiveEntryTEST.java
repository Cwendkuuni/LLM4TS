package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

public class ZipArchiveEntryTest {

    @Test
    public void testBasicFunctionality() throws IOException, ZipException {
        String entryName = "test.txt";
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(entryName);

        // Check default values
        Assert.assertEquals(-1, zipEntry.getMethod());
        Assert.assertEquals(0, zipEntry.getInternalAttributes());
        Assert.assertEquals(0L, zipEntry.getExternalAttributes());
        Assert.assertEquals(null, zipEntry.getExtraFields());
        Assert.assertEquals(null, zipEntry.getName());

        // Set values and check them
        zipEntry.setMethod(ZipEntry.STORED);
        Assert.assertEquals(ZipEntry.STORED, zipEntry.getMethod());
        zipEntry.setInternalAttributes(ZipEntry.GENERIC_MASK | ZipEntry.FILE_FLAG);
        Assert.assertEquals(ZipEntry.GENERIC_MASK | ZipEntry.FILE_FLAG, zipEntry.getInternalAttributes());
        zipEntry.setExternalAttributes(0x12345678L);
        Assert.assertEquals(0x12345678L, zipEntry.getExternalAttributes());
        zipEntry.setExtraFields(new ZipExtraField[]{new MyCustomField("MyID", "MyData")});
        Assert.assertArrayEquals(new ZipExtraField[]{new MyCustomField("MyID", "MyData")}, zipEntry.getExtraFields());
        zipEntry.setName("newName.txt");
        Assert.assertEquals("newName.txt", zipEntry.getName());

        // Test parsing a ZipEntry
        ZipEntry zipEntryFromInputStream = new ZipArchiveEntry(new ByteArrayInputStream(getZipEntryBytes()));
        Assert.assertEquals(ZipEntry.STORED, zipEntryFromInputStream.getMethod());
        Assert.assertEquals(ZipEntry.GENERIC_MASK | ZipEntry.FILE_FLAG, zipEntryFromInputStream.getInternalAttributes());
        Assert.assertEquals(0x12345678L, zipEntryFromInputStream.getExternalAttributes());
        Assert.assertArrayEquals(new ZipExtraField[]{new MyCustomField("MyID", "MyData")}, zipEntryFromInputStream.getExtraFields());
        Assert.assertEquals("test.txt", zipEntryFromInputStream.getName());

        // Test cloning
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) zipEntry.clone();
        Assert.assertEquals(-1, clonedEntry.getMethod());
        Assert.assertEquals(0, clonedEntry.getInternalAttributes());
        Assert.assertEquals(0L, clonedEntry.getExternalAttributes());
        Assert.assertEquals(null, clonedEntry.getExtraFields());
        Assert.assertEquals(null, clonedEntry.getName());
        Assert.assertEquals(zipEntry, clonedEntry);
    }

    private byte[] getZipEntryBytes() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ZipExtraField myCustomField = new MyCustomField("MyID", "MyData");
        byte[] myCustomFieldBytes = myCustomField.getData();

        byteStream.write(0x02); // Version made by
        byteStream.write(0x14); // Version needed to extract
        byteStream.write(0x14); // OS
        byteStream.write(0x02); // Application
        byteStream.write(0x01); // Modification time
        byteStream.write(0x02, 0, 4); // Modification time (MS)
        byteStream.write(0x02, 0, 4); // Modification time (LS)

        byteStream.write(0x04); // Compressed size
        byteStream.write(0x04); // Uncompressed size
        byteStream.write(0x04); // File name length
        byteStream.write('t', 'e', 's', 't', '.', 't', 'x', 't');
        byteStream.write(0x04); // Extra field length

        byteStream.write(myCustomFieldBytes[0]); // Extra field ID
        byteStream.write(myCustomFieldBytes[1]); // Extra field ID
        byteStream.write(myCustomFieldBytes[2]); // Extra field ID
        byteStream.write(myCustomFieldBytes[3]); // Extra field ID
        byteStream.write(myCustomFieldBytes[4]); // Extra field ID
        byteStream.write(myCustomFieldBytes[5]); // Extra field ID
        byteStream.write(myCustomFieldBytes[6]); // Extra field ID
        byteStream.write(myCustomFieldBytes[7]); // Extra field ID

        byteStream.write(myCustomFieldBytes[8]); // Extra field length
        byteStream.write(myCustomFieldBytes[9]); // Extra field length
        byteStream.write(myCustomFieldBytes[10]); // Extra field length
        byteStream.write(myCustomFieldBytes[11]); // Extra field length

        byteStream.write(myCustomFieldBytes[12]); // Extra field data
        byteStream.write(myCustomFieldBytes[13]); // Extra field data
        byteStream.write(myCustomFieldBytes[14]); // Extra field data
        byteStream.write(myCustomFieldBytes[15]); // Extra field data

        byteStream.write(0x00); // End of central directory record signature
        byteStream.write(0x06); // End of central directory disc number
        byteStream.write(0x00); // Central directory disk number start
        byteStream.write(0x00); // Central directory disk number end
        byteStream.writeShort(Short.parseShort("0", Integer.toHexString(byteStream.size() - 22).substring(1))); // Central directory size

        return byteStream.toByteArray();
    }

    private static class MyCustomField extends ZipExtraField {

        public MyCustomField(String id, String data) {
            super(ZipShort.parseShort(id), new ByteArrayInputStream(data.getBytes()), null);
        }

        @Override
        public byte[] getData() {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byteStream.write(id.getBytes());
            byteStream.write(Long.toString(crc32Checksum(id.getBytes())).getBytes());
            byteStream.write(Long.toString(crc32Checksum(data.getBytes())).getBytes());
            return byteStream.toByteArray();
        }

        private long crc32Checksum(byte[] data) {
            // CRC32 implementation omitted
            return 0;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMethod() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("");
        zipEntry.setMethod(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePlatform() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("");
        zipEntry.setPlatform(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeExternalAttributes() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("");
        zipEntry.setExternalAttributes(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullExtraFields() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("");
        zipEntry.setExtraFields(null);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidExtraData() {
        byte[] invalidData = new byte[1];
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("");
        try {
            zipEntry.setExtra(invalidData);
        } catch (RuntimeException e) {
            Assert.assertEquals("Error parsing extra data", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPlatform() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("");
        zipEntry.setPlatform(4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInternalAttributes() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("");
        zipEntry.setInternalAttributes(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeExternalAttributes() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("");
        zipEntry.setExternalAttributes(-1);
    }
}