package org.apache.commons.codec.binary;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {
    
    private static final byte[] DATA = "Hello World!".getBytes();
    
    @Test
    public void testReadSingleByte() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(DATA);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        int result = base64InputStream.read();
        
        assertEquals('H', result);
        
        base64InputStream.close();
    }
    
    @Test
    public void testReadMultipleBytes() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(DATA);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] buffer = new byte[5];
        int result = base64InputStream.read(buffer);
        
        assertEquals(5, result);
        assertArrayEquals("Hello".getBytes(), buffer);
        
        base64InputStream.close();
    }
    
    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(DATA);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] buffer = new byte[10];
        int result = base64InputStream.read(buffer, 2, 6);
        
        assertEquals(6, result);
        assertArrayEquals(new byte[]{0, 0, 'H', 'e', 'l', 'l', 'o', 0, 0, 0}, buffer);
        
        base64InputStream.close();
    }
    
    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(DATA);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        long result = base64InputStream.skip(5);
        
        assertEquals(5, result);
        
        base64InputStream.close();
    }
    
    @Test
    public void testMarkSupported() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(DATA);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        boolean result = base64InputStream.markSupported();
        
        assertFalse(result);
        
        base64InputStream.close();
    }
}