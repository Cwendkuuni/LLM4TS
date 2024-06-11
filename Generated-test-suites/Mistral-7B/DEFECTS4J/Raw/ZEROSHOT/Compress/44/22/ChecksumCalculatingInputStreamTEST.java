package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Tests for {@link ChecksumCalculatingInputStream} class.
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test method for {@link ChecksumCalculatingInputStream#ChecksumCalculatingInputStream(Checksum, InputStream)}.
     * Test case for empty stream.
     * @throws IOException if an error occurs while testing.
     */
    @Test
    public void testEmptyStream() throws IOException {
        final Checksum checksum = new Adler32();
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        checksumCalculatingInputStream.close();
        final long expectedChecksum = checksum.getValue();
        final long actualChecksum = checksumCalculatingInputStream.getValue();
        Assert.assertEquals("Checksum should match the expected value for empty stream", expectedChecksum, actualChecksum);
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#ChecksumCalculatingInputStream(Checksum, InputStream)}.
     * Test case for single byte stream.
     * @throws IOException if an error occurs while testing.
     */
    @Test
    public void testSingleByteStream() throws IOException {
        final Checksum checksum = new Adler32();
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{-128});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        final int byteToRead = inputStream.read();
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.close();
        final long expectedChecksum = checksum.getValue();
        final long actualChecksum = checksumCalculatingInputStream.getValue();
        Assert.assertEquals("Checksum should match the expected value for single byte stream", expectedChecksum, actualChecksum);
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#ChecksumCalculatingInputStream(Checksum, InputStream)}.
     * Test case for multiple bytes stream.
     * @throws IOException if an error occurs while testing.
     * @throws NoSuchAlgorithmException if the algorithm for checksum generation is not available.
     */
    @Test
    public void testMultipleBytesStream() throws IOException, NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{-128, 0, 127});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md, inputStream);
        final int bytesRead = inputStream.read(new byte[3]);
        checksumCalculatingInputStream.read(new byte[3], 0, 3);
        checksumCalculatingInputStream.close();
        final byte[] expectedChecksumBytes = md.digest();
        final byte[] actualChecksumBytes = md.digest();
        for (int i = 0; i < expectedChecksumBytes.length; i++) {
            Assert.assertEquals(String.format("Expected and actual checksum bytes should be equal by index %d", i),
                    expectedChecksumBytes[i], actualChecksumBytes[i]);
        }
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#getValue()}.
     * Test case for single byte stream.
     * @throws IOException if an error occurs while testing.
     */
    @Test
    public void testSingleByteStreamValue() throws IOException {
        final Checksum checksum = new Adler32();
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{-128});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        checksumCalculatingInputStream.close();
        final long expectedChecksum = checksum.getValue();
        final long actualChecksum = checksumCalculatingInputStream.getValue();
        Assert.assertEquals("Checksum should match the expected value for single byte stream", expectedChecksum, actualChecksum);
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#getValue()}.
     * Test case for multiple bytes stream.
     * @throws IOException if an error occurs while testing.
     * @throws NoSuchAlgorithmException if the algorithm for checksum generation is not available.
     */
    @Test
    public void testMultipleBytesStreamValue() throws IOException, NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{-128, 0, 127});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md, inputStream);
        final int bytesRead = inputStream.read(new byte[3]);
        checksumCalculatingInputStream.read(new byte[3], 0, 3);
        checksumCalculatingInputStream.close();
        final byte[] expectedChecksumBytes = md.digest();
        final long expectedChecksum = byteArrayToLong(expectedChecksumBytes);
        final long actualChecksum = checksumCalculatingInputStream.getValue();
        Assert.assertEquals("Checksum should match the expected value for multiple bytes stream", expectedChecksum, actualChecksum);
    }

    /**
     * Converts a byte array to a long value.
     * @param bytes the byte array
     * @return the long value
     */
    private long byteArrayToLong(final byte[] bytes) {
        long result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result |= (bytes[i] & 0xFF) << (8 * (bytes.length - i - 1));
        }
        return result;
    }
}