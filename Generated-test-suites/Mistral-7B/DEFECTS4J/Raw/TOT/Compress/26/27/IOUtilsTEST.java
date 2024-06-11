package org.apache.commons.compress.utils;

import org.apache.commons.io.input.CloseShieldInputStream;
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
 * Test class for IOUtils.
 */
public final class IOUtilsTest {

    private InputStream input;
    private OutputStream output;

    @Before
    public void setUp() throws IOException {
        input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        output = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);
    }

    @Test
    public void testCopy() throws IOException {
        long actual = IOUtils.copy(input, output);
        Assert.assertEquals(input.available(), actual);
        byte[] actualBytes = output.toByteArray();
        byte[] expectedBytes = new byte[input.available()];
        input.read(expectedBytes);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testSkip() throws IOException {
        long actual = IOUtils.skip(input, 5L);
        Assert.assertEquals(5L, actual);
        byte[] actualBytes = new byte[4];
        input.read(actualBytes);
        Assert.assertArrayEquals(new byte[]{5, 6, 7, 8}, actualBytes);
    }

    @Test
    public void testCopyWithBuffersize() throws IOException {
        long actual = IOUtils.copy(input, output, 2);
        Assert.assertEquals(input.available(), actual);
        byte[] actualBytes = output.toByteArray();
        byte[] expectedBytes = new byte[input.available()];
        input.read(expectedBytes);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testCopyWithBuffersizeZero() throws IOException {
        long actual = IOUtils.copy(input, output, 0);
        Assert.assertEquals(input.available(), actual);
        byte[] actualBytes = output.toByteArray();
        byte[] expectedBytes = new byte[input.available()];
        input.read(expectedBytes);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopyWithNegativeBuffersize() throws IOException {
        IOUtils.copy(input, output, -1);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] expected = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] actual = new byte[expected.length];
        IOUtils.readFully(input, actual);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testReadFullyWithPartialFill() throws IOException {
        byte[] expected = new byte[]{1, 2, 3};
        byte[] actual = new byte[expected.length];
        IOUtils.readFully(input, actual, 0, 3);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] expected = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] actual = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        Closeable closeable = new CloseShieldInputStream(input);
        IOUtils.closeQuietly(closeable);
        Assert.assertTrue(closeable.isClosed());
    }
}