package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() throws IOException {
        final Checksum checksum = mock(Checksum.class);
        final InputStream in = new ByteArrayInputStream(new byte[]{1, 2, 3});

        final ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        assertEquals(in, inputStream.in);
        assertEquals(checksum, inputStream.checksum);
    }

    @Test
    public void testReadByte() throws IOException {
        final Checksum checksum = mock(Checksum.class);
        final InputStream in = new ByteArrayInputStream(new byte[]{1, 2, 3});

        final ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        final int readByte = inputStream.read();
        assertEquals(1, readByte);

        verify(checksum).update((byte) 1);
    }

    @Test
    public void testReadByteThrowsIOException() throws IOException {
        final Checksum checksum = mock(Checksum.class);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        doThrow(new IOException("test")).when(inputStream).read();

        try {
            checksumCalculatingInputStream.read();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("test", e.getMessage());
        }

        verify(checksum, never()).update(anyByte());
    }

    @Test
    public void testReadByteArray() throws IOException {
        final Checksum checksum = mock(Checksum.class);
        final byte[] expectedArray = new byte[]{1, 2, 3};
        final InputStream in = new ByteArrayInputStream(expectedArray);

        final ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        final int readLength = inputStream.read(expectedArray);

        assertEquals(expectedArray.length, readLength);

        verify(checksum).update(expectedArray);
    }

    @Test
    public void testReadByteArrayThrowsIOException() throws IOException {
        final Checksum checksum = mock(Checksum.class);
        final byte[] expectedArray = new byte[]{1, 2, 3};
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        doThrow(new IOException("test")).when(inputStream).read(expectedArray);

        try {
            checksumCalculatingInputStream.read(expectedArray);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("test", e.getMessage());
        }

        verify(checksum, never()).update(expectedArray);
    }

    @Test
    public void testReadByteArrayWithPartialRead() throws IOException {
        final Checksum checksum = mock(Checksum.class);
        final byte[] expectedArray = new byte[]{1, 2, 3};
        final byte[] resultArray = new byte[]{1, 2};
        final InputStream inputStream = new ByteArrayInputStream(expectedArray);

        doReturn(resultArray.length).when(inputStream).read(resultArray);
        doThrow(new IOException("test")).when(inputStream).read(expectedArray, 2, 1);

        final ChecksumCalculatingInputStream inputStreamCalculating = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int readLength = inputStreamCalculating.read(expectedArray);

        assertArrayEquals(new byte[]{1, 2}, expectedArray);
        verify(checksum).update(expectedArray, 0, expectedArray.length);
    }

    @Test
    public void testSkip() throws IOException {
        final Checksum checksum = mock(Checksum.class);
        final InputStream in = new ByteArrayInputStream(new byte[]{1, 2, 3});

        final ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        try {
            inputStream.skip(100);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected
        }

        verify(checksum, never()).update(anyByte());
    }

    @Test
    public void testGetValue() throws IOException {
        final Checksum checksum = mock(Checksum.class);
        final byte[] bytes = new byte[]{1, 2, 3};
        final InputStream in = new ByteArrayInputStream(bytes);

        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, in);

        final int readLength = checksumCalculatingInputStream.read(new byte[bytes.length]);
        final long checksumValue = checksumCalculatingInputStream.getValue();

        final Adler32 testChecksum = new Adler32();
        testChecksum.update(bytes);
        assertEquals(testChecksum.getValue(), checksumValue);
    }
}