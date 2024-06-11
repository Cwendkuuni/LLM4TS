import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream checksumStream;

    @Before
    public void setUp() {
        checksumStream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(new byte[] {1, 2, 3}));
    }

    @Test
    public void testReadSingleByte() throws IOException {
        int readByte = checksumStream.read();
        Assert.assertEquals(1, readByte);
        long checksumValue = checksumStream.getValue();
        Assert.assertEquals(1635740242, checksumValue); // CRC32 checksum value after reading 1 byte
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[3];
        int bytesRead = checksumStream.read(buffer);
        Assert.assertEquals(3, bytesRead);
        long checksumValue = checksumStream.getValue();
        Assert.assertEquals(3047656874, checksumValue); // CRC32 checksum value after reading byte array
    }

    @Test
    public void testReadPartialByteArray() throws IOException {
        byte[] buffer = new byte[2];
        int bytesRead = checksumStream.read(buffer, 1, 2);
        Assert.assertEquals(2, bytesRead);
        long checksumValue = checksumStream.getValue();
        Assert.assertEquals(2963672226, checksumValue); // CRC32 checksum value after reading partial byte array
    }

    @Test
    public void testSkip() throws IOException {
        long skippedBytes = checksumStream.skip(2);
        Assert.assertEquals(0, skippedBytes); // Cannot skip bytes as checksum needs to be calculated
        long checksumValue = checksumStream.getValue();
        Assert.assertEquals(1635740242, checksumValue); // CRC32 checksum value without skipping bytes
    }

    @Test(expected = IOException.class)
    public void testReadException() throws IOException {
        checksumStream.read();
        checksumStream.read(); // Exhaust the stream to trigger IOException
    }
}