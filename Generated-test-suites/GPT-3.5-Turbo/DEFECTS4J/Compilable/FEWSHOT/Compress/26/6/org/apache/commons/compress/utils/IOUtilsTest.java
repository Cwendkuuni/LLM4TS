package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;

import org.junit.Test;

public class IOUtilsTest {
    @Test
    public void testCopyDefaultBufferSize() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        long count = IOUtils.copy(inputStream, outputStream);
        
        assertEquals(input.length, count);
        assertArrayEquals(input, outputStream.toByteArray());
        
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testCopyCustomBufferSize() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        long count = IOUtils.copy(inputStream, outputStream, 10);
        
        assertEquals(input.length, count);
        assertArrayEquals(input, outputStream.toByteArray());
        
        inputStream.close();
        outputStream.close();
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        
        long skipped = IOUtils.skip(inputStream, 3);
        
        assertEquals(3, skipped);
        assertEquals(4, inputStream.read());
        
        inputStream.close();
    }
    
    @Test
    public void testReadFully() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        byte[] output = new byte[5];
        
        int count = IOUtils.readFully(inputStream, output);
        
        assertEquals(5, count);
        assertArrayEquals(input, output);
        
        inputStream.close();
    }
    
    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        byte[] output = new byte[7];
        
        int count = IOUtils.readFully(inputStream, output, 1, 5);
        
        assertEquals(5, count);
        assertEquals(0, output[0]);
        assertArrayEquals(input, output);
        
        inputStream.close();
    }
    
    @Test
    public void testToByteArray() throws IOException {
        byte[] input = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        
        byte[] output = IOUtils.toByteArray(inputStream);
        
        assertArrayEquals(input, output);
        
        inputStream.close();
    }
    
    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        IOUtils.closeQuietly(inputStream);
        
        assertTrue(inputStream.markSupported());
    }
}