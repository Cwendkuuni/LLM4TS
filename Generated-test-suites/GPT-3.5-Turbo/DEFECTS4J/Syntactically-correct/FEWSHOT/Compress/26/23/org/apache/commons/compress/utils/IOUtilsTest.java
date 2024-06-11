
package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class IOUtilsTest {
    
    @Test
    public void testCopy() throws IOException {
        // Create test data
        byte[] data = "Hello, world!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        // Perform copy
        long count = IOUtils.copy(input, output);
        
        // Verify output and count
        assertArrayEquals(data, output.toByteArray());
        assertEquals(data.length, count);
        
        // Close streams
        input.close();
        output.close();
    }
    
    @Test
    public void testCopyWithBufferSize() throws IOException {
        // Create test data
        byte[] data = "Hello, world!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        // Perform copy with larger buffer size
        int bufferSize = 1024;
        long count = IOUtils.copy(input, output, bufferSize);
        
        // Verify output and count
        assertArrayEquals(data, output.toByteArray());
        assertEquals(data.length, count);
        
        // Close streams
        input.close();
        output.close();
    }
    
    @Test
    public void testSkip() throws IOException {
        // Create test data
        byte[] data = "Hello, world!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        
        // Perform skip
        long numToSkip = 5;
        long amountSkipped = IOUtils.skip(input, numToSkip);
        
        // Verify amount skipped
        assertEquals(numToSkip, amountSkipped);
        
        // Read remaining data
        byte[] remainingData = new byte[data.length - (int) numToSkip];
        int bytesRead = input.read(remainingData);
        
        // Verify remaining data
        assertArrayEquals("world!".getBytes(), remainingData);
        assertEquals(remainingData.length, bytesRead);
        
        // Close stream
        input.close();
    }
    
    @Test
    public void testReadFully() throws IOException {
        // Create test data
        byte[] data = "Hello, world!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        byte[] buffer = new byte[data.length];
        
        // Perform read
        int bytesRead = IOUtils.readFully(input, buffer);
        
        // Verify bytes read and buffer content
        assertEquals(data.length, bytesRead);
        assertArrayEquals(data, buffer);
        
        // Close stream
        input.close();
    }
    
    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        // Create test data
        byte[] data = "Hello, world!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        byte[] buffer = new byte[data.length];
        
        // Perform read with offset and length
        int offset = 7;
        int length = 5;
        int bytesRead = IOUtils.readFully(input, buffer, offset, length);
        
        // Create expected data
        byte[] expectedData = new byte[buffer.length];
        System.arraycopy(data, offset, expectedData, offset, length);
        
        // Verify bytes read and buffer content
        assertEquals(length, bytesRead);
        assertArrayEquals(expectedData, buffer);
        
        // Close stream
        input.close();
    }
    
    @Test
    public void testToByteArray() throws IOException {
        // Create test data
        byte[] data = "Hello, world!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        
        // Perform conversion
        byte[] result = IOUtils.toByteArray(input);
        
        // Verify result
        assertArrayEquals(data, result);
        
        // Close stream
        input.close();
    }
    
    @Test
    public void testCloseQuietly() throws IOException {
        // Create test data
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Closeable c = output;
        
        // Perform close quietly
        IOUtils.closeQuietly(c);
        
        // Verify that no exception is thrown
    }
    
}
