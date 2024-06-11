package org.apache.commons.compress.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for IOUtils class.
 */
public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        final byte[] inputData = ArrayUtils.toPrimitive(new byte[]{1, 2, 3, 4, 5, 6});
        final byte[] outputData = new byte[inputData.length];
        
        // Positive scenario
        long copiedBytes = IOUtils.copy(new ByteArrayInputStream(inputData), new ByteArrayOutputStream(inputData.length));
        Assert.assertEquals(copiedBytes, inputData.length);
        
        byte[] resultData = ((ByteArrayOutputStream) IOUtils.toByteArray(new ByteArrayInputStream(outputData))).toByteArray();
        Assert.assertArrayEquals(inputData, resultData);

        // Edge case: empty input
        byte[] emptyInput = {};
        byte[] emptyOutput = new byte[0];
        copiedBytes = IOUtils.copy(new ByteArrayInputStream(emptyInput), new ByteArrayOutputStream(emptyOutput.length));
        Assert.assertEquals(copiedBytes, 0);

        // Edge case: output buffer size is smaller than input data
        byte[] smallerOutput = new byte[inputData.length - 1];
        copiedBytes = IOUtils.copy(new ByteArrayInputStream(inputData), new ByteArrayOutputStream(smallerOutput.length));
        Assert.assertEquals(copiedBytes, inputData.length - 1);

        // Error case: null input
        assertThrows(NullPointerException.class, () -> IOUtils.copy(null, new ByteArrayOutputStream(10)));

        // Error case: null output
        assertThrows(NullPointerException.class, () -> IOUtils.copy(new ByteArrayInputStream(inputData), null));
    }

    @Test
    public void testSkip() throws IOException {
        final byte[] inputData = ArrayUtils.toPrimitive(new byte[]{1, 2, 3, 4, 5, 6});
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        // Positive scenario
        IOUtils.copy(new ByteArrayInputStream(inputData), output);
        long skipped = IOUtils.skip(new ByteArrayInputStream(output.toByteArray()), 5L);
        assertTrue(skipped > 0);
        assertEquals(skipped, 5);

        // Edge case: skipping to the end of the stream
        skipped = IOUtils.skip(new ByteArrayInputStream(output.toByteArray()), output.size() + 1L);
        assertEquals(skipped, output.size());

        // Edge case: negative number to skip
        assertThrows(IllegalArgumentException.class, () -> IOUtils.skip(new ByteArrayInputStream(output.toByteArray()), -1L));

        // Error case: null input
        assertThrows(NullPointerException.class, () -> IOUtils.skip(null, 0L));
    }

    @Test
    public void testReadFully() throws IOException {
        final byte[] expectedData = ArrayUtils.toPrimitive(new byte[]{1, 2, 3, 4, 5, 6});
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(new ByteArrayInputStream(expectedData), output);

        // Positive scenario
        byte[] resultData = new byte[expectedData.length];
        int readBytes = IOUtils.readFully(new ByteArrayInputStream(output.toByteArray()), resultData);
        Assert.assertEquals(readBytes, expectedData.length);
        Assert.assertArrayEquals(expectedData, resultData);

        // Edge case: reading less bytes than the buffer size
        resultData = new byte[expectedData.length - 1];
        readBytes = IOUtils.readFully(new ByteArrayInputStream(output.toByteArray()), resultData, 0, resultData.length);
        Assert.assertEquals(readBytes, resultData.length);
        Assert.assertArrayEquals(ArrayUtils.subarray(expectedData, 0, resultData.length), resultData);

        // Edge case: reading all bytes from the buffer
        resultData = new byte[expectedData.length];
        readBytes = IOUtils.readFully(new ByteArrayInputStream(output.toByteArray()), resultData, 0, resultData.length);
        Assert.assertEquals(readBytes, expectedData.length);
        Assert.assertArrayEquals(expectedData, resultData);

        // Edge case: reading from an empty buffer
        resultData = new byte[0];
        readBytes = IOUtils.readFully(new ByteArrayInputStream(output.toByteArray()), resultData);
        Assert.assertEquals(readBytes, 0);
        Assert.assertArrayEquals(ArrayUtils.EMPTY_BYTE_ARRAY, resultData);

        // Error case: null input
        assertThrows(NullPointerException.class, () -> IOUtils.readFully(null, new byte[1]));

        // Error case: null result data
        assertThrows(NullPointerException.class, () -> IOUtils.readFully(new ByteArrayInputStream(output.toByteArray()), null));
    }

    @Test
    public void testToByteArray() throws IOException {
        final byte[] inputData = ArrayUtils.toPrimitive(new byte[]{1, 2, 3, 4, 5, 6});
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(new ByteArrayInputStream(inputData), output);

        // Positive scenario
        byte[] resultData = IOUtils.toByteArray(new ByteArrayInputStream(output.toByteArray()));
        Assert.assertNotNull(resultData);
        Assert.assertArrayEquals(inputData, resultData);

        // Edge case: empty input
        byte[] emptyInput = {};
        resultData = IOUtils.toByteArray(new ByteArrayInputStream(emptyInput));
        Assert.assertNotNull(resultData);
        Assert.assertArrayEquals(ArrayUtils.EMPTY_BYTE_ARRAY, resultData);

        // Error case: null input
        assertThrows(NullPointerException.class, () -> IOUtils.toByteArray(null));
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(RandomUtils.nextBytes(1024));
        try {
            IOUtils.copy(input, output);
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        } catch (IOException e) {
            // swallow exception
        }
        assertNotNull(output.toByteArray());
    }
}