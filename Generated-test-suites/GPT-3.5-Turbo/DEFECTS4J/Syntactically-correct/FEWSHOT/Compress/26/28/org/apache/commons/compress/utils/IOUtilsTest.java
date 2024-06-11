package org.apache.commons.compress.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class IOUtilsTest {
    
    @Test
    public void testCopyWithDefaultBufferSize() throws IOException {
        byte[] inputBytes = "This is the input stream".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long count = IOUtils.copy(input, output);
        
        byte[] outputBytes = output.toByteArray();
        
        assertEquals(count, inputBytes.length);
        assertArrayEquals(inputBytes, outputBytes);
        
        input.close();
        output.close();
    }
    
    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        byte[] inputBytes = "This is the input stream".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        int bufferSize = 10;
        
        long count = IOUtils.copy(input, output, bufferSize);
        
        byte[] outputBytes = output.toByteArray();
        
        assertEquals(count, inputBytes.length);
        assertArrayEquals(inputBytes, outputBytes);
        
        input.close();
        output.close();
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = "This is the input stream".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        
        long numToSkip = 10;
        
        long skipped = IOUtils.skip(input, numToSkip);
        
        assertEquals(skipped, numToSkip);
        
        input.close();
    }
    
    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = "This is the input stream".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        
        byte[] buffer = new byte[20];
        
        int count = IOUtils.readFully(input, buffer);
        
        assertEquals(count, inputBytes.length);
        assertArrayEquals(inputBytes, buffer);
        
        input.close();
    }
    
    @Test
    public void testReadFullyWithOffset() throws IOException {
        byte[] inputBytes = "This is the input stream".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        
        byte[] buffer = new byte[30];
        
        int offset = 5;
        int length = 20;
        
        int count = IOUtils.readFully(input, buffer, offset, length);
        
        assertEquals(count, length);
        assertArrayEquals(inputBytes, 0, buffer, offset, length);
        
        input.close();
    }
    
    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = "This is the input stream".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        
        byte[] outputBytes = IOUtils.toByteArray(input);
        
        assertArrayEquals(inputBytes, outputBytes);
        
        input.close();
    }
    
    @Test
    public void testCloseQuietly() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        IOUtils.closeQuietly(input);
        
        assertNull(input);
    }
}