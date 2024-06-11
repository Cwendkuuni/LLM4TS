import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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

    private ChecksumCalculatingInputStream checksumCalculatingInputStream;
    private InputStream inputStream;
    private byte[] testData;
    private long expectedChecksum;

    @Before
    public void setUp() throws IOException, NoSuchAlgorithmException {
        inputStream = new ByteArrayInputStream(testData);
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(ChecksumUtils.getAdler32Checksum(), inputStream);
        expectedChecksum = getAdler32Checksum(testData);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        int readByte = checksumCalculatingInputStream.read();
        Assert.assertEquals(-1, readByte); // End of stream
        Assert.assertEquals(expectedChecksum, checksumCalculatingInputStream.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[testData.length];
        int bytesRead = checksumCalculatingInputStream.read(buffer);
        Assert.assertEquals(testData.length, bytesRead);
        Assert.assertArrayEquals(testData, buffer);
        Assert.assertEquals(expectedChecksum, checksumCalculatingInputStream.getValue());
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] buffer = new byte[testData.length];
        int offset = 0;
        int length = testData.length;
        int bytesRead = checksumCalculatingInputStream.read(buffer, offset, length);
        Assert.assertEquals(testData.length, bytesRead);
        Assert.assertArrayEquals(testData, buffer);
        Assert.assertEquals(expectedChecksum, checksumCalculatingInputStream.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        long skippedBytes = 0;
        while (checksumCalculatingInputStream.read() >= 0) {
            skippedBytes++;
        }
        Assert.assertEquals(testData.length, skippedBytes);
        Assert.assertEquals(expectedChecksum, checksumCalculatingInputStream.getValue());
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] exceptionBytes = "Test IOException".getBytes(StandardCharsets.UTF_8);
        OutputStream os = new ByteArrayOutputStream(outputStream);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(ChecksumUtils.getAdler32Checksum(), os);
        os.write(exceptionBytes, 0, exceptionBytes.length);
        os.close();
        checksumCalculatingInputStream.read();
    }

    @After
    public void tearDown() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
        if (checksumCalculatingInputStream != null) {
            checksumCalculatingInputStream.close();
        }
    }

    private long getAdler32Checksum(byte[] data) {
        MessageDigest messageDigest = MessageDigest.getInstance("Adler32");
        messageDigest.update(data);
        byte[] digest = messageDigest.digest();
        return ((long) digest[0] << 32) | ((long) digest[1] & 0xFFFFFFFFL);
    }
}