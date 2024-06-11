package org.apache.commons.codec.binary;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {
    
    private static final byte[] BASE64_ENCODED = {82, 71, 86, 115, 98, 71};
    private static final byte[] BASE64_DECODED = {74, 85, 78, 112, 75, 85};
    
    private Base64InputStream base64InputStream;

    @Before
    public void setUp() {
        InputStream inputStream = new ByteArrayInputStream(BASE64_ENCODED);
        base64InputStream = new Base64InputStream(inputStream, false);
    }
    
    @Test
    public void testRead() throws IOException {
        int readValue = base64InputStream.read();
        assertEquals(BASE64_DECODED[0], readValue);
    }
    
    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[2];
        int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(BASE64_DECODED[0], buffer[0]);
        assertEquals(BASE64_DECODED[1], buffer[1]);
    }
    
    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[6];
        int bytesRead = base64InputStream.read(buffer, 1, 4);
        assertEquals(4, bytesRead);
        assertEquals(0, buffer[0]);
        assertEquals(BASE64_DECODED[0], buffer[1]);
        assertEquals(BASE64_DECODED[1], buffer[2]);
        assertEquals(BASE64_DECODED[2], buffer[3]);
        assertEquals(BASE64_DECODED[3], buffer[4]);
        assertEquals(0, buffer[5]);
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        base64InputStream.read(null, 0, 4);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[4];
        base64InputStream.read(buffer, -1, 4);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        byte[] buffer = new byte[4];
        base64InputStream.read(buffer, 0, 10);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        byte[] buffer = new byte[2];
        base64InputStream.read(buffer, 0, 4);
    }
    
    @Test
    public void testMarkSupported() {
        boolean markSupported = base64InputStream.markSupported();
        assertEquals(false, markSupported);
    }
}