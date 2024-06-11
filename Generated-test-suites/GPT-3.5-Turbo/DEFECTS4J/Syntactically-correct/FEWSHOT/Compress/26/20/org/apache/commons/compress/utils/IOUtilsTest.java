package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {
    @Test
    public void testCopy() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        assertEquals(3, count);
        assertArrayEquals(new byte[]{1, 2, 3}, output.toByteArray());
    }
    
    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output, 2);
        assertEquals(3, count);
        assertArrayEquals(new byte[]{1, 2, 3}, output.toByteArray());
    }
    
    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
        long skipped = IOUtils.skip(input, 2);
        assertEquals(2, skipped);
        assertEquals(3, input.read());
    }
    
    @Test
    public void testSkipWithZeroSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
        long skipped = IOUtils.skip(input, 0);
        assertEquals(0, skipped);
        assertEquals(1, input.read());
    }
    
    @Test
    public void testReadFully() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
        byte[] buffer = new byte[3];
        int bytesRead = IOUtils.readFully(input, buffer);
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{1, 2, 3}, buffer);
    }
    
    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
        byte[] buffer = new byte[4];
        int bytesRead = IOUtils.readFully(input, buffer, 1, 3);
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0, 1, 2, 3}, buffer);
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidOffsetAndLength() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
        byte[] buffer = new byte[3];
        IOUtils.readFully(input, buffer, 1, 4);
    }
    
    @Test
    public void testToByteArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
        byte[] bytes = IOUtils.toByteArray(input);
        assertArrayEquals(new byte[]{1, 2, 3}, bytes);
    }
    
    @Test
    public void testCloseQuietly() {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
        IOUtils.closeQuietly(input);
    }
    
    @Test
    public void testCloseQuietlyWithNull() {
        InputStream input = null;
        IOUtils.closeQuietly(input);
    }
}