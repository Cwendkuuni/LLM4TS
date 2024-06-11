package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class IOUtilsTest {
    
    @Test
    public void testCopy() throws Exception {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(data);
        OutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        assertEquals(data.length, count);
        assertArrayEquals(data, ((ByteArrayOutputStream) output).toByteArray());
    }
    
    @Test
    public void testCopyWithBufferSize() throws Exception {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(data);
        OutputStream output = new ByteArrayOutputStream();
        int bufferSize = 2;
        long count = IOUtils.copy(input, output, bufferSize);
        assertEquals(data.length, count);
        assertArrayEquals(data, ((ByteArrayOutputStream) output).toByteArray());
    }
    
    @Test
    public void testSkip() throws Exception {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(data);
        long numToSkip = 2;
        long skipped = IOUtils.skip(input, numToSkip);
        assertEquals(numToSkip, skipped);
        assertEquals(3, input.read());
    }
    
    @Test
    public void testReadFully() throws Exception {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(data);
        byte[] buffer = new byte[data.length];
        int count = IOUtils.readFully(input, buffer);
        assertEquals(data.length, count);
        assertArrayEquals(data, buffer);
    }
    
    @Test
    public void testReadFullyWithOffsetAndLength() throws Exception {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(data);
        byte[] buffer = new byte[data.length + 2];
        int offset = 1;
        int length = data.length;
        int count = IOUtils.readFully(input, buffer, offset, length);
        assertEquals(length, count);
        assertEquals(0, buffer[0]);
        assertArrayEquals(data, buffer, offset, length);
    }
    
    @Test
    public void testToByteArray() throws Exception {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(data);
        byte[] result = IOUtils.toByteArray(input);
        assertArrayEquals(data, result);
    }
    
    @Test
    public void testCloseQuietly() throws Exception {
        CloseableMock closeable = new CloseableMock();
        IOUtils.closeQuietly(closeable);
        assertTrue(closeable.isClosed());
    }
    
    // Helper class for testCloseQuietly
    class CloseableMock implements Closeable {
        private boolean closed = false;
        
        public void close() {
            closed = true;
        }
        
        public boolean isClosed() {
            return closed;
        }
    }
}