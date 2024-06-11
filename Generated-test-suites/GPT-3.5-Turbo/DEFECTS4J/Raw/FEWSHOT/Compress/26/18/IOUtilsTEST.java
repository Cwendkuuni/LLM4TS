package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class IOUtilsTest {
    @Test
    public void testCopyWithDefaultBufferSize() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5};
        byte[] outputData = new byte[5];

        ByteArrayInputStream input = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        IOUtils.copy(input, output);

        assertArrayEquals(inputData, output.toByteArray());
    }

    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5};
        byte[] outputData = new byte[5];

        ByteArrayInputStream input = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        IOUtils.copy(input, output, 10);

        assertArrayEquals(inputData, output.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputData);

        long bytesSkipped = IOUtils.skip(input, 2);

        assertEquals(2, bytesSkipped);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5};
        byte[] outputData = new byte[5];

        ByteArrayInputStream input = new ByteArrayInputStream(inputData);

        int bytesRead = IOUtils.readFully(input, outputData);

        assertEquals(5, bytesRead);
        assertArrayEquals(inputData, outputData);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5};

        ByteArrayInputStream input = new ByteArrayInputStream(inputData);

        byte[] outputData = IOUtils.toByteArray(input);

        assertArrayEquals(inputData, outputData);
    }

    @Test
    public void testCloseQuietlyWithNull() {
        IOUtils.closeQuietly(null);

        // If no exception is thrown, the test is considered successful
        assertTrue(true);
    }
}