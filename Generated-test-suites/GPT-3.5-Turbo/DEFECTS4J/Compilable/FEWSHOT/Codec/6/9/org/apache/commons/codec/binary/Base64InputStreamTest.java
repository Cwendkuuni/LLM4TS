package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class Base64InputStreamTest {
    
    @Test
    public void testRead() throws IOException {
        byte[] input = new byte[] { 65, 66, 67 }; // ABC
        
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream bis = new Base64InputStream(bais);
        
        assertEquals(65, bis.read());
        assertEquals(66, bis.read());
        assertEquals(67, bis.read());
        assertEquals(-1, bis.read());
        
        bis.close();
    }
    
    @Test
    public void testReadWithZeroLength() throws IOException {
        byte[] input = new byte[] { 65, 66, 67 }; // ABC
        
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream bis = new Base64InputStream(bais);
        
        byte[] buffer = new byte[0];
        assertEquals(0, bis.read(buffer, 0, 0));
        
        bis.close();
    }
    
    @Test
    public void testReadWithNullBuffer() throws IOException {
        byte[] input = new byte[] { 65, 66, 67 }; // ABC
        
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream bis = new Base64InputStream(bais);
        
        try {
            bis.read(null, 0, 1);
        } catch (NullPointerException e) {
            assertFalse(bis.markSupported());
        }
        
        bis.close();
    }
    
    @Test
    public void testReadWithNegativeOffset() throws IOException {
        byte[] input = new byte[] { 65, 66, 67 }; // ABC
        
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream bis = new Base64InputStream(bais);
        
        byte[] buffer = new byte[3];
        try {
            bis.read(buffer, -1, 3);
        } catch (IndexOutOfBoundsException e) {
            assertFalse(bis.markSupported());
        }
        
        bis.close();
    }
    
    @Test
    public void testReadWithNegativeLength() throws IOException {
        byte[] input = new byte[] { 65, 66, 67 }; // ABC
        
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream bis = new Base64InputStream(bais);
        
        byte[] buffer = new byte[3];
        try {
            bis.read(buffer, 0, -1);
        } catch (IndexOutOfBoundsException e) {
            assertFalse(bis.markSupported());
        }
        
        bis.close();
    }
    
    @Test
    public void testReadWithInvalidBufferOffset() throws IOException {
        byte[] input = new byte[] { 65, 66, 67 }; // ABC
        
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream bis = new Base64InputStream(bais);
        
        byte[] buffer = new byte[3];
        try {
            bis.read(buffer, 4, 2);
        } catch (IndexOutOfBoundsException e) {
            assertFalse(bis.markSupported());
        }
        
        bis.close();
    }
    
    @Test
    public void testReadWithInvalidBufferLength() throws IOException {
        byte[] input = new byte[] { 65, 66, 67 }; // ABC
        
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream bis = new Base64InputStream(bais);
        
        byte[] buffer = new byte[3];
        try {
            bis.read(buffer, 2, 4);
        } catch (IndexOutOfBoundsException e) {
            assertFalse(bis.markSupported());
        }
        
        bis.close();
    }
    
    @Test
    public void testReadWithFullLengthBuffer() throws IOException {
        byte[] input = new byte[] { 65, 66, 67 }; // ABC
        
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream bis = new Base64InputStream(bais);
        
        byte[] buffer = new byte[3];
        
        assertEquals(3, bis.read(buffer, 0, 3));
        assertEquals(65, buffer[0]);
        assertEquals(66, buffer[1]);
        assertEquals(67, buffer[2]);
        
        bis.close();
    }
    
    @Test
    public void testReadWithPartialLengthBuffer() throws IOException {
        byte[] input = new byte[] { 65, 66, 67 }; // ABC
        
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream bis = new Base64InputStream(bais);
        
        byte[] buffer = new byte[3];
        
        assertEquals(2, bis.read(buffer, 0, 2));
        assertEquals(65, buffer[0]);
        assertEquals(66, buffer[1]);
        assertEquals(0, buffer[2]);
        
        bis.close();
    }
    
    @Test
    public void testReadWithLessThanLengthBuffer() throws IOException {
        byte[] input = new byte[] { 65, 66, 67 }; // ABC
        
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream bis = new Base64InputStream(bais);
        
        byte[] buffer = new byte[5];
        
        assertEquals(3, bis.read(buffer, 0, 5));
        assertEquals(65, buffer[0]);
        assertEquals(66, buffer[1]);
        assertEquals(67, buffer[2]);
        assertEquals(0, buffer[3]);
        assertEquals(0, buffer[4]);
        
        bis.close();
    }
}