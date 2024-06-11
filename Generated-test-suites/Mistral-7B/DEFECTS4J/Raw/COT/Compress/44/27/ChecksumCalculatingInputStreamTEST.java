package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for ChecksumCalculatingInputStream class
 */
public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream checksumStream;
    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;
    private byte[] testData;

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        testData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        inputStream = new ByteArrayInputStream(testData);
        outputStream = new ByteArrayOutputStream(testData.length);
        checksumStream = new ChecksumCalculatingInputStream(md, inputStream);
    }

    /**
     * Test for read() method
     * @throws IOException
     */
    @Test
    public void testRead() throws IOException {
        byte readByte = (byte) checksumStream.read();
        Assume.assumeTrue(readByte >= 0);
        byte expectedByte = testData[0];
        assertEquals(expectedByte, readByte);
        byte readByteFromOutput = (byte) outputStream.read();
        assertEquals(expectedByte, readByteFromOutput);
        checksumVerification();
    }

    /**
     * Test for read(byte[]) method
     * @throws IOException
     */
    @Test
    public void testReadByteArray() throws IOException {
        byte[] readBytes = new byte[testData.length];
        int readLength = checksumStream.read(readBytes, 0, readBytes.length);
        assertEquals(testData.length, readLength);
        assertArrayEquals(testData, readBytes);
        byte[] readBytesFromOutput = outputStream.toByteArray();
        assertArrayEquals(testData, readBytesFromOutput);
        checksumVerification();
    }

    /**
     * Test for read(byte[], int, int) method
     * @throws IOException
     */
    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] readBytes = new byte[testData.length];
        int offset = 0;
        int length = testData.length;
        int readLength = checksumStream.read(readBytes, offset, length);
        assertEquals(testData.length, readLength);
        assertArrayEquals(testData, readBytes);
        byte[] readBytesFromOutput = outputStream.toByteArray();
        byte[] expectedBytes = new byte[]{testData[0], testData[1], testData[2]};
        assertArrayEquals(expectedBytes, readBytesFromOutput, 0, 3);
        checksumVerification();
    }

    /**
     * Test for skip() method
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testSkip() throws IOException {
        checksumStream.skip(testData.length + 1);
    }

    /**
     * Test for getValue() method
     */
    @Test
    public void testGetValue() {
        long expectedValue = getMD5Checksum(testData);
        long actualValue = checksumStream.getValue();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Helper method to check the checksum matches the expected value
     */
    private void checksumVerification() {
        long expectedValue = getMD5Checksum(testData);
        long actualValue = checksumStream.getValue();
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Helper method to get the MD5 checksum of a byte array
     * @param data byte array
     * @return MD5 checksum as a long
     */
    private long getMD5Checksum(byte[] data) {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(data);
        long checksum = 0;
        for (byte b : digest) {
            checksum <<= 8;
            checksum |= b & 0xFF;
        }
        return checksum;
    }
}