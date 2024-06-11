package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

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
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long count = IOUtils.copy(input, output);
        
        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, output.toByteArray());
    }
    
    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long count = IOUtils.copy(input, output, 10);
        
        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, output.toByteArray());
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        
        long skipped = IOUtils.skip(input, 5);
        
        assertEquals(5, skipped);
        assertEquals(6, input.available());
    }
    
    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[inputBytes.length];
        
        int bytesRead = IOUtils.readFully(input, outputBytes);
        
        assertEquals(inputBytes.length, bytesRead);
        assertArrayEquals(inputBytes, outputBytes);
    }
    
    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[5];
        
        int bytesRead = IOUtils.readFully(input, outputBytes, 1, 3);
        
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0, inputBytes[0], inputBytes[1], inputBytes[2], 0}, outputBytes);
    }
    
    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        
        byte[] outputBytes = IOUtils.toByteArray(input);
        
        assertArrayEquals(inputBytes, outputBytes);
    }
    
    @Test
    public void testCloseQuietly() throws IOException {
        CloseableImpl closeable = new CloseableImpl();
        
        IOUtils.closeQuietly(closeable);
        
        assertTrue(closeable.isClosed());
    }
    
    private static class CloseableImpl implements Closeable {
        private boolean closed = false;
        
        @Override
        public void close() throws IOException {
            closed = true;
        }
        
        public boolean isClosed() {
            return closed;
        }
    }
}