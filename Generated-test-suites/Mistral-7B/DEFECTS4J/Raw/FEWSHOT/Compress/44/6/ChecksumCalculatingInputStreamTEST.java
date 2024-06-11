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
import java.util.zip.Adler32;
import java.util.zip.Checksum;

import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() {
        final Checksum checksum = new Adler32();
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        assertEquals(inputStream, checksumCalculatingInputStream.in);
        assertNotNull(checksumCalculatingInputStream.checksum);
    }

    @Test
    public void testRead() throws IOException {
        final Checksum checksum = mock(Checksum.class);
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        verify(checksum).update((byte) 1);
        assertEquals(1, checksumCalculatingInputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        final Checksum checksum = mock(Checksum.class);
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final byte[] expected = new byte[] { 1, 2, 3 };

        verify(checksum).update(expected);
        assertArrayEquals(expected, checksumCalculatingInputStream.read(expected, 0, expected.length));
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        final Checksum checksum = mock(Checksum.class);
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final byte[] expected = new byte[] { 2, 3 };

        verify(checksum).update(expected);
        assertArrayEquals(expected, checksumCalculatingInputStream.read(expected, 1, expected.length));
    }

    @Test
    public void testSkip() throws IOException {
        final Checksum checksum = mock(Checksum.class);
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        // Can't really skip, we have to hash everything to verify the checksum
        assertEquals(0, checksumCalculatingInputStream.skip(3));
    }

    @Test
    public void testGetValue() throws IOException {
        final Checksum checksum = new Adler32();
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        final byte[] expected = new byte[] { 1, 2, 3 };

        final int read = checksumCalculatingInputStream.read(expected, 0, expected.length);
        checksumCalculatingInputStream.read(expected, 0, expected.length - read); // read the rest

        final byte[] actual = new byte[checksum.size()];
        System.arraycopy(checksum.digest(), 0, actual, 0, checksum.size());

        assertArrayEquals(actual, checksumCalculatingInputStream.getValue());
    }
}