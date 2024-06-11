
package org.apache.commons.codec.binary;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {
    
    // TODO: Add more test cases to cover all methods in the Base64InputStream class
    
    @Test
    public void testRead() throws IOException {
        byte[] data = new byte[]{0x49, 0x4D, 0x41, 0x47, 0x45};
        InputStream in = new Base64InputStream(new ByteArrayInputStream(data));
        
        assertEquals(0x49, in.read());
        assertEquals(0x4D, in.read());
        assertEquals(0x41, in.read());
        assertEquals(0x47, in.read());
        assertEquals(0x45, in.read());
        
        assertEquals(-1, in.read());
        
        in.close();
    }
    
    @Test
    public void testReadArray() throws IOException {
        byte[] data = new byte[]{0x49, 0x4D, 0x41, 0x47, 0x45};
        InputStream in = new Base64InputStream(new ByteArrayInputStream(data));
        
        byte[] buffer = new byte[4];
        
        assertEquals(4, in.read(buffer, 0, 4));
        assertArrayEquals(new byte[]{0x49, 0x4D, 0x41, 0x47}, buffer);
        
        assertEquals(1, in.read(buffer, 0, 4));
        assertArrayEquals(new byte[]{0x45, 0x47, 0x41, 0x47}, buffer);
        
        assertEquals(-1, in.read(buffer, 0, 4));
        
        in.close();
    }
    
    @Test
    public void testReadArrayWithOffset() throws IOException {
        byte[] data = new byte[]{0x49, 0x4D, 0x41, 0x47, 0x45};
        InputStream in = new Base64InputStream(new ByteArrayInputStream(data));
        
        byte[] buffer = new byte[6];
        
        assertEquals(4, in.read(buffer, 1, 4));
        assertArrayEquals(new byte[]{0x00, 0x49, 0x4D, 0x41, 0x47, 0x00}, buffer);
        
        assertEquals(1, in.read(buffer, 3, 4));
        assertArrayEquals(new byte[]{0x00, 0x49, 0x4D, 0x41, 0x45, 0x47}, buffer);
        
        assertEquals(-1, in.read(buffer, 0, 6));
        
        in.close();
    }
    
    @Test
    public void testReadArrayWithInvalidOffset() throws IOException {
        byte[] data = new byte[]{0x49, 0x4D, 0x41, 0x47, 0x45};
        InputStream in = new Base64InputStream(new ByteArrayInputStream(data));
        
        byte[] buffer = new byte[4];
        
        try {
            in.read(buffer, -1, 4);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Test passed
        }
        
        try {
            in.read(buffer, 5, 4);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Test passed
        }
        
        in.close();
    }
    
    @Test
    public void testReadArrayWithNullBuffer() throws IOException {
        byte[] data = new byte[]{0x49, 0x4D, 0x41, 0x47, 0x45};
        InputStream in = new Base64InputStream(new ByteArrayInputStream(data));
        
        try {
            in.read(null, 0, 4);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Test passed
        }
        
        in.close();
    }
    
    @Test
    public void testMarkSupported() {
        InputStream in = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        assertFalse(in.markSupported());
    }

}
