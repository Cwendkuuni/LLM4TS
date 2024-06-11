import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Test;

public class Base64InputStreamTest {

    // Test case for constructor 1
    @Test
    public void testConstructor1() {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        assertNotNull(base64InputStream);
    }
    
    // Test case for constructor 2
    @Test
    public void testConstructor2() {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        
        assertNotNull(base64InputStream);
    }
    
    // Test case for constructor 3
    @Test
    public void testConstructor3() {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, "\r\n".getBytes());
        
        assertNotNull(base64InputStream);
    }

    // Typical use case for read() method
    @Test
    public void testReadTypicalCase() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        int data;
        StringBuilder result = new StringBuilder();
        while ((data = base64InputStream.read()) != -1) {
            result.append((char) data);
        }
        
        assertEquals("Hello World", result.toString());
    }
    
    // Edge case for read() method with empty input stream
    @Test
    public void testReadEmptyStream() throws IOException {
        byte[] inputBytes = "".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        int data;
        StringBuilder result = new StringBuilder();
        while ((data = base64InputStream.read()) != -1) {
            result.append((char) data);
        }
        
        assertEquals("", result.toString());
    }
    
    // Edge case for read() method with single byte input stream
    @Test
    public void testReadSingleByte() throws IOException {
        byte[] inputBytes = "Hello".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        int data;
        StringBuilder result = new StringBuilder();
        while ((data = base64InputStream.read()) != -1) {
            result.append((char) data);
        }
        
        assertEquals("Hello", result.toString());
    }
    
    // Error scenario for read() method with null byte array
    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        base64InputStream.read(null, 0, 1);
    }
    
    // Error scenario for read() method with negative offset
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeOffset() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, -1, 5);
    }
    
    // Error scenario for read() method with negative length
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeLength() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 0, -5);
    }
    
    // Error scenario for read() method with invalid offset
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffset() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 20, 5);
    }
    
    // Error scenario for read() method with invalid buffer size
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidBufferSize() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, 0, 10);
    }
    
    // Error scenario for read() method with empty buffer but valid length
    @Test
    public void testReadEmptyBuffer() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] buffer = new byte[0];
        
        int readLength = base64InputStream.read(buffer, 0, 5);
        
        assertEquals(0, readLength);
    }
    
    // Typical use case for markSupported() method
    @Test
    public void testMarkSupportedTypicalCase() {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        assertFalse(base64InputStream.markSupported());
    }
}