package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;

/**
 * Test cases for ChecksumCalculatingInputStream class
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test constructor
     */
    @org.junit.Test
    public void testConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[] { 1 });
        Checksum checksum = ChecksumUtils.getChecksum("MD5");
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);
    }

    /**
     * Test read method
     */
    @org.junit.Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1 });
        Checksum checksum = ChecksumUtils.getChecksum("MD5");
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        int read = cis.read();
        assertEquals(-1, read);

        byte[] buffer = new byte[1];
        read = cis.read(buffer);
        assertEquals(1, read);
        assertEquals((byte) 1, buffer[0]);
        checksum.update(buffer);
        byte[] expected = new byte[] { 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3a, 0x3b, 0x3c, 0x3d, 0x3e, 0x3f };
        assertArrayEquals(expected, checksum.getDigest());
    }

    /**
     * Test read method with empty buffer
     */
    @org.junit.Test
    public void testReadEmptyBuffer() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1 });
        Checksum checksum = ChecksumUtils.getChecksum("MD5");
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        byte[] buffer = new byte[0];
        int read = cis.read(buffer);
        assertEquals(0, read);
        assertEquals(0, buffer.length);
    }

    /**
     * Test read method with larger buffer than available data
     */
    @org.junit.Test
    public void testReadLargerBuffer() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1 });
        Checksum checksum = ChecksumUtils.getChecksum("MD5");
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        byte[] buffer = new byte[3];
        int read = cis.read(buffer);
        assertEquals(1, read);
        assertEquals((byte) 1, buffer[0]);
        checksum.update(buffer);
        byte[] expected = new byte[] { 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3a, 0x3b, 0x3c, 0x3d, 0x3e, 0x3f };
        assertArrayEquals(expected, checksum.getDigest());
    }

    /**
     * Test read method with null buffer
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testReadNullBuffer() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1 });
        Checksum checksum = ChecksumUtils.getChecksum("MD5");
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        byte[] buffer = null;
        int read = cis.read(buffer);
    }

    /**
     * Test read method with exhausted stream
     */
    @org.junit.Test
    public void testReadExhaustedStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        Checksum checksum = ChecksumUtils.getChecksum("MD5");
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        int read = cis.read();
        assertEquals(1, read);
        byte[] buffer = new byte[1];
        read = cis.read(buffer);
        assertEquals(2, read);
        checksum.update(buffer);
        buffer = new byte[1];
        read = cis.read(buffer);
        assertEquals(-1, read);
    }

    /**
     * Test skip method
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1 });
        Checksum checksum = ChecksumUtils.getChecksum("MD5");
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        cis.skip(5);
    }

    /**
     * Test getValue method
     */
    @org.junit.Test
    public void testGetValue() throws IOException, NoSuchAlgorithmException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        Checksum checksum = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        int read = cis.read();
        byte[] buffer = new byte[1];
        read = cis.read(buffer);

        cis.getValue();

        byte[] expected = new byte[] { 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3a, 0x3b, 0x3c, 0x3d, 0x3e, 0x3f };
        assertArrayEquals(expected, checksum.digest());
    }
}