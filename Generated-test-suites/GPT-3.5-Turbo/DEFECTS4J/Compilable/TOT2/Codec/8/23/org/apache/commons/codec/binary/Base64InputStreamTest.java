package org.apache.commons.codec.binary;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.codec.binary.Base64InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64In = new Base64InputStream(in, true);
        
        int expectedByte = 'H';
        int actualByte = base64In.read();
        
        assertEquals(expectedByte, actualByte);
        
        base64In.close();
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64In = new Base64InputStream(in, true);
        
        byte[] buffer = new byte[5];
        int bytesRead = base64In.read(buffer, 0, 5);
        
        assertArrayEquals("Hello".getBytes(), buffer);
        assertEquals(5, bytesRead);
        
        base64In.close();
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadWithNullArray() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64In = new Base64InputStream(in, true);
        
        base64In.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64In = new Base64InputStream(in, true);
        
        base64In.read(new byte[1], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64In = new Base64InputStream(in, true);
        
        base64In.read(new byte[1], 0, -1);
    }

    @Test
    public void testMarkSupported() {
        Base64InputStream base64In = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        
        assertFalse(base64In.markSupported());
    }
}