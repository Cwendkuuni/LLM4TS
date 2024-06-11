package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output);
        byte[] expected = new byte[] { 1, 2, 3 };

        assertArrayEquals(expected, output.toByteArray());
        assertEquals(expected.length, actual);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output, 1);
        byte[] expected = new byte[] { 1, 2, 3 };

        assertArrayEquals(expected, output.toByteArray());
        assertEquals(expected.length, actual);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });

        long actual = IOUtils.skip(input, 5L);
        long expected = 1L;

        assertEquals(expected, actual);

        byte[] bytes = new byte[5];
        InputStream input2 = new ByteArrayInputStream(bytes);

        actual = IOUtils.skip(input2, 10L);
        expected = 5L;

        assertEquals(expected, actual);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        byte[] output = new byte[5];

        int actual = IOUtils.readFully(input, output);
        int expected = 5;

        assertArrayEquals(output, new byte[] { 1, 2, 3, 4, 5 });
        assertEquals(expected, actual);

        byte[] output2 = new byte[3];
        InputStream input2 = new ByteArrayInputStream(new byte[] { 1, 2, 3 });

        actual = IOUtils.readFully(input2, output2);
        expected = 3;

        assertArrayEquals(output2, new byte[] { 1, 2, 3 });
        assertEquals(expected, actual);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        byte[] expected = new byte[] { 1, 2, 3 };

        byte[] actual = IOUtils.toByteArray(input);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            IOUtils.closeQuietly((Closeable) outputStream);
            assertTrue(outputStream.size() == 0);
        } finally {
            outputStream.close();
        }

        String inputString = "Hello World";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8));
        try {
            IOUtils.closeQuietly((Closeable) inputStream);
        } finally {
            // do nothing
        }
    }
}