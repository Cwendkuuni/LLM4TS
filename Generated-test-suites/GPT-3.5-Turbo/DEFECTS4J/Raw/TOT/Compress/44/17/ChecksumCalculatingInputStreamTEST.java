import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        // Typical use case: reading bytes from the stream with expected checksum
        byte[] input = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int readByte = calculatingInputStream.read();
        Assert.assertEquals(1, readByte);
        Assert.assertEquals(1, calculatingInputStream.getValue());

        readByte = calculatingInputStream.read();
        Assert.assertEquals(2, readByte);
        Assert.assertEquals(3, calculatingInputStream.getValue());

        readByte = calculatingInputStream.read();
        Assert.assertEquals(3, readByte);
        Assert.assertEquals(6, calculatingInputStream.getValue());
        
        readByte = calculatingInputStream.read();
        Assert.assertEquals(4, readByte);
        Assert.assertEquals(10, calculatingInputStream.getValue());
        
        readByte = calculatingInputStream.read();
        Assert.assertEquals(5, readByte);
        Assert.assertEquals(15, calculatingInputStream.getValue());
        
        readByte = calculatingInputStream.read();
        Assert.assertEquals(-1, readByte);
        Assert.assertEquals(15, calculatingInputStream.getValue());

        calculatingInputStream.close();
    }

    @Test
    public void testReadByteArray() throws IOException {
        // Tests edge case of reading a byte array from the stream
        byte[] input = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] byteArray = new byte[5];
        int bytesRead = calculatingInputStream.read(byteArray);
        Assert.assertEquals(5, bytesRead);
        Assert.assertEquals(1, calculatingInputStream.getValue());

        bytesRead = calculatingInputStream.read(byteArray);
        Assert.assertEquals(5, bytesRead);
        Assert.assertEquals(3, calculatingInputStream.getValue());

        bytesRead = calculatingInputStream.read(byteArray);
        Assert.assertEquals(0, bytesRead); // stream is exhausted
        Assert.assertEquals(3, calculatingInputStream.getValue());

        calculatingInputStream.close();
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        // Tests error scenario of reading a byte array from the stream with incorrect length
        byte[] input = {65, 66, 67, 68, 69};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] byteArray = new byte[4];
        int bytesRead = calculatingInputStream.read(byteArray, 1, 5);
        Assert.assertEquals(1, bytesRead);
        Assert.assertEquals(1, calculatingInputStream.getValue());

        bytesRead = calculatingInputStream.read(byteArray, 1, 4);
        Assert.assertEquals(0, bytesRead); // incorrect length parameter
        Assert.assertEquals(1, calculatingInputStream.getValue());

        calculatingInputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        // Tests edge case of skipping bytes in the stream
        byte[] input = {97, 98, 99, 100, 101};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long skippedBytes = calculatingInputStream.skip(2);
        Assert.assertEquals(2, skippedBytes);
        Assert.assertEquals(0, calculatingInputStream.getValue());

        int readByte = calculatingInputStream.read();
        Assert.assertEquals('c', readByte);
        Assert.assertEquals(99, calculatingInputStream.getValue());

        skippingBytes = calculatingInputStream.skip(10); // stream is exhausted
        Assert.assertEquals(3, skippedBytes);
        Assert.assertEquals(99, calculatingInputStream.getValue());

        calculatingInputStream.close();
    }
}