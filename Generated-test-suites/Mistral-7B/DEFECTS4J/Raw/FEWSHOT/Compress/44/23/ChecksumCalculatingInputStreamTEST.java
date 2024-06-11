package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 'a' });
        Checksum checksum = mock(Checksum.class);
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, input);

        assertEquals((int) 'a', inputStream.read());
        verify(checksum).update((byte) 'a');
    }

    @Test
    public void testReadByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 'a', 'b' });
        Checksum checksum = mock(Checksum.class);
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, input);

        byte[] buffer = new byte[2];
        int len = inputStream.read(buffer);
        assertEquals(2, len);

        assertArrayEquals(new byte[] { 'a', 'b' }, buffer);
        verify(checksum).update(buffer, 0, 2);
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 'a', 'b' });
        Checksum checksum = mock(Checksum.class);
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, input);

        byte[] buffer = new byte[3];
        int offset = 1;
        int len = inputStream.read(buffer, offset, buffer.length - offset);

        assertEquals(2, len);
        assertArrayEquals(new byte[] { 'b', 'a' }, buffer);
        verify(checksum).update(buffer, offset, len);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 'a' });
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(mock(Checksum.class), input);

        assertEquals(1, inputStream.skip(1));
        verify(input).read();
        verify(input).read();
        verify(input).read();
        verify(input).read();
        verify(input).read();
    }

    @Test(expected = IOException.class)
    public void testReadExhaustedStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(mock(Checksum.class), input);

        inputStream.read();
        inputStream.read();
        inputStream.read();

        inputStream.read(); // Should throw an IOException
    }

    @Test
    public void testGetValue() throws NoSuchAlgorithmException, IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 'a' });
        MessageDigest md = MessageDigest.getInstance("MD5");
        Checksum checksum = md;
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, input);

        assertEquals(1, inputStream.read());
        assertEquals(-1L, inputStream.skip(1));

        byte[] result = md.digest();
        long expectedChecksum = ByteBuffer.wrap(result).getLong();

        assertEquals(expectedChecksum, inputStream.getValue());
    }
}