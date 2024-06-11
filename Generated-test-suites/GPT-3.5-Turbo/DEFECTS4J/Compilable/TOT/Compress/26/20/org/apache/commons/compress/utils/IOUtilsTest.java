package org.apache.commons.compress.utils;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {
    
    @Test
    public void testCopyDefaultBufferSize() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        IOUtils.copy(input, output);
        
        byte[] outputBytes = output.toByteArray();
        
        assertArrayEquals(inputBytes, outputBytes);
    }
    
    @Test
    public void testCopyCustomBufferSize() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int bufferSize = 1024;
        
        IOUtils.copy(input, output, bufferSize);
        
        byte[] outputBytes = output.toByteArray();
        
        assertArrayEquals(inputBytes, outputBytes);
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        long numToSkip = 7;
        
        long skipped = IOUtils.skip(input, numToSkip);
        
        assertEquals(7, skipped);
        assertEquals('W', input.read());
    }
    
    @Test
    public void testSkipZeroBytes() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        long numToSkip = 0;
        
        long skipped = IOUtils.skip(input, numToSkip);
        
        assertEquals(0, skipped);
        assertEquals('H', input.read());
    }
    
    @Test
    public void testSkipMoreThanAvailable() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        long numToSkip = 20;
        
        long skipped = IOUtils.skip(input, numToSkip);
        
        assertEquals(13, skipped);
        assertEquals(-1, input.read());
    }
    
    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[6];
        
        int bytesRead = IOUtils.readFully(input, buffer);
        
        assertEquals(6, bytesRead);
        assertArrayEquals("Hello,".getBytes(), buffer);
    }
    
    @Test
    public void testReadFullyOffset() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[6];
        
        int bytesRead = IOUtils.readFully(input, buffer, 1, 4);
        
        assertEquals(4, bytesRead);
        assertArrayEquals(new byte[]{0, 'H', 'e', 'l', 0, 0}, buffer);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyInvalidOffset() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[6];
        
        IOUtils.readFully(input, buffer, -1, 4);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyInvalidLength() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[6];
        
        IOUtils.readFully(input, buffer, 1, 6);
    }
    
    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        
        byte[] outputBytes = IOUtils.toByteArray(input);
        
        assertArrayEquals(inputBytes, outputBytes);
    }
    
    @Test
    public void testCloseQuietly() {
        // Since closeQuietly() only swallows exceptions and there is no direct
        // impact on the code flow, it is difficult to write a meaningful test case.
        // We can only check that it executes without throwing an exception.
        IOUtils.closeQuietly(null);
    }
}