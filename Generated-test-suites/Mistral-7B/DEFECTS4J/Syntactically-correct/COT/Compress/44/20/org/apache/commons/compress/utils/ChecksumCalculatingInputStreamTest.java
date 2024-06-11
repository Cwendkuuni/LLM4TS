package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.io.input.ByteArrayInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test class for ChecksumCalculatingInputStream.
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Checksum checksum = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(checksum, input);
        Assert.assertNotNull(csis);
    }

    /**
     * Test read method with single byte.
     */
    @Test
    public void testReadSingleByte() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01});
        Checksum checksum = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(checksum, input);

        int actual = csis.read();
        int expected = 0x01;
        Assert.assertEquals(expected, actual);

        byte[] calculatedChecksum = csis.getValue();
        byte[] expectedChecksum = new byte[16]; // MD5 checksum length
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(new byte[]{0x01});
        md.digest(expectedChecksum, 0, expectedChecksum.length);

        Assert.assertArrayEquals(expectedChecksum, calculatedChecksum);
    }

    /**
     * Test read method with byte array.
     */
    @Test
    public void testReadByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        Checksum checksum = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(checksum, input);

        byte[] actual = new byte[3];
        int readLength = csis.read(actual);
        Assert.assertEquals(readLength, 3);

        byte[] expected = new byte[]{0x01, 0x02, 0x03};
        Assert.assertArrayEquals(expected, actual);

        byte[] calculatedChecksum = csis.getValue();
        byte[] expectedChecksum = new byte[16]; // MD5 checksum length
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(new byte[]{0x01, 0x02, 0x03});
        md.digest(expectedChecksum, 0, expectedChecksum.length);

        Assert.assertArrayEquals(expectedChecksum, calculatedChecksum);
    }

    /**
     * Test read method with byte array and offset.
     */
    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        Checksum checksum = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(checksum, input);

        byte[] actual = new byte[3];
        int offset = 1;
        int readLength = csis.read(actual, offset);
        Assert.assertEquals(readLength, 3);

        byte[] expected = new byte[]{0x02, 0x03, 0x04};
        Assert.assertArrayEquals(expected, actual);

        byte[] calculatedChecksum = csis.getValue();
        byte[] expectedChecksum = new byte[16]; // MD5 checksum length
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        md.digest(expectedChecksum, 0, expectedChecksum.length);

        Assert.assertArrayEquals(expectedChecksum, calculatedChecksum);
    }

    /**
     * Test read method with byte array and length.
     */
    @Test
    public void testReadByteArrayWithLength() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        Checksum checksum = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(checksum, input);

        byte[] actual = new byte[3];
        int length = 3;
        int readLength = csis.read(actual, 0, length);
        Assert.assertEquals(readLength, 3);

        byte[] expected = new byte[]{0x01, 0x02, 0x03};
        Assert.assertArrayEquals(expected, actual);

        byte[] calculatedChecksum = csis.getValue();
        byte[] expectedChecksum = new byte[16]; // MD5 checksum length
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        md.digest(expectedChecksum, 0, expectedChecksum.length);

        Assert.assertArrayEquals(expectedChecksum, calculatedChecksum);
    }

    /**
     * Test skip method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01});
        Checksum checksum = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(checksum, input);

        csis.skip(1);
    }

    /**
     * Test getValue method.
     */
    @Test
    public void testGetValue() throws IOException, NoSuchAlgorithmException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        Checksum checksum = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(checksum, input);

        byte[] calculatedChecksum = csis.getValue();
        byte[] expectedChecksum = new byte[16]; // MD5 checksum length
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        md.digest(expectedChecksum, 0, expectedChecksum.length);

        Assert.assertArrayEquals(expectedChecksum, calculatedChecksum);
    }

}