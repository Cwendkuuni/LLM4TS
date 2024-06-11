package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

public class IOUtilsTest {

    private InputStream input;
    private OutputStream output;

    @Before
    public void setup() {
        input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        output = new ByteArrayOutputStream();
    }

    @Test
    public void testCopy() throws IOException {
        long result = IOUtils.copy(input, output);
        byte[] expected = new byte[] { 1, 2, 3, 4, 5 };
        assertArrayEquals(expected, output.toByteArray());
        assertEquals(expected.length, result);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        long result = IOUtils.copy(input, output, 10);
        byte[] expected = new byte[] { 1, 2, 3, 4, 5 };
        assertArrayEquals(expected, output.toByteArray());
        assertEquals(expected.length, result);
    }

    @Test
    public void testNullInputStream() throws IOException {
        assertNull(IOUtils.copy(null, output));
    }

    @Test
    public void testNullOutputStream() throws IOException {
        assertNull(IOUtils.copy(input, null));
    }

    @Test
    public void testSkip() throws IOException {
        long result = IOUtils.skip(input, 10);
        assertEquals(5, result);
        assertEquals(0, input.read());
    }

    @Test
    public void testSkipWhenEndOfStreamReached() throws IOException {
        InputStream endOfStream = new ByteArrayInputStream(new byte[0]);
        long result = IOUtils.skip(endOfStream, 10);
        assertEquals(0, result);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] expected = new byte[] { 1, 2, 3, 4, 5 };
        byte[] result = IOUtils.toByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        byte[] expected = new byte[] { 2, 3, 4 };
        byte[] result = new byte[5];
        IOUtils.readFully(input, result, 1, 3);
        assertArrayEquals(expected, new byte[] { 2, 3, 4 });
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] expected = new byte[] { 1, 2, 3, 4, 5 };
        byte[] result = IOUtils.toByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testCloseQuietlyNullCloseable() {
        IOUtils.closeQuietly(null);
    }

    @Test
    public void testCloseQuietly() {
        OutputStream closeable = new ByteArrayOutputStream();
        IOUtils.closeQuietly(closeable);
    }

    @Test
    public void testCloseQuietlyThrowsIOException() {
        ByteArrayOutputStream closeable = new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
                throw new IOException("Test IOException");
            }
        };
        try {
            IOUtils.closeQuietly(closeable);
        } catch (IOException e) {
            assertEquals("Test IOException", e.getMessage());
        }
    }
}