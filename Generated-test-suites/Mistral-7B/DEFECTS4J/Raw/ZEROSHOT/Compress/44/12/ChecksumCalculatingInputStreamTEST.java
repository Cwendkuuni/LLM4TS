package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksum;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ChecksumCalculatingInputStreamTest class file.
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Checksum checksum = new Adler32(); // Replace with your preferred checksum algorithm
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});

        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        Assert.assertNotNull(checksumCalculatingInputStream);
        Assert.assertNotNull(checksumCalculatingInputStream.checksum);
        Assert.assertNotNull(checksumCalculatingInputStream.in);
    }

    /**
     * Test reading a single byte.
     */
    @Test
    public void testReadSingleByte() throws IOException {
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});

        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int readResult = checksumCalculatingInputStream.read();
        Assert.assertEquals(1, readResult);
        Assert.assertEquals(1, checksum.getValue());

        readResult = checksumCalculatingInputStream.read();
        Assert.assertEquals(2, readResult);
        Assert.assertEquals(1, checksum.getValue());

        readResult = checksumCalculatingInputStream.read();
        Assert.assertEquals(-1, readResult);
        Assert.assertEquals(1, checksum.getValue());
    }

    /**
     * Test reading a byte array.
     */
    @Test
    public void testReadByteArray() throws IOException {
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6});

        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[3];
        int readResult = checksumCalculatingInputStream.read(buffer, 0, buffer.length);

        Assert.assertEquals(3, readResult);
        Assert.assertArrayEquals(new byte[]{1, 2, 3}, buffer);
        Assert.assertEquals(1, checksum.getValue());

        readResult = checksumCalculatingInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(3, readResult);
        Assert.assertArrayEquals(new byte[]{4, 5, 6}, buffer);
        Assert.assertEquals(1, checksum.getValue());

        readResult = checksumCalculatingInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, readResult);
        Assert.assertEquals(1, checksum.getValue());
    }

    /**
     * Test skipping.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSkip() throws IOException {
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6});

        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        checksumCalculatingInputStream.skip(10);
    }

    /**
     * Test reading checksum value.
     */
    @Test
    public void testGetValue() throws IOException, NoSuchAlgorithmException {
        Checksum checksum = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6});

        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int readResult = checksumCalculatingInputStream.read();
        readResult = checksumCalculatingInputStream.read();
        readResult = checksumCalculatingInputStream.read();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        byte[] expectedValue = outputStream.toByteArray();

        long checksumValue = checksumCalculatingInputStream.getValue();
        Assert.assertArrayEquals(expectedValue, HexUtils.bytesToHex(ByteBuffer.allocate(checksum.getDigestLength()).put(checksum.digest()).array()));
    }
}