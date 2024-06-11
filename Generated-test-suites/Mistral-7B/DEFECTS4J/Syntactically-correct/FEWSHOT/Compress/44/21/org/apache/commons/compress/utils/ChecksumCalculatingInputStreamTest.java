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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;

import org.junit.Before;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    private InputStream inputStream;
    private ChecksumCalculatingInputStream checksumInputStream;
    private Checksum checksum;

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        checksum = new Adler32();
        inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        assertEquals(1, checksumInputStream.read());
        checksum.update((byte) 1);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] result = new byte[3];
        checksumInputStream.read(result);
        checksum.update(result, 0, 3);

        assertArrayEquals(new byte[] { 1, 2, 3 }, result);
    }

    @Test
    public void testGetValue() throws IOException {
        byte[] result = new byte[4];
        checksumInputStream.read(result, 0, 4);

        long expectedChecksum = checksum.getValue();
        long actualChecksum = checksumInputStream.getValue();

        assertEquals(expectedChecksum, actualChecksum);
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        when(inputStream.read()).thenThrow(new IOException("Test exception"));
        checksumInputStream.read();
    }

    @Test
    public void testGetValueWhenInputStreamIsExhausted() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        ByteArrayInputStream exhaustedInputStream = new ByteArrayInputStream(outputStream.toByteArray());

        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, exhaustedInputStream);
        long checksumValue = checksumInputStream.getValue();
        assertEquals(checksumValue, checksum.getValue());
    }
}