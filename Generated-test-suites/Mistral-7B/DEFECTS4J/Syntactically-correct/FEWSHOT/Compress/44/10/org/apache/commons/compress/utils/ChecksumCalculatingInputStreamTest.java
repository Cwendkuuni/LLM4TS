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
import java.util.zip.CRC32;

import org.junit.Before;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    private InputStream inputStream;
    private Checksum checksum;
    private ChecksumCalculatingInputStream checksumCalculatingInputStream;

    @Before
    public void setUp() {
        checksum = new Adler32();
        inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        assertEquals(inputStream.read(), checksumCalculatingInputStream.read());
        verify(checksum).update((byte) inputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] expectedOutput = new byte[] { 1, 2, 3, 4, 5 };
        byte[] output = new byte[5];
        checksumCalculatingInputStream.read(output, 0, 5);
        assertArrayEquals(expectedOutput, output);
        verify(checksum).update(expectedOutput);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] expectedOutput = new byte[] { 1, 2, 3 };
        byte[] output = new byte[5];
        checksumCalculatingInputStream.read(output, 2, 3);
        assertArrayEquals(expectedOutput, new byte[] { 3, 2, 1 }); // read from the buffer starts from index 2 and length is 3
        verify(checksum).update(expectedOutput);
    }

    @Test
    public void testSkipThrowsIOException() throws IOException {
        checksumCalculatingInputStream.skip(10);
        verify(checksum).update((byte) checksumCalculatingInputStream.read());
    }

    @Test
    public void testGetValue() throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(new CRC32(), inputStream);
        int bytesRead = -1;
        while ((bytesRead = checksumCalculatingInputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] expectedOutput = outputStream.toByteArray();
        long calculatedChecksum = checksumCalculatingInputStream.getValue();
        long expectedChecksum = new CRC32().getValue();
        assertEquals(expectedOutput.length, calculatedChecksum);
        assertEquals(expectedChecksum, calculatedChecksum);
    }

    @Test
    public void testGetValueWithDifferentChecksums() throws IOException {
        checksum = new Adler32();
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        int bytesRead = -1;
        while ((bytesRead = checksumCalculatingInputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] expectedOutput = outputStream.toByteArray();
        long calculatedChecksum = checksumCalculatingInputStream.getValue();
        long expectedAdlerChecksum = new Adler32().getValue(expectedOutput);
        assertNotEquals(expectedChecksum, calculatedChecksum);
        assertEquals(expectedAdlerChecksum, calculatedChecksum);
    }

    @Test(expected = IOException.class)
    public void testGetValueThrowsIOException() throws IOException {
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(mock(Checksum.class), inputStream);
        checksumCalculatingInputStream.getValue();
    }
}