package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

import org.junit.Before;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    private InputStream inputStream;
    private ChecksumCalculatingInputStream checksumCalculatingInputStream;
    private Checksum checksum;

    @Before
    public void setUp() {
        checksum = new Adler32();
        inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        assertEquals(inputStream.read(), checksumCalculatingInputStream.read());
        verify(checksum).update((byte) inputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] expected = new byte[] { 'A', 'B' };
        byte[] result = new byte[expected.length];
        int read = checksumCalculatingInputStream.read(result);
        assertArrayEquals(expected, result);
        verify(checksum).update(expected, 0, expected.length);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] expected = new byte[] { 'A', 'B' };
        byte[] result = new byte[expected.length];
        int offset = 0;
        int length = expected.length;
        int read = checksumCalculatingInputStream.read(result, offset, length);
        assertArrayEquals(expected, result, 0, expected.length);
        verify(checksum).update(expected, offset, length);
    }

    @Test
    public void testSkip() throws IOException {
        checksumCalculatingInputStream.skip(1);
        verify(checksum).update((byte) inputStream.read());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipMoreThanAvailable() throws IOException {
        checksumCalculatingInputStream.skip(inputStream.available() + 1);
    }

    @Test
    public void testGetValue() throws IOException {
        byte[] data = new byte[] { 'A', 'B' };
        inputStream = new ByteArrayInputStream(data);
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        checksumCalculatingInputStream.read(data, 0, data.length);
        long expectedValue = checksum.getValue();
        long actualValue = checksumCalculatingInputStream.getValue();
        assertEquals(expectedValue, actualValue);
    }
}