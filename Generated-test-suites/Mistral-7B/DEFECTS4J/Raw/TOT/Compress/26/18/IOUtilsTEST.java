package org.apache.commons.compress.utils;

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
public final class IOUtilsTest {

    private InputStream input;
    private OutputStream output;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;

    @Before
    public void setup() {
        output = new ByteArrayOutputStream();
        outputStream = new ByteArrayOutputStream();
        input = new ByteArrayInputStream(new byte[0]);
        inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
    }

    /**
     * Test copy method with InputStream and OutputStream
     *
     * @throws IOException
     */
    @Test
    public void testCopy() throws IOException {
        input = new ByteArrayInputStream(new byte[]{'A', 'B', 'C', 'D', 'E', 'F'});
        output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        Assert.assertEquals(6, count);
        byte[] outputBytes = output.toByteArray();
        Assert.assertArrayEquals(new byte[]{'A', 'B', 'C', 'D', 'E', 'F'}, outputBytes);
    }

    /**
     * Test copy method with InputStream, OutputStream and custom buffer size
     *
     * @throws IOException
     */
    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        input = new ByteArrayInputStream(new byte[]{'A', 'B', 'C', 'D', 'E', 'F'});
        output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output, 3);
        Assert.assertEquals(3, count);
        byte[] outputBytes = output.toByteArray();
        Assert.assertArrayEquals(new byte[]{'A', 'B', 'C'}, outputBytes);
    }

    /**
     * Test skip method with InputStream
     *
     * @throws IOException
     */
    @Test
    public void testSkip() throws IOException {
        input = new ByteArrayInputStream(new byte[]{'A', 'B', 'C', 'D', 'E', 'F'});
        long skipped = IOUtils.skip(input, 6);
        Assert.assertEquals(1, skipped);
        long skippedAll = IOUtils.skip(input, 6);
        Assert.assertEquals(0, skippedAll);
    }

    /**
     * Test readFully method with InputStream and byte[]
     *
     * @throws IOException
     */
    @Test
    public void testReadFully() throws IOException {
        byte[] bytesExpected = new byte[]{'A', 'B', 'C'};
        byte[] bytesActual = new byte[3];
        int bytesActualCount = IOUtils.readFully(inputStream, bytesActual);
        Assert.assertArrayEquals(bytesExpected, bytesActual);
        Assert.assertEquals(3, bytesActualCount);
    }

    /**
     * Test readFully method with InputStream, byte[] and custom offset and length
     *
     * @throws IOException
     */
    @Test
    public void testReadFullyCustomOffsetAndLength() throws IOException {
        byte[] bytesExpected = new byte[]{'B', 'C'};
        byte[] bytesActual = new byte[2];
        int bytesActualCount = IOUtils.readFully(inputStream, bytesActual, 1, 2);
        Assert.assertArrayEquals(bytesExpected, bytesActual);
        Assert.assertEquals(2, bytesActualCount);
    }

    /**
     * Test toByteArray method with InputStream
     *
     * @throws IOException
     */
    @Test
    public void testToByteArray() throws IOException {
        byte[] bytesExpected = new byte[]{'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] bytesActual = IOUtils.toByteArray(inputStream);
        Assert.assertArrayEquals(bytesExpected, bytesActual);
    }

    /**
     * Test closeQuietly method
     */
    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream closeableStream = new ByteArrayOutputStream();
        IOUtils.closeQuietly(closeableStream);
        Assert.assertNull(closeableStream);
    }

    /**
     * Test copy method with InputStream, OutputStream and IllegalArgumentException
     *
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCopyWithNullOutputStream() throws IOException {
        input = new ByteArrayInputStream(new byte[]{'A', 'B', 'C', 'D', 'E', 'F'});
        output = null;
        long count = IOUtils.copy(input, output);
    }

    /**
     * Test copy method with InputStream, OutputStream and IOException
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testCopyWithIOException() throws IOException {
        input = new ByteArrayInputStream(new byte[]{'A', 'B', 'C', 'D', 'E', 'F'});
        output = new ByteArrayOutputStream();
        OutputStream outputWrapped = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Test IOException");
            }
        };
        long count = IOUtils.copy(input, outputWrapped);
    }
}