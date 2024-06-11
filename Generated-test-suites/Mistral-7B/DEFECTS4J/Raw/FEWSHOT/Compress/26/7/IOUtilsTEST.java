package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        long copiedBytes = IOUtils.copy(input, output);
        byte[] expected = "Test".getBytes();
        byte[] actual = output.toByteArray();
        assertEquals(expected.length, copiedBytes);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        long copiedBytes = IOUtils.copy(input, output, 5);
        byte[] expected = "Test".getBytes();
        byte[] actual = output.toByteArray();
        assertArrayEquals(expected, actual);
        // Ensure the buffer was used
        assertTrue(copiedBytes > expected.length);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test".getBytes());
        long skipped = IOUtils.skip(input, 4);
        long expected = 4;
        assertEquals(expected, skipped);
        byte[] actual = input.readAllBytes();
        byte[] expectedBytes = "st".getBytes();
        assertArrayEquals(expectedBytes, Arrays.copyOfRange(actual, 4, actual.length));
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        byte[] buffer = new byte[5];
        int read = IOUtils.readFully(input, buffer, 0, buffer.length);
        assertEquals(5, read);
        byte[] expected = "Test".getBytes();
        assertArrayEquals(expected, buffer);

        // Test reading less than the buffer size
        byte[] buffer2 = new byte[3];
        read = IOUtils.readFully(input, buffer2, 0, buffer2.length);
        assertEquals(3, read);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test".getBytes());
        IOUtils.closeQuietly((Closeable) input);
        assertNull(input.available());
        try {
            input.read();
            fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testToByteArray() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        byte[] expected = "Test".getBytes();
        byte[] actual = IOUtils.toByteArray(input);
        assertArrayEquals(expected, actual);
    }
}