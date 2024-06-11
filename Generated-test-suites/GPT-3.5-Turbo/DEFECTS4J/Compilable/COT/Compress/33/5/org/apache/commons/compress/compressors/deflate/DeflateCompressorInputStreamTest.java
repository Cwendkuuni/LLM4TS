package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] inputData = {0x78, 0x01, 0x01};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        int result = deflateInputStream.read();
        
        assertEquals(1, result);
    }
    
    @Test
    public void testRead_withBuffer() throws IOException {
        byte[] inputData = {0x78, 0x01, 0x01};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        byte[] buffer = new byte[10];
        int result = deflateInputStream.read(buffer, 0, 3);
        
        assertEquals(3, result);
        assertEquals(1, buffer[0]);
        assertEquals(0, buffer[1]);
        assertEquals(0, buffer[2]);
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] inputData = {0x78, 0x01, 0x01};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        long result = deflateInputStream.skip(2);
        
        assertEquals(2, result);
    }
    
    @Test
    public void testAvailable() throws IOException {
        byte[] inputData = {0x78, 0x01, 0x01};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        int result = deflateInputStream.available();
        
        assertEquals(3, result);
    }
    
    @Test
    public void testClose() throws IOException {
        byte[] inputData = {0x78, 0x01, 0x01};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        deflateInputStream.close();
        
        // Ensure no exceptions are thrown
    }

}
