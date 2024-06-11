package org.apache.commons.compress.utils;
import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

public class ChecksumCalculatingInputStreamTest {

    // Test case for the constructor
    @Test
    public void testConstructor() throws IOException {
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(new byte[]{}));
        Assert.assertNotNull(stream);
    }

    // Test case for the read() method
    @Test
    public void testRead() throws IOException {
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03}));
        int value = stream.read();
        Assert.assertEquals(0x01, value);
        Assert.assertEquals(1, stream.getValue());
    }

    // Test case for the read(byte[]) method
    @Test
    public void testReadByteArray() throws IOException {
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03}));
        byte[] buffer = new byte[2];
        int bytesRead = stream.read(buffer);
        Assert.assertEquals(2, bytesRead);
        Assert.assertEquals(0x01, buffer[0]);
        Assert.assertEquals(0x02, buffer[1]);
        Assert.assertEquals(0x02, stream.getValue());
    }

    // Test case for the read(byte[], int, int) method
    @Test
    public void testReadByteArrayOffset() throws IOException {
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03}));
        byte[] buffer = new byte[3];
        int bytesRead = stream.read(buffer, 1, 2);
        Assert.assertEquals(2, bytesRead);
        Assert.assertEquals(0x00, buffer[0]);
        Assert.assertEquals(0x01, buffer[1]);
        Assert.assertEquals(0x02, buffer[2]);
        Assert.assertEquals(0x02, stream.getValue());
    }

    // Test case for the skip() method
    @Test
    public void testSkip() throws IOException {
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03}));
        long skipped = stream.skip(2);
        Assert.assertEquals(2, skipped);

        int value = stream.read();
        Assert.assertEquals(0x03, value);
        Assert.assertEquals(0x03, stream.getValue());
    }

    // Test case for typical use case with GZIPInputStream and ZipInputStream
    @Test
    public void testTypicalUseCaseWithGzipAndZip() throws IOException {
        byte[] gzipData = new byte[]{(byte) 0x1f, (byte) 0x8b, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x2a, (byte) 0xc9, (byte) 0x4f, (byte) 0x2e, (byte) 0xc9, (byte) 0x55, (byte) 0x28, (byte) 0x48, (byte) 0xcb, (byte) 0xcf, (byte) 0xd5, (byte) 0x49, (byte) 0x4a, (byte) 0x4f, (byte) 0xd6, (byte) 0xd5, (byte) 0xd7, (byte) 0x03, (byte) 0xd9, (byte) 0x71, (byte) 0x72, (byte) 0x73, (byte) 0x74, (byte) 0x75, (byte) 0xf1, (byte) 0x70, (byte) 0x71, (byte) 0x72, (byte) 0x73, (byte) 0x74, (byte) 0xf5, (byte) 0x70, (byte) 0x71, (byte) 0x72, (byte) 0x73, (byte) 0x74, (byte) 0xf2, (byte) 0x70, (byte) 0x71, (byte) 0x72, (byte) 0x73, (byte) 0x74, (byte) 0xf6, (byte) 0x70, (byte) 0x71, (byte) 0x72, (byte) 0x73, (byte) 0x74, (byte) 0xf4, (byte) 0x70, (byte) 0x71, (byte) 0x72, (byte) 0x73, (byte) 0x74, (byte) 0xf3, (byte) 0x70, (byte) 0x71, (byte) 0x72, (byte) 0x73, (byte) 0x74, (byte) 0x01, (byte) 0x13, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0x2a, (byte) 0xc9, (byte) 0x4f, (byte) 0x2e, (byte) 0x02, (byte) 0x00, (byte) 0xe8, (byte) 0xff, (byte) 0xff, (byte) 0x2a, (byte) 0xc9, (byte) 0x4f, (byte) 0x2e, (byte) 0x00, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x1c, (byte) 0x00, (byte) 0x00, (byte) 0x00};

        ChecksumCalculatingInputStream stream1 = new ChecksumCalculatingInputStream(new CRC32(), new GZIPInputStream(new ByteArrayInputStream(gzipData)));
        ChecksumCalculatingInputStream stream2 = new ChecksumCalculatingInputStream(new CRC32(), new ZipInputStream(new ByteArrayInputStream(gzipData)));

        int value1 = stream1.read();
        int value2 = stream2.read();
        Assert.assertEquals(value1, value2);
        Assert.assertEquals(0x01, value1);

        int bytesRead1 = stream1.read(new byte[2]);
        int bytesRead2 = stream2.read(new byte[2]);
        Assert.assertEquals(bytesRead1, bytesRead2);
        Assert.assertEquals(2, bytesRead1);

        byte[] buffer1 = new byte[3];
        byte[] buffer2 = new byte[3];
        int bytesReadOffset1 = stream1.read(buffer1, 1, 2);
        int bytesReadOffset2 = stream2.read(buffer2, 1, 2);
        Assert.assertEquals(bytesReadOffset1, bytesReadOffset2);
        Assert.assertEquals(2, bytesReadOffset1);

        long skipped1 = stream1.skip(2);
        long skipped2 = stream2.skip(2);
        Assert.assertEquals(skipped1, skipped2);
        Assert.assertEquals(2, skipped1);

        Assert.assertEquals(stream1.getValue(), stream2.getValue());
        Assert.assertEquals(1315141800, stream1.getValue());
    }

    // Test case for error scenario when underlying stream is null
    @Test(expected = NullPointerException.class)
    public void testNullUnderlyingStream() {
        new ChecksumCalculatingInputStream(new CRC32(), null);
    }

    // Test case for error scenario when underlying stream throws IOException
    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03}));
        stream.read();
        stream.close();
        stream.read();
    }

}