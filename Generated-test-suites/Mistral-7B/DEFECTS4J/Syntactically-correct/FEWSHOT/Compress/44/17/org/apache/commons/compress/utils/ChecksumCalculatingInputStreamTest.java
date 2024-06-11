package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;

/**
 * Test cases for {@link ChecksumCalculatingInputStream} class.
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test reading single byte from the stream.
     * @throws IOException if an error occurs while reading from the stream.
     */
    @Test
    public void testReadSingleByte() throws IOException {
        final byte expected = 0x01;
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { expected });
        final Checksum checksum = mock(Checksum.class);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int actual = checksumCalculatingInputStream.read();

        assertEquals(expected, actual);
        verify(checksum).update(expected);
    }

    /**
     * Test reading byte array from the stream.
     * @throws IOException if an error occurs while reading from the stream.
     */
    @Test
    public void testReadByteArray() throws IOException {
        final byte[] expected = new byte[] { 0x01, 0x02, 0x03 };
        final InputStream inputStream = new ByteArrayInputStream(expected);
        final Checksum checksum = mock(Checksum.class);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int length = checksumCalculatingInputStream.read(expected, 0, expected.length);

        assertEquals(expected.length, length);
        assertArrayEquals(expected, expected);
        verify(checksum).update(expected);
    }

    /**
     * Test reading zero bytes from the stream.
     * @throws IOException if an error occurs while reading from the stream.
     */
    @Test
    public void testReadZeroBytes() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final Checksum checksum = mock(Checksum.class);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int actual = checksumCalculatingInputStream.read();

        assertEquals(-1, actual);
    }

    /**
     * Test getting the calculated checksum.
     * @throws NoSuchAlgorithmException if the specified algorithm is not supported.
     */
    @Test
    public void testGetValue() throws NoSuchAlgorithmException, IOException {
        final Checksum checksum = mock(Checksum.class);
        final MessageDigest messageDigest = mock(MessageDigest.class);
        when(checksum.getChecksum()).thenReturn(messageDigest);
        when(messageDigest.digest()).thenReturn(new byte[Checksum.SIZE]);
        final InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final long actual = checksumCalculatingInputStream.getValue();

        assertEquals(Checksum.SIZE, actual);
        verify(checksum).getValue();
    }
}