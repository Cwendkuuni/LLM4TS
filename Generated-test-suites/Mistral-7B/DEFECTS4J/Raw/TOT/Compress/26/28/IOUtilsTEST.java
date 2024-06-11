package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test class for IOUtils
 */
public class IOUtilsTest {

    private InputStream input;
    private OutputStream output;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;

    @Before
    public void setUp() throws IOException {
        output = new OutputStream() {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            @Override
            public void write(int b) throws IOException {
                out.write(b);
            }

            @Override
            public void close() throws IOException {
                inputStream = new ByteArrayInputStream(out.toByteArray());
            }
        };
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);
    }

    @Test
    public void testCopy() throws IOException {
        // Given
        input = new ByteArrayInputStream("Test String".getBytes());
        long expected = input.available();

        // When
        long actual = IOUtils.copy(input, output);

        // Then
        Assert.assertEquals(expected, actual);
        String result = IOUtils.toByteArray(inputStream)[0];
        Assert.assertEquals("Test String", new String(result));
    }

    @Test(expected = NullPointerException.class)
    public void testCopyNullInput() {
        // Given
        InputStream input = null;

        // When
        IOUtils.copy(input, output);
    }

    @Test(expected = NullPointerException.class)
    public void testCopyNullOutput() {
        // Given
        OutputStream output = null;

        // When
        IOUtils.copy(input, output);
    }

    @Test
    public void testCopyEndOfStream() throws IOException {
        // Given
        input = new ByteArrayInputStream("Test String".getBytes());

        // When
        long actual = IOUtils.copy(input, output);
        input.close();

        // Then
        Assert.assertEquals(-1, actual);
        Assert.assertEquals(0, inputStream.available());
    }

    @Test
    public void testCopyBufferSize() throws IOException {
        // Given
        input = new ByteArrayInputStream("Test String".getBytes());

        // When
        long actual = IOUtils.copy(input, output, 10);

        // Then
        String result = IOUtils.toByteArray(inputStream)[0];
        Assert.assertEquals("Test String", new String(result));
        Assert.assertEquals(IOUtils.COPY_BUF_SIZE - 1, actual);
    }

    @Test
    public void testSkip() throws IOException {
        // Given
        input = new ByteArrayInputStream("Test String".getBytes());

        // When
        long actual = IOUtils.skip(input, 5);

        // Then
        Assert.assertEquals(5, actual);
        long expected = input.available() - 5;
        Assert.assertEquals(expected, input.available());
    }

    @Test(expected = NullPointerException.class)
    public void testSkipNullInput() {
        // Given
        InputStream input = null;

        // When
        IOUtils.skip(input, 0);
    }

    @Test
    public void testReadFully() throws IOException {
        // Given
        input = new ByteArrayInputStream("Test String".getBytes());
        byte[] buffer = new byte[5];

        // When
        IOUtils.readFully(input, buffer);

        // Then
        String result = new String(buffer);
        Assert.assertEquals("Test Str", result);
        Assert.assertEquals(5, input.available());
    }

    @Test(expected = NullPointerException.class)
    public void testReadFullyNullInput() {
        // Given
        InputStream input = null;

        // When
        byte[] buffer = new byte[10];
        IOUtils.readFully(input, buffer);
    }

    @Test(expected = IOException.class)
    public void testReadFullyBufferSizeGreaterThanAvailable() throws IOException {
        // Given
        input = new ByteArrayInputStream("Test String".getBytes());
        byte[] buffer = new byte[10];

        // When & Then
        Assert.assertEquals(5, input.available());
        IOUtils.readFully(input, buffer);
        Assert.assertEquals("Test String", new String(buffer, 0, 5));
        Assert.assertEquals(0, input.available());
    }
}