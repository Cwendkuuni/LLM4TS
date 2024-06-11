package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.zip.Adler32;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the ChecksumCalculatingInputStream class
 */
public class ChecksumCalculatingInputStreamTest {

    private InputStream input;
    private ChecksumCalculatingInputStream checksumInputStream;
    private Adler32 checksum;

    @Before
    public void setUp() {
        input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        checksum = new Adler32();
        checksumInputStream = new ChecksumCalculatingInputStream(checksum, input);
    }

    @After
    public void tearDown() throws IOException {
        checksumInputStream.close();
    }

    /**
     * Test reading a single byte
     * @throws IOException if an error occurs while reading
     */
    @Test
    public void testReadByte() throws IOException {
        final int byteRead = checksumInputStream.read();
        assertEquals(-1, byteRead);
        assertEquals(0x0102, checksum.getValue());
    }

    /**
     * Test reading a byte array
     * @throws IOException if an error occurs while reading
     */
    @Test
    public void testReadByteArray() throws IOException {
        final byte[] buffer = new byte[3];
        final int bytesRead = checksumInputStream.read(buffer);
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[] { 0x01, 0x02, 0x03 }, buffer);
        assertEquals(0x0102, checksum.getValue());
    }

    /**
     * Test reading a byte array with an offset
     * @throws IOException if an error occurs while reading
     */
    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        final byte[] buffer = new byte[3];
        final int bytesRead = checksumInputStream.read(buffer, 1);
        assertEquals(2, bytesRead);
        assertArrayEquals(new byte[] { 0x02, 0x03 }, buffer);
        assertEquals(0x0102, checksum.getValue());
    }

    /**
     * Test reading a byte array with a negative length
     * @throws IOException if an error occurs while reading
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadByteArrayWithNegativeLength() throws IOException {
        checksumInputStream.read(new byte[] { 0x01, 0x02, 0x03 }, -1);
    }

    /**
     * Test reading a byte array with a length greater than the remaining data
     * @throws IOException if an error occurs while reading
     */
    @Test
    public void testReadByteArrayWithLengthGreaterThanRemainingData() throws IOException {
        final byte[] buffer = new byte[6];
        final int bytesRead = checksumInputStream.read(buffer);
        assertEquals(-1, bytesRead);
        assertEquals(0x0102, checksum.getValue());
    }

    /**
     * Test getting the calculated checksum
     */
    @Test
    public void testGetValue() {
        assertEquals(0x0102, checksumInputStream.getValue());
    }

    /**
     * Test handling streams with different checksums
     * @throws IOException if an error occurs while reading
     */
    @Test(expected = IOException.class)
    public void testMismatchedChecksum() throws IOException {
        final InputStream inputWithMismatchedChecksum = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        final ChecksumCalculatingInputStream checksumInputStreamWithMismatchedChecksum = new ChecksumCalculatingInputStream(mock(Checksum.class), inputWithMismatchedChecksum);
        checksumInputStreamWithMismatchedChecksum.read();
    }
}