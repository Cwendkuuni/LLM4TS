package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        input = mock(InputStream.class);
        output = mock(OutputStream.class);
    }

    @Test
    public void testCopy() throws IOException {
        byte[] inputData = new byte[] { 0x01, 0x02, 0x03 };
        byte[] expectedOutputData = new byte[] { 0x01, 0x02, 0x03 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        IOUtils.copy(new ByteArrayInputStream(inputData), outputStream);
        byte[] actualOutputData = outputStream.toByteArray();

        assertEquals("Copied data is not equal to expected data", expectedOutputData, actualOutputData);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] inputData = new byte[] { 0x01, 0x02, 0x03 };
        byte[] expectedOutputData = new byte[] { 0x01, 0x02, 0x03 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        IOUtils.copy(new ByteArrayInputStream(inputData), outputStream, 512);
        byte[] actualOutputData = outputStream.toByteArray();

        assertEquals("Copied data is not equal to expected data", expectedOutputData, actualOutputData);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };

        long numToSkip = 3;
        long skipped = IOUtils.skip(input, numToSkip);

        byte[] expectedRemainingData = new byte[] { 0x04, 0x05 };
        byte[] actualRemainingData = new byte[4];
        System.arraycopy(IOUtils.toByteArray(input), 0, actualRemainingData, 0, actualRemainingData.length);

        assertArrayEquals("Skipped bytes are not equal to expected", expectedRemainingData, actualRemainingData);
        assertEquals("Number of bytes skipped is not equal to expected", numToSkip, skipped);

        verify(input).skip(numToSkip);
    }

    @Test
    public void testSkipWithLessBytesToSkip() throws IOException {
        byte[] inputData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };

        long numToSkip = 5;
        long skipped = IOUtils.skip(input, numToSkip);

        byte[] expectedEndOfStreamData = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        byte[] actualEndOfStreamData = new byte[4];
        System.arraycopy(IOUtils.toByteArray(input), 0, actualEndOfStreamData, 0, actualEndOfStreamData.length);

        assertArrayEquals("Data at end of stream is not equal to expected", expectedEndOfStreamData, actualEndOfStreamData);
        assertEquals("Number of bytes skipped is not equal to requested", 4, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
        byte[] expectedOutputData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        IOUtils.copy(input, outputStream);
        byte[] actualOutputData = outputStream.toByteArray();

        byte[] buffer = new byte[5];
        int length = IOUtils.readFully(input, buffer);

        assertArrayEquals("Read data is not equal to expected", expectedOutputData, buffer);
        assertEquals("Number of bytes read is not equal to size of buffer", 5, length);
    }

    @Test
    public void testReadFullyWithLessBytesToRead() throws IOException {
        byte[] inputData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
        byte[] expectedOutputData = new byte[] { 0x01, 0x02, 0x03 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        IOUtils.copy(input, outputStream);
        byte[] actualOutputData = outputStream.toByteArray();

        byte[] buffer = new byte[3];
        int length = IOUtils.readFully(input, buffer);

        assertArrayEquals("Read data is not equal to expected", expectedOutputData, buffer);
        assertEquals("Number of bytes read is not equal to size of buffer", 3, length);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
        byte[] expectedOutputData = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };

        byte[] actualOutputData = IOUtils.toByteArray(new ByteArrayInputStream(inputData));

        assertArrayEquals("Converted data is not equal to expected", expectedOutputData, actualOutputData);
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = mock(Closeable.class);

        IOUtils.closeQuietly(closeable);

        verify(closeable).close();
    }

    @Test
    public void testCloseQuietlyWithNull() {
        IOUtils.closeQuietly(null);
    }
}