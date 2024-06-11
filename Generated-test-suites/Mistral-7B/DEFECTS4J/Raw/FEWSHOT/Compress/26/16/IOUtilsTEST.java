package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output);
        byte[] expected = "Hello World".getBytes();
        byte[] result = output.toByteArray();
        assertEquals(expected.length, actual);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        OutputStream output = new ByteArrayOutputStream(5);
        long actual = IOUtils.copy(input, output, 5);
        byte[] expected = "Hello World".getBytes();
        byte[] result = output.toByteArray();
        assertEquals(expected.length, actual);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        long actual = IOUtils.skip(input, 5);
        long expected = 5;
        assertEquals(expected, actual);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        input = new ByteArrayInputStream(output.toByteArray());
        actual = IOUtils.skip(input, 10);
        expected = 5; // The first 5 bytes were already read during the creation of the input stream
        assertEquals(expected, actual);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        byte[] buffer = new byte[10];
        int actual = IOUtils.readFully(input, buffer);
        byte[] expected = "Hello World".getBytes();
        assertEquals(expected.length, actual);
        assertArrayEquals(expected, buffer, 0, expected.length);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        byte[] buffer = new byte[10];
        int offset = 5;
        int length = 5;
        int actual = IOUtils.readFully(input, buffer, offset, length);
        byte[] expected = "World".getBytes();
        assertEquals(expected.length, actual);
        assertArrayEquals(expected, new byte[] { buffer[offset], buffer[offset + 1], buffer[offset + 2], buffer[offset + 3], buffer[offset + 4] }, 0, expected.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidOffsetAndLength() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        byte[] buffer = new byte[10];
        int offset = 15;
        int length = 5;
        IOUtils.readFully(input, buffer, offset, length);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        byte[] result = IOUtils.toByteArray(input);
        byte[] expected = "Hello World".getBytes();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(input, output);
        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);
    }

    @Test
    public void testCloseQuietlyWithNull() {
        InputStream input = null;
        IOUtils.closeQuietly(input);
    }

    @Test(expected = IOException.class)
    public void testCloseQuietlyWithThrowingClose() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes()) {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
        IOUtils.closeQuietly(input);
    }

}