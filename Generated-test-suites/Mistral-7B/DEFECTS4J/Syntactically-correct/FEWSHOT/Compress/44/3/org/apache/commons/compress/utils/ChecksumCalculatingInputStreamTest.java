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
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;

import org.junit.Before;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    private InputStream inputStream;
    private ChecksumCalculatingInputStream checksumCalculatingInputStream;
    private Checksum checksum;

    @Before
    public void setUp() {
        checksum = mock(Checksum.class);
        inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[1];

        int read = checksumCalculatingInputStream.read();
        assertEquals(0x01, read);
        verify(checksum).update((byte) 0x01);

        read = checksumCalculatingInputStream.read();
        assertEquals(0x02, read);
        verify(checksum).update((byte) 0x02);

        read = checksumCalculatingInputStream.read();
        assertEquals(0x03, read);
        verify(checksum).update((byte) 0x03);

        read = checksumCalculatingInputStream.read();
        assertEquals(-1, read);
        verify(checksum, mockito.times(3)).update((byte[]) null);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[3];

        int length = checksumCalculatingInputStream.read(buffer);
        assertEquals(3, length);
        assertArrayEquals(new byte[] { 0x01, 0x02, 0x03 }, buffer);
        verify(checksum).update(buffer);

        length = checksumCalculatingInputStream.read(buffer, 1, 1);
        assertEquals(0, length);
        verify(checksum).update((byte[]) null);
    }

    @Test
    public void testReadSkipsChecksumUpdate() throws IOException {
        byte[] buffer = new byte[3];

        int length = checksumCalculatingInputStream.read(buffer, 1, 1);
        assertEquals(0, length);

        int read = checksumCalculatingInputStream.read();
        assertEquals(0x01, read);
        verify(checksum).update((byte) 0x01);

        read = checksumCalculatingInputStream.read();
        assertEquals(0x02, read);
        verify(checksum).update((byte) 0x02);

        read = checksumCalculatingInputStream.read();
        assertEquals(0x03, read);
        verify(checksum).update((byte) 0x03);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkip() throws IOException {
        checksumCalculatingInputStream.skip(1);
    }

    @Test
    public void testGetValue() {
        Adler32 adler32 = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(adler32, inputStream);

        int read = checksumCalculatingInputStream.read();
        read = checksumCalculatingInputStream.read();
        read = checksumCalculatingInputStream.read();

        long checksumValue = checksumCalculatingInputStream.getValue();
        assertEquals(adler32.getValue(), checksumValue);
    }
}