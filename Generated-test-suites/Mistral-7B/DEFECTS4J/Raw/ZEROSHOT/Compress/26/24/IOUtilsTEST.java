package org.apache.commons.compress.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Test class for IOUtils
 */
public class IOUtilsTest {

    private InputStream input;
    private OutputStream output;
    private byte[] inputData = new byte[]{(byte) 0x01, (byte) 0x02};

    @Before
    public void setUp() {
        input = new ByteArrayInputStream(inputData);
        output = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() {
        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);
    }

    @Test
    public void testCopy() throws IOException {
        long copied = IOUtils.copy(input, output);
        assertEquals(inputData.length, copied);
        byte[] outputData = output.toByteArray();
        assertSame(inputData, outputData);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        long copied = IOUtils.copy(input, output, 1024);
        assertEquals(inputData.length, copied);
    }

    @Test
    public void testSkip() throws IOException {
        long skipped = IOUtils.skip(input, 1);
        assertEquals(1, skipped);
        byte[] remainingData = new byte[inputData.length - 1];
        System.arraycopy(inputData, 0, remainingData, 0, remainingData.length);
        InputStream skippedInput = new ByteArrayInputStream(remainingData);
        long skippedAgain = IOUtils.skip(skippedInput, 1);
        assertEquals(0, skippedAgain);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] buffer = new byte[inputData.length];
        int bytesRead = IOUtils.readFully(input, buffer);
        assertEquals(inputData.length, bytesRead);
        assertSame(inputData, buffer);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[inputData.length];
        int offset = 0;
        int length = inputData.length;
        int bytesRead = IOUtils.readFully(input, buffer, offset, length);
        assertEquals(inputData.length, bytesRead);
        assertSame(inputData, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] expected = new byte[]{0x01, 0x02};
        byte[] actual = IOUtils.toByteArray(input);
        assertSame(expected, actual);
    }
}