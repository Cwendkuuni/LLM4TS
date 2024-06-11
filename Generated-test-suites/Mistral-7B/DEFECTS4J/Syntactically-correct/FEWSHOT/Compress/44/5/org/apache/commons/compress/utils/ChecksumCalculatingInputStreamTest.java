package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Checksum;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the ChecksumCalculatingInputStream class.
 */
public class ChecksumCalculatingInputStreamTest {

    private InputStream inputStream;
    private Checksum checksum;
    private ChecksumCalculatingInputStream checksumCalculatingInputStream;

    @Before
    public void setup() throws NoSuchAlgorithmException {
        inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        checksum = mock(Checksum.class);
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        assertEquals(inputStream.read(), checksumCalculatingInputStream.read());
        checksum.update((byte) 0x01);
        assertEquals(inputStream.read(), checksumCalculatingInputStream.read());
        checksum.update((byte) 0x02);
        assertEquals(inputStream.read(), checksumCalculatingInputStream.read());
        checksum.update((byte) 0x03);
        assertEquals(-1, checksumCalculatingInputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        final int expectedChecksum = 0x9d636663; // MD5 checksum for "abc"
        final byte[] expectedBytes = { 0x61, 0x62, 0x63 };

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        checksumCalculatingInputStream.read(outputStream.toByteArray(), 0, outputStream.size());
        assertArrayEquals(expectedBytes, outputStream.toByteArray());
        assertEquals(expectedChecksum, checksumCalculatingInputStream.getValue());
    }

    @Test
    public void testReadAndVerifyChecksum() throws IOException {
        final byte[] input = { 0x01, 0x02, 0x03, 0x04, 0x05 };
        final byte[] expectedChecksumBytes = { 0x01, 0x02 }; // MD5 checksum for "abcd"

        inputStream = new ByteArrayInputStream(input);
        checksum = mock(Checksum.class);

        when(checksum.getInstance("MD5")).thenReturn(mock(MessageDigest.class));

        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] inputCopy = new byte[input.length];
        System.arraycopy(input, 0, inputCopy, 0, input.length);
        byte[] output = new byte[input.length];

        int readBytes = checksumCalculatingInputStream.read(output, 0, output.length);
        assertArrayEquals(input, output);
        assertEquals(expectedChecksumBytes.length, readBytes);

        MessageDigest md = (MessageDigest) when(checksum.getInstance("MD5")).thenReturn(mock(MessageDigest.class));
        byte[] actualChecksumBytes = md.digest(output);
        assertArrayEquals(expectedChecksumBytes, actualChecksumBytes);
        assertEquals(expectedChecksumBytes.length * 2, checksumCalculatingInputStream.getValue());
    }

    @Test(expected = IOException.class)
    public void testReadWhenUnderlyingStreamThrows() throws IOException {
        when(inputStream.read()).thenThrow(IOException.class);
        checksumCalculatingInputStream.read();
    }

    @Test(expected = IOException.class)
    public void testReadWhenChecksumMismatch() throws IOException {
        when(inputStream.read()).thenAnswer(i -> i.read(new byte[0], 0, 0));
        checksumCalculatingInputStream.read(new byte[1], 0, 1);
        assertEquals(-1, checksumCalculatingInputStream.read());
    }

    @Test
    public void testReadWhenUnderlyingStreamExhausted() throws IOException {
        byte[] input = new byte[1024 * 1024];
        inputStream = new ByteArrayInputStream(input);

        byte[] buffer = new byte[1];
        int read = 0;
        while ((read = checksumCalculatingInputStream.read(buffer)) >= 0) {
            // consume the data
        }

        assertEquals(-1, checksumCalculatingInputStream.read());
    }
}