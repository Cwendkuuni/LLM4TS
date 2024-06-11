package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache. CommonsIO;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculation() throws IOException, NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] inputData = "Test Data".getBytes();
        final InputStream inputStream = new ByteArrayInputStream(inputData);
        final ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(md, inputStream);
        final int read = checksumStream.read(inputData, 0, inputData.length);
        Assert.assertEquals(inputData.length, read);
        final long calculatedChecksum = checksumStream.getValue();
        final byte[] expectedChecksum = md.digest(inputData);
        Assert.assertArrayEquals(expectedChecksum, CommonsIO.toByteArray(new File("expectedChecksum.md5")));
        Assert.assertEquals(BytesUtil.bytesToLong(expectedChecksum), calculatedChecksum);
    }

    @Test(expected = IOException.class)
    public void testExceptionWhenStreamIsExhausted() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(null, inputStream);
        checksumStream.read();
        checksumStream.read();
        checksumStream.read();
    }

    @Test
    public void testCorrectChecksumCalculation() throws IOException {
        final String fileName = "testFile.txt";
        try ( final InputStream inputStream = new FileInputStream(fileName);
             final ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);) {
            final long expectedChecksum = Files.probeContentType(new File(fileName)).equals("text/plain") ? 123456789012L : 0L;
            final long actualChecksum = checksumStream.getValue();
            Assert.assertEquals(expectedChecksum, actualChecksum);
        }
    }
}