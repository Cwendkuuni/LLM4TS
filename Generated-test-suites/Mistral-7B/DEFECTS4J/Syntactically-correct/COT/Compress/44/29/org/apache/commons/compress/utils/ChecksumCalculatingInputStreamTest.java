package org.apache.commons.compress.utils.test;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;
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

    @Test
    public void testRead() throws IOException, NoSuchAlgorithmException {
        byte[] input = {0x01, 0x02, 0x03};
        Checksum checksum = ChecksumUtils.getChecksum("MD5");
        InputStream inputStream = new ByteArrayInputStream(input);
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte b = 0;
        for (int i = 0; i < input.length; i++) {
            b = (byte) testStream.read();
            Assert.assertEquals(input[i], b);
        }

        Assert.assertArrayEquals(checksum.getValue(), testStream.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException, NoSuchAlgorithmException {
        byte[] input = {0x01, 0x02, 0x03};
        Checksum checksum = ChecksumUtils.getChecksum("MD5");
        InputStream inputStream = new ByteArrayInputStream(input);
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] output = new byte[input.length];
        int actual = testStream.read(output);
        Assert.assertEquals(input.length, actual);

        Assert.assertArrayEquals(input, output);
        Assert.assertArrayEquals(checksum.getValue(), testStream.getValue());
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnRead() throws IOException {
        byte[] input = {0x01, 0x02, 0x03};
        Checksum checksum = ChecksumUtils.getChecksum("MD5");
        InputStream inputStream = new ByteArrayInputStream(input);
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        testStream.read(); // read successfully

        testStream.read(); // throw IOException
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnReadByteArray() throws IOException {
        byte[] input = {0x01, 0x02, 0x03};
        Checksum checksum = ChecksumUtils.getChecksum("MD5");
        InputStream inputStream = new ByteArrayInputStream(input);
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        testStream.read(new byte[input.length]); // read successfully

        testStream.read(new byte[input.length + 1]); // throw IOException
    }

    @Test
    public void testSkip() throws IOException, NoSuchAlgorithmException {
        byte[] input = {0x01, 0x02, 0x03};
        Checksum checksum = ChecksumUtils.getChecksum("MD5");
        InputStream inputStream = new ByteArrayInputStream(input);
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long skipped = 0;
        for (int i = 0; i < input.length; i++) {
            skipped += testStream.read();
        }

        Assert.assertEquals(input.length, skipped);
        Assert.assertArrayEquals(checksum.getValue(), testStream.getValue());
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnClose() throws IOException {
        byte[] input = {0x01, 0x02, 0x03};
        Checksum checksum = ChecksumUtils.getChecksum("MD5");
        InputStream inputStream = new ByteArrayInputStream(input);
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        testStream.close(); // read successfully

        testStream.close(); // throw IOException
    }

    @Test
    public void testGetValue() throws IOException, NoSuchAlgorithmException {
        byte[] input = {0x01, 0x02, 0x03};
        Checksum checksum = ChecksumUtils.getChecksum("MD5");
        InputStream inputStream = new ByteArrayInputStream(input);
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        testStream.read();
        testStream.read();
        testStream.read();

        Assert.assertArrayEquals(checksum.getValue(), testStream.getValue());
    }
}