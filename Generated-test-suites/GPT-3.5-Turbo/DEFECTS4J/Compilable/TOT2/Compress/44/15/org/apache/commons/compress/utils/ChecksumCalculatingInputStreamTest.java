package org.apache.commons.compress.utils;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        byte[] testData = {1, 2, 3, 4, 5};
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(testData));

        int result = inputStream.read();
        assertEquals(1, result);
        assertEquals(2214581704L, inputStream.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] testData = {1, 2, 3, 4, 5};
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(testData));

        byte[] buffer = new byte[3];
        int result = inputStream.read(buffer);

        assertEquals(3, result);
        assertArrayEquals(new byte[]{1, 2, 3}, buffer);
        assertEquals(35018803935L, inputStream.getValue());
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] testData = {1, 2, 3, 4, 5};
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(testData));

        byte[] buffer = new byte[3];
        int result = inputStream.read(buffer, 1, 2);

        assertEquals(2, result);
        assertArrayEquals(new byte[]{0, 1, 0}, buffer);
        assertEquals(1855890229L, inputStream.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] testData = {1, 2, 3, 4, 5};
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(testData));

        long skipped = inputStream.skip(2);

        assertEquals(2, skipped);
        assertEquals(0L, inputStream.getValue());
    }

    @Test(expected = IOException.class)
    public void testReadWithError() throws IOException {
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(new byte[0]));

        inputStream.read();
    }

}