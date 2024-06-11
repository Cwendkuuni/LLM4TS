package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IOUtilsTest {
    
    private InputStream input;
    private OutputStream output;

    @Before
    public void setUp() {
        input = mock(InputStream.class);
        output = mock(OutputStream.class);
    }
    
    @After
    public void tearDown() {
        input = null;
        output = null;
    }

    @Test
    public void testCopy() throws IOException {
        // Set up
        byte[] inputBytes = { 1, 2, 3 };
        byte[] outputBytes = new byte[3];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        // Execute
        IOUtils.copy(inputStream, outputBytes);
        
        // Verify
        assertArrayEquals(inputBytes, outputBytes);
        verify(input).read(outputBytes, 0, 3);
        verify(output).write(outputBytes, 0, 3);
    }
    
    @Test
    public void testCopyWithBufferSize() throws IOException {
        // Set up
        int bufferSize = 1024;
        byte[] inputBytes = new byte[bufferSize];
        byte[] outputBytes = new byte[bufferSize];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        // Execute
        IOUtils.copy(inputStream, outputBytes, bufferSize);
        
        // Verify
        assertArrayEquals(inputBytes, outputBytes);
        verify(input).read(outputBytes, 0, bufferSize);
        verify(output).write(outputBytes, 0, bufferSize);
    }
    
    @Test
    public void testSkip() throws IOException {
        // Set up
        long numToSkip = 10L;
        
        // Execute
        long skipped = IOUtils.skip(input, numToSkip);
        
        // Verify
        assertEquals(numToSkip, skipped);
        verify(input, times(2)).skip(10L);
    }
    
    @Test
    public void testReadFully() throws IOException {
        // Set up
        int offset = 0;
        int len = 5;
        byte[] inputBytes = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[len];
        
        // Execute
        int bytesRead = IOUtils.readFully(inputStream, outputBytes, offset, len);
        
        // Verify
        assertEquals(len, bytesRead);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, outputBytes);
        verify(input).read(outputBytes, 0, len);
    }
    
    @Test
    public void testToByteArray() throws IOException {
        // Set up
        byte[] inputBytes = { 1, 2, 3 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        
        // Execute
        byte[] outputBytes = IOUtils.toByteArray(inputStream);
        
        // Verify
        assertArrayEquals(inputBytes, outputBytes);
        verify(input).read(any(byte[].class), eq(0), eq(8024));
        verify(output).write(inputBytes, 0, inputBytes.length);
    }
    
    @Test
    public void testCloseQuietly() throws IOException {
        // Set up
        Closeable closable = mock(Closeable.class);
        
        // Execute
        IOUtils.closeQuietly(closable);
        
        // Verify
        verify(closable).close();
    }

}