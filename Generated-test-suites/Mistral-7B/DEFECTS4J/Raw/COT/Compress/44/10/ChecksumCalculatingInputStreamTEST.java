package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for ChecksumCalculatingInputStream class
 */
public class ChecksumCalculatingInputStreamTest {

    private InputStream inputStream;
    private Checksum checksum;
    private ChecksumCalculatingInputStream checksumCalculatingInputStream;

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        checksum = mock(Checksum.class);
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
    }

    @Test
    public void testConstructor() throws IOException {
        assertNotNull(checksumCalculatingInputStream);
        assertNotNull(checksumCalculatingInputStream.in);
        assertNotNull(checksumCalculatingInputStream.checksum);
    }

    @Test
    public void testReadByte() throws IOException {
        assertEquals(0x01, checksumCalculatingInputStream.read());
        checksum.update((byte) 0x01);
        assertEquals(0x02, checksumCalculatingInputStream.read());
        checksum.update((byte) 0x02);
        assertEquals(-1, checksumCalculatingInputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[3];
        checksumCalculatingInputStream.read(buffer);
        assertArrayEquals(new byte[] { 0x01, 0x02, 0x03 }, buffer);
        assertEquals(3, checksum.getByteArray().length);
        checksum.update(buffer, 0, 3);
    }

    @Test
    public void testSkip() throws IOException {
        assertFalse(checksumCalculatingInputStream.skip(1) >= 0);
    }

    @Test
    public void testGetValue() throws IOException {
        MessageDigest mockMessageDigest = mock(MessageDigest.class);
        when(checksum.getChecksum()).thenReturn(mockMessageDigest);

        byte[] expectedChecksum = new byte[] { 0x01, 0x02, 0x03 };
        when(mockMessageDigest.digest()).thenReturn(expectedChecksum);

        byte[] resultChecksum = checksumCalculatingInputStream.getValue();
        assertArrayEquals(expectedChecksum, resultChecksum);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnUnderlyingStream() throws IOException {
        when(inputStream.read()).thenThrow(new IOException());

        checksumCalculatingInputStream.read();
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenChecksumDoesntMatch() throws IOException {
        when(checksum.getValue()).thenThrow(new IOException());

        checksumCalculatingInputStream.getValue();
    }

    @Test
    public void testChecksumCalculatingInputStreamWithBigInput() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bigInput = new byte[1024 * 1024];
        ByteBuffer sourceBuffer = ByteBuffer.wrap(bigInput);
        OutputStream output = outputStream;

        InputStream input = new ByteArrayInputStream(bigInput);
        Checksum checksum = mock(Checksum.class);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, input);

        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        while (bytesRead < bigInput.length) {
            int n = checksumCalculatingInputStream.read(buffer, 0, buffer.length);
            if (n < 0) break;
            bytesRead += n;
            output.write(buffer, 0, n);
        }
        output.close();
        input.close();

        byte[] resultChecksum = checksumCalculatingInputStream.getValue();
        byte[] expectedChecksum = outputStream.toByteArray();

        assertArrayEquals(expectedChecksum, resultChecksum);
    }
}