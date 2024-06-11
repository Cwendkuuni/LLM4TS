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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() {
        Checksum checksum = mock(Checksum.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });

        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        assertEquals(checksum, checksumCalculatingInputStream.getChecksum());
        assertEquals(inputStream, checksumCalculatingInputStream.getInputStream());
    }

    @Test
    public void testRead() throws IOException {
        Checksum checksum = mock(Checksum.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01 });

        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[1];

        int read = checksumCalculatingInputStream.read();
        verify(checksum).update((byte) read);
        assertEquals(0x01, read);

        read = checksumCalculatingInputStream.read(buffer, 0, buffer.length);
        verify(checksum).update(buffer, 0, 1);
        assertEquals(1, read);
        assertArrayEquals(new byte[] { (byte) 0x01 }, buffer);
    }

    @Test
    public void testReadByteArray() throws IOException {
        Checksum checksum = mock(Checksum.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });

        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[3];

        int read = checksumCalculatingInputStream.read(buffer, 0, buffer.length);
        verify(checksum).update(buffer, 0, 3);
        assertEquals(3, read);
        assertArrayEquals(new byte[] { 0x01, 0x02, 0x03 }, buffer);
    }

    @Test
    public void testSkip() throws IOException {
        Checksum checksum = mock(Checksum.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });

        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long skipped = checksumCalculatingInputStream.skip(5);
        assertEquals(1, skipped);

        verify(checksum).update(new byte[] { 0x01 }, 0, 1);
        verify(checksum).update(new byte[] { 0x02 }, 0, 1);
        verify(checksum).update(new byte[] { 0x03 }, 0, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClose() throws IOException {
        Checksum checksum = mock(Checksum.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });

        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        checksumCalculatingInputStream.close();
    }

    @Test
    public void testGetValue() throws IOException, NoSuchAlgorithmException {
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });

        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[3];

        checksumCalculatingInputStream.read(buffer, 0, buffer.length);
        checksumCalculatingInputStream.read(buffer, 0, buffer.length);
        checksumCalculatingInputStream.read(buffer, 0, buffer.length);

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] expected = md.digest(new byte[] { 0x01, 0x02, 0x03 });

        assertEquals(expected, checksumCalculatingInputStream.getValue());
    }
}

class Adler32 extends Checksum {
    @Override
    public long getValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(byte b) {
        // ... implementation ...
    }

    @Override
    public void update(byte[] b) {
        // ... implementation ...
    }

    @Override
    public void update(byte[] b, int off, int len) {
        // ... implementation ...
    }
}