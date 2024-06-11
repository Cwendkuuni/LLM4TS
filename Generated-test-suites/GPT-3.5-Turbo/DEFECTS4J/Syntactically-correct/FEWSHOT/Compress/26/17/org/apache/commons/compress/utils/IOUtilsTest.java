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
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long count = IOUtils.copy(input, output);
        
        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, output.toByteArray());
        
        input.close();
        output.close();
    }
    
    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        int bufferSize = 1024;
        long count = IOUtils.copy(input, output, bufferSize);
        
        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, output.toByteArray());
        
        input.close();
        output.close();
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        
        long skipped = IOUtils.skip(input, 5);
        
        assertEquals(5, skipped);
        
        input.close();
    }
    
    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        
        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(input, buffer);
        
        assertEquals(5, bytesRead);
        assertArrayEquals("Test ".getBytes(), buffer);
        
        input.close();
    }
    
    @Test
    public void testReadFullyWithOffsetAndLen() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        
        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(input, buffer, 1, 3);
        
        assertEquals(3, bytesRead);
        assertArrayEquals("est".getBytes(), buffer);
        
        input.close();
    }
    
    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        
        byte[] outputBytes = IOUtils.toByteArray(input);
        
        assertArrayEquals(inputBytes, outputBytes);
        
        input.close();
    }
    
    @Test
    public void testCloseQuietly() {
        // Not sure how to test this since it swallows the IOException.
        // Just checking that nothing crashes for now.
        IOUtils.closeQuietly(null);
    }
}