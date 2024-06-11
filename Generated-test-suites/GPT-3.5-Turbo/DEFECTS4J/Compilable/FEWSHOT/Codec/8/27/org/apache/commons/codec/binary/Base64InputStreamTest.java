package org.apache.commons.codec.binary;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.Test;

public class Base64InputStreamTest {
    
    @Test
    public void testConstructor() {
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64In = new Base64InputStream(in);
        assertNotNull(base64In);
    }
    
    @Test
    public void testReadSingleByte() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64In = new Base64InputStream(in);
        
        int result = base64In.read();
        
        assertEquals(116, result);
    }
    
    @Test
    public void testReadArray() throws IOException {
        byte[] buffer = new byte[4];
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64In = new Base64InputStream(in);
        
        int result = base64In.read(buffer, 0, 4);
        
        assertEquals(4, result);
        assertArrayEquals(new byte[] { 116, 101, 115, 116 }, buffer);
    }

    @Test
    public void testReadArray_OffsetAndLength() throws IOException {
        byte[] buffer = new byte[6];
        ByteArrayInputStream in = new ByteArrayInputStream("testing".getBytes());
        Base64InputStream base64In = new Base64InputStream(in);
        
        int result = base64In.read(buffer, 1, 4);
        
        assertEquals(4, result);
        assertArrayEquals(new byte[] { 0, 116, 101, 115, 116, 0 }, buffer);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArray_InvalidOffsetAndLength() throws IOException {
        byte[] buffer = new byte[6];
        ByteArrayInputStream in = new ByteArrayInputStream("testing".getBytes());
        Base64InputStream base64In = new Base64InputStream(in);
        
        base64In.read(buffer, 0, 7);
    }
    
    @Test
    public void testReadArray_NoData() throws IOException {
        byte[] buffer = new byte[4];
        ByteArrayInputStream in = new ByteArrayInputStream("".getBytes());
        Base64InputStream base64In = new Base64InputStream(in);
        
        int result = base64In.read(buffer, 0, 4);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testMarkSupported() {
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64In = new Base64InputStream(in);
        
        boolean result = base64In.markSupported();
        
        assertFalse(result);
    }
}