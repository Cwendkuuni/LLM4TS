package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output);
        assertEquals("test", IOUtils.toByteArray(output));
        assertEquals("test", new String(IOUtils.toByteArray(input)));
        assertEquals(actual, IOUtils.toByteArray(input).length);
    }

    @Test
    public void testCopyBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        OutputStream output = new ByteArrayOutputStream(10);
        long actual = IOUtils.copy(input, output);
        assertNotNull(actual);
        assertEquals(0, IOUtils.toByteArray(output).length);
    }

    @Test(expected = IOException.class)
    public void testCopyIOException() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        OutputStream outputError = new ByteArrayOutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException();
            }
        };
        IOUtils.copy(input, outputError);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        long skipped = IOUtils.skip(input, 3);
        assertEquals(3, skipped);
        assertEquals("test", IOUtils.toByteArray(input));
    }

    @Test
    public void testSkipLessThanRequested() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        long skipped = IOUtils.skip(input, 10);
        assertEquals(5, skipped);
        assertEquals("est", IOUtils.toByteArray(input));
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] buffer = new byte[5];
        int read = IOUtils.readFully(input, buffer);
        assertEquals(5, read);
        assertEquals("test", new String(buffer));
    }

    @Test
    public void testReadFullyLessThanRequested() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] buffer = new byte[10];
        int read = IOUtils.readFully(input, buffer, 5, 5);
        assertEquals(5, read);
        assertEquals("test", new String(buffer, 0, 5));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyInvalidOffset() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] buffer = new byte[5];
        IOUtils.readFully(input, buffer, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyInvalidLength() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] buffer = new byte[5];
        IOUtils.readFully(input, buffer, 0, buffer.length + 1);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] expected = "test".getBytes();
        byte[] actual = IOUtils.toByteArray(input);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("test".getBytes());
        IOUtils.closeQuietly(input);
        assertNull(input.in);
    }
}