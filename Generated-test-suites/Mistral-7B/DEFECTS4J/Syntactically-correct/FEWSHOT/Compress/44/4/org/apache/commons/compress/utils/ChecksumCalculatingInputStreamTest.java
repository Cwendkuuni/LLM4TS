package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.After;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    private InputStream inputStream;

    @Test
    public void testChecksumCalculatingInputStreamWithMD5Checksum() throws IOException, NoSuchAlgorithmException {
        final byte[] input = "Apache Commons Compress Utils".getBytes();
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] expectedMD5Checksum = md.digest(input);

        final InputStream inputStream = new ByteArrayInputStream(input);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(new MessageDigestChecksum(md), inputStream);

        final byte[] readBytes = new byte[input.length];
        final int bytesRead = checksumCalculatingInputStream.read(readBytes, 0, readBytes.length);
        assertTrue(bytesRead == readBytes.length);

        final byte[] calculatedMD5Checksum = checksumCalculatingInputStream.getValue();
        assertArrayEquals(expectedMD5Checksum, calculatedMD5Checksum);
    }

    @Test
    public void testChecksumCalculatingInputStreamWithNullChecksum() {
        final InputStream inputStream = new ByteArrayInputStream("Apache Commons Compress Utils".getBytes());
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(null, inputStream);
        assertNull(checksumCalculatingInputStream.getValue());
    }

    @Test
    public void testChecksumCalculatingInputStreamWithNullInput() {
        final Checksum checksum = new MessageDigestChecksum(MessageDigest.getInstance("MD5"));
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, null);
        assertNull(checksumCalculatingInputStream.getValue());
    }

    @Test
    public void testChecksumCalculatingInputStreamWithZeroLengthInput() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(new MessageDigestChecksum(MessageDigest.getInstance("MD5")), inputStream);
        final byte[] calculatedMD5Checksum = checksumCalculatingInputStream.getValue();
        assertArrayEquals(new byte[0], calculatedMD5Checksum);
    }

    @Test
    public void testChecksumCalculatingInputStreamWithByteRead() throws IOException {
        final byte[] input = "Apache Commons Compress Utils".getBytes();
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] expectedMD5Checksum = md.digest(input);

        final InputStream inputStream = new ByteArrayInputStream(input);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(new MessageDigestChecksum(md), inputStream);

        final byte readByte = (byte) checksumCalculatingInputStream.read();
        assertSame(input[0], readByte);

        final byte[] calculatedMD5Checksum = checksumCalculatingInputStream.getValue();
        assertArrayEquals(expectedMD5Checksum, calculatedMD5Checksum);
    }

    @Test
    public void testChecksumCalculatingInputStreamWithByteArrayRead() throws IOException {
        final byte[] input = "Apache Commons Compress Utils".getBytes();
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] expectedMD5Checksum = md.digest(input);

        final InputStream inputStream = new ByteArrayInputStream(input);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(new MessageDigestChecksum(md), inputStream);

        final byte[] readBytes = new byte[input.length];
        final int bytesRead = checksumCalculatingInputStream.read(readBytes, 0, readBytes.length);
        assertTrue(bytesRead == readBytes.length);

        final byte[] calculatedMD5Checksum = checksumCalculatingInputStream.getValue();
        assertArrayEquals(expectedMD5Checksum, calculatedMD5Checksum);
    }

    @Test
    public void testChecksumCalculatingInputStreamWithByteArrayReadWithPartialRead() throws IOException {
        final byte[] input = "Apache Commons Compress Utils".getBytes();
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] expectedMD5Checksum = md.digest(input);

        final InputStream inputStream = new ByteArrayInputStream(input);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(new MessageDigestChecksum(md), inputStream);

        final byte[] readBytes = new byte[input.length / 2];
        final int bytesRead = checksumCalculatingInputStream.read(readBytes, 0, readBytes.length);
        assertTrue(bytesRead == readBytes.length);

        final byte[] calculatedMD5Checksum = checksumCalculatingInputStream.getValue();
        assertFalse(Arrays.equals(expectedMD5Checksum, calculatedMD5Checksum));
    }

    @Test
    public void testChecksumCalculatingInputStreamWithByteArrayReadWithNegativeOffset() throws IOException {
        final byte[] input = "Apache Commons Compress Utils".getBytes();
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] expectedMD5Checksum = md.digest(input);

        final InputStream inputStream = new ByteArrayInputStream(input);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(new MessageDigestChecksum(md), inputStream);

        final byte[] readBytes = new byte[input.length];
        final int bytesRead = checksumCalculatingInputStream.read(readBytes, -1, readBytes.length);
        assertFalse(bytesRead >= 0);
    }

    @Test
    public void testChecksumCalculatingInputStreamWithByteArrayReadWithNegativeLength() throws IOException {
        final byte[] input = "Apache Commons Compress Utils".getBytes();
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] expectedMD5Checksum = md.digest(input);

        final InputStream inputStream = new ByteArrayInputStream(input);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(new MessageDigestChecksum(md), inputStream);

        final byte[] readBytes = new byte[input.length];
        final int bytesRead = checksumCalculatingInputStream.read(readBytes, 0, -1);
        assertFalse(bytesRead >= 0);
    }

    @Test
    public void testChecksumCalculatingInputStreamWithByteArrayReadWithZeroLength() throws IOException {
        final byte[] input = "Apache Commons Compress Utils".getBytes();
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] expectedMD5Checksum = md.digest(input);

        final InputStream inputStream = new ByteArrayInputStream(input);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(new MessageDigestChecksum(md), inputStream);

        final byte[] readBytes = new byte[0];
        final int bytesRead = checksumCalculatingInputStream.read(readBytes, 0, readBytes.length);
        assertTrue(bytesRead == 0);

        final byte[] calculatedMD5Checksum = checksumCalculatingInputStream.getValue();
        assertArrayEquals(expectedMD5Checksum, calculatedMD5Checksum);
    }

    @After
    public void after() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }
}