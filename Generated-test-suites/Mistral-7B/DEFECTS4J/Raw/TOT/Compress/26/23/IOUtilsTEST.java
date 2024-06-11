package org.apache.commons.compress.utils;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IOUtils test class
 */
public class IOUtilsTest {

    /**
     * Test copy method with positive input and output streams.
     */
    @Test
    public void testCopyPositive() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        long length = IOUtils.copy(input, output);
        Assert.assertEquals(5, length);
        Assert.assertEquals("Test", new String(output.toByteArray()));
    }

    /**
     * Test copy method with negative input and output streams.
     */
    @Test
    public void testCopyNegative() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        long length = IOUtils.copy(input, output);
        Assert.assertEquals(0, length);
    }

    /**
     * Test copy method with error thrown.
     */
    @Test(expected = IOException.class)
    public void testCopyError() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test".getBytes()) {
            @Override
            public int read() throws IOException {
                throw new IOException("Stream error");
            }
        };
        IOUtils.copy(input, output);
    }

    /**
     * Test skip method with positive input stream and requested bytes to skip.
     */
    @Test
    public void testSkipPositive() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("TestStream".getBytes());
        long bytesToSkip = 3;
        long skippedBytes = IOUtils.skip(input, bytesToSkip);
        Assert.assertEquals("Stream", new String(input.readAllBytes()));
        Assert.assertEquals(bytesToSkip, skippedBytes);
    }

    /**
     * Test skip method with zero bytes to skip.
     */
    @Test
    public void testSkipZero() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        long bytesToSkip = 0;
        long skippedBytes = IOUtils.skip(input, bytesToSkip);
        Assert.assertEquals(0, skippedBytes);
    }

    /**
     * Test skip method with negative input stream and requested bytes to skip.
     */
    @Test(expected = IOException.class)
    public void testSkipNegative() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        long bytesToSkip = -3;
        IOUtils.skip(input, bytesToSkip);
    }

    /**
     * Test readFully method with positive input stream and byte array.
     */
    @Test
    public void testReadFullyPositive() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test".getBytes());
        byte[] result = new byte[5];
        int bytesRead = IOUtils.readFully(input, result);
        Assert.assertEquals(5, bytesRead);
        Assert.assertEquals("Test", new String(result));
    }

    /**
     * Test readFully method with zero bytes read.
     */
    @Test
    public void testReadFullyZero() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        byte[] result = new byte[5];
        int bytesRead = IOUtils.readFully(input, result);
        Assert.assertEquals(0, bytesRead);
    }

    /**
     * Test readFully method with less bytes read than requested.
     */
    @Test
    public void testReadFullyLess() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test".getBytes());
        byte[] result = new byte[6];
        int bytesRead = IOUtils.readFully(input, result);
        Assert.assertEquals(5, bytesRead);
        Assert.assertEquals("Test", new String(result, 0, bytesRead));
    }

    /**
     * Test readFully method with error thrown.
     */
    @Test(expected = IOException.class)
    public void testReadFullyError() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test".getBytes()) {
            @Override
            public int read() throws IOException {
                throw new IOException("Stream error");
            }
        };
        byte[] result = new byte[5];
        IOUtils.readFully(input, result);
    }

    /**
     * Test toByteArray method with positive input stream.
     */
    @Test
    public void testToByteArrayPositive() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test".getBytes());
        byte[] result = IOUtils.toByteArray(input);
        Assert.assertEquals(5, result.length);
        Assert.assertEquals("Test", new String(result));
    }

    /**
     * Test toByteArray method with empty input stream.
     */
    @Test
    public void testToByteArrayEmpty() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        byte[] result = IOUtils.toByteArray(input);
        Assert.assertEquals(0, result.length);
    }

    /**
     * Test toByteArray method with error thrown.
     */
    @Test(expected = IOException.class)
    public void testToByteArrayError() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test".getBytes()) {
            @Override
            public int read() throws IOException {
                throw new IOException("Stream error");
            }
        };
        IOUtils.toByteArray(input);
    }

    /**
     * Test closeQuietly method with null Closeable.
     */
    @Test
    public void testCloseQuietlyNull() {
        IOUtils.closeQuietly(null);
    }

    /**
     * Test closeQuietly method with empty InputStream.
     */
    @Test
    public void testCloseQuietlyEmptyInputStream() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        IOUtils.closeQuietly(input);
    }

    /**
     * Test closeQuietly method with InputStream throwing an IOException.
     */
    @Test
    public void testCloseQuietlyIOException() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test".getBytes()) {
            @Override
            public void close() throws IOException {
                throw new IOException("IOException");
            }
        };
        IOUtils.closeQuietly(input);
    }
}