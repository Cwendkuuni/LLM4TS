package org.apache.commons.compress.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class IOUtilsTest {

    @Test
    public void testCopy() {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        try {
            IOUtils.copy(input, output);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
        assertEquals("test", output.toString());
    }

    @Test
    public void testCopyWithBufferSize() {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        try {
            IOUtils.copy(input, output, 10);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
        assertEquals("test", output.toString());
    }

    @Test
    public void testSkip() {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        
        try {
            long numSkipped = IOUtils.skip(input, 2);
            assertEquals(2, numSkipped);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadFully() {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] buffer = new byte[4];
        
        try {
            int numRead = IOUtils.readFully(input, buffer);
            assertEquals(4, numRead);
            assertEquals("test", new String(buffer));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadFullyWithOffsetAndLength() {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] buffer = new byte[4];
        
        try {
            int numRead = IOUtils.readFully(input, buffer, 1, 2);
            assertEquals(2, numRead);
            assertEquals(0, buffer[0]);
            assertEquals('e', buffer[1]);
            assertEquals('s', buffer[2]);
            assertEquals(0, buffer[3]);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testToByteArray() {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        
        try {
            byte[] byteArray = IOUtils.toByteArray(input);
            assertEquals("test", new String(byteArray));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCloseQuietly() {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        
        try {
            IOUtils.closeQuietly(input);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
}
