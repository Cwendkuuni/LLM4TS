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
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        assertEquals(5, IOUtils.copy(input, output));
        assertArrayEquals(inputBytes, output.toByteArray());
        
        // Testing buffer size
        input = new ByteArrayInputStream(inputBytes);
        output.reset();
        assertEquals(5, IOUtils.copy(input, output, 1024));
        assertArrayEquals(inputBytes, output.toByteArray());
    }
  
    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        
        assertEquals(3, IOUtils.skip(input, 3));
        assertEquals(2, IOUtils.skip(input, 10));
    }
  
    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        
        byte[] buffer = new byte[5];
        assertEquals(5, IOUtils.readFully(input, buffer));
        assertArrayEquals(inputBytes, buffer);
        
        // Testing offset and length
        input = new ByteArrayInputStream(inputBytes);
        buffer = new byte[10];
        assertEquals(5, IOUtils.readFully(input, buffer, 3, 5));
        byte[] expectedBuffer = {0, 0, 0, 1, 2, 3, 4, 5, 0, 0};
        assertArrayEquals(expectedBuffer, buffer);
    }
  
    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        
        assertArrayEquals(inputBytes, IOUtils.toByteArray(input));
    }
  
    @Test
    public void testCloseQuietly() {
        IOUtils.closeQuietly(null);
        // TODO: Create a mock Closeable object and test closing
    }
}