package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() {
        final Checksum checksum = mock(Checksum.class);
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });

        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        assertEquals(checksum, checksumCalculatingInputStream.checksum);
        assertEquals(inputStream, checksumCalculatingInputStream.in);
    }

    @Test
    public void testReadByte() throws IOException {
        final Checksum checksum = mock(Checksum.class);
        final InputStream inputStream = mock(InputStream.class);
        final byte expectedByte = (byte) 0x01;

        when(inputStream.read()).thenReturn(expectedByte);

        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        assertEquals(expectedByte, checksumCalculatingInputStream.read());
        verify(checksum).update(expectedByte);
    }

    @Test
    public void testReadByteArray() throws IOException {
        final Checksum checksum = mock(Checksum.class);
        final InputStream inputStream = mock(InputStream.class);
        final byte[] expectedBytes = { 0x01, 0x02, 0x03 };

        when(inputStream.read(expectedBytes)).thenReturn(expectedBytes.length);

        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final byte[] actualBytes = new byte[expectedBytes.length];
        final int readBytes = checksumCalculatingInputStream.read(actualBytes);

        assertArrayEquals(expectedBytes, actualBytes);
        verify(checksum).update(expectedBytes);
        assertEquals(expectedBytes.length, readBytes);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        final Checksum checksum = mock(Checksum.class);
        final InputStream inputStream = mock(InputStream.class);
        final byte[] expectedBytes = { 0x01, 0x02, 0x03 };
        final int offset = 1;
        final int length = 2;

        when(inputStream.read(expectedBytes)).thenReturn(expectedBytes.length);

        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final byte[] actualBytes = new byte[expectedBytes.length];
        final int readBytes = checksumCalculatingInputStream.read(actualBytes, offset, length);

        assertArrayEquals(expectedBytes.subArray(offset, length), actualBytes.subArray(offset, length));
        verify(checksum).update(expectedBytes.subArray(offset, length));
        assertEquals(length, readBytes);
    }

    @Test
    public void testSkip() throws IOException {
        final Checksum checksum = mock(Checksum.class);
        final InputStream inputStream = mock(InputStream.class);

        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final long expectedBytesToSkip = 1;

        when(inputStream.read()).thenReturn(0);

        checksumCalculatingInputStream.skip(expectedBytesToSkip);

        verify(inputStream, atLeastOnce()).read();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClose() throws IOException {
        final Checksum checksum = mock(Checksum.class);
        final InputStream inputStream = mock(InputStream.class);

        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        checksumCalculatingInputStream.close();
    }

    @Test
    public void testGetValue() throws IOException, NoSuchAlgorithmException {
        final Checksum checksum = new Adler32();
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int expectedBytesToRead = 3;
        final byte[] expectedBytes = new byte[expectedBytesToRead];
        final int readBytes = checksumCalculatingInputStream.read(expectedBytes);

        assertEquals(expectedBytesToRead, readBytes);

        final long actualChecksum = checksumCalculatingInputStream.getValue();

        assertEquals(checksum.getValue(), actualChecksum);
    }

    @Test
    public void testGetValueWithDifferentChecksumImplementation() throws IOException, NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md, inputStream);

        final int expectedBytesToRead = 3;
        final byte[] expectedBytes = new byte[expectedBytesToRead];
        final int readBytes = checksumCalculatingInputStream.read(expectedBytes);

        assertEquals(expectedBytesToRead, readBytes);

        final byte[] actualDigest = md.digest();
        final byte[] expectedDigest = new byte[] { 0x32, 0x2e, 0x1b, 0x12, 0x1a, 0x19, 0x18, 0x17, 0x16, 0x15, 0x14, 0x13, 0x12, 0x11, 0x10, 0x09, 0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01 };

        assertArrayEquals(expectedDigest, actualDigest);

        final long actualChecksum = checksumCalculatingInputStream.getValue();

        assertTrue(actualChecksum > 0);
    }

    @Test
    public void testGetValueAfterClosingStream() throws IOException, NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md, inputStream);

        final int expectedBytesToRead = 3;
        final byte[] expectedBytes = new byte[expectedBytesToRead];
        final int readBytes = checksumCalculatingInputStream.read(expectedBytes);

        assertEquals(expectedBytesToRead, readBytes);

        checksumCalculatingInputStream.close();

        final long actualChecksum = checksumCalculatingInputStream.getValue();

        assertNull(actualChecksum);
    }

    @Test
    public void testGetValueAfterReadingAllBytesFromStream() throws IOException, NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md, inputStream);

        final int expectedBytesToRead = 3;
        final byte[] expectedBytes = new byte[expectedBytesToRead];
        final int readBytes = checksumCalculatingInputStream.read(expectedBytes);

        assertEquals(expectedBytesToRead, readBytes);

        final int bytesLeftToRead = 0;

        checksumCalculatingInputStream.read(new byte[bytesLeftToRead]);

        final long actualChecksum = checksumCalculatingInputStream.getValue();

        assertTrue(actualChecksum > 0);
    }

    @Test
    public void testGetValueWithNullChecksum() {
        final InputStream inputStream = mock(InputStream.class);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(null, inputStream);

        assertNull(checksumCalculatingInputStream.getValue());
    }

    @Test
    public void testGetValueWithNullInputStream() {
        final Checksum checksum = mock(Checksum.class);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, null);

        assertNull(checksumCalculatingInputStream.getValue());
    }

    @Test
    public void testGetValueWithDifferentChecksumImplementationAndNullInputStream() {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md, null);

        assertNull(checksumCalculatingInputStream.getValue());
    }

}