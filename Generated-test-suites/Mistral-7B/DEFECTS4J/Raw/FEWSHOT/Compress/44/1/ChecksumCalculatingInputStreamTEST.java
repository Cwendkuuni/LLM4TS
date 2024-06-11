package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;

/**
 * Test cases for ChecksumCalculatingInputStream class
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test case for constructor
     */
    @Test
    public void testConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        Checksum checksum = createMockChecksum();
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);
        assertEquals(input, cis.in);
        assertEquals(checksum, cis.checksum);
    }

    /**
     * Test case for read method with single byte
     */
    @Test
    public void testReadSingleByte() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01 });
        Checksum checksum = createMockChecksum();
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        byte expectedByte = 0x01;
        int actualByte = cis.read();

        assertEquals(expectedByte, actualByte);
        assertTrue(checksum.isIntegrityIntact());
        assertEquals(expectedByte, checksum.getValue());
    }

    /**
     * Test case for read method with multiple bytes
     */
    @Test
    public void testReadMultipleBytes() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        Checksum checksum = createMockChecksum();
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        byte[] expectedBytes = new byte[] { 0x01, 0x02, 0x03 };
        byte[] actualBytes = new byte[3];
        int actualBytesRead = cis.read(actualBytes);

        assertArrayEquals(expectedBytes, actualBytes);
        assertTrue(checksum.isIntegrityIntact());
        assertEquals(expectedBytes.length, checksum.getValue());
    }

    /**
     * Test case for read method with empty byte array
     */
    @Test
    public void testReadEmptyByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Checksum checksum = createMockChecksum();
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        int actualBytesRead = cis.read(new byte[0]);

        assertEquals(0, actualBytesRead);
        assertTrue(checksum.isIntegrityIntact());
        assertEquals(0, checksum.getValue());
    }

    /**
     * Test case for read method with null byte array
     */
    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        Checksum checksum = createMockChecksum();
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        cis.read(null);
    }

    /**
     * Test case for read method with negative offset
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testReadNegativeOffset() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        Checksum checksum = createMockChecksum();
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        byte[] actualBytes = new byte[3];
        cis.read(actualBytes, -1, 3);
    }

    /**
     * Test case for read method with negative length
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testReadNegativeLength() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        Checksum checksum = createMockChecksum();
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        byte[] actualBytes = new byte[3];
        cis.read(actualBytes, 0, -1);
    }

    /**
     * Test case for read method with zero length
     */
    @Test
    public void testReadZeroLength() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        Checksum checksum = createMockChecksum();
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        byte[] actualBytes = new byte[0];
        int actualBytesRead = cis.read(actualBytes);

        assertEquals(0, actualBytesRead);
        assertTrue(checksum.isIntegrityIntact());
        assertEquals(0, checksum.getValue());
    }

    /**
     * Test case for skip method
     */
    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        Checksum checksum = createMockChecksum();
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        long expectedBytesToSkip = 1;
        long actualBytesSkipped = cis.skip(expectedBytesToSkip);

        assertEquals(expectedBytesToSkip, actualBytesSkipped);
        assertTrue(checksum.isIntegrityIntact());
        assertEquals(expectedBytesToSkip, checksum.getValue());
    }

    /**
     * Test case for skip method with negative number
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegativeNumber() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        Checksum checksum = createMockChecksum();
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        cis.skip(-1);
    }

    /**
     * Test case for getValue method
     */
    @Test
    public void testGetValue() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        Checksum checksum = createMockChecksum();
        ChecksumCalculatingInputStream cis = new ChecksumCalculatingInputStream(checksum, input);

        long expectedValue = 0x010203L;
        long actualValue = cis.getValue();

        assertEquals(expectedValue, actualValue);
        assertTrue(checksum.isIntegrityIntact());
    }

    /**
     * Helper method to create a mock checksum
     */
    private Checksum createMockChecksum() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return new Checksum() {
                @Override
                public int getBlockSize() {
                    return md.getBlockSize();
                }

                @Override
                public int calculateBlockValue(byte[] data, int offset, int len) {
                    md.update(data, offset, len);
                    return len;
                }

                @Override
                public long getValue() {
                    byte[] digest = md.digest();
                    return ByteBuffer.wrap(digest).getLong();
                }

                @Override
                public void reset() {
                    md.reset();
                }

                @Override
                public boolean isIntegrityIntact() {
                    return true;
                }
            };
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unexpected error", e);
        }
    }
}