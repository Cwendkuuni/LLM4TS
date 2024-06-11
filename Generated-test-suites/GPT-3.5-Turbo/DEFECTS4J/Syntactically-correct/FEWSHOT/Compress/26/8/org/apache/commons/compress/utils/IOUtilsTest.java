
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
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
        long count = IOUtils.copy(input, output);
        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, output.toByteArray());
        
        input.close();
        output.close();
    }

    @Test
    public void testCopyWithBuffer() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int bufferSize = 2;
        long count = IOUtils.copy(input, output, bufferSize);
        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, output.toByteArray());
        
        input.close();
        output.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        long skipped = IOUtils.skip(input, 2);
        assertEquals(2, skipped);
        
        input.close();
    }

    @Test
    public void testSkipWithZeroSkip() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        long skipped = IOUtils.skip(input, 0);
        assertEquals(0, skipped);
        
        input.close();
    }
    
    @Test
    public void testSkipWithLessThanRequestedSkip() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        long skipped = IOUtils.skip(input, 10);
        assertEquals(inputBytes.length, skipped);
        
        input.close();
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[5];
        int bytesRead = IOUtils.readFully(input, outputBytes);
        assertEquals(5, bytesRead);
        assertArrayEquals(inputBytes, outputBytes);
        
        input.close();
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[3];
        int bytesRead = IOUtils.readFully(input, outputBytes, 1, 2);
        assertEquals(2, bytesRead);
        assertArrayEquals(new byte[] {0, 1, 2}, outputBytes);
        
        input.close();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidOffset() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[3];
        IOUtils.readFully(input, outputBytes, -1, 2);
        
        input.close();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidLength() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[3];
        IOUtils.readFully(input, outputBytes, 0, 4);
        
        input.close();
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = IOUtils.toByteArray(input);
        assertArrayEquals(inputBytes, outputBytes);
        
        input.close();
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
        IOUtils.closeQuietly(closeable);

        closeable = null;
        IOUtils.closeQuietly(closeable);
    }

}
