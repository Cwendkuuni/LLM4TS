package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStreamConstructor() {
        Checksum md5 = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ChecksumCalculatingInputStream testInput = new ChecksumCalculatingInputStream(md5, inputStream);
        Assert.assertNotNull(testInput);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        Checksum md5 = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ChecksumCalculatingInputStream testInput = new ChecksumCalculatingInputStream(md5, inputStream);
        int readByte = testInput.read();
        byte expectedByte = 1;
        Assert.assertEquals(expectedByte, readByte);
        byte[] testChecksum = md5.digest();
        Assert.assertArrayEquals(testChecksum, md5.digest(new byte[]{1}));
    }

    @Test
    public void testReadByteArray() throws IOException, NoSuchAlgorithmException {
        Checksum md5 = MessageDigest.getInstance("MD5");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write('a'.toString().getBytes());
        outputStream.write('b'.toString().getBytes());
        outputStream.write('c'.toString().getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ChecksumCalculatingInputStream testInput = new ChecksumCalculatingInputStream(md5, inputStream);
        byte[] expectedResult = md5.digest(outputStream.toByteArray());
        byte[] readByteArray = new byte[outputStream.size()];
        int readLength = testInput.read(readByteArray);
        Assert.assertEquals(outputStream.size(), readLength);
        Assert.assertArrayEquals(expectedResult, readByteArray);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenStreamExhausted() throws IOException {
        Checksum md5 = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ChecksumCalculatingInputStream testInput = new ChecksumCalculatingInputStream(md5, inputStream);
        byte[] readByteArray = new byte[10];
        testInput.read(readByteArray);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenChecksumDoesntMatchExpectedValue() throws IOException {
        Checksum md5 = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ChecksumCalculatingInputStream testInput = new ChecksumCalculatingInputStream(md5, inputStream);
        int readByte = testInput.read();
        int wrongChecksumByte = 4;
        checksumMismatch(testInput, wrongChecksumByte);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenReadNegativeLength() throws IOException {
        Checksum md5 = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ChecksumCalculatingInputStream testInput = new ChecksumCalculatingInputStream(md5, inputStream);
        testInput.skip(-1);
    }

    @Test
    public void testGetChecksumValue() throws IOException {
        Checksum md5 = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ChecksumCalculatingInputStream testInput = new ChecksumCalculatingInputStream(md5, inputStream);
        long expectedChecksum = getMD5Checksum(new byte[]{1, 2, 3});
        long actualChecksum = testInput.getValue();
        Assert.assertEquals(expectedChecksum, actualChecksum);
    }

    private long getMD5Checksum(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(input);
        long checksum = 0;
        for (int i = 0; i < md.getDigestLength(); i++) {
            checksum += ((long) digest[i] & 0xff) << (i * 8);
        }
        return checksum;
    }

    private void checksumMismatch(ChecksumCalculatingInputStream testInput, int wrongChecksumByte) throws IOException {
        int readByte = testInput.read();
        if (readByte == wrongChecksumByte) {
            throw new IOException("Checksum mismatch.");
        }
    }
}