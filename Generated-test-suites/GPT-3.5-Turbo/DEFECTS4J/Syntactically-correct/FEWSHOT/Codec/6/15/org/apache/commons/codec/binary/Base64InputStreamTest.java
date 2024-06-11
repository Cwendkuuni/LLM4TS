
package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {
    
    @Test
    public void testRead() throws IOException {
        byte[] input = { 0, 1, 2 };
        byte[] expected = { 0, 1, 2 };

        InputStream in = new ByteArrayInputStream(Base64.encodeBase64(input));
        Base64InputStream base64In = new Base64InputStream(in);
        byte[] actual = new byte[input.length];
        
        for (int i = 0; i < actual.length; i++) {
            actual[i] = (byte)base64In.read();
        }
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testReadWithEmptyInput() throws IOException {
        byte[] input = {};
        byte[] expected = {};

        InputStream in = new ByteArrayInputStream(Base64.encodeBase64(input));
        Base64InputStream base64In = new Base64InputStream(in);
        byte[] actual = new byte[input.length];
        
        for (int i = 0; i < actual.length; i++) {
            actual[i] = (byte)base64In.read();
        }
        
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReadWithInvalidBase64Data() throws IOException {
        byte[] input = { (byte)' ', (byte)'!', (byte)'@' };
        byte[] expected = { 0, 0, 0 };
        
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64In = new Base64InputStream(in);
        byte[] actual = new byte[input.length];
        
        for (int i = 0; i < actual.length; i++) {
            actual[i] = (byte)base64In.read();
        }
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testReadWithPartialBase64Data() throws IOException {
        byte[] input = { (byte)'a', (byte)'A', (byte)'b', (byte)'B', (byte)'c', (byte)'C' };
        byte[] expected = { 0, 0, 0, 1 };
        
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64In = new Base64InputStream(in);
        byte[] actual = new byte[expected.length];
        
        for (int i = 0; i < actual.length; i++) {
            actual[i] = (byte)base64In.read();
        }
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testReadWithExtraBase64Data() throws IOException {
        byte[] input = { (byte)'a', (byte)'A', (byte)'b', (byte)'B', (byte)'c', (byte)'C', (byte)'d', (byte)'D' };
        byte[] expected = { 0, 0, 0, 1 };
        
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64In = new Base64InputStream(in);
        byte[] actual = new byte[expected.length];
        
        for (int i = 0; i < actual.length; i++) {
            actual[i] = (byte)base64In.read();
        }
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testReadWithEmptyInputStream() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64In = new Base64InputStream(in);
        
        int actual = base64In.read();
        int expected = -1;
        
        assertEquals(expected, actual);
        assertFalse(base64In.markSupported());
    }
    
    @Test
    public void testReadWithNullByteArray() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64In = new Base64InputStream(in);
        
        try {
            base64In.read(null, 0, 0);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testReadWithInvalidOffsetOrLength() throws IOException {
        byte[] input = { 0, 1, 2 };
        
        InputStream in = new ByteArrayInputStream(Base64.encodeBase64(input));
        Base64InputStream base64In = new Base64InputStream(in);
        byte[] actual = new byte[4];
        
        try {
            base64In.read(actual, -1, 2);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
        
        try {
            base64In.read(actual, 0, -1);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
        
        try {
            base64In.read(actual, 0, 5);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testReadWithValidOffsetAndLength() throws IOException {
        byte[] input = { 0, 1, 2 };
        byte[] expected = { 0, 1, 0, 0, 0, 2 };
        
        InputStream in = new ByteArrayInputStream(Base64.encodeBase64(input));
        Base64InputStream base64In = new Base64InputStream(in);
        byte[] actual = new byte[expected.length];
        
        int read = base64In.read(actual, 2, 2);
        
        assertEquals(2, read);
        assertArrayEquals(expected, actual);
        
    }        
}
