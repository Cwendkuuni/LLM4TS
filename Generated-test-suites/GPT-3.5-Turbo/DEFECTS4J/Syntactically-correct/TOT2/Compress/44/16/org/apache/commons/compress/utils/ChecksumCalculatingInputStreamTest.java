package org.apache.commons.compress.utils;

import org.junit.Test;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.zip.CRC32;
import static org.junit.Assert.assertEquals;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testReadSingleByte() throws Exception {
        byte[] data = {10, 20, 30, 40, 50};
        CRC32 crc32 = new CRC32();
        InputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumInput = new ChecksumCalculatingInputStream(crc32, inputStream);
        
        assertEquals(10, checksumInput.read());
        assertEquals(20, checksumInput.read());
    }

    @Test
    public void testReadByteArray() throws Exception {
        byte[] data = {10, 20, 30, 40, 50};
        CRC32 crc32 = new CRC32();
        InputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumInput = new ChecksumCalculatingInputStream(crc32, inputStream);
        
        byte[] output = new byte[3];
        checksumInput.read(output);
        
        assertEquals(10, output[0]);
        assertEquals(20, output[1]);
        assertEquals(30, output[2]);
    }

    @Test
    public void testSkip() throws Exception {
        byte[] data = {10, 20, 30, 40, 50};
        CRC32 crc32 = new CRC32();
        InputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumInput = new ChecksumCalculatingInputStream(crc32, inputStream);
        
        long skipped = checksumInput.skip(2);

        assertEquals(2, skipped);
    }

    @Test
    public void testGetValue() throws Exception {
        byte[] data = {10, 20, 30, 40, 50};
        CRC32 crc32 = new CRC32();
        InputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumInput = new ChecksumCalculatingInputStream(crc32, inputStream);
        
        while (checksumInput.read() != -1) {}

        assertEquals(2256791406, checksumInput.getValue());
    }

}