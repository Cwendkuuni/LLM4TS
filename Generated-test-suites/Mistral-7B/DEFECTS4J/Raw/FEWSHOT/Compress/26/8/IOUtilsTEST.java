package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });

        long copiedBytes = IOUtils.copy(input, output);
        assertEquals(input.available(), copiedBytes);
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, output.toByteArray());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });

        long copiedBytes = IOUtils.copy(input, output, 2);
        assertEquals(input.available(), copiedBytes);
        assertArrayEquals(new byte[] { 1, 1, 2, 2, 3, 3, 4, 4, 5, 5 }, output.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });

        long skippedBytes = IOUtils.skip(input, 3);
        assertEquals(3, skippedBytes);
        assertEquals(2, input.available());
        assertArrayEquals(new byte[] { 3, 4, 5 }, input.readAllBytes());
    }

    @Test
    public void testSkipFallback() throws IOException {
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });

        long skippedBytes = IOUtils.skip(input, 8);
        assertEquals(5, skippedBytes);
        assertEquals(0, input.available());
    }

    @Test
    public void testReadFully() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });

        byte[] buffer = new byte[5];
        IOUtils.readFully(input, buffer);
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, buffer);

        byte[] buffer2 = new byte[10];
        IOUtils.readFully(input, buffer2, 0, 5);
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, Arrays.copyOfRange(buffer2, 0, 5));
    }

    @Test
    public void testToByteArray() throws IOException {
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        byte[] output = IOUtils.toByteArray(input);
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, output);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        IOUtils.closeQuietly(input);
        assertTrue(input.isClosed());
    }

    @Test
    public void testCloseQuietlyNull() {
        IOUtils.closeQuietly(null);
        // no exception should be thrown
    }
}