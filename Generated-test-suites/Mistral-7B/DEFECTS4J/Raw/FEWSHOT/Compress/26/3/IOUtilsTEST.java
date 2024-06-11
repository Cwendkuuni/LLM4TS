package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
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
        final byte[] input = "Hello World".getBytes();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long bytesCopied = IOUtils.copy(new ByteArrayInputStream(input), output);
        byte[] expectedOutput = "Hello World".getBytes();

        assertEquals(expectedOutput.length, bytesCopied);
        assertArrayEquals(expectedOutput, output.toByteArray());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        final byte[] input = "Hello World".getBytes();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long bytesCopied = IOUtils.copy(new ByteArrayInputStream(input), output, 5);
        byte[] expectedOutput = "Hello World".getBytes();

        assertEquals(expectedOutput.length, bytesCopied);
        assertArrayEquals(expectedOutput, output.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        final byte[] input = "Hello World".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long skipped = IOUtils.skip(inputStream, 4);
        assertEquals(4, skipped);
        byte[] expectedOutput = "World".getBytes();

        assertArrayEquals(expectedOutput, output.toByteArray());
    }

    @Test
    public void testSkipLessBytesThanRequested() throws IOException {
        final byte[] input = "Hello World".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);

        long skipped = IOUtils.skip(inputStream, input.length + 1);
        assertEquals(input.length, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        final byte[] input = "Hello World".getBytes();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        IOUtils.readFully(new ByteArrayInputStream(input), output.toByteArray());
        byte[] expectedOutput = "Hello World".getBytes();

        assertArrayEquals(expectedOutput, output.toByteArray());
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        final byte[] input = "Hello World".getBytes();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        IOUtils.readFully(new ByteArrayInputStream(input), output.toByteArray(), 1, 5);
        byte[] expectedOutput = "elo World".getBytes();

        assertArrayEquals(expectedOutput, output.toByteArray());
    }

    @Test
    public void testToByteArray() throws IOException {
        final byte[] input = "Hello World".getBytes();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        copy(new ByteArrayInputStream(input), output);
        byte[] expectedOutput = "Hello World".getBytes();

        assertArrayEquals(expectedOutput, IOUtils.toByteArray(output.toInputStream()));
    }

    @Test
    public void testCloseQuietlyNullInputStream() {
        Closeable nullInputStream = null;

        IOUtils.closeQuietly(nullInputStream);

        // no exception is thrown
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        IOUtils.closeQuietly(output);

        // no exception is thrown
    }

    @Test
    public void testCloseQuietlyIOException() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
                throw new IOException("Close failed");
            }
        };

        IOUtils.closeQuietly(output);

        // no exception is thrown
    }

    @Test
    public void testCloseQuietlyInputOutputMismatch() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[0]);

        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);

        // no exception is thrown
    }
}