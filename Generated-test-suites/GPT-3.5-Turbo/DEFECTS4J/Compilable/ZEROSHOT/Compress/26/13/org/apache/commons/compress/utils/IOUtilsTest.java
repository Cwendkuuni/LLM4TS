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
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("Test string".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        long count = IOUtils.copy(input, output);
        
        assertEquals("Test string".length(), count);
        assertEquals("Test string", output.toString());
    }
    
    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test string".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        long count = IOUtils.copy(input, output, 10);
        
        assertEquals("Test string".length(), count);
        assertEquals("Test string", output.toString());
    }
    
    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("Test string".getBytes());
        
        long skipped = IOUtils.skip(input, 5);
        
        assertEquals(5, skipped);
    }
    
    @Test
    public void testSkipLessThanRequested() throws IOException {
        InputStream input = new ByteArrayInputStream("Test string".getBytes());
        
        long skipped = IOUtils.skip(input, 50);
        
        assertEquals("Test string".length(), skipped);
    }
    
    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Test string".getBytes());
        byte[] buffer = new byte[10];
        
        int bytesRead = IOUtils.readFully(input, buffer);
        
        assertEquals("Test string".length(), bytesRead);
        assertEquals("Test string", new String(buffer));
    }
    
    @Test
    public void testReadFullyWithOffsetAndLen() throws IOException {
        InputStream input = new ByteArrayInputStream("Test string".getBytes());
        byte[] buffer = new byte[10];
        
        int bytesRead = IOUtils.readFully(input, buffer, 2, 5);
        
        assertEquals(5, bytesRead);
        assertEquals("\0\0Test ", new String(buffer));
    }
    
    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test string".getBytes());
        
        byte[] bytes = IOUtils.toByteArray(input);
        
        assertEquals("Test string".getBytes(), bytes);
    }
    
    @Test
    public void testCloseQuietly() {
        // No need to test this method as it only closes the Closeable object
        // and swallows any IOException that may occur.
        // The behavior is implicit based on Java's close() method.
        // Therefore, no test case needed.
    }

}