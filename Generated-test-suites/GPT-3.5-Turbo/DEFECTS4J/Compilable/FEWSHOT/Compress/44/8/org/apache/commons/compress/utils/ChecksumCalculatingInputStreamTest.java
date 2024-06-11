package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Adler32;
import java.util.zip.Checksum;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] inputData = new byte[]{1, 2, 3, 4, 5};
        long expectedChecksum = calculateChecksum(inputData);
        InputStream inputStream = new ByteArrayInputStream(inputData);
        ChecksumCalculatingInputStream csInputStream = new ChecksumCalculatingInputStream(new Adler32(), inputStream);
        
        int readByte;
        while ((readByte = csInputStream.read()) != -1) {
            // read all bytes and calculate checksum
        }
        long actualChecksum = csInputStream.getValue();
        
        assertEquals(expectedChecksum, actualChecksum);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] inputData = new byte[]{1, 2, 3, 4, 5};
        long expectedChecksum = calculateChecksum(inputData);
        InputStream inputStream = new ByteArrayInputStream(inputData);
        ChecksumCalculatingInputStream csInputStream = new ChecksumCalculatingInputStream(new Adler32(), inputStream);
        
        byte[] readData = new byte[5];
        csInputStream.read(readData);
        long actualChecksum = csInputStream.getValue();
        
        assertEquals(expectedChecksum, actualChecksum);
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] inputData = new byte[]{1, 2, 3, 4, 5};
        long expectedChecksum = calculateChecksum(inputData);
        InputStream inputStream = new ByteArrayInputStream(inputData);
        ChecksumCalculatingInputStream csInputStream = new ChecksumCalculatingInputStream(new Adler32(), inputStream);
        
        byte[] readData = new byte[5];
        csInputStream.read(readData, 0, 5);
        long actualChecksum = csInputStream.getValue();
        
        assertEquals(expectedChecksum, actualChecksum);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputData = new byte[]{1, 2, 3, 4, 5};
        long expectedChecksum = calculateChecksum(inputData);
        InputStream inputStream = new ByteArrayInputStream(inputData);
        ChecksumCalculatingInputStream csInputStream = new ChecksumCalculatingInputStream(new Adler32(), inputStream);
        
        long skippedBytes = csInputStream.skip(2);
        long actualChecksum = csInputStream.getValue();
        
        assertEquals(2, skippedBytes);
        assertEquals(expectedChecksum, actualChecksum);
    }

    private long calculateChecksum(byte[] data) {
        Checksum checksum = new Adler32();
        checksum.update(data, 0, data.length);
        return checksum.getValue();
    }
}