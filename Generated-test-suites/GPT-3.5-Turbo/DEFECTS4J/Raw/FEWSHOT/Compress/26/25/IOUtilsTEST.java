
package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        try {
            long count = IOUtils.copy(input, output);
            assertEquals(4, count);
            assertEquals("Test", output.toString());
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
    }
    
    @Test
    public void testCopyWithBufferSize() {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        
        try {
            long count = IOUtils.copy(input, output, 2);
            assertEquals(4, count);
            assertEquals("Test", output.toString());
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
    }
    
    @Test
    public void testSkip() {
        byte[] bytes = new byte[10];
        InputStream input = new ByteArrayInputStream(bytes);
        
        try {
            long skipped = IOUtils.skip(input, 5);
            assertEquals(5, skipped);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
    
    @Test
    public void testSkipWithInsufficientBytes() {
        byte[] bytes = new byte[2];
        InputStream input = new ByteArrayInputStream(bytes);
        
        try {
            long skipped = IOUtils.skip(input, 5);
            assertEquals(2, skipped);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
    
    @Test
    public void testReadFully() {
        byte[] bytes = new byte[10];
        InputStream input = new ByteArrayInputStream(bytes);
        byte[] buffer = new byte[10];
        
        try {
            int count = IOUtils.readFully(input, buffer);
            assertEquals(10, count);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
    
    @Test
    public void testReadFullyWithInsufficientBytes() {
        byte[] bytes = new byte[5];
        InputStream input = new ByteArrayInputStream(bytes);
        byte[] buffer = new byte[10];
        
        try {
            int count = IOUtils.readFully(input, buffer);
            assertEquals(5, count);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
    
    @Test
    public void testToByteArray() {
        byte[] bytes = new byte[10];
        InputStream input = new ByteArrayInputStream(bytes);
        
        try {
            byte[] result = IOUtils.toByteArray(input);
            assertEquals(bytes, result);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
    
    @Test
    public void testCloseQuietly() {
        Closeable c = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException("Test");
            }
        };
        
        try {
            IOUtils.closeQuietly(c);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
}
