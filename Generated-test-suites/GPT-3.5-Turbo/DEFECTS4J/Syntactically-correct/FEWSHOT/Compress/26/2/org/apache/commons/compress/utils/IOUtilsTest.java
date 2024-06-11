package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {
  
    @Test
    public void testCopy() throws IOException {
        // Test copying from input stream to output stream
        InputStream is = createInputStream();
        OutputStream os = createOutputStream();
        long count = IOUtils.copy(is, os);
        assertEquals(20, count);
        assertArrayEquals("This is a test input".getBytes(), os.toByteArray());
        is.close();
        os.close();
      
        // Test copying with custom buffer size
        is = createInputStream();
        os = createOutputStream();
        count = IOUtils.copy(is, os, 1024);
        assertEquals(20, count);
        assertArrayEquals("This is a test input".getBytes(), os.toByteArray());
        is.close();
        os.close();
    }
  
    @Test
    public void testSkip() throws IOException {
        // Test skipping bytes from an input stream
        InputStream is = createInputStream();
        long skipped = IOUtils.skip(is, 10);
        assertEquals(10, skipped);
        long skippedAgain = IOUtils.skip(is, 10);
        assertEquals(10, skippedAgain);
        is.close();
      
        // Test skipping bytes that exceeds the available bytes in input stream
        is = createInputStream();
        skipped = IOUtils.skip(is, 50);
        assertEquals(20, skipped);
        is.close();
    }
    
    @Test
    public void testReadFully() throws IOException {
        // Test reading bytes from an input stream to fill a buffer
        InputStream is = createInputStream();
        byte[] buffer = new byte[10];
        int bytesRead = IOUtils.readFully(is, buffer);
        assertEquals(10, bytesRead);
        assertArrayEquals("This is a ".getBytes(), buffer);
        is.close();
      
        // Test reading bytes with offset and length
        is = createInputStream();
        buffer = new byte[20];
        bytesRead = IOUtils.readFully(is, buffer, 5, 10);
        assertEquals(10, bytesRead);
        byte[] expectedBytes = new byte[20];
        System.arraycopy("     This is ".getBytes(), 0, expectedBytes, 5, 10);
        assertArrayEquals(expectedBytes, buffer);
        is.close();
      
        // Test reading bytes that exceeds the available bytes in input stream
        is = createInputStream();
        buffer = new byte[30];
        bytesRead = IOUtils.readFully(is, buffer);
        assertEquals(20, bytesRead);
        expectedBytes = new byte[30];
        System.arraycopy("This is a test input".getBytes(), 0, expectedBytes, 0, 20);
        assertArrayEquals(expectedBytes, buffer);
        is.close();
    }
    
    @Test
    public void testToByteArray() throws IOException {
      // Test converting input stream to byte array
      InputStream is = createInputStream();
      byte[] byteArray = IOUtils.toByteArray(is);
      assertArrayEquals("This is a test input".getBytes(), byteArray);
      is.close();
    }
    
    @Test
    public void testCloseQuietly() {
      // Test closing a Closeable object
      Closeable closeable = createCloseable();
      IOUtils.closeQuietly(closeable);
      
      // Test closing a null Closeable object
      Closeable nullCloseable = null;
      IOUtils.closeQuietly(nullCloseable);
    }
    
    // Helper methods for test cases
    private InputStream createInputStream() {
      return new InputStream() {
        private int count = 0;
        
        @Override
        public int read() throws IOException {
          if (count < 20) {
            return "This is a test input".getBytes()[count++];
          }
          return -1;
        }
      };
    }
  
    private OutputStream createOutputStream() {
      return new ByteArrayOutputStream();
    }
    
    private Closeable createCloseable() {
      return new Closeable() {
        private boolean closed = false;
        
        @Override
        public void close() throws IOException {
          closed = true;
        }
        
        public boolean isClosed() {
          return closed;
        }
      };
    }

}