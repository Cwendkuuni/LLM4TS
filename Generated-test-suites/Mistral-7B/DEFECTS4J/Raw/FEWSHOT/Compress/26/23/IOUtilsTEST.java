package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{-1, 0, 1, 2, 3});
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long count = IOUtils.copy(input, output);
        assertEquals(5L, count);

        byte[] expected = new byte[]{0, 1, 2, 3, -1};
        assertArrayEquals(expected, output.toByteArray());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{-1, 0, 1, 2, 3});
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long count = IOUtils.copy(input, output, 1);
        assertEquals(5L, count);

        byte[] expected = new byte[]{0, 1, 2, 3, -1};
        assertArrayEquals(expected, output.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{-1, 0, 1, 2, 3});

        long skipped = IOUtils.skip(input, 2L);
        assertEquals(2L, skipped);

        byte[] expected = new byte[]{1, 2, 3, -1};
        assertArrayEquals(expected, input.readAllBytes());
    }

    @Test
    public void testSkipWithEndOfStream() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{-1});

        long skipped = IOUtils.skip(input, 2L);
        assertEquals(0L, skipped);

        assertNull(input.read());
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[5];

        IOUtils.write(output, "Hello World".getBytes(), 0, "Hello World".length());
        IOUtils.readFully(output.getInputStream(), buffer);

        assertArrayEquals(new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'}, buffer);
    }

    @Test
    public void testReadFullyWithEndOfStream() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[10];

        IOUtils.write(output, "Hello World".getBytes(), 0, "Hello World".length());
        IOUtils.readFully(output.getInputStream(), buffer);

        assertArrayEquals(new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'}, buffer);

        // Read past the end of the stream
        byte[] remainingBytes = new byte[5];
        int length = IOUtils.readFully(output.getInputStream(), remainingBytes);
        assertFalse(length > 0);
    }

    @Test
    public void testToByteArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{-1, 0, 1, 2, 3});
        byte[] expected = new byte[]{-1, 0, 1, 2, 3};

        byte[] actual = IOUtils.toByteArray(input);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);

        IOUtils.closeQuietly(input);
        assertSame(input, InputStream.nullInputStream());
    }

    @Test(expected = NullPointerException.class)
    public void testCloseQuietlyNull() {
        IOUtils.closeQuietly(null);
    }
}