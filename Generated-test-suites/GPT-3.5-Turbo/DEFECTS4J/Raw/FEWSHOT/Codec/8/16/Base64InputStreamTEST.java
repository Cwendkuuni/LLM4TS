
package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class Base64InputStreamTest {
    @Test
    public void testRead() throws Exception {
        // Given
        byte[] input = {65, 66, 67, 68, 69, 70}; // ABCDEF
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(input));
        
        // When
        int byte1 = base64InputStream.read();
        int byte2 = base64InputStream.read();
        int byte3 = base64InputStream.read();
        int byte4 = base64InputStream.read();
        
        // Then
        assertEquals(0, byte1);
        assertEquals(1, byte2);
        assertEquals(2, byte3);
        assertEquals(3, byte4);
    }

    @Test
    public void testRead_withNoData() throws Exception {
        // Given
        byte[] input = {};
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(input));
        
        // When
        int byte1 = base64InputStream.read();
        
        // Then
        assertEquals(-1, byte1);
    }

    @Test
    public void testRead_withNullArray() throws Exception {
        // Given
        byte[] input = null;
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(input));
        
        // When
        int byte1 = base64InputStream.read();
        
        // Then
        assertEquals(-1, byte1);
    }
    
    @Test
    public void testRead_withOffsetAndLength() throws Exception {
        // Given
        byte[] input = {65, 66, 67, 68, 69, 70}; // ABCDEF 
        byte[] output = new byte[4];
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(input));
        
        // When
        int bytesRead = base64InputStream.read(output, 1, 3);
        
        // Then
        assertEquals(3, bytesRead);
        assertEquals(0, output[0]);
        assertEquals(1, output[1]);
        assertEquals(2, output[2]);
        assertEquals(0, output[3]);
    }
    
    @Test
    public void testRead_withOffsetAndLength_exceedingBufferSize() throws Exception {
        // Given
        byte[] input = {65, 66, 67, 68, 69, 70}; // ABCDEF 
        byte[] output = new byte[4];
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(input));
        
        // When
        int bytesRead = base64InputStream.read(output, 1, 5);
        
        // Then
        assertEquals(3, bytesRead);
        assertEquals(0, output[0]);
        assertEquals(1, output[1]);
        assertEquals(2, output[2]);
        assertEquals(0, output[3]);
    }
    
    @Test(expected = NullPointerException.class)
    public void testRead_withNullBuffer() throws Exception {
        // Given
        byte[] input = {65, 66, 67, 68, 69, 70}; // ABCDEF
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(input));
        
        // When
        base64InputStream.read(null, 0, 0);
        
        // Then
        // Exception is thrown
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_withNegativeOffset() throws Exception {
        // Given
        byte[] input = {65, 66, 67, 68, 69, 70}; // ABCDEF
        byte[] output = new byte[4];
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(input));
        
        // When
        base64InputStream.read(output, -1, 0);
        
        // Then
        // Exception is thrown
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_withNegativeLength() throws Exception {
        // Given
        byte[] input = {65, 66, 67, 68, 69, 70}; // ABCDEF
        byte[] output = new byte[4];
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(input));
        
        // When
        base64InputStream.read(output, 0, -1);
        
        // Then
        // Exception is thrown
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_withInvalidOffset() throws Exception {
        // Given
        byte[] input = {65, 66, 67, 68, 69, 70}; // ABCDEF
        byte[] output = new byte[4];
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(input));
        
        // When
        base64InputStream.read(output, 5, 0);
        
        // Then
        // Exception is thrown
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_withInvalidLength() throws Exception {
        // Given
        byte[] input = {65, 66, 67, 68, 69, 70}; // ABCDEF
        byte[] output = new byte[4];
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(input));
        
        // When
        base64InputStream.read(output, 0, 5);
        
        // Then
        // Exception is thrown
    }
    
    @Test
    public void testMarkSupported() {
        // Given
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        
        // When
        boolean markSupported = base64InputStream.markSupported();
        
        // Then
        assertFalse(markSupported);
    }
}
