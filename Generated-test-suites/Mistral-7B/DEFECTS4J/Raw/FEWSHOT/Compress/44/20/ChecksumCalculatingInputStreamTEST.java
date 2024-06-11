package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test cases for ChecksumCalculatingInputStream.
 */
public class ChecksumCalculatingInputStreamTest {
    private static final byte[] TEST_DATA = " checking stream test data ".getBytes(StandardCharsets.UTF_8);

    @Test
    public void testChecksumCalculatingInputStream() {
        final Checksum md5 = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(TEST_DATA);
        final ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(md5, input);

        final byte[] expectedMd5Sum = md5.digest(TEST_DATA);

        final int read = csis.read();
        assertTrue(read >= 0);

        final int readAll = csis.read(new byte[TEST_DATA.length]);
        assertTrue(readAll == TEST_DATA.length);

        assertArrayEquals(expectedMd5Sum, csis.getValue(), "Checksum doesn't match expected value.");

        try {
            csis.skip(TEST_DATA.length * 2);
            assertFalse("Should throw IOException when skipping more bytes than available in the stream.", true);
        } catch (IOException e) {
            // Expected
        }

        assertNull(csis.getValue(), "Checksum should be empty after closing the stream.");

        try {
            csis.read();
            assertFalse("Should throw IOException after closing the stream.", true);
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testChecksumCalculatingInputStreamWithEmptyInputStream() {
        final Checksum md5 = MessageDigest.getInstance("MD5");
        final InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        final ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(md5, emptyInput);
        final byte[] expectedMd5Sum = new byte[0];

        assertArrayEquals(expectedMd5Sum, csis.getValue(), "Checksum should be empty when reading from an empty stream.");
    }

    @Test(expected = NoSuchAlgorithmException.class)
    public void testChecksumCalculatingInputStreamWithInvalidAlgorithm() {
        final InputStream input = new ByteArrayInputStream(TEST_DATA);
        final ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(new Checksum() {
            @Override
            public void update(byte b) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void update(byte[] b, int off, int len) {
                throw new UnsupportedOperationException();
            }

            @Override
            public long getValue() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void reset() {
                throw new UnsupportedOperationException();
            }
        }, input);
    }

    @Test
    public void testChecksumCalculatingInputStreamWithClosedInputStream() {
        final Checksum md5 = MessageDigest.getInstance("MD5");
        final byte[] inputData = "test data".getBytes(StandardCharsets.UTF_8);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final InputStream input = new ByteArrayInputStream(inputData);
        final ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(md5, input);
        final byte[] expectedMd5Sum = md5.digest(inputData);

        final int read = csis.read();
        assertTrue(read >= 0);

        byteArrayOutputStream.write(inputData, 0, inputData.length);
        final byte[] actualMd5Sum = md5.digest(byteArrayOutputStream.toByteArray());

        assertArrayEquals(expectedMd5Sum, csis.getValue(), "Checksum doesn't match expected value.");
        assertArrayEquals(expectedMd5Sum, actualMd5Sum, "Calculated checksum doesn't match the expected MD5 sum.");

        try {
            csis.read();
            assertFalse("Should throw IOException after closing the stream.", true);
        } catch (IOException e) {
            // Expected
        }

        assertNull(csis.getValue(), "Checksum should be empty after closing the stream.");
    }
}