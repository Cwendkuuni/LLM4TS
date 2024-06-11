package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {
    
    @Test
    public void testRead() throws IOException {
        byte[] data = {0x78, 0x9C, 0x01};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        
        int result = compressorInputStream.read();
        assertEquals(0x01, result);
        
        result = compressorInputStream.read();
        assertEquals(-1, result);
        
        compressorInputStream.close();
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = {0x78, 0x9C, 0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        
        byte[] buffer = new byte[3];
        int result = compressorInputStream.read(buffer, 0, buffer.length);
        assertEquals(3, result);
        assertEquals(data[2], buffer[0]);
        assertEquals(data[3], buffer[1]);
        assertEquals(data[4], buffer[2]);
        
        result = compressorInputStream.read(buffer, 0, buffer.length);
        assertEquals(2, result);
        assertEquals(data[5], buffer[0]);
        assertEquals(-1, result);
        
        compressorInputStream.close();
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] data = {0x78, 0x9C, 0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        
        long result = compressorInputStream.skip(2);
        assertEquals(2, result);
        
        result = compressorInputStream.skip(4);
        assertEquals(2, result);
        
        compressorInputStream.close();
    }
    
    @Test
    public void testAvailable() throws IOException {
        byte[] data = {0x78, 0x9C, 0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        
        int result = compressorInputStream.available();
        assertEquals(data.length, result);
        
        compressorInputStream.close();
    }
    
    @Test
    public void testClose() throws IOException {
        byte[] data = {0x78, 0x9C, 0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        
        compressorInputStream.close();
        
        // Verify that the input stream is closed
        try {
            int result = compressorInputStream.read();
        } catch (IOException e) {
            // Expected exception
        }
    }
}