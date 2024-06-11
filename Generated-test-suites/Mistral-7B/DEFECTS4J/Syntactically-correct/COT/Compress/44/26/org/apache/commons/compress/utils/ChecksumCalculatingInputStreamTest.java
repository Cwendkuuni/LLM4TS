package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01 });
        new ChecksumCalculatingInputStream(checksum, inputStream);
    }

    /**
     * Test read method with single byte
     * @throws IOException
     */
    @Test
    public void testReadSingleByte() throws IOException {
        final byte testByte = (byte) 0x01;
        final byte[] testData = new byte[] { testByte };
        final InputStream inputStream = new ByteArrayInputStream(testData);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        final int readByte = checksumInputStream.read();
        assertEquals(testByte, readByte);
    }

    /**
     * Test read method with byte array
     * @throws IOException
     */
    @Test
    public void testReadByteArray() throws IOException {
        final byte[] testData = new byte[] { 0x01, 0x02, 0x03 };
        final InputStream inputStream = new ByteArrayInputStream(testData);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        final byte[] readBytes = new byte[3];
        final int readLength = checksumInputStream.read(readBytes);
        assert readLength == testData.length : "incorrect number of bytes read";
        for (int i = 0; i < testData.length; i++) {
            assertEquals(testData[i], readBytes[i]);
        }
    }

    /**
     * Test read method with byte array and offset
     * @throws IOException
     */
    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        final byte[] testData = new byte[] { 0x01, 0x02, 0x03 };
        final InputStream inputStream = new ByteArrayInputStream(testData);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        final byte[] readBytes = new byte[3];
        final int readOffset = 1;
        final int readLength = 2;
        final int read = checksumInputStream.read(readBytes, readOffset, readLength);
        assertEquals(readLength, read);
        for (int i = readOffset; i < readOffset + readLength; i++) {
            assertEquals(testData[i], readBytes[i - readOffset]);
        }
    }

    /**
     * Test skip method
     * @throws IOException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSkip() throws IOException {
        final byte[] testData = new byte[] { 0x01, 0x02, 0x03 };
        final InputStream inputStream = new ByteArrayInputStream(testData);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        checksumInputStream.skip(1);
    }

    /**
     * Test getValue method
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testGetValue() throws IOException, NoSuchAlgorithmException {
        final byte[] testData = new byte[] { 0x01, 0x02, 0x03 };
        final InputStream inputStream = new ByteArrayInputStream(testData);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        final long checksumValue = checksumInputStream.getValue();
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] mdBytes = md.digest(testData);
        final byte[] expectedChecksum = new byte[16];
        System.arraycopy(mdBytes, 0, expectedChecksum, 0, mdBytes.length);
        assertEquals(expectedChecksum, checksumValue);
    }

    /**
     * Test read with EOF
     * @throws IOException
     */
    @Test
    public void testReadWithEOF() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        final int read = checksumInputStream.read();
        assertEquals(-1, read);
    }

    /**
     * Test read with underlying stream throws exception
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testReadWithUnderlyingStreamThrowsException() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Test".getBytes());
        final InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        checksumInputStream.read();
        outputStream.write("Test2".getBytes());
    }
}