import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {

    @Test
    public void testZipArchiveInputStream() throws IOException {
        byte[] zipData = generateZipData();
        InputStream inputStream = new ByteArrayInputStream(zipData);

        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF8, true);

        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("entry1.txt", entry.getName());

        byte[] buffer = new byte[1024];
        int len = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertNotNull(len);
        Assert.assertFalse(len == -1);

        zipArchiveInputStream.close();

        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testZipArchiveInputStreamWithNullEncoding() throws IOException {
        byte[] zipData = generateZipData();
        InputStream inputStream = new ByteArrayInputStream(zipData);

        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, null, true);
    }

    @Test(expected = IOException.class)
    public void testZipArchiveInputStreamWithInvalidSignature() throws IOException {
        byte[] zipData = generateInvalidSignatureZipData();
        InputStream inputStream = new ByteArrayInputStream(zipData);

        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF8, true);
    }

    @Test
    public void testZipArchiveInputStreamWithEmptyZipFile() throws IOException {
        byte[] zipData = generateEmptyZipData();
        InputStream inputStream = new ByteArrayInputStream(zipData);

        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF8, true);
        Assert.assertNull(zipArchiveInputStream.getNextZipEntry());
        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test
    public void testZipArchiveInputStreamWithMultipleEntries() throws IOException {
        byte[] zipData = generateMultipleEntriesZipData();
        InputStream inputStream = new ByteArrayInputStream(zipData);

        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF8, true);

        ZipArchiveEntry entry1 = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry1);
        Assert.assertEquals("entry1.txt", entry1.getName());

        ZipArchiveEntry entry2 = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry2);
        Assert.assertEquals("entry2.txt", entry2.getName());

        zipArchiveInputStream.close();
        inputStream.close();
    }

    @Test(expected = ZipException.class)
    public void testZipArchiveInputStreamWithInvalidDataDescriptor() throws IOException {
        byte[] zipData = generateInvalidDataDescriptorZipData();
        InputStream inputStream = new ByteArrayInputStream(zipData);

        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, ZipEncoding.UTF8, true);
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.close();
        inputStream.close();
    }

    private byte[] generateZipData() {
        byte[] signature = ZipArchiveOutputStream.LFH_SIG;
        byte[] fileName = "entry1.txt".getBytes();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(signature);
        byteArrayOutputStream.write(ZipShort.toBytes((short) 3150));
        byteArrayOutputStream.write(0x02);
        byteArrayOutputStream.write(0x08);
        byteArrayOutputStream.write(ZipShort.toBytes((short) 8));
        byteArrayOutputStream.write(0x00);
        byteArrayOutputStream.write(ZipShort.toBytes((short) 0));
        byteArrayOutputStream.write(ZipShort.toBytes((short) 3150));
        byteArrayOutputStream.write(ZipLong.toBytes(835));
        byteArrayOutputStream.write(ZipLong.toBytes(182));
        byteArrayOutputStream.write(ZipLong.toBytes(835));
        byteArrayOutputStream.write(ZipLong.toBytes(182));
        byteArrayOutputStream.write(ZipShort.toBytes((short) 8));
        byteArrayOutputStream.write(0x00);
        byteArrayOutputStream.write(ZipShort.toBytes((short) (fileName.length + 4)));
        byteArrayOutputStream.write(ZipShort.toBytes((short) 0));
        byteArrayOutputStream.write(fileName);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);

        return byteArrayOutputStream.toByteArray();
    }

    private byte[] generateInvalidSignatureZipData() {
        byte[] signature = new byte[ZipArchiveOutputStream.LFH_SIG.length + 1];
        System.arraycopy(ZipArchiveOutputStream.LFH_SIG, 0, signature, 0, ZipArchiveOutputStream.LFH_SIG.length);
        signature[ZipArchiveOutputStream.LFH_SIG.length] = (byte) 0x1;

        return signature;
    }

    private byte[] generateEmptyZipData() {
        byte[] signature = ZipArchiveOutputStream.EOCD_SIG;
        return signature;
    }

    private byte[] generateMultipleEntriesZipData() {
        byte[] signature1 = ZipArchiveOutputStream.LFH_SIG;
        byte[] signature2 = ZipArchiveOutputStream.LFH_SIG;

        byte[] fileName1 = "entry1.txt".getBytes();
        byte[] fileName2 = "entry2.txt".getBytes();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byteArrayOutputStream.write(signature1);
        byteArrayOutputStream.write(ZipShort.toBytes((short) 3150));
        byteArrayOutputStream.write(0x02);
        byteArrayOutputStream.write(0x08);
        byteArrayOutputStream.write(ZipShort.toBytes((short) 8));
        byteArrayOutputStream.write(0x00);
        byteArrayOutputStream.write(ZipShort.toBytes((short) 0));
        byteArrayOutputStream.write(ZipShort.toBytes((short) 3150));
        byteArrayOutputStream.write(ZipLong.toBytes(835));
        byteArrayOutputStream.write(ZipLong.toBytes(182));
        byteArrayOutputStream.write(ZipLong.toBytes(835));
        byteArrayOutputStream.write(ZipLong.toBytes(182));
        byteArrayOutputStream.write(ZipShort.toBytes((short) 8));
        byteArrayOutputStream.write(0x00);
        byteArrayOutputStream.write(ZipShort.toBytes((short) (fileName1.length + 4)));
        byteArrayOutputStream.write(ZipShort.toBytes((short) 0));
        byteArrayOutputStream.write(fileName1);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);

        byteArrayOutputStream.write(signature2);
        byteArrayOutputStream.write(ZipShort.toBytes((short) 3150));
        byteArrayOutputStream.write(0x02);
        byteArrayOutputStream.write(0x08);
        byteArrayOutputStream.write(ZipShort.toBytes((short) 8));
        byteArrayOutputStream.write(0x00);
        byteArrayOutputStream.write(ZipShort.toBytes((short) 0));
        byteArrayOutputStream.write(ZipShort.toBytes((short) 3150));
        byteArrayOutputStream.write(ZipLong.toBytes(851));
        byteArrayOutputStream.write(ZipLong.toBytes(185));
        byteArrayOutputStream.write(ZipLong.toBytes(851));
        byteArrayOutputStream.write(ZipLong.toBytes(185));
        byteArrayOutputStream.write(ZipShort.toBytes((short) 8));
        byteArrayOutputStream.write(0x00);
        byteArrayOutputStream.write(ZipShort.toBytes((short) (fileName2.length + 4)));
        byteArrayOutputStream.write(ZipShort.toBytes((short) 0));
        byteArrayOutputStream.write(fileName2);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);

        byteArrayOutputStream.write(ZipArchiveOutputStream.EOCD_SIG);

        return byteArrayOutputStream.toByteArray();
    }

    private byte[] generateInvalidDataDescriptorZipData() {
        byte[] signature = ZipArchiveOutputStream.LFH_SIG;

        byte[] fileName = "entry1.txt".getBytes();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(signature);
        byteArrayOutputStream.write(ZipShort.toBytes((short) 3150));
        byteArrayOutputStream.write(0x02);
        byteArrayOutputStream.write(0x08);
        byteArrayOutputStream.write(ZipShort.toBytes((short) 8));
        byteArrayOutputStream.write(0x00);
        byteArrayOutputStream.write(ZipShort.toBytes((short) 0));
        byteArrayOutputStream.write(ZipShort.toBytes((short) 3150));
        byteArrayOutputStream.write(ZipLong.toBytes(835));
        byteArrayOutputStream.write(ZipLong.toBytes(182));
        byteArrayOutputStream.write(ZipLong.toBytes(835));
        byteArrayOutputStream.write(ZipLong.toBytes(182));
        byteArrayOutputStream.write(ZipShort.toBytes((short) 8));
        byteArrayOutputStream.write(0x00);
        byteArrayOutputStream.write(ZipShort.toBytes((short) (fileName.length + 4)));
        byteArrayOutputStream.write(ZipShort.toBytes((short) 0));
        byteArrayOutputStream.write(fileName);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(ZipShort.toBytes((short) 8));
        byteArrayOutputStream.write(0x00);
        byteArrayOutputStream.write(ZipShort.toBytes((short) 0));
        byteArrayOutputStream.write(ZipShort.toBytes((short) 0));
        byteArrayOutputStream.write(ZipShort.toBytes((short) 0));
        byteArrayOutputStream.write(ZipLong.toBytes(835));
        byteArrayOutputStream.write(ZipLong.toBytes(182));
        byteArrayOutputStream.write(ZipLong.toBytes(835));
        byteArrayOutputStream.write(ZipLong.toBytes(182));

        byteArrayOutputStream.write(ZipArchiveOutputStream.EOCD_SIG);

        return byteArrayOutputStream.toByteArray();
    }
}