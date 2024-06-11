package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculation() throws IOException, NoSuchAlgorithmException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Test data for ChecksumCalculatingInputStreamTest".getBytes(StandardCharsets.UTF_8));
        final byte[] testData = outputStream.toByteArray();
        final MessageDigest md = MessageDigest.getInstance("MD5");

        try (final InputStream inputStream = new ChecksumCalculatingInputStream(md, new ByteArrayInputStream(testData))) {
            IOUtils.copy(inputStream, outputStream);
        }

        final byte[] calculatedChecksum = md.digest();
        final byte[] expectedChecksum = new byte[]{(byte) 0x27, (byte) 0x43, (byte) 0x12, (byte) 0xEB, (byte) 0x24, (byte) 0x75, (byte) 0x1D, (byte) 0x1D, (byte) 0x75, (byte) 0x24, (byte) 0xEB, (byte) 0x1D, (byte) 0x75, (byte) 0x24, (byte) 0x04, (byte) 0x03, (byte) 0x01, (byte) 0x00};
        Assert.assertArrayEquals("Checksums do not match", expectedChecksum, calculatedChecksum);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenStreamExhausted() throws IOException {
        try (final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(null, new ByteArrayInputStream(new byte[0]))) {
            checksumCalculatingInputStream.read();
            checksumCalculatingInputStream.read();
            checksumCalculatingInputStream.read();
            checksumCalculatingInputStream.read();
            checksumCalculatingInputStream.read();
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullChecksum() {
        try {
            new ChecksumCalculatingInputStream(null, new ByteArrayInputStream(new byte[0]));
        } catch (IOException e) {
            throw new NullPointerException("Unexpected IOException", e);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputStream() {
        try {
            new ChecksumCalculatingInputStream(new Checksum(), null);
        } catch (IOException e) {
            throw new NullPointerException("Unexpected IOException", e);
        }
    }
}