import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksum;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStreamConstructor() {
        // Testing constructor
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        // Testing read method with single byte
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int readByte = checksumCalculatingInputStream.read();
        Assert.assertEquals(0x01, readByte);
        Assert.assertEquals(0x01, checksum.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        // Testing read method with a byte array
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] bytesToRead = new byte[4];
        int bytesRead = checksumCalculatingInputStream.read(bytesToRead);
        Assert.assertEquals(4, bytesRead);

        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03, 0x04}, bytesToRead);
        Assert.assertEquals(0x07ad96e6, checksum.getValue()); // Adler32 checksum for the given test data
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        // Testing read method with a byte array, offset and length
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] bytesToRead = new byte[4];
        int bytesRead = checksumCalculatingInputStream.read(bytesToRead, 1, 3);
        Assert.assertEquals(3, bytesRead);

        Assert.assertArrayEquals(new byte[]{0x02, 0x03, 0x04}, bytesToRead);
        Assert.assertEquals(0x04d67644, checksum.getValue()); // Adler32 checksum for the given test data
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        // Testing IOException when the underlying stream throws an IOException
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) inputStream;
        byteArrayInputStream.reset();
        byteArrayInputStream.close();

        int readByte = checksumCalculatingInputStream.read();
        Assert.assertEquals(0x01, readByte);
        Assert.assertEquals(0x01, checksum.getValue());

        inputStream = new ByteArrayInputStream(new byte[]{0x01});
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byteArrayInputStream = (ByteArrayInputStream) inputStream;
        byteArrayInputStream.reset();

        inputStream.close(); // Simulating IOException

        checksumCalculatingInputStream.read();
    }

    @Test
    public void testSkipMethod() throws IOException {
        // Testing skip method, which can't really be tested since it returns the number of bytes actually skipped
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long bytesSkipped = checksumCalculatingInputStream.skip(5);
        Assert.assertEquals(bytesSkipped, 5);
        Assert.assertEquals(0x02e8b1ba, checksum.getValue()); // Adler32 checksum for the given test data, skipping 5 bytes
    }

    @Test
    public void testGetValue() {
        // Testing getValue method
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        checksumCalculatingInputStream.read();

        long value = checksumCalculatingInputStream.getValue();
        Assert.assertEquals(0x02e8b1ba, value); // Adler32 checksum for the given test data
    }
}