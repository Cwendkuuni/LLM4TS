package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    private InputStream inputStream;
    private Checksum checksum;
    private ChecksumCalculatingInputStream checksumCalculatingInputStream;

    @Before
    public void setUp() throws IOException, NoSuchAlgorithmException {
        checksum = new Adler32();
        inputStream = new ByteArrayInputStream("test data".getBytes(StandardCharsets.UTF_8));
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        assertEquals(-1, checksumCalculatingInputStream.read());
        verify(inputStream).read();
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] expected = new byte[] { 0x65, 0x74, 0x68, 0x69, 0x73, 0x20, 0x61, 0x64, 0x64 };
        assertArrayEquals(expected, new byte[expected.length]);
        int read = checksumCalculatingInputStream.read(expected);
        assertEquals(expected.length, read);
        verify(inputStream).read(expected);
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] expected = new byte[] { 0x65, 0x74, 0x68 };
        byte[] result = new byte[expected.length];
        int read = checksumCalculatingInputStream.read(result, 0, expected.length);
        assertEquals(expected.length, read);
        assertArrayEquals(expected, result);
        verify(inputStream).read(result);
    }

    @Test
    public void testReadByteArrayWithLength() throws IOException {
        byte[] expected = new byte[] { 0x65, 0x74, 0x68, 0x69, 0x73, 0x20, 0x61, 0x64, 0x64 };
        byte[] result = new byte[expected.length + 1];
        int read = checksumCalculatingInputStream.read(result, 0, expected.length + 1);
        assertEquals(expected.length, read);
        assertArrayEquals(expected, result, 0, expected.length);
        verify(inputStream).read(result, 0, expected.length);
    }

    @Test
    public void testSkip() throws IOException {
        checksumCalculatingInputStream.skip(1);
        verify(inputStream).read();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipWithNegativeNumber() throws IOException {
        checksumCalculatingInputStream.skip(-1);
    }

    @Test(expected = IOException.class)
    public void testSkipWithExhaustedStream() throws IOException {
        byte[] testData = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(testData);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(mock(Checksum.class), inputStream);

        int read = 0;
        while (read >= 0) {
            read = checksumCalculatingInputStream.read();
            outputStream.write(read > 0 ? (byte) read : -1);
        }

        checksumCalculatingInputStream.skip(1);
    }

    @Test
    public void testGetValue() {
        long expected = 0x924a1112c654331eL;
        assertEquals(expected, checksumCalculatingInputStream.getValue());
    }

    @After
    public void tearDown() throws IOException {
        inputStream.close();
        checksumCalculatingInputStream.close();
    }
}