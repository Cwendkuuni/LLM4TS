import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculation() throws IOException, NoSuchAlgorithmException {
        final byte[] testData = new byte[]{'T', 'e', 's', 't', 'D', 'a', 't', 'a'};
        final InputStream input = new ByteArrayInputStream(testData);
        final Checksum checksum = new Adler32(); // Choose a suitable checksum algorithm
        final ChecksumCalculatingInputStream checksumInput = new ChecksumCalculatingInputStream(checksum, input);

        final int bytesRead = checksumInput.read(testData, 0, testData.length);
        Assert.assertEquals(testData.length, bytesRead);

        final long expectedChecksum = checksum.getValue();
        final long actualChecksum = checksumInput.getValue();
        Assert.assertEquals("Checksums don't match", expectedChecksum, actualChecksum);
    }

    @Test(expected = IOException.class)
    public void testReadExhaustedStream() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final Checksum checksum = new Adler32();
        final ChecksumCalculatingInputStream checksumInput = new ChecksumCalculatingInputStream(checksum, input);

        checksumInput.read();
        checksumInput.read(); // Should read 0 bytes in this case
        checksumInput.read(); // Throws an IOException when exhausted
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputStream() {
        final Checksum checksum = new Adler32();
        final InputStream input = null;

        try {
            new ChecksumCalculatingInputStream(checksum, input);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullChecksum() {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final Checksum checksum = null;

        try {
            new ChecksumCalculatingInputStream(checksum, input);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}