package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;

/**
 * Test cases for the ChecksumCalculatingInputStream class
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test constructor with valid arguments
     * @throws IOException if the constructor throws or the test data is corrupted
     */
    @Test
    public void testConstruction() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        final ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(md, input);
        // basic functionality test is performed in other test methods
    }

    /**
     * Test read method with valid data
     * @throws IOException if the method throws
     */
    @Test
    public void testReadValidData() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        final ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(md, input);

        final int readByte = cis.read();
        assertEquals(1, readByte);

        final byte[] readBytes = new byte[3];
        final int bytesRead = cis.read(readBytes);
        assertEquals(3, bytesRead);
        assertEquals(1, readBytes[0]);
        assertEquals(2, readBytes[1]);
        assertEquals(3, readBytes[2]);
    }

    /**
     * Test read method with exhausted stream
     * @throws IOException if the method throws
     */
    @Test
    public void testReadExhaustedStream() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        final ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(md, input);

        final int readByte = cis.read();
        assertEquals(1, readByte);

        final byte[] readBytes = new byte[3];
        final int bytesRead = cis.read(readBytes);
        assertEquals(3, bytesRead);

        // Test that reading past EOF does not throw an exception
        final int readAfterEof = cis.read();
        assertEquals(-1, readAfterEof);
    }

    /**
     * Test read method with malformed data
     * @throws IOException if the method throws
     * @throws NoSuchAlgorithmException if MessageDigest is not available
     */
    @Test
    public void testReadMalformedData() throws IOException, NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, (byte) 0xFF });
        final ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(md, input);

        final int readByte = cis.read();
        assertEquals(1, readByte);

        final byte[] readBytes = new byte[3];
        final int bytesRead = cis.read(readBytes);
        assertEquals(2, bytesRead);
        assertEquals(1, readBytes[0]);
        assertEquals(255, readBytes[1]);

        // Test that reading past EOF throws an exception
        final int readAfterEof = cis.read();
        assertEquals(-1, readAfterEof);

        // Test that reading beyond the capacity of the byte array throws an exception
        final byte[] readBytesBeyondCapacity = new byte[2];
        assertThrows(IOException.class, () -> cis.read(readBytesBeyondCapacity));
    }

    /**
     * Test skip method with valid data
     * @throws IOException if the method throws
     */
    @Test
    public void testSkipValidData() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        final ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(md, input);

        final long bytesSkipped = cis.skip(2);
        assertEquals(2, bytesSkipped);

        final int readByte = cis.read();
        assertEquals(3, readByte);
    }

    /**
     * Test skip method with exhausted stream
     * @throws IOException if the method throws
     */
    @Test
    public void testSkipExhaustedStream() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        final ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(md, input);

        final long bytesSkipped = cis.skip(5);
        assertEquals(3, bytesSkipped); // All data has been read from the input stream

        // Test that skipping past the available data does not throw an exception
        final long bytesSkippedAfterEof = cis.skip(2);
        assertEquals(0, bytesSkippedAfterEof);
    }

    /**
     * Test getValue method with valid data
     * @throws IOException if the method throws
     * @throws NoSuchAlgorithmException if MessageDigest is not available
     */
    @Test
    public void testGetValueValidData() throws IOException, NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        final ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(md, input);

        final byte[] expectedMd = new byte[] { 0x30, 0xCC, 0x42, 0x2D, 0x34, 0x9B, 0x43, 0x4A, 0x97, 0x0A, 0xB0, 0xE6, 0xD4, 0x74, 0x8A, 0x6E};
        final byte[] actualMd = new byte[16];

        final int bytesRead = cis.read(actualMd);
        assertEquals(16, bytesRead);

        assertEquals(expectedMd, actualMd);
        assertEquals(expectedMd[0], cis.getValue() & 0xFF);
        assertEquals(expectedMd[1], (cis.getValue() >> 8) & 0xFF);
        assertEquals(expectedMd[2], (cis.getValue() >> 16) & 0xFF);
        assertEquals(expectedMd[3], (cis.getValue() >> 24) & 0xFF);
    }

    /**
     * Test getValue method with exhausted stream
     * @throws IOException if the method throws
     * @throws NoSuchAlgorithmException if MessageDigest is not available
     */
    @Test
    public void testGetValueExhaustedStream() throws IOException, NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        final ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(md, input);

        final byte[] expectedMd = new byte[] { 0x30, 0xCC, 0x42, 0x2D, 0x34, 0x9B, 0x43, 0x4A, 0x97, 0x0A, 0xB0, 0xE6, 0xD4, 0x74, 0x8A, 0x6E};
        final byte[] actualMd = new byte[16];

        final int bytesRead = cis.read(actualMd);
        assertEquals(16, bytesRead);

        // Test that reading past EOF does not change the checksum
        cis.skip(3);
        final long actualValue = cis.getValue();
        assertEquals(expectedMd[0], (actualValue >> 0) & 0xFF);
        assertEquals(expectedMd[1], (actualValue >> 8) & 0xFF);
        assertEquals(expectedMd[2], (actualValue >> 16) & 0xFF);
        assertEquals(expectedMd[3], (actualValue >> 24) & 0xFF);
    }

    /**
     * Test getValue method with malformed data
     * @throws IOException if the method throws
     * @throws NoSuchAlgorithmException if MessageDigest is not available
     */
    @Test
    public void testGetValueMalformedData() throws IOException, NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, (byte) 0xFF });
        final ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(md, input);

        final byte[] expectedMd = new byte[] { 0x30, 0xCC, 0x42, 0x2D, 0x34, 0x9B, 0x43, 0x4A, 0x97, 0x0A, 0xB0, 0xE6, 0xD4, 0x74, 0x8A, 0x6E};
        final byte[] actualMd = new byte[16];

        final int bytesRead = cis.read(actualMd);
        assertEquals(15, bytesRead); // The last byte is read as -1 due to EOF

        // Test that reading past EOF does not change the checksum
        cis.skip(1);
        final long actualValue = cis.getValue();
        assertEquals(expectedMd[0], (actualValue >> 0) & 0xFF);
        assertEquals(expectedMd[1], (actualValue >> 8) & 0xFF);
        assertEquals(expectedMd[2], (actualValue >> 16) & 0xFF);
        assertEquals(expectedMd[3], (actualValue >> 24) & 0xFF);
    }
}