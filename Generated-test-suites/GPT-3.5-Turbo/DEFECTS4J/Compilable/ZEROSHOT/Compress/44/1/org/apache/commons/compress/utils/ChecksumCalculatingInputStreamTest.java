package org.apache.commons.compress.utils;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead_withoutData() throws IOException {
        Checksum checksum = new CRC32();
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(checksum, is);
        
        int data = csis.read();
        
        assertEquals(-1, data);
        assertEquals(0, csis.getValue());
    }
    
    @Test
    public void testRead_withData() throws IOException {
        Checksum checksum = new CRC32();
        byte[] bytes = new byte[] {1, 2, 3};
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(checksum, is);
        
        int data = csis.read();
        
        assertEquals(1, data);
        assertEquals(1, csis.getValue());
    }
    
    @Test
    public void testRead_byteArray() throws IOException {
        Checksum checksum = new CRC32();
        byte[] bytes = new byte[] {1, 2, 3};
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(checksum, is);
        
        byte[] readBytes = new byte[3];
        int bytesRead = csis.read(readBytes);

        assertArrayEquals(bytes, readBytes);
        assertEquals(3, bytesRead);
        assertEquals(464489720, csis.getValue());
    }
    
    @Test
    public void testRead_byteArray_withOffset() throws IOException {
        Checksum checksum = new CRC32();
        byte[] bytes = new byte[] {1, 2, 3};
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(checksum, is);
        
        byte[] readBytes = new byte[3];
        int bytesRead = csis.read(readBytes, 1, 2);

        assertArrayEquals(new byte[]{0, 1, 2}, readBytes);
        assertEquals(2, bytesRead);
        assertEquals(301475820, csis.getValue());
    }
    
    @Test
    public void testSkip() throws IOException {
        Checksum checksum = new CRC32();
        byte[] bytes = new byte[] {1, 2, 3};
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(checksum, is);
        
        long skipped = csis.skip(2);

        assertEquals(2, skipped);
        assertEquals(1, csis.read());
        assertEquals(1, csis.getValue());
    }
}