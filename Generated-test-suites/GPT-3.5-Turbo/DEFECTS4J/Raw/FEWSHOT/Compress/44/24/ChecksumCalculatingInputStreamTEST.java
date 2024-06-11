package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {
    @Test
    public void testRead() throws IOException {
        byte[] data = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream checkInput = new ChecksumCalculatingInputStream(checksum, inputStream);
        
        int read = checkInput.read();
        assertEquals(1, read);
        assertEquals(1, checksum.getValue());
        
        read = checkInput.read();
        assertEquals(2, read);
        assertEquals(770909310, checksum.getValue());
        
        read = checkInput.read();
        assertEquals(3, read);
        assertEquals(1998283259, checksum.getValue());
        
        read = checkInput.read();
        assertEquals(4, read);
        assertEquals(2498305588, checksum.getValue());
        
        read = checkInput.read();
        assertEquals(5, read);
        assertEquals(3462565395, checksum.getValue());
        
        read = checkInput.read();
        assertEquals(-1, read);
        assertEquals(3462565395, checksum.getValue());
    }
    
    @Test
    public void testReadArray() throws IOException {
        byte[] data = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream checkInput = new ChecksumCalculatingInputStream(checksum, inputStream);
        
        byte[] buffer = new byte[3];
        int read = checkInput.read(buffer);
        assertEquals(3, read);
        assertEquals(1, checksum.getValue());
        assertEquals(1, buffer[0]);
        assertEquals(2, buffer[1]);
        assertEquals(3, buffer[2]);
        
        read = checkInput.read(buffer);
        assertEquals(2, read);
        assertEquals(1998283259, checksum.getValue());
        assertEquals(4, buffer[0]);
        assertEquals(5, buffer[1]);
        
        read = checkInput.read(buffer);
        assertEquals(-1, read);
        assertEquals(3462565395, checksum.getValue());
    }
    
    @Test
    public void testReadOffLen() throws IOException {
        byte[] data = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream checkInput = new ChecksumCalculatingInputStream(checksum, inputStream);
        
        byte[] buffer = new byte[5];
        int read = checkInput.read(buffer, 1, 3);
        assertEquals(3, read);
        assertEquals(1, checksum.getValue());
        assertEquals(0, buffer[0]);
        assertEquals(1, buffer[1]);
        assertEquals(2, buffer[2]);
        assertEquals(3, buffer[3]);
        assertEquals(0, buffer[4]);
        
        read = checkInput.read(buffer, 1, 3);
        assertEquals(2, read);
        assertEquals(1998283259, checksum.getValue());
        assertEquals(0, buffer[0]);
        assertEquals(4, buffer[1]);
        assertEquals(5, buffer[2]);
        assertEquals(3, buffer[3]);
        assertEquals(0, buffer[4]);
        
        read = checkInput.read(buffer, 1, 3);
        assertEquals(-1, read);
        assertEquals(3462565395, checksum.getValue());
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] data = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream checkInput = new ChecksumCalculatingInputStream(checksum, inputStream);
        
        long skipped = checkInput.skip(3);
        assertEquals(3, skipped);
        assertEquals(3, checksum.getValue());
        
        int read = checkInput.read();
        assertEquals(4, read);
        assertEquals(2498305588, checksum.getValue());
        
        read = checkInput.read();
        assertEquals(5, read);
        assertEquals(3462565395, checksum.getValue());
        
        skipped = checkInput.skip(1);
        assertEquals(0, skipped);
        assertEquals(3462565395, checksum.getValue());
    }
    
    @Test
    public void testGetValue() throws IOException {
        byte[] data = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream checkInput = new ChecksumCalculatingInputStream(checksum, inputStream);
        
        int read = checkInput.read();
        assertEquals(1, read);
        assertEquals(1, checkInput.getValue());
        
        read = checkInput.read();
        assertEquals(2, read);
        assertEquals(770909310, checkInput.getValue());
        
        read = checkInput.read();
        assertEquals(3, read);
        assertEquals(1998283259, checkInput.getValue());
        
        read = checkInput.read();
        assertEquals(4, read);
        assertEquals(2498305588, checkInput.getValue());
        
        read = checkInput.read();
        assertEquals(5, read);
        assertEquals(3462565395, checkInput.getValue());
        
        read = checkInput.read();
        assertEquals(-1, read);
        assertEquals(3462565395, checkInput.getValue());
    }
}