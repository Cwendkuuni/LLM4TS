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
import java.util.zip.Adler32;
import java.util.zip.Checksum;

import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream_singleByte() throws IOException {
        byte testByte = (byte) 0x01;
        Checksum checksum = mock(Checksum.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[] { testByte });

        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int readByte = checksumCalculatingInputStream.read();
        assertEquals(testByte, readByte);
        verify(checksum).update(testByte);
        assertEquals(testByte, checksum.getValue());
    }

    @Test
    public void testChecksumCalculatingInputStream_byteArray() throws IOException {
        byte[] testData = new byte[] { 0x01, 0x02, 0x03 };
        Checksum checksum = mock(Checksum.class);
        InputStream inputStream = new ByteArrayInputStream(testData);

        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int readLength = checksumCalculatingInputStream.read(new byte[testData.length], 0, testData.length);
        assertArrayEquals(testData, new byte[testData.length]);
        verify(checksum).update(testData);
        assertEquals(Adler32.adler32(testData), checksum.getValue());
    }

    @Test
    public void testChecksumCalculatingInputStream_byteArray_partial_read() throws IOException {
        byte[] testData = new byte[] { 0x01, 0x02, 0x03 };
        Checksum checksum = mock(Checksum.class);
        InputStream inputStream = new ByteArrayInputStream(testData);

        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int readLength = checksumCalculatingInputStream.read(new byte[2], 0, 2);
        byte[] expectedData = new byte[] { 0x01, 0x02 };
        assertArrayEquals(expectedData, new byte[2]);
        verify(checksum).update(expectedData);
        assertEquals(Adler32.adler32(expectedData), checksum.getValue());
    }

    @Test(expected = IOException.class)
    public void testChecksumCalculatingInputStream_IOException() throws IOException {
        Checksum checksum = mock(Checksum.class);
        InputStream inputStream = mock(InputStream.class);

        doThrow(new IOException("IOException")).when(inputStream).read();

        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        checksumCalculatingInputStream.read();
    }

    @Test(expected = IOException.class)
    public void testChecksumCalculatingInputStream_ChecksumDoesntMatch() throws IOException {
        Checksum checksum = mock(Checksum.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01 });

        doThrow(new IOException("Checksum does not match")).when(checksum).update(anyByte());

        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        checksumCalculatingInputStream.read();
    }
}