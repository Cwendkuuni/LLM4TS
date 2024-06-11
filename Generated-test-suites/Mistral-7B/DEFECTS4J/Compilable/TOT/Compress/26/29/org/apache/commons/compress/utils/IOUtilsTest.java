package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.utils.IOUtils;

import org.junit.Test;

/**
 * IOUtils Test Cases
 */
public class IOUtilsTest {

    /**
     * Test copy method with valid InputStream and OutputStream
     */
    @Test
    public void testCopyValid() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});

        long bytesCopied = IOUtils.copy(inputStream, outputStream);
        assertEquals(1, bytesCopied);

        assertEquals('A', outputStream.toByteArray()[0]);
    }

    /**
     * Test copy method with valid InputStream, OutputStream and custom buffer size
     */
    @Test
    public void testCopyCustomBufferSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});

        long bytesCopied = IOUtils.copy(inputStream, outputStream, 2);
        assertEquals(3, bytesCopied);

        byte[] result = outputStream.toByteArray();
        assertArrayEquals(new byte[]{'A', 'B', 'C'}, result);
    }

    /**
     * Test copy method with null InputStream
     */
    @Test(expected = NullPointerException.class)
    public void testCopyNullInputStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        IOUtils.copy(null, outputStream);
    }

    /**
     * Test copy method with null OutputStream
     */
    @Test(expected = NullPointerException.class)
    public void testCopyNullOutputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});

        IOUtils.copy(inputStream, null);
    }

    /**
     * Test skip method with valid InputStream and given number of bytes to skip
     */
    @Test
    public void testSkipValid() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});

        long bytesSkipped = IOUtils.skip(inputStream, 2);
        assertEquals(2, bytesSkipped);

        assertEquals('C', inputStream.read());
    }

    /**
     * Test skip method with valid InputStream and negative number of bytes to skip
     */
    @Test
    public void testSkipNegative() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});

        long bytesSkipped = IOUtils.skip(inputStream, -1);
        assertEquals(0, bytesSkipped);

        assertEquals('A', inputStream.read());
    }

    /**
     * Test skip method with valid InputStream, zero number of bytes to skip
     */
    @Test
    public void testSkipZero() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});

        long bytesSkipped = IOUtils.skip(inputStream, 0);
        assertEquals(0, bytesSkipped);

        assertEquals('A', inputStream.read());
    }

    /**
     * Test skip method with null InputStream
     */
    @Test(expected = NullPointerException.class)
    public void testSkipNullInputStream() throws IOException {
        IOUtils.skip(null, 1);
    }

    /**
     * Test readFully method with valid InputStream and byte array
     */
    @Test
    public void testReadFullyValid() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        byte[] result = new byte[3];

        int bytesRead = IOUtils.readFully(inputStream, result);

        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{'A', 'B', 'C'}, result);
    }

    /**
     * Test readFully method with valid InputStream, byte array, offset and length
     */
    @Test
    public void testReadFullyCustom() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C', 'D'});
        byte[] result = new byte[3];

        int bytesRead = IOUtils.readFully(inputStream, result, 1, 3);

        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{'B', 'C', 'D'}, result);
    }

    /**
     * Test readFully method with valid InputStream, byte array and negative length
     */
    @Test
    public void testReadFullyNegative() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        byte[] result = new byte[3];

        int bytesRead = IOUtils.readFully(inputStream, result, 2, -3);

        assertEquals(0, bytesRead);
        assertEquals(new byte[]{}, result);
    }

    /**
     * Test toByteArray method with valid InputStream
     */
    @Test
    public void testToByteArrayValid() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
        byte[] result = IOUtils.toByteArray(inputStream);

        assertEquals(3, result.length);
        assertArrayEquals(new byte[]{'A', 'B', 'C'}, result);
    }

    /**
     * Test toByteArray method with null InputStream
     */
    @Test(expected = NullPointerException.class)
    public void testToByteArrayNullInputStream() throws IOException {
        IOUtils.toByteArray(null);
    }
}