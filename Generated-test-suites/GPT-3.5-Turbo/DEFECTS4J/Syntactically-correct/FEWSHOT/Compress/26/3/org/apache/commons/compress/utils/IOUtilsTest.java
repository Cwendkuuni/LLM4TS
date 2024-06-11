package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class IOUtilsTest {
    @Test
    public void testCopy() throws IOException {
        byte[] inputBytes = "test".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long count = IOUtils.copy(input, output);
        
        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, output.toByteArray());
        
        input.close();
        output.close();
    }
    
    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] inputBytes = "test".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
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
        byte[] inputBytes = "test".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        long numToSkip = 2;
        
        long actualSkipped = IOUtils.skip(input, numToSkip);
        
        assertEquals(numToSkip, actualSkipped);
        
        input.close();
    }
    
    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = "test".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[4];
        
        int actualBytesRead = IOUtils.readFully(input, buffer);
        
        assertEquals(inputBytes.length, actualBytesRead);
        assertArrayEquals(inputBytes, buffer);
        
        input.close();
    }
    
    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        byte[] inputBytes = "test".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[8];
        int offset = 2;
        int length = 4;
        
        int actualBytesRead = IOUtils.readFully(input, buffer, offset, length);
        
        assertEquals(length, actualBytesRead);
        assertArrayEquals(new byte[] {0, 0, 't', 'e', 's', 't', 0, 0}, buffer);
        
        input.close();
    }
    
    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = "test".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        
        byte[] actualBytes = IOUtils.toByteArray(input);
        
        assertArrayEquals(inputBytes, actualBytes);
        
        input.close();
    }
    
    @Test
    public void testCloseQuietly() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        
        IOUtils.closeQuietly(input);
        
        assertNull(input);
    }
}