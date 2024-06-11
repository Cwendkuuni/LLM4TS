package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {
    
    @Test
    public void testRead_singleByte() throws IOException {
        byte[] data = new byte[]{0x01, 0x02, 0x03};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);
        
        int byteValue = stream.read();
        
        assertEquals(0x01, byteValue);
        assertEquals(1, checksum.getValue());
    }
    
    @Test
    public void testRead_byteArray() throws IOException {
        byte[] data = new byte[]{0x01, 0x02, 0x03};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);
        
        byte[] buffer = new byte[2];
        int bytesRead = stream.read(buffer);
        
        assertEquals(2, bytesRead);
        assertEquals(0x01, buffer[0]);
        assertEquals(0x02, buffer[1]);
        assertEquals(3, checksum.getValue());
    }
    
    @Test
    public void testRead_byteArrayWithOffset() throws IOException {
        byte[] data = new byte[]{0x01, 0x02, 0x03};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);
        
        byte[] buffer = new byte[3];
        int bytesRead = stream.read(buffer, 1, 2);
        
        assertEquals(2, bytesRead);
        assertEquals(0x00, buffer[0]);
        assertEquals(0x01, buffer[1]);
        assertEquals(0x02, buffer[2]);
        assertEquals(3, checksum.getValue());
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] data = new byte[]{0x01, 0x02, 0x03};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);
        
        long skipped = stream.skip(2);
        
        assertEquals(2, skipped);
        assertEquals(3, stream.getValue());
    }
    
    @Test
    public void testGetValue() {
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, null);
        
        assertEquals(0, stream.getValue());
    }
}