package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        Assert.assertNotNull(checksumCalculatingInputStream);
    }

    /**
     * Test read method
     */
    @Test
    public void testRead() throws IOException {
        byte[] inputData = {1, 2, 3};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int result = checksumCalculatingInputStream.read();
        Assert.assertEquals(inputData[0], result);

        result = checksumCalculatingInputStream.read();
        Assert.assertEquals(inputData[1], result);

        result = checksumCalculatingInputStream.read();
        Assert.assertEquals(inputData[2], result);

        result = checksumCalculatingInputStream.read();
        Assert.assertEquals(-1, result);

        long checksumValue = checksumCalculatingInputStream.getValue();
        Adler32 expectedChecksum = new Adler32();
        expectedChecksum.update(inputData);
        Assert.assertEquals(expectedChecksum.getValue(), checksumValue);
    }

    /**
     * Test read method with byte array
     */
    @Test
    public void testReadByteArray() throws IOException {
        byte[] inputData = {1, 2, 3};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[3];
        int length = checksumCalculatingInputStream.read(buffer);
        Assert.assertEquals(3, length);
        Assert.assertArrayEquals(inputData, buffer);

        length = checksumCalculatingInputStream.read(buffer, 0, 3);
        Assert.assertEquals(3, length);
        Assert.assertArrayEquals(inputData, buffer);

        length = checksumCalculatingInputStream.read(buffer, 1, 2);
        Assert.assertEquals(2, length);
        Assert.assertArrayEquals(new byte[]{2, 3}, buffer, 1);

        length = checksumCalculatingInputStream.read(buffer, 2, 1);
        Assert.assertEquals(1, length);
        Assert.assertEquals(3, buffer[2]);

        length = checksumCalculatingInputStream.read(buffer, 3, 1);
        Assert.assertEquals(-1, length);

        long checksumValue = checksumCalculatingInputStream.getValue();
        Adler32 expectedChecksum = new Adler32();
        expectedChecksum.update(inputData);
        Assert.assertEquals(expectedChecksum.getValue(), checksumValue);
    }

    /**
     * Test skip method
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSkip() throws IOException {
        byte[] inputData = {1, 2, 3};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long skipAmount = 5;
        checksumCalculatingInputStream.skip(skipAmount);
    }

    /**
     * Test getValue method
     */
    @Test
    public void testGetValue() throws IOException {
        byte[] inputData = {1, 2, 3};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long expectedChecksumValue = 1902128584L; // Adler32 checksum for inputData
        long actualChecksumValue = checksumCalculatingInputStream.getValue();
        Assert.assertEquals(expectedChecksumValue, actualChecksumValue);
    }
}