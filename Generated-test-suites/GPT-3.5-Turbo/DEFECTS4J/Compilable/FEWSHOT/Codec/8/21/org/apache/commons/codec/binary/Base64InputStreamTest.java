package org.apache.commons.codec.binary;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class Base64InputStreamTest {
    @Test
    public void testRead() throws IOException {
        byte[] testBytes = "This is a test".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(testBytes);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        int intValue = 0;
        for (int i = 0; i < testBytes.length; i++) {
            intValue = base64InputStream.read();
            assertEquals(testBytes[i], (byte)intValue);
        }
        intValue = base64InputStream.read();
        assertEquals(-1, intValue);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] testBytes = "This is a test".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(testBytes);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        byte[] byteArray = new byte[testBytes.length];
        int readLen = base64InputStream.read(byteArray, 0, testBytes.length);
        
        assertArrayEquals(testBytes, byteArray);
        assertEquals(testBytes.length, readLen);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        base64InputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        base64InputStream.read(new byte[1], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeLength() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        base64InputStream.read(new byte[1], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        base64InputStream.read(new byte[1], 1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        base64InputStream.read(new byte[1], 0, 2);
    }

    @Test
    public void testReadWhenDoEncodeIsTrue() throws IOException {
        byte[] testBytes = "This is a test".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(testBytes);
        Base64InputStream base64InputStream = new Base64InputStream(in, true);
        
        byte[] byteArray = new byte[testBytes.length];
        int readLen = base64InputStream.read(byteArray, 0, testBytes.length);
        
        assertEquals(testBytes.length, readLen);
    }

    @Test
    public void testReadWhenDoEncodeIsFalse() throws IOException {
        byte[] testBytes = "VGhpcyBpcyBhIHRlc3Q=".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(testBytes);
        Base64InputStream base64InputStream = new Base64InputStream(in, false);
        
        byte[] byteArray = new byte[testBytes.length];
        int readLen = base64InputStream.read(byteArray, 0, testBytes.length);
        
        assertEquals(testBytes.length, readLen);
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        assertFalse(base64InputStream.markSupported());
    }
}