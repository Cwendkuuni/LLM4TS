package org.apache.commons.compress.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class IOUtilsTest {

    @Test
    public void testCopyInputStreamToOutputStream() throws IOException {
        byte[] testData = "Hello, world!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long bytesCopied = IOUtils.copy(input, output);

        assertEquals(testData.length, bytesCopied);
        assertArrayEquals(testData, output.toByteArray());
    }

    @Test
    public void testCopyInputStreamToOutputStreamWithBufferSize() throws IOException {
        byte[] testData = "Hello, world!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        int bufferSize = 10;
        long bytesCopied = IOUtils.copy(input, output, bufferSize);

        assertEquals(testData.length, bytesCopied);
        assertArrayEquals(testData, output.toByteArray());
    }

    @Test
    public void testSkipInputStream() throws IOException {
        byte[] testData = "Hello, world!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(testData);

        long bytesSkipped = IOUtils.skip(input, 7);

        assertEquals(7, bytesSkipped);
    }

    @Test
    public void testReadFullyInputStream() throws IOException {
        byte[] testData = "Hello, world!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        byte[] output = new byte[5];

        int bytesRead = IOUtils.readFully(input, output);

        assertEquals(5, bytesRead);
        assertArrayEquals("Hello".getBytes(), output);
    }

    @Test
    public void testReadFullyInputStreamWithOffset() throws IOException {
        byte[] testData = "Hello, world!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        byte[] output = new byte[5];

        int bytesRead = IOUtils.readFully(input, output, 0, 5);

        assertEquals(5, bytesRead);
        assertArrayEquals("Hello".getBytes(), output);
    }

    @Test
    public void testToByteArrayInputStream() throws IOException {
        byte[] testData = "Hello, world!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(testData);

        byte[] output = IOUtils.toByteArray(input);

        assertArrayEquals(testData, output);
    }

    @Test
    public void testCloseQuietly() {
        // Test for coverage, no need for assertion
        IOUtils.closeQuietly(null);
    }

}