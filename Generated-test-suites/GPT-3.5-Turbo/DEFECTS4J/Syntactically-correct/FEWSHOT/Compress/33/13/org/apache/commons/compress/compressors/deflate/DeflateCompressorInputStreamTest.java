package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class DeflateCompressorInputStreamTest {
    
    @Test
    public void testConstructor() throws IOException {
        byte[] data = "Test data".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);
        assertNotNull(stream);
    }
    
    @Test
    public void testRead() throws IOException {
        byte[] data = "Test data".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);
        
        int b = stream.read();
        assertEquals('T', b);
        
        byte[] buffer = new byte[4];
        int bytesRead = stream.read(buffer);
        assertEquals(4, bytesRead);
        assertTrue("Test".equals(new String(buffer, StandardCharsets.UTF_8)));
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] data = "Test data".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);
        
        long skipped = stream.skip(2);
        assertEquals(2, skipped);
        
        int b = stream.read();
        assertEquals('s', b);
    }
    
    @Test
    public void testAvailable() throws IOException {
        byte[] data = "Test data".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);
        
        int available = stream.available();
        assertEquals(data.length, available);
    }
    
    @Test
    public void testClose() throws IOException {
        byte[] data = "Test data".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);
        
        stream.close();
        
        try {
            int b = stream.read();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // expected
        }
    }
    
    @Test
    public void testSignatureCheck() throws IOException {
        byte[] data = "Test data".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);
        
        byte[] signature = "Signature".getBytes(StandardCharsets.UTF_8);
        
        boolean result = stream.signatureCheck(signature, signature.length);
        assertTrue(result);
    }
    
}