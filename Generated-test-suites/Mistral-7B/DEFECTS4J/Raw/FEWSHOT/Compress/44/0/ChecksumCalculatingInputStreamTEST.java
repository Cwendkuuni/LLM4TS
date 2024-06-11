package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link ChecksumCalculatingInputStream}.
 */
public class ChecksumCalculatingInputStreamTest {

    private Checksum checksum;
    private InputStream inputStream;
    private ChecksumCalculatingInputStream checksumCalculatingInputStream;

    @Before
    public void setUp() throws IOException, NoSuchAlgorithmException {
        checksum = mock(Checksum.class);
        inputStream = mock(InputStream.class);
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        byte testByte = 1;
        byte readByte = 1;
        verify(inputStream).read();
        assertEquals(testByte, readByte);
        checksum.update(testByte);
    }

    @Test
    public void testReadSingleByteNegative() throws IOException {
        verify(inputStream).read();
        assertEquals(-1, checksumCalculatingInputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] testBytes = new byte[] { 1, 2, 3 };
        byte[] readBytes = new byte[] { 1, 2, 3 };
        byte[] buffer = new byte[testBytes.length];

        verify(inputStream).read(buffer);
        assertArrayEquals(testBytes, readBytes);
        checksum.update(testBytes);
    }

    @Test
    public void testReadByteArrayNegative() throws IOException {
        byte[] testBytes = new byte[] { 1, 2, 3 };
        byte[] readBytes = new byte[testBytes.length];
        byte[] buffer = new byte[testBytes.length + 1];

        verify(inputStream).read(buffer);
        assertEquals(-1, checksumCalculatingInputStream.read(readBytes));
    }

    @Test
    public void testReadByteArrayNegativeFirst() throws IOException {
        byte[] testBytes = new byte[] { 1, 2, 3 };
        byte[] readBytes = new byte[testBytes.length];
        byte[] buffer = new byte[1];

        verify(inputStream).read(buffer);
        assertEquals(-1, checksumCalculatingInputStream.read(readBytes));
    }

    @Test
    public void testReadByteArrayNegativeLast() throws IOException {
        byte[] testBytes = new byte[] { 1, 2, 3 };
        byte[] readBytes = new byte[testBytes.length];
        byte[] buffer = new byte[testBytes.length + 1];

        verify(inputStream).read(buffer);
        assertEquals(0, checksumCalculatingInputStream.read(readBytes));
    }

    @Test
    public void testSkip() throws IOException {
        long testSkip = 1L;
        verify(inputStream).read();
        assertEquals(1, checksumCalculatingInputStream.skip(testSkip));
    }

    @Test
    public void testSkipNegative() throws IOException {
        assertEquals(0, checksumCalculatingInputStream.skip(-1L));
    }

    @Test
    public void testGetValue() {
        MessageDigest md = mock(MessageDigest.class);
        when(checksum.getAlgorithmName()).thenReturn("MD5");
        when(checksum.getValue()).thenReturn(new byte[] { 1, 2, 3, 4, 5 });
        when(checksum.getBlockSize()).thenReturn(16);
        when(checksum.getByteArray()).thenReturn(md.digest());

        byte[] expectedValue = new byte[] { 1, 2, 3, 4, 5 };
        byte[] readBytes = new byte[checksum.getBlockSize()];

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream os = baos;
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(new byte[]{}));
        checksumCalculatingInputStream.writeTo(os);

        assertEquals(expectedValue, checksumCalculatingInputStream.getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClose() throws IOException {
        checksumCalculatingInputStream.close();
    }
}