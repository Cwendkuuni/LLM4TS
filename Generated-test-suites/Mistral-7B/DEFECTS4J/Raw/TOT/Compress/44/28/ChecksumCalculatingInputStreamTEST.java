package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrderedData;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for ChecksumCalculatingInputStream class.
 */
public class ChecksumCalculatingInputStreamTest {

    private static final int BUFFER_SIZE = 4096;
    private InputStream inputStream;
   private ChecksumCalculatingInputStream checksumInputStream;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStreamFromOutput;
    private long expectedChecksum;

    @Before
    public void setUp() throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] testData = "Test data for ChecksumCalculatingInputStream".getBytes();
        md.update(testData);
        expectedChecksum = ByteBuffer.wrap(md.digest()).getLongammal();

        outputStream = new ByteArrayOutputStream();
        inputStream = new CloseShieldInputStream(new ByteArrayInputStream(testData), false);
        checksumInputStream = new ChecksumCalculatingInputStream(new MD5Checksum(), inputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        int readByte = checksumInputStream.read();
        assertEquals(testData[0], readByte);
        assertEquals(expectedChecksum, checksumInputStream.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] readBuffer = new byte[BUFFER_SIZE];
        int length = checksumInputStream.read(readBuffer);
        assertEquals(inputStream.read(readBuffer, 0, BUFFER_SIZE), length);
        assertArrayEquals(testData, readBuffer);
        assertEquals(expectedChecksum, checksumInputStream.getValue());
    }

    @Test
    public void testReadByteArrayWithPartialData() throws IOException {
        byte[] readBuffer = new byte[BUFFER_SIZE / 2];
        int length = checksumInputStream.read(readBuffer);
        assertEquals(inputStream.read(readBuffer, 0, BUFFER_SIZE / 2), length);
        assertArrayEquals(testData, 0, BUFFER_SIZE / 2, readBuffer);
        assertEquals(expectedChecksum, checksumInputStream.getValue());
    }

    @Test(expected = IOException.class)
    public void testSkippingData() throws IOException {
        checksumInputStream.skip(inputStream.available() + 1);
    }

    @Test
    public void testReadFromOutputStream() throws IOException {
        inputStream.transferTo(outputStream);
        inputStreamFromOutput = new ByteArrayInputStream(outputStream.toByteArray());
        checksumInputStream = new ChecksumCalculatingInputStream(new MD5Checksum(), inputStreamFromOutput);
        byte[] readBuffer = new byte[BUFFER_SIZE];
        int length = checksumInputStream.read(readBuffer);
        assertEquals(inputStream.available(), length);
        assertArrayEquals(testData, 0, testData.length, readBuffer);
        assertEquals(expectedChecksum, checksumInputStream.getValue());
    }

    @After
    public void cleanUp() throws IOException {
        outputStream.close();
        inputStream.close();
        checksumInputStream.close();
    }
}