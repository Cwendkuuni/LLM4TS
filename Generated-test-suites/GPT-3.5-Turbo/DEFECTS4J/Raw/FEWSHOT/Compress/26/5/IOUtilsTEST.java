package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
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
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long count = IOUtils.copy(input, output);

        assertEquals(5, output.toByteArray().length);
        assertEquals(5, count);
        assertArrayEquals(inputBytes, output.toByteArray());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long count = IOUtils.copy(input, output, 10);

        assertEquals(5, output.toByteArray().length);
        assertEquals(5, count);
        assertArrayEquals(inputBytes, output.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        long skipped = IOUtils.skip(input, 3);

        assertEquals(2, skipped);
    }

    @Test
    public void testSkipWithZeroSkip() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        long skipped = IOUtils.skip(input, 0);

        assertEquals(0, skipped);
    }

    @Test
    public void testSkipWithSkipGreaterThanLength() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        long skipped = IOUtils.skip(input, 6);

        assertEquals(5, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[5];

        int bytesRead = IOUtils.readFully(input, outputBytes);

        assertEquals(5, bytesRead);
        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testReadFullyWithOffset() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[5];

        int bytesRead = IOUtils.readFully(input, outputBytes, 1, 3);

        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[] {0, 1, 2, 0, 0}, outputBytes);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidOffset() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[5];

        IOUtils.readFully(input, outputBytes, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidLength() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[5];

        IOUtils.readFully(input, outputBytes, 0, 6);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        byte[] outputBytes = IOUtils.toByteArray(input);

        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        MyCloseable myCloseable = new MyCloseable();

        IOUtils.closeQuietly(myCloseable);

        assertEquals(1, myCloseable.getCloseCallCount());
    }

    private static class MyCloseable implements Closeable {
        private int closeCallCount = 0;

        @Override
        public void close() throws IOException {
            closeCallCount++;
        }

        public int getCloseCallCount() {
            return closeCallCount;
        }
    }
}