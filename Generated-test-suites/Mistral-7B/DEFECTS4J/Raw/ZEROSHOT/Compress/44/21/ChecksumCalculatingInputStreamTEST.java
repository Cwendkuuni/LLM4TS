package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test class for {@link ChecksumCalculatingInputStream}
 */
public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream_SingleByte() throws IOException {
        final byte testByte = 'A';
        final Checksum checksum = Checksums.checksum(Checksums.MD5);
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{testByte});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int read = checksumCalculatingInputStream.read();
        Assert.assertEquals(-1, read);
        final byte[] expectedMd5 = {0x31, 0x30, 0x31, 0x31, 0x31, 0x30, 0x34, 0x32, 0x2E, 0x31, 0x30, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31};
        final byte[] actualMd5 = getBytesFromChecksum(checksumCalculatingInputStream.getValue());
        Assert.assertArrayEquals(expectedMd5, actualMd5);
    }

    @Test
    public void testChecksumCalculatingInputStream_ByteArray() throws IOException, NoSuchAlgorithmException {
        final byte[] testData = "Apache Commons Compress".getBytes(StandardCharsets.UTF_8);
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(testData);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int read = checksumCalculatingInputStream.read(new byte[testData.length]);
        Assert.assertEquals(-1, read);

        final byte[] expectedMd5 = testData;
        final byte[] actualMd5 = getBytesFromChecksum(checksumCalculatingInputStream.getValue());
        Assert.assertArrayEquals(expectedMd5, actualMd5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChecksumCalculatingInputStream_Skip() throws IOException {
        final byte[] testData = "Apache Commons Compress".getBytes(StandardCharsets.UTF_8);
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(testData);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        checksumCalculatingInputStream.skip(testData.length);
    }

    private byte[] getBytesFromChecksum(long checksumValue) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Checksum checksum = Checksums.checksum(Checksums.MD5);
        checksum.update(new byte[0]);
        checksum.update((byte) 0);
        checksum.updateLong(checksumValue);
        outputStream.write(checksum.digest(), 0, checksum.digest().length);
        return outputStream.toByteArray();
    }
}