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
import java.util.zip.Adler32;
import java.util.zip.CRC32;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the {@link ChecksumCalculatingInputStream} class.
 */
public class ChecksumCalculatingInputStreamTest {
    private InputStream inputStream;
    private ChecksumCalculatingInputStream checksumCalculatingInputStream;
    private Checksum checksum;

    @Before
    public void setUp() {
        checksum = new CRC32();
        inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
    }

    @Test
    public void testReadByte() throws IOException {
        assertEquals(1, checksumCalculatingInputStream.read());
        verify(inputStream).read();
        assertEquals(1, checksum.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        checksumCalculatingInputStream.read(new byte[3][], 0, 3);

        byte[] expected = new byte[] { 1, 2, 3 };
        assertArrayEquals(expected, checksumCalculatingInputStream.getChecksumAsByteArray());

        verify(inputStream).read(expected, 0, 3);
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        checksumCalculatingInputStream.read(new byte[] { 0, 1, 2 }, 1, 2);

        byte[] expected = new byte[] { 1, 2 };
        assertArrayEquals(expected, checksumCalculatingInputStream.getChecksumAsByteArray());

        verify(inputStream).read(new byte[] { 1, 2 }, 1, 2);
    }

    @Test
    public void testReadByteArrayWithLength() throws IOException {
        checksumCalculatingInputStream.read(new byte[] { 0, 1, 2, 3 }, 0, 2);

        byte[] expected = new byte[] { 1, 2 };
        assertArrayEquals(expected, checksumCalculatingInputStream.getChecksumAsByteArray());

        verify(inputStream).read(new byte[] { 1, 2 }, 0, 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkip() throws IOException {
        checksumCalculatingInputStream.skip(1);
    }

    @Test
    public void testGetValue() throws IOException {
        assertEquals(1L, checksumCalculatingInputStream.getValue());
    }

    @Test
    public void testGetValueWithOtherChecksum() throws IOException {
        Checksum adler32 = mock(Adler32.class);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(adler32, inputStream);
        assertEquals(1L, checksumCalculatingInputStream.getValue());
        verify(adler32).getValue();
    }

    @Test
    public void testGetChecksumAsByteArray() throws IOException {
        byte[] expected = new byte[] { (byte) 0x1a, (byte) 0x46, (byte) 0x7c, (byte) 0xd3, (byte) 0x6b };
        assertArrayEquals(expected, checksumCalculatingInputStream.getChecksumAsByteArray());

        byte[] input = new byte[] { 1, 2, 3 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(input));
        checksumCalculatingInputStream.read(outputStream.getBuffer(), 0, input.length);
        outputStream.close();
        assertArrayEquals(expected, checksumCalculatingInputStream.getChecksumAsByteArray());
    }

    @Test
    public void testGetChecksumAsByteArrayWithOtherChecksum() throws IOException {
        byte[] expected = new byte[] { (byte) 0x1a, (byte) 0x46, (byte) 0x7c, (byte) 0xd3, (byte) 0x6b };
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(mock(Adler32.class), new ByteArrayInputStream(new byte[] { 1, 2, 3 }));
        assertArrayEquals(expected, checksumCalculatingInputStream.getChecksumAsByteArray());
    }
}