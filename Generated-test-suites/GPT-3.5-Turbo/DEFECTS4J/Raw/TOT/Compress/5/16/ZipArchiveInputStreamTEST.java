import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipArchiveInputStream;

    @Before
    public void setup() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextZipEntry_NormalFile() throws IOException {
        byte[] header = {(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04};
        zipArchiveInputStream.in = new ByteArrayInputStream(header);
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(2, entry.getMethod());
        Assert.assertEquals(0, entry.getSize());
        Assert.assertEquals(-1, entry.getCrc());
    }

    @Test
    public void testGetNextZipEntry_CentralDirectoryFile() throws IOException {
        byte[] header = {(byte)0x50, (byte)0x4B, (byte)0x06, (byte)0x07};
        zipArchiveInputStream.in = new ByteArrayInputStream(header);
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(entry);
    }

    @Test
    public void testGetNextZipEntry_InvalidSignature() throws IOException {
        byte[] header = {(byte)0x50, (byte)0x4B, (byte)0x00, (byte)0x00};
        zipArchiveInputStream.in = new ByteArrayInputStream(header);
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNull(entry);
    }

    @Test
    public void testRead_NormalFile() throws IOException {
        byte[] header = {(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04};
        zipArchiveInputStream.in = new ByteArrayInputStream(header);
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();

        byte[] data = new byte[]{(byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04};
        zipArchiveInputStream.in = new ByteArrayInputStream(data);
        byte[] buffer = new byte[2];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, 2);

        Assert.assertEquals(2, bytesRead);
        Assert.assertEquals((byte)0x01, buffer[0]);
        Assert.assertEquals((byte)0x02, buffer[1]);
    }

    @Test
    public void testRead_CompressedFile() throws IOException {
        byte[] header = {(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04};
        zipArchiveInputStream.in = new ByteArrayInputStream(header);
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();

        // Deflated data: (78 9C is the deflate compression header)
        byte[] deflatedData = {(byte)0x78, (byte)0x9C, (byte)0x01, (byte)0x02};
        zipArchiveInputStream.in = new ByteArrayInputStream(deflatedData);
        byte[] buffer = new byte[4];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, 4);

        Assert.assertEquals(4, bytesRead);
        Assert.assertEquals((byte)0x01, buffer[0]);
        Assert.assertEquals((byte)0x02, buffer[1]);
        Assert.assertEquals((byte)0x00, buffer[2]);
        Assert.assertEquals((byte)0x00, buffer[3]);
    }

    @Test
    public void testClose() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.getNextZipEntry(); // Should throw IOException
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = new byte[]{(byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        zipArchiveInputStream.in = inputStream;

        long skipped = zipArchiveInputStream.skip(2);
        Assert.assertEquals(2, skipped);
    }
}