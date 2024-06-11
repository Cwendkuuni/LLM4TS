
package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class Base64InputStreamTest {
    
    @Test
    public void testConstructor() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[] { 0, 1, 2, 3 });
        
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        assertFalse(base64InputStream.doEncode);
        assertNotNull(base64InputStream.base64);
        assertTrue(base64InputStream.base64 instanceof Base64);
    }
    
    @Test
    public void testConstructorWithEncode() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[] { 0, 1, 2, 3 });
        
        Base64InputStream base64InputStream = new Base64InputStream(in, true);
        
        assertTrue(base64InputStream.doEncode);
        assertNotNull(base64InputStream.base64);
        assertTrue(base64InputStream.base64 instanceof Base64);
    }
    
    @Test
    public void testConstructorWithEncodeAndLineLength() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[] { 0, 1, 2, 3 });
        
        Base64InputStream base64InputStream = new Base64InputStream(in, true, 76, Base64.CRLF);
        
        assertTrue(base64InputStream.doEncode);
        assertNotNull(base64InputStream.base64);
        assertEquals(76, base64InputStream.base64.getLineLength());
        assertNotNull(base64InputStream.base64.getLineSeparator());
        assertArrayEquals(Base64.CRLF, base64InputStream.base64.getLineSeparator());
    }
    
    @Test
    public void testRead() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.encodeBase64("test".getBytes()));
        
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        assertEquals('t', base64InputStream.read());
        assertEquals('e', base64InputStream.read());
        assertEquals('s', base64InputStream.read());
        assertEquals('t', base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }
    
    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.encodeBase64("test".getBytes()));
        byte[] buffer = new byte[10];
        
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        assertEquals(4, base64InputStream.read(buffer, 0, 4));
        assertEquals('t', base64InputStream.read());
        assertEquals('e', base64InputStream.read());
        assertEquals('s', base64InputStream.read());
        assertEquals('t', base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }
    
    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.encodeBase64("test".getBytes()));
        byte[] buffer = new byte[0];
        
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        assertEquals(0, base64InputStream.read(buffer, 0, 0));
    }
    
    @Test
    public void testReadWithNullBuffer() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.encodeBase64("test".getBytes()));
        byte[] buffer = null;
        
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        assertThrows(NullPointerException.class, () -> base64InputStream.read(buffer, 0, 4));
    }
    
    @Test
    public void testReadWithInvalidOffset() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.encodeBase64("test".getBytes()));
        byte[] buffer = new byte[10];
        
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        assertThrows(IndexOutOfBoundsException.class, () -> base64InputStream.read(buffer, -1, 4));
    }
    
    @Test
    public void testReadWithInvalidLength() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.encodeBase64("test".getBytes()));
        byte[] buffer = new byte[10];
        
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        assertThrows(IndexOutOfBoundsException.class, () -> base64InputStream.read(buffer, 0, -1));
    }
    
    @Test
    public void testReadWithInvalidBufferSize() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.encodeBase64("test".getBytes()));
        byte[] buffer = new byte[5];
        
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        assertThrows(IndexOutOfBoundsException.class, () -> base64InputStream.read(buffer, 0, 5));
    }
    
    @Test
    public void testReadWithInvalidBufferSizeAndLength() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.encodeBase64("test".getBytes()));
        byte[] buffer = new byte[5];
        
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        assertThrows(IndexOutOfBoundsException.class, () -> base64InputStream.read(buffer, 0, 6));
    }
    
    @Test
    public void testReadWithZeroLength() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.encodeBase64("test".getBytes()));
        byte[] buffer = new byte[10];
        
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        assertEquals(4, base64InputStream.read(buffer, 0, 0));
        assertEquals('t', base64InputStream.read());
        assertEquals('e', base64InputStream.read());
        assertEquals('s', base64InputStream.read());
        assertEquals('t', base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }
    
    @Test
    public void testReadWithNoData() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.encodeBase64("".getBytes()));
        byte[] buffer = new byte[10];
        
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        assertEquals(0, base64InputStream.read(buffer, 0, 10));
    }
    
    @Test
    public void testMarkSupported() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[] { 0, 1, 2, 3 });
        
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        assertFalse(base64InputStream.markSupported());
    }
}
