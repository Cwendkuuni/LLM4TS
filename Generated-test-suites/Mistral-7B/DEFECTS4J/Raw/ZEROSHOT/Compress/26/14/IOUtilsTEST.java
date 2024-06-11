package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

/**
 * JUnit 4 test file for IOUtils class
 */
public class IOUtilsTest {

    /**
     * Test copy method with default buffer size
     * @throws IOException
     */
    @Test
    public void testCopyDefaultBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        long copiedBytes = IOUtils.copy(input, output);
        assertEquals(13, copiedBytes);
        String result = new String(output.toByteArray());
        assertEquals("Hello World", result);
    }

    /**
     * Test copy method with custom buffer size
     * @throws IOException
     */
    @Test
    public void testCopyCustomBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        long copiedBytes = IOUtils.copy(input, output, 5);
        assertEquals(1, copiedBytes);

        for (int i = 0; i < 13; i++) {
            copiedBytes = IOUtils.copy(input, output, 5);
            assertTrue(copiedBytes > 0);
        }

        String result = new String(output.toByteArray());
        assertEquals("Hello World", result);
    }

    /**
     * Test skip method
     * @throws IOException
     */
    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World\r\n".getBytes());
        long numToSkip = 6;
        long skippedBytes = IOUtils.skip(input, numToSkip);
        assertEquals(6, skippedBytes);

        int c;
        while ((c = input.read()) != -1) {
            assertEquals('W', (char) c);
        }
    }

    /**
     * Test readFully method with default buffer size
     * @throws IOException
     */
    @Test
    public void testReadFullyDefaultBufferSize() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World".getBytes());
        byte[] buffer = new byte[10];
        int actualBytesRead = IOUtils.readFully(input, buffer);
        assertEquals(13, actualBytesRead);
        String result = new String(buffer, 0, actualBytesRead);
        assertEquals("Hello World", result);
    }

    /**
     * Test readFully method with custom buffer size
     * @throws IOException
     */
    @Test
    public void testReadFullyCustomBufferSize() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World".getBytes());
        byte[] buffer = new byte[5];
        int actualBytesRead = IOUtils.readFully(input, buffer, 0, buffer.length);
        assertEquals(5, actualBytesRead);
        String result = new String(buffer, 0, actualBytesRead);
        assertEquals("Hello ", result);

        int remainingBytesToRead = buffer.length - actualBytesRead;
        byte[] remainingBuffer = new byte[remainingBytesToRead];
        actualBytesRead = IOUtils.readFully(input, remainingBuffer, 0, remainingBytesToRead);
        assertEquals(remainingBytesToRead, actualBytesRead);
        String finalResult = new String(new byte[] {}.concat(buffer).concat(remainingBuffer));
        assertEquals("Hello World", finalResult);
    }

    /**
     * Test toByteArray method
     * @throws IOException
     */
    @Test
    public void testToByteArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World".getBytes());
        byte[] expectedByteArray = "Hello World".getBytes();
        byte[] actualByteArray = IOUtils.toByteArray(input);
        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    /**
     * Test closeQuietly method
     * @throws IOException
     */
    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World".getBytes());
        IOUtils.closeQuietly(input);
        assertTrue(input.isClosed());
    }
}