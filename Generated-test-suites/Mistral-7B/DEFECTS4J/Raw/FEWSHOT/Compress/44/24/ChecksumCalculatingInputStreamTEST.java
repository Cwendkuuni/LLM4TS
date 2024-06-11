package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    private InputStream inputStream;
    private Checksum checksum;
    private ChecksumCalculatingInputStream checksumCalculatingInputStream;

    @Before
    public void setup() {
        try {
            checksum = mock(Checksum.class);
            inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
            checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        } catch (NoSuchAlgorithmException e) {
            // This should not be thrown
        }
    }

    @Test
    public void testReadSingleByte() throws IOException {
        assertEquals(1, checksumCalculatingInputStream.read());
        verify(checksum).update((byte) 1);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] result = new byte[3];
        checksumCalculatingInputStream.read(result, 0, result.length);
        assertArrayEquals(new byte[] { 1, 2, 3 }, result);
        verify(checksum).update(result, 0, result.length);
    }

    @Test
    public void testReadByteArrayPartially() throws IOException {
        byte[] result = new byte[2];
        checksumCalculatingInputStream.read(result, 0, result.length);
        assertArrayEquals(new byte[] { 1, 2 }, result);
        verify(checksum).update(result, 0, result.length);
    }

    @Test
    public void testGetValue() throws IOException {
        byte[] expectedChecksum = new byte[16];
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(new byte[] { 1, 2, 3 });
        expectedChecksum = md.digest();

        byte[] result = new byte[expectedChecksum.length];
        checksumCalculatingInputStream.read(result);
        byte[] readChecksum = new byte[expectedChecksum.length];
        System.arraycopy(result, 0, readChecksum, 0, result.length);

        assertEquals(expectedChecksum, readChecksum);
        verify(checksum).getValue();
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read()).thenThrow(new IOException("IOException"));
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        checksumCalculatingInputStream.read();
    }

    @Test(expected = IOException.class)
    public void testChecksumMismatch() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(mock(Checksum.class), inputStream);

        checksumCalculatingInputStream.read(new byte[3], 0, 3);
        when(checksum.getValue()).thenReturn(42L);
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read();

        checksumCalculatingInputStream.getValue(); // This should throw an IOException
    }
}