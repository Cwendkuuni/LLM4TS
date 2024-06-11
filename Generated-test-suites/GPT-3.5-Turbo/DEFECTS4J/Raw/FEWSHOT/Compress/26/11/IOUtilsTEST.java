package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class IOUtilsTest {
    @Test
    public void testCopyWithDefaultBufferSize() throws Exception {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long result = IOUtils.copy(input, output);

        assertEquals(inputBytes.length, result);
        assertArrayEquals(inputBytes, output.toByteArray());
    }

    @Test
    public void testCopyWithCustomBufferSize() throws Exception {
        byte[] inputBytes = "Custom buffer size test.".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        int bufferSize = 64;
        long result = IOUtils.copy(input, output, bufferSize);

        assertEquals(inputBytes.length, result);
        assertArrayEquals(inputBytes, output.toByteArray());
    }

    @Test
    public void testSkip() throws Exception {
        byte[] inputBytes = "Skipping test.".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        long numToSkip = 5;

        long result = IOUtils.skip(input, numToSkip);

        assertEquals(numToSkip, result);
        assertEquals(inputBytes.length - numToSkip, input.available());
    }

    @Test
    public void testSkipWithInvalidNumberToSkip() throws Exception {
        byte[] inputBytes = "Skipping test.".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        long numToSkip = 20;

        long result = IOUtils.skip(input, numToSkip);

        assertEquals(inputBytes.length, result);
        assertEquals(0, input.available());
    }

    @Test
    public void testReadFully() throws Exception {
        byte[] inputBytes = "Read fully test.".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[inputBytes.length];

        int result = IOUtils.readFully(input, outputBytes);

        assertEquals(inputBytes.length, result);
        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws Exception {
        byte[] inputBytes = "Read fully test with offset and length.".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        int offset = 3;
        int length = 20;
        byte[] outputBytes = new byte[inputBytes.length];

        int result = IOUtils.readFully(input, outputBytes, offset, length);

        assertEquals(length, result);
        assertArrayEquals(inputBytes, 0, inputBytes.length, outputBytes, offset, length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidOffsetAndLength() throws Exception {
        byte[] inputBytes = "Read fully test with invalid offset and length.".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        int offset = 30;
        int length = 20;
        byte[] outputBytes = new byte[inputBytes.length];

        int result = IOUtils.readFully(input, outputBytes, offset, length);
    }

    @Test
    public void testToByteArray() throws Exception {
        byte[] inputBytes = "To byte array test.".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);

        byte[] result = IOUtils.toByteArray(input);

        assertArrayEquals(inputBytes, result);
    }

    @Test
    public void testCloseQuietly() throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream("Closing test.".getBytes());

        IOUtils.closeQuietly(input);
    }
}