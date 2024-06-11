package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

/**
 * IOUtils test class
 */
public class IOUtilsTest {

    /**
     * Test copy method with positive input and output stream
     * @throws IOException
     */
    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output);
        long expected = 5L;
        assertEquals(expected, actual);
        assertEquals("Test", output.toString());
    }

    /**
     * Test copy method with null input stream
     */
    @Test(expected = NullPointerException.class)
    public void testCopyWithNullInput() {
        OutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(null, output);
    }

    /**
     * Test copy method with null output stream
     */
    @Test(expected = NullPointerException.class)
    public void testCopyWithNullOutput() {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        IOUtils.copy(input, null);
    }

    /**
     * Test copy method with negative buffer size
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCopyWithNegativeBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(input, output, -1);
    }

    /**
     * Test skip method with positive input stream and number to skip
     * @throws IOException
     */
    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        long actual = IOUtils.skip(input, 2L);
        long expected = 3L;
        assertEquals(expected, actual);
        assertEquals("tst", input.readLine());
    }

    /**
     * Test skip method with null input stream
     */
    @Test(expected = NullPointerException.class)
    public void testSkipWithNullInput() {
        IOUtils.skip(null, 0);
    }

    /**
     * Test skip method with negative number to skip
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSkipWithNegativeNumber() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        IOUtils.skip(input, -1);
    }

    /**
     * Test readFully method with positive input stream and buffer
     * @throws IOException
     */
    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        byte[] buffer = new byte[5];
        int actual = IOUtils.readFully(input, buffer);
        int expected = 5;
        assertEquals(expected, actual);
        assertEquals("Test", new String(buffer));
    }

    /**
     * Test readFully method with null input stream
     */
    @Test(expected = NullPointerException.class)
    public void testReadFullyWithNullInput() {
        byte[] buffer = new byte[5];
        IOUtils.readFully(null, buffer);
    }

    /**
     * Test readFully method with buffer smaller than the input length
     * @throws IOException
     */
    @Test
    public void testReadFullyWithSmallBuffer() throws IOException {
        InputStream input = new ByteArrayInputStream("TestTestTestTestTestTest".getBytes());
        byte[] buffer = new byte[3];
        int actual = IOUtils.readFully(input, buffer);
        int expected = 3;
        assertEquals(expected, actual);
        assertEquals("Test", new String(buffer));
    }

    /**
     * Test toByteArray method with positive input stream
     * @throws IOException
     */
    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        byte[] actual = IOUtils.toByteArray(input);
        byte[] expected = "Test".getBytes();
        assertArrayEquals(expected, actual);
    }

    /**
     * Test toByteArray method with null input stream
     */
    @Test(expected = NullPointerException.class)
    public void testToByteArrayWithNullInput() {
        IOUtils.toByteArray(null);
    }

    /**
     * Test closeQuietly method with null Closeable
     */
    @Test
    public void testCloseQuietlyWithNullCloseable() {
        IOUtils.closeQuietly(null);
    }
}