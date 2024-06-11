package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import java.util.zip.CRC32;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() {
        InputStream input = new ByteArrayInputStream("TestString".getBytes(StandardCharsets.UTF_8));
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, input);
        assertEquals(input.available(), checksumCalculatingInputStream.available());
        assertNotNull(checksumCalculatingInputStream.checksum);
        assertNotNull(checksumCalculatingInputStream.in);
    }

    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream("TestString".getBytes(StandardCharsets.UTF_8));
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, input);

        byte[] buffer = new byte[1];

        int read = checksumCalculatingInputStream.read();
        assertEquals(-1, read);

        read = checksumCalculatingInputStream.read();
        assertEquals((int) 'T', read);
        verify(checksum).update((byte) 'T');

        read = checksumCalculatingInputStream.read();
        assertEquals((int) 'e', read);
        verify(checksum).update((byte) 'e');

        read = checksumCalculatingInputStream.read();
        assertEquals((int) 's', read);
        verify(checksum).update((byte) 's');

        read = checksumCalculatingInputStream.read();
        assertEquals((int) 't', read);
        verify(checksum).update((byte) 't');

        read = checksumCalculatingInputStream.read();
        assertEquals(-1, read);

        checksumCalculatingInputStream.close();
        verify(checksum).done();
    }

    @Test
    public void testReadByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("TestString".getBytes(StandardCharsets.UTF_8));
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, input);

        byte[] buffer = new byte[4];

        int read = checksumCalculatingInputStream.read(buffer);
        assertEquals(4, read);

        byte[] expected = "Test".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expected, buffer);
        verify(checksum).update(expected);

        checksumCalculatingInputStream.close();
        verify(checksum).done();
    }

    @Test
    public void testReadByteArrayWithRemainingBytes() throws IOException {
        InputStream input = new ByteArrayInputStream("TestString".getBytes(StandardCharsets.UTF_8));
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, input);

        byte[] buffer = new byte[6];

        int read = checksumCalculatingInputStream.read(buffer);
        assertEquals(4, read);

        byte[] expected = "Test".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expected, buffer, 0, 4);

        byte[] remainingBytes = new byte[2];

        read = checksumCalculatingInputStream.read(remainingBytes);
        assertEquals(2, read);

        byte[] expectedRemainingBytes = "String".getBytes(StandardCharsets.UTF_8);
        assertArrayEquals(expectedRemainingBytes, remainingBytes);

        checksumCalculatingInputStream.close();
        verify(checksum).done();
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("TestString".getBytes(StandardCharsets.UTF_8));
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, input);

        long skipped = checksumCalculatingInputStream.skip(1);
        assertEquals(0, skipped);

        byte[] buffer = new byte[1];

        skipped = checksumCalculatingInputStream.read(buffer);
        assertEquals(1, skipped);
        assertEquals((int) 'T', buffer[0]);

        skipped = checksumCalculatingInputStream.skip(2);
        assertEquals(1, skipped);
        assertEquals((int) 's', checksumCalculatingInputStream.read());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipWithLargeNumber() throws IOException {
        InputStream input = new ByteArrayInputStream("TestString".getBytes(StandardCharsets.UTF_8));
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, input);

        checksumCalculatingInputStream.skip(Long.MAX_VALUE);
    }

    @Test
    public void testGetValue() throws IOException, NoSuchAlgorithmException {
        InputStream input = new ByteArrayInputStream("TestString".getBytes(StandardCharsets.UTF_8));
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, input);

        int read = checksumCalculatingInputStream.read();
        assertEquals((int) 'T', read);

        read = checksumCalculatingInputStream.read();
        assertEquals((int) 'e', read);

        read = checksumCalculatingInputStream.read();
        assertEquals((int) 's', read);

        read = checksumCalculatingInputStream.read();
        assertEquals((int) 't', read);

        long calculatedChecksum = checksumCalculatingInputStream.getValue();
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] expected = "416e2f1e64244b857b613b56".getBytes(StandardCharsets.UTF_8);
        byte[] actual = md.digest("TestString".getBytes(StandardCharsets.UTF_8));

        assertArrayEquals(expected, actual);
        assertEquals(expected.length * 8, calculatedChecksum);

        checksumCalculatingInputStream.close();
    }

    @Test
    public void testGetValueWithCustomChecksum() throws IOException {
        InputStream input = new ByteArrayInputStream("TestString".getBytes(StandardCharsets.UTF_8));
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, input);

        int read = checksumCalculatingInputStream.read();
        assertEquals((int) 'T', read);

        read = checksumCalculatingInputStream.read();
        assertEquals((int) 'e', read);

        read = checksumCalculatingInputStream.read();
        assertEquals((int) 's', read);

        read = checksumCalculatingInputStream.read();
        assertEquals((int) 't', read);

        long calculatedChecksum = checksumCalculatingInputStream.getValue();
        long expectedChecksum = 0x1b28e2d5L;

        assertEquals(expectedChecksum, calculatedChecksum);

        checksumCalculatingInputStream.close();
    }
}