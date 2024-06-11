package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the {@link ChecksumCalculatingInputStream} class.
 */
public class ChecksumCalculatingInputStreamTest {

    private static final byte[] TEST_DATA = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };
    private static final int TEST_DATA_LENGTH = TEST_DATA.length;
    private static final long TEST_CHECKSUM = 1L; // Placeholder for actual checksum value
    private ChecksumCalculatingInputStream inputStream;
    private InputStream mockInput;
    private Checksum checksum;

    @Before
    public void setUp() throws IOException, NoSuchAlgorithmException {
        mockInput = mock(InputStream.class);
        checksum = spy(new Adler32());
        inputStream = new ChecksumCalculatingInputStream(checksum, mockInput);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        whenReadSingleByteReturns(0x01);
        assertEquals((int) 0x01, inputStream.read());
        verify(checksum).update((byte) 0x01);
    }

    @Test
    public void testReadSingleByteReturnsMinusOne() throws IOException {
        whenReadSingleByteReturns(-1);
        assertNull(inputStream.read());
        verify(checksum, never()).update((byte) 0xFF);
    }

    @Test
    public void testReadByteArray() throws IOException {
        whenReadByteArrayReturns(TEST_DATA);
        byte[] readBytes = new byte[TEST_DATA_LENGTH];
        int bytesRead = inputStream.read(readBytes);
        assertEquals(TEST_DATA_LENGTH, bytesRead);
        assertArrayEquals(TEST_DATA, readBytes);
        verify(checksum).update(TEST_DATA);
    }

    @Test
    public void testReadByteArrayReturnsZero() throws IOException {
        whenReadByteArrayReturns(new byte[0]);
        byte[] readBytes = new byte[TEST_DATA_LENGTH];
        int bytesRead = inputStream.read(readBytes);
        assertEquals(0, bytesRead);
        assertArrayEquals(new byte[0], readBytes);
        verify(checksum, never()).update(TEST_DATA);
    }

    @Test
    public void testReadByteArrayNull() throws IOException {
        whenReadByteArrayReturnsNull();
        inputStream.read(null);
        verify(checksum, never()).update(null);
    }

    @Test
    public void testSkip() throws IOException {
        whenReadSingleByteReturns(0x01);
        when(mockInput.skip(TEST_DATA_LENGTH)).thenReturn(TEST_DATA_LENGTH);
        inputStream.skip(TEST_DATA_LENGTH);
        verify(checksum).update(TEST_DATA);
        verify(mockInput).skip(TEST_DATA_LENGTH);
    }

    @Test
    public void testGetValue() {
        checksum.update(TEST_DATA);
        assertEquals(TEST_CHECKSUM, inputStream.getValue());
    }

    private void whenReadSingleByteReturns(int byteToRead) throws IOException {
        when(mockInput.read()).thenReturn(byteToRead);
    }

    private void whenReadByteArrayReturns(byte[] bytesToRead) throws IOException {
        when(mockInput.read(anyByteArray(), eq(0), anyInt())).thenReturn(TEST_DATA_LENGTH);
        when(mockInput.readAllBytes()).thenReturn(TEST_DATA);
    }

    private void whenReadByteArrayReturnsNull() throws IOException {
        when(mockInput.readAllBytes()).thenReturn(null);
    }
}