package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class Base64InputStreamTest {
    
    private final String testString = "This is a test string";
    
    @Test
    public void testConstructor() {
        ByteArrayInputStream bais = new ByteArrayInputStream(testString.getBytes());
        Base64InputStream inputStream = new Base64InputStream(bais);
        assertNotNull(inputStream);
    }
    
    @Test
    public void testConstructorWithEncodeFlag() {
        ByteArrayInputStream bais = new ByteArrayInputStream(testString.getBytes());
        Base64InputStream inputStream = new Base64InputStream(bais, true);
        assertNotNull(inputStream);
    }
    
    @Test
    public void testConstructorWithLineLengthAndLineSeparator() {
        ByteArrayInputStream bais = new ByteArrayInputStream(testString.getBytes());
        Base64InputStream inputStream = new Base64InputStream(bais, true, 76, "\r\n".getBytes());
        assertNotNull(inputStream);
    }
    
    @Test
    public void testRead() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(testString.getBytes());
        Base64InputStream inputStream = new Base64InputStream(bais);
        int data = inputStream.read();
        assertEquals(84, data);
    }
    
    @Test
    public void testReadWithByteArray() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(testString.getBytes());
        Base64InputStream inputStream = new Base64InputStream(bais);
        byte[] buffer = new byte[10];
        int bytesRead = inputStream.read(buffer, 0, buffer.length);
        assertEquals(10, bytesRead);
        assertEquals("This is a ", new String(buffer));
    }
    
    @Test
    public void testReadWithByteArrayAndOffset() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(testString.getBytes());
        Base64InputStream inputStream = new Base64InputStream(bais);
        byte[] buffer = new byte[10];
        int bytesRead = inputStream.read(buffer, 3, buffer.length);
        assertEquals(7, bytesRead);
        assertEquals("\u0000\u0000\u0000This is ", new String(buffer));
    }
    
    @Test
    public void testReadWithByteArrayAndOffsetAndLength() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(testString.getBytes());
        Base64InputStream inputStream = new Base64InputStream(bais);
        byte[] buffer = new byte[10];
        int bytesRead = inputStream.read(buffer, 3, 5);
        assertEquals(5, bytesRead);
        assertEquals("\u0000\u0000\u0000This ", new String(buffer));
    }
    
    @Test
    public void testReadWithEmptyByteArray() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(testString.getBytes());
        Base64InputStream inputStream = new Base64InputStream(bais);
        byte[] buffer = new byte[0];
        int bytesRead = inputStream.read(buffer, 0, buffer.length);
        assertEquals(0, bytesRead);
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(testString.getBytes());
        Base64InputStream inputStream = new Base64InputStream(bais);
        inputStream.read(null, 0, 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(testString.getBytes());
        Base64InputStream inputStream = new Base64InputStream(bais);
        byte[] buffer = new byte[10];
        inputStream.read(buffer, -1, 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(testString.getBytes());
        Base64InputStream inputStream = new Base64InputStream(bais);
        byte[] buffer = new byte[10];
        inputStream.read(buffer, 11, 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferLength() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(testString.getBytes());
        Base64InputStream inputStream = new Base64InputStream(bais);
        byte[] buffer = new byte[10];
        inputStream.read(buffer, 0, 15);
    }
    
    @Test
    public void testMarkSupported() {
        ByteArrayInputStream bais = new ByteArrayInputStream(testString.getBytes());
        Base64InputStream inputStream = new Base64InputStream(bais);
        assertFalse(inputStream.markSupported());
    }
}