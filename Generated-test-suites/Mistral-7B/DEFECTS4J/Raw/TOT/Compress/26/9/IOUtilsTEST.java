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
 * IOUtils test class.
 */
public class IOUtilsTest {

    /**
     * Test copy method with valid InputStream and OutputStream.
     */
    @Test
    public void testCopyValid() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x1, 0x2, 0x3 });
        OutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output);
        assertEquals(3, actual);
        byte[] expected = new byte[] { 0x1, 0x2, 0x3 };
        byte[] result = output.toByteArray();
        assertArrayEquals(expected, result);
    }

    /**
     * Test copy method with null InputStream.
     */
    @Test(expected = NullPointerException.class)
    public void testCopyNullInputStream() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(null, output);
    }

    /**
     * Test copy method with null OutputStream.
     */
    @Test(expected = NullPointerException.class)
    public void testCopyNullOutputStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x1, 0x2, 0x3 });
        IOUtils.copy(input, null);
    }

    /**
     * Test skip method with valid InputStream and given number of bytes to skip.
     */
    @Test
    public void testSkipValid() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5 });
        long expected = 3;
        long actual = IOUtils.skip(input, expected);
        assertEquals(expected, actual);
        byte[] result = new byte[2];
        IOUtils.readFully(input, result);
        byte[] expectedResult = new byte[] { 0x4, 0x5 };
        assertArrayEquals(expectedResult, result);
    }

    /**
     * Test skip method with InputStream that has less data than expected number of bytes to skip.
     */
    @Test
    public void testSkipLessThanExpected() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x1, 0x2 });
        long expected = 3;
        long actual = IOUtils.skip(input, expected);
        assertEquals(2, actual);
    }

    /**
     * Test skip method with null InputStream.
     */
    @Test(expected = NullPointerException.class)
    public void testSkipNullInputStream() throws IOException {
        long numToSkip = 3;
        IOUtils.skip(null, numToSkip);
    }

    /**
     * Test readFully method with valid InputStream and byte array.
     */
    @Test
    public void testReadFullyValid() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5 });
        byte[] expected = new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5 };
        byte[] result = IOUtils.toByteArray(input);
        assertArrayEquals(expected, result);
    }

    /**
     * Test readFully method with null InputStream.
     */
    @Test(expected = NullPointerException.class)
    public void testReadFullyNullInputStream() throws IOException {
        byte[] result = IOUtils.toByteArray(null);
    }

    /**
     * Test closeQuietly method with null Closeable.
     */
    @Test
    public void testCloseQuietlyNull() {
        Closeable nullCloseable = null;
        IOUtils.closeQuietly(nullCloseable);
    }

    /**
     * Test closeQuietly method with valid Closeable.
     */
    @Test
    public void testCloseQuietlyValid() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(new ByteArrayInputStream(new byte[] { 0x1, 0x2, 0x3 }), output);
        IOUtils.closeQuietly(output);
    }
}