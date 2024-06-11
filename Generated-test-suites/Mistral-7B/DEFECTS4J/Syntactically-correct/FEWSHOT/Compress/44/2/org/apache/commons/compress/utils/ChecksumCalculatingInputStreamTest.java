package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() {
        // Test empty stream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), emptyInputStream);
        assertNull(checksumCalculatingInputStream.getValue());

        // Test regular input
        String testString = "Test string for checksum test.";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        byte[] expected = new byte[16]; // MD5 has a length of 16 bytes
        inputStream.read(expected);
        assertArrayEquals(expected, checksumCalculatingInputStream.getValue());
    }

    @Test
    public void testReadSingleByte() throws IOException {
        String testString = "Test string for checksum test.";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        byte testByte = 'A';
        byte readByte;

        readByte = (byte) checksumCalculatingInputStream.read();
        assertTrue(readByte == testByte || readByte == -1);

        readByte = (byte) checksumCalculatingInputStream.read();
        assertTrue(readByte >= 0);

        checksumCalculatingInputStream.close();

        byte[] expected = new byte[16]; // MD5 has a length of 16 bytes
        inputStream.read(expected);
        assertArrayEquals(expected, checksumCalculatingInputStream.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        String testString = "Test string for checksum test.";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        byte[] inputByteArray = testString.getBytes(StandardCharsets.UTF_8);
        byte[] expected = new byte[16]; // MD5 has a length of 16 bytes

        checksumCalculatingInputStream.read(expected);
        assertArrayEquals(expected, inputByteArray);
        assertArrayEquals(expected, checksumCalculatingInputStream.getValue());
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        String testString = "Test string for checksum test.";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        byte[] inputByteArray = testString.getBytes(StandardCharsets.UTF_8);
        byte[] expected = new byte[16]; // MD5 has a length of 16 bytes
        int offset = 0;
        int length = 8;

        checksumCalculatingInputStream.read(expected, offset, length);
        assertArrayEquals(expected, Arrays.copyOfRange(inputByteArray, offset, offset + length));
        assertArrayEquals(expected, Arrays.copyOfRange(checksumCalculatingInputStream.getValue(), 0, length));
    }

    @Test
    public void testSkip() throws IOException {
        String testString = "Test string for checksum test.";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        long expected = testString.length();

        long skipped = checksumCalculatingInputStream.skip(expected);
        assertTrue(skipped == inputStream.available());
        assertTrue(Arrays.equals(testString.getBytes(StandardCharsets.UTF_8), checksumCalculatingInputStream.getValue()));
    }

    @Test
    public void testClose() throws IOException {
        String testString = "Test string for checksum test.";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);

        checksumCalculatingInputStream.close();
        assertNull(checksumCalculatingInputStream.getValue());
    }

    @Test
    public void testNullArguments() {
        try {
            new ChecksumCalculatingInputStream(null, null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testClosedStream() throws IOException {
        String testString = "Test string for checksum test.";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        byte[] expected = new byte[16]; // MD5 has a length of 16 bytes

        checksumCalculatingInputStream.close();

        checksumCalculatingInputStream.read(expected);
        assertNull(expected);
    }

    @Test
    public void testChecksumCalculatingInputStreamWithInvalidAlgorithm() {
        try {
            new ChecksumCalculatingInputStream(null, new ByteArrayInputStream(new byte[0]));
            fail();
        } catch (NoSuchAlgorithmException e) {
            // expected
        }
    }
}