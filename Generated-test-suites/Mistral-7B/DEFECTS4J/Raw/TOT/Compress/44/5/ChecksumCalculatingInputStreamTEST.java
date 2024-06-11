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

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculation() throws IOException, NoSuchAlgorithmException {
        final byte[] inputData = "Apache Commons Compress".getBytes(StandardCharsets.UTF_8);
        final Checksum checksum = new Adler32(); // Use any checksum algorithm of your choice
        final InputStream inputStream = new ByteArrayInputStream(inputData);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int bytesRead = checksumCalculatingInputStream.read(new byte[inputData.length]);
        Assert.assertEquals(-1, bytesRead); // Stream should be exhausted

        final long expectedChecksum = checksum.getValue();
        final long actualChecksum = checksumCalculatingInputStream.getValue();
        Assert.assertEquals(expectedChecksum, actualChecksum);

        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] md5Expected = md.digest(inputData);
        final byte[] md5Actual = checksum.getValue().isValid() ? checksum.digest() : null;
        Assert.assertArrayEquals(md5Expected, md5Actual);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnInvalidData() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{-1}); // Intentionally adding an invalid byte
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(new Adler32(), inputStream);
        checksumCalculatingInputStream.read();
    }
}