package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IOUtils Test Cases
 */
public class IOUtilsTest {

    /**
     * Test copy method with valid InputStream and OutputStream
     */
    @Test
    public void testCopyValid() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        final OutputStream output = new ByteArrayOutputStream();
        final long copiedBytes = IOUtils.copy(input, output);
        final byte[] expectedOutput = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        final byte[] actualOutput = output.toByteArray();
        Assert.assertEquals("Unexpected number of bytes copied.", copiedBytes, 9L);
        Assert.assertArrayEquals("Unexpected output.", expectedOutput, actualOutput);
    }

    /**
     * Test copy method with null InputStream and OutputStream
     */
    @Test(expected = IOException.class)
    public void testCopyNullInput() {
        final OutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(null, output);
    }

    /**
     * Test copy method with null OutputStream
     */
    @Test(expected = IOException.class)
    public void testCopyNullOutput() {
        final InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        IOUtils.copy(input, null);
    }

    /**
     * Test skip method with valid InputStream and number to skip
     */
    @Test
    public void testSkipValid() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        final long skippedBytes = IOUtils.skip(input, 5L);
        final long expectedSkippedBytes = 5L;
        Assert.assertEquals("Unexpected number of bytes skipped.", expectedSkippedBytes, skippedBytes);
    }

    /**
     * Test skip method with null InputStream
     */
    @Test(expected = IOException.class)
    public void testSkipNullInput() {
        IOUtils.skip(null, 5L);
    }

    /**
     * Test skip method with negative number to skip
     */
    @Test(expected = IOException.class)
    public void testSkipNegativeNumber() {
        final InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        IOUtils.skip(input, -5L);
    }

    /**
     * Test readFully method with valid InputStream and byte array
     */
    @Test
    public void testReadFullyValid() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        final byte[] expectedOutput = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        final byte[] actualOutput = new byte[10];
        final int readFully = IOUtils.readFully(input, actualOutput);
        Assert.assertEquals("Unexpected number of bytes read.", 9, readFully);
        Assert.assertArrayEquals("Unexpected output.", expectedOutput, actualOutput, 0, 9);
    }

    /**
     * Test readFully method with null InputStream
     */
    @Test(expected = IOException.class)
    public void testReadFullyNullInput() {
        final byte[] actualOutput = new byte[10];
        IOUtils.readFully(null, actualOutput);
    }

    /**
     * Test readFully method with null byte array
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadFullyNullOutput() {
        final InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        IOUtils.readFully(input, null);
    }

    /**
     * Test toByteArray method with valid InputStream
     */    @Test    public void testToByteArrayValid() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        final byte[] expectedOutput = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        final byte[] actualOutput = IOUtils.toByteArray(input);
        Assert.assertArrayEquals("Unexpected output.", expectedOutput, actualOutput);
    }

    /**
     * Test toByteArray method with null InputStream
     */
    @Test(expected = IOException.class)
    public void testToByteArrayNullInput() {
        IOUtils.toByteArray(null);
    }

    /**
     * Test closeQuietly method with null Closeable
     */
    @Test
    public void testCloseQuietlyNull() {
        IOUtils.closeQuietly(null);
    }
}