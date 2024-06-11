package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class IOUtilsTest {

    // copy method tests
    @Test
    public void testCopyWithDefaultBufferSize() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long bytesCopied = IOUtils.copy(input, output);
        
        assertEquals(4, bytesCopied);
        assertEquals("test", output.toString());
        
        input.close();
        output.close();
    }
    
    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long bytesCopied = IOUtils.copy(input, output, 2);
        
        assertEquals(4, bytesCopied);
        assertEquals("test", output.toString());
        
        input.close();
        output.close();
    }
    
    // skip method tests
    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("test".getBytes());
        
        long bytesSkipped = IOUtils.skip(input, 2);
        
        assertEquals(2, bytesSkipped);
        
        input.close();
    }
    
    // readFully method tests
    @Test
    public void testReadFully() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] buffer = new byte[4];
        
        int bytesRead = IOUtils.readFully(input, buffer);
        
        assertEquals(4, bytesRead);
        assertEquals("test", new String(buffer));
        
        input.close();
    }
    
    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] buffer = new byte[6];
        
        int bytesRead = IOUtils.readFully(input, buffer, 1, 4);
        
        assertEquals(4, bytesRead);
        assertEquals("\u0000test", new String(buffer));
        
        input.close();
    }
    
    // toByteArray method tests
    @Test
    public void testToByteArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("test".getBytes());
        
        byte[] byteArray = IOUtils.toByteArray(input);
        
        assertNotNull(byteArray);
        assertEquals("test", new String(byteArray));
        
        input.close();
    }
    
    // closeQuietly method tests
    @Test
    public void testCloseQuietlyWithNull() {
        IOUtils.closeQuietly(null); // just to cover code path, may not be needed
        assertTrue(true);
    }

}