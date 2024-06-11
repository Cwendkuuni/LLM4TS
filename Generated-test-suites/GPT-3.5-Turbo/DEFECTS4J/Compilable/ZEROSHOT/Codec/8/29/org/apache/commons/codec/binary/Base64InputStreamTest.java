package org.apache.commons.codec.binary;
import org.junit.Test;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class Base64InputStreamTest {
    
    @Test
    public void testRead() throws Exception {
        byte[] data = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 };
        InputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        int result = base64InputStream.read();
        
        assertEquals(72, result);
    }
    
    @Test
    public void testReadWithByteArray() throws Exception {
        byte[] data = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 };
        InputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] result = new byte[5];
        base64InputStream.read(result, 0, 5);
        
        byte[] expected = { 72, 101, 108, 108, 111 };
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testReadWithByteArrayAtOffset() throws Exception {
        byte[] data = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 };
        InputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] result = new byte[10];
        base64InputStream.read(result, 2, 5);
        
        byte[] expected = { 0, 0, 72, 101, 108, 108, 111, 0, 0, 0 };
        assertArrayEquals(expected, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws Exception {
        byte[] data = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 };
        InputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] result = null;
        base64InputStream.read(result, 0, 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws Exception {
        byte[] data = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 };
        InputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] result = new byte[5];
        base64InputStream.read(result, -1, 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws Exception {
        byte[] data = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 };
        InputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] result = new byte[5];
        base64InputStream.read(result, 0, -1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws Exception {
        byte[] data = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 };
        InputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] result = new byte[20];
        base64InputStream.read(result, 0, 5);
    }
    
    @Test
    public void testMarkSupported() throws Exception {
        byte[] data = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 };
        InputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        boolean result = base64InputStream.markSupported();
        
        assertEquals(false, result);
    }
}