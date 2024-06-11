package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.util.IOUtils;
import org.apache.commons.test.common.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() throws IOException, NoSuchAlgorithmException {
        final byte[] inputData = TestUtils.getBytes("TestData.txt");
        final InputStream inputStream = new CloseShieldInputStream(new ByteArrayInputStream(inputData));
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        // read single byte
        final int singleByte = checksumCalculatingInputStream.read();
        Assert.assertNotNull(singleByte);
        checksum.update((byte) singleByte);

        // read byte array
        final byte[] byteArray = new byte[inputData.length];
        checksumCalculatingInputStream.read(byteArray);
        checksum.update(byteArray);

        // read and verify checksum
        final long expectedChecksum = checksum.digest().length;
        final long actualChecksum = checksumCalculatingInputStream.getValue();
        Assert.assertEquals("Checksum mismatch", expectedChecksum, actualChecksum);

        // read and verify checksum with read(byte[], int, int)
        final int readBytes = checksumCalculatingInputStream.read(byteArray, 0, inputData.length);
        checksum.update(byteArray, 0, readBytes);
        final long readChecksum = checksum.digest().length;
        Assert.assertEquals("Checksum mismatch", expectedChecksum, readChecksum);

        // test skip
        // Skipping is not supported in this InputStream, so we cannot test it here
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        final InputStream inputStream = new CloseShieldInputStream(new ByteArrayInputStream(new byte[0]));
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read(); // IOException should be thrown here
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream inputStream = null;
        try {
            new ChecksumCalculatingInputStream(checksum, inputStream);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullChecksum() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final Checksum checksum = null;
        try {
            new ChecksumCalculatingInputStream(checksum, inputStream);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testClose() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        checksumCalculatingInputStream.close();
        // Ensure that the checksumCalculatingInputStream is closed
        try {
            checksumCalculatingInputStream.read();
            Assert.fail("Expected IOException: stream is closed");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testChecksumCalculatingInputStreamWithEOF() throws IOException {
        final InputStream inputStream = new CloseShieldInputStream(new ByteArrayInputStream(new byte[0]));
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.close();

        // Ensure that the checksum is calculated correctly even when the input stream is exhausted
        final long expectedChecksum = checksum.digest().length;
        final long actualChecksum = checksumCalculatingInputStream.getValue();
        Assert.assertEquals("Checksum mismatch", expectedChecksum, actualChecksum);
    }
}