package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test the constructor.
     * @throws IOException if the streams are not available
     */
    @Test
    public void testConstructor() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        Assert.assertNotNull(checksumCalculatingInputStream);
    }

    /**
     * Test the read method with single byte.
     * @throws IOException if the stream is not available or the checksum doesn't match
     */
    @Test
    public void testReadSingleByte() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        final int readByte = checksumCalculatingInputStream.read();
        Assert.assertEquals(0x01, readByte);
        Assert.assertFalse(checksum.isComplete()); // Checksum is not complete after reading a single byte
    }

    /**
     * Test the read method with multiple bytes.
     * @throws IOException if the stream is not available or the checksum doesn't match
     */
    @Test
    public void testReadMultipleBytes() throws IOException, NoSuchAlgorithmException {
        final byte[] testData = new byte[]{(byte)0x01, (byte)0x02, (byte)0x03};
        final InputStream inputStream = new ByteArrayInputStream(testData);
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        final byte[] readData = new byte[testData.length];
        final int readBytes = checksumCalculatingInputStream.read(readData, 0, testData.length);
        Assert.assertEquals(testData.length, readBytes);
        Assert.assertArrayEquals(testData, readData);
        Assert.assertTrue(checksum.isComplete());
    }

    /**
     * Test the read method with an empty byte array.
     * @throws IOException if the stream is not available or the checksum doesn't match
     */
    @Test
    public void testReadEmptyByteArray() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{}, 0, 0);
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        final byte[] readData = new byte[0];
        final int readBytes = checksumCalculatingInputStream.read(readData, 0, readData.length);
        Assert.assertEquals(0, readBytes);
    }

    /**
     * Test the read method with a larger byte array than available data.
     * @throws IOException if the stream is not available or the checksum doesn't match
     */
    @Test
    public void testReadLargerByteArray() throws IOException {
        final byte[] testData = new byte[]{(byte)0x01, (byte)0x02, (byte)0x03};
        final InputStream inputStream = new ByteArrayInputStream(testData);
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        final byte[] readData = new byte[testData.length * 2];
        final int readBytes = checksumCalculatingInputStream.read(readData, 0, readData.length);
        Assert.assertEquals(testData.length, readBytes);
    }

    /**
     * Test the skip method.
     * @throws IOException if the stream is not available or the checksum doesn't match
     */
    @Test
    public void testSkip() throws IOException {
        final byte[] testData = new byte[]{(byte)0x01, (byte)0x02, (byte)0x03};
        final InputStream inputStream = new ByteArrayInputStream(testData);
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        final long skippedBytes = checksumCalculatingInputStream.skip(testData.length * 2);
        Assert.assertEquals(testData.length * 2, skippedBytes);
        Assert.assertTrue(checksum.isComplete());
    }

    /**
     * Test the getValue method.
     * @throws IOException if the stream is not available or the checksum doesn't match
     */
    @Test
    public void testGetValue() throws IOException, NoSuchAlgorithmException {
        final byte[] testData = new byte[]{(byte)0x01, (byte)0x02, (byte)0x03};
        final InputStream inputStream = new ByteArrayInputStream(testData);
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        final long checksumValue = checksumCalculatingInputStream.getValue();
        final byte[] expectedChecksum = checksum.digest();
        Assert.assertArrayEquals(expectedChecksum, new byte[]{
                0x30, (byte)0x5d, 0x32, 0x45, 0x66, 0x6f, 0x72, 0x6d, 0x61, 0x78,
                0x78, 0x01, 0x3f, (byte)0x85, 0xe1, 0x64, 0x03, 0x88, 0x5e, 0x2d,
                0x07, 0x0e, 0x62, 0x8e, 0x66, 0x49, 0x1c, 0x54, 0x03, 0x83, 0xd8,
                0x1e, 0x6c, 0x26, 0x32
        });
        Assert.assertArrayEquals(expectedChecksum, ByteBuffer.wrap(Long.toByteArray(checksumValue)).array());
    }

    /**
     * Test the throwExceptions method.
     * @throws IOException if the stream is not available or the checksum doesn't match
     */
    @Test(expected = IOException.class)
    public void testThrowExceptions() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{});
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        checksumCalculatingInputStream.read();
    }

    /**
     * Test the throwExceptions when the checksum doesn't match.
     * @throws IOException if the stream is not available
     * @throws NoSuchAlgorithmException if the checksum algorithm is not available
     */
    @Test(expected = IOException.class)
    public void testThrowExceptionsWithChecksumMismatch() throws IOException, NoSuchAlgorithmException {
        final byte[] testData = new byte[]{(byte)0x01, (byte)0x02, (byte)0x03};
        final InputStream inputStream = new ByteArrayInputStream(testData);
        final Checksum checksum = MessageDigest.getInstance("SHA-256");
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        checksumCalculatingInputStream.read(testData, 0, testData.length);
    }

}