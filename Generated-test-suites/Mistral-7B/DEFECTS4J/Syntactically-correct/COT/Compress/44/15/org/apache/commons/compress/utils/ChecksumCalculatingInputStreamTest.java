package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStreamConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        Checksum checksum = MessageDigest.getInstance("MD5"); // for example, MD5 checksum
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, inputStream);
        Assert.assertNotNull(ccis);
    }

    @Test
    public void testReadMethod() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        Checksum checksum = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, inputStream);

        int readByte = ccis.read();
        Assert.assertEquals(1, readByte);
        Assert.assertEquals(1, checksum.getValue());

        readByte = ccis.read();
        Assert.assertEquals(2, readByte);
        Assert.assertEquals(3, checksum.getValue());

        readByte = ccis.read();
        Assert.assertEquals(-1, readByte);
        Assert.assertEquals(5, checksum.getValue()); // 1+2+2
    }

    @Test
    public void testReadByteArrayMethod() throws IOException, NoSuchAlgorithmException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        Checksum checksum = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] readData = new byte[3];
        int readLength = ccis.read(readData);

        Assert.assertEquals(3, readLength);
        Assert.assertArrayEquals(new byte[]{1, 2, 3}, readData);
        Assert.assertEquals(15, checksum.getValue()); // 1+2+3

        byte[] expectData = new byte[2];
        readLength = ccis.read(expectData);

        Assert.assertEquals(2, readLength);
        Assert.assertArrayEquals(new byte[]{4, 5}, expectData);
        Assert.assertEquals(17, checksum.getValue()); // 1+2+3+4+5

        byte[] noData = new byte[10];
        readLength = ccis.read(noData);

        Assert.assertEquals(-1, readLength);
        Assert.assertEquals(17, checksum.getValue());
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenStreamExhausted() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        Checksum checksum = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, inputStream);

        int readByte = ccis.read();
        readByte = ccis.read();
        readByte = ccis.read();

        // this read method should throw IOException
        int exhaustedByte = ccis.read();
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenChecksumMismatch() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        Checksum checksum = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, inputStream);

        int readByte = ccis.read();
        readByte = ccis.read();
        readByte = ccis.read();
        readByte = ccis.read();

        // modify the checksum
        checksum.reset();

        int exhaustedByte = ccis.read();
    }

    @Test
    public void testSkipMethod() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        Checksum checksum = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, inputStream);

        long skipped = ccis.skip(3);
        Assert.assertEquals(3, skipped);
        Assert.assertEquals(8, checksum.getValue()); // 1+2+2+1+1
    }

    @Test
    public void testGetValueMethod() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        Checksum checksum = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, inputStream);

        int readByte = ccis.read();
        readByte = ccis.read();
        readByte = ccis.read();
        readByte = ccis.read();
        readByte = ccis.read();

        long checksumValue = ccis.getValue();
        Assert.assertEquals(17, checksumValue); // 1+2+3+4+5
    }

}