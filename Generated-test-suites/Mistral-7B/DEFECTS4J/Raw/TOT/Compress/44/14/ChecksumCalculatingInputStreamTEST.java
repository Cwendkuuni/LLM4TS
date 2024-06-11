import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test class for ChecksumCalculatingInputStream.
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test method for {@link ChecksumCalculatingInputStream#ChecksumCalculatingInputStream(Checksum, InputStream)}
     * with byte array input and known checksum.
     */
    @Test
    public void testCalculateChecksum() throws IOException, NoSuchAlgorithmException {
        final byte[] inputData = "Test data for ChecksumCalculatingInputStream".getBytes(StandardCharsets.UTF_8);
        final Checksum md5Checksum = ChecksumUtils.newChecksum(MessageDigest.getInstance("MD5"));
        final InputStream inputStream = new ByteArrayInputStream(inputData);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(md5Checksum, inputStream);
        final byte[] readData = new byte[inputData.length];
        checksumInputStream.read(readData);
        Assert.assertArrayEquals(inputData, readData);
        final long expectedChecksum = 0x30CC4F5D8A653B325A324219B265531B1B49015Dl; // MD5 checksum for "Test data for ChecksumCalculatingInputStream"
        Assert.assertEquals(expectedChecksum, checksumInputStream.getValue());
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#ChecksumCalculatingInputStream(Checksum, InputStream)}
     * with empty input stream.
     */
    @Test
    public void testEmptyInputStream() throws IOException {
        final Checksum emptyChecksum = ChecksumUtils.newChecksum(MessageDigest.getInstance("MD5"));
        final InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(emptyChecksum, emptyInputStream);
        final int readByte = checksumInputStream.read();
        Assert.assertEquals(-1, readByte);
        Assert.assertEquals(0, checksumInputStream.getValue());
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#ChecksumCalculatingInputStream(Checksum, InputStream)}
     * with null checksum.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullChecksum() {
        final InputStream inputStream = new ByteArrayInputStream("Test data".getBytes(StandardCharsets.UTF_8));
        new ChecksumCalculatingInputStream(null, inputStream);
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#ChecksumCalculatingInputStream(Checksum, InputStream)}
     * with null input stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullInputStream() {
        final Checksum md5Checksum = ChecksumUtils.newChecksum(MessageDigest.getInstance("MD5"));
        new ChecksumCalculatingInputStream(md5Checksum, null);
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#read()}
     * with EOF reached.
     */
    @Test
    public void testEndOfStream() throws IOException {
        final Checksum md5Checksum = ChecksumUtils.newChecksum(MessageDigest.getInstance("MD5"));
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(md5Checksum, inputStream);
        final int readByte = checksumInputStream.read();
        Assert.assertEquals(-1, readByte);
        Assert.assertEquals(0, checksumInputStream.getValue());
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#read(byte[], int, int)}
     * with EOF reached.
     */
    @Test
    public void testEndOfStreamReadByteArray() throws IOException {
        final Checksum md5Checksum = ChecksumUtils.newChecksum(MessageDigest.getInstance("MD5"));
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(md5Checksum, inputStream);
        final byte[] readData = new byte[1];
        final int bytesRead = checksumInputStream.read(readData, 0, readData.length);
        Assert.assertEquals(-1, bytesRead);
        Assert.assertEquals(0, checksumInputStream.getValue());
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#read(byte[], int, int)}
     * with input data smaller than read length.
     */
    @Test
    public void testReadLessThanInputDataSize() throws IOException {
        final byte[] inputData = "Test data for ChecksumCalculatingInputStream".getBytes(StandardCharsets.UTF_8);
        final Checksum md5Checksum = ChecksumUtils.newChecksum(MessageDigest.getInstance("MD5"));
        final InputStream inputStream = new ByteArrayInputStream(inputData);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(md5Checksum, inputStream);
        final byte[] readData = new byte[3];
        final int bytesRead = checksumInputStream.read(readData, 0, readData.length);
        Assert.assertEquals(inputData.length - 3, bytesRead);
        Assert.assertArrayEquals(inputData, 0, inputData.length - 3, readData, 0, bytesRead);
        final long expectedChecksum = 0x30CC4F5D8A653B325A324219B265531B1B49015Dl; // MD5 checksum for "Test data for ChecksumCalculatingInputStream"
        Assert.assertEquals(expectedChecksum, checksumInputStream.getValue());
    }
}