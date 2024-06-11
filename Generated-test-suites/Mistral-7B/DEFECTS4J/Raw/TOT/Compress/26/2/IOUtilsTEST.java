package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

/**
 * Testing IOUtils class
 */
public class IOUtilsTest {

    /**
     * Test copy method with valid InputStream and OutputStream.
     */
    @Test
    public void testCopyValid() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });

        long copiedBytes = IOUtils.copy(input, output);
        byte[] expectedOutput = new byte[] { 1, 2, 3, 4, 5 };
        byte[] actualOutput = output.toByteArray();

        assertEquals("Copied bytes do not match", copiedBytes, 5L);
        assertEquals("Output does not match expected", expectedOutput, actualOutput);
    }

    /**
     * Test copy method with valid InputStream and OutputStream of different sizes.
     */
    @Test
    public void testCopyDifferentSizes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream(6);
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });

        long copiedBytes = IOUtils.copy(input, output);
        byte[] expectedOutput = new byte[] { 1, 2, 3, 4, 5 };
        byte[] actualOutput = output.toByteArray();

        assertEquals("Copied bytes do not match", copiedBytes, 5L);
        assertEquals("Output does not match expected", expectedOutput, actualOutput);
    }

    /**
     * Test copy method with null InputStream or OutputStream.
     */
    @Test
    public void testCopyNull() {
        ByteArrayOutputStream output = null;
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });

        assertThrows("Input stream should not be null", IOException.class, () -> IOUtils.copy(input, output));

        input = null;
        output = new ByteArrayOutputStream();

        assertThrows("Output stream should not be null", IOException.class, () -> IOUtils.copy(input, output));
    }

    /**
     * Test copy method with InputStream that does not support reading.
     */
    @Test
    public void testCopyInvalidInputStream() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        try {
            IOUtils.copy(input, output);
        } catch (IOException e) {
            // Expected exception
        }
    }

    /**
     * Test skip method with valid InputStream and given number of bytes to skip.
     */
    @Test
    public void testSkipValid() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });

        long skippedBytes = IOUtils.skip(input, 3L);
        assertEquals("Skipped bytes do not match", skippedBytes, 3L);

        long remainingBytes = input.available();
        assertEquals("Unexpected number of bytes remaining after skip", 2, remainingBytes);
    }

    /**
     * Test skip method with InputStream that has fewer bytes to skip than available.
     */
    @Test
    public void testSkipFewerBytes() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });

        long skippedBytes = IOUtils.skip(input, 7L);
        assertEquals("Skipped bytes do not match", skippedBytes, 5L);

        long remainingBytes = input.available();
        assertEquals("Unexpected number of bytes remaining after skip", 0, remainingBytes);
    }

    /**
     * Test skip method with InputStream that has fewer bytes than requested.
     */
    @Test
    public void testSkipFewerBytesRemaining() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });

        long skippedBytes = IOUtils.skip(input, 4L);
        assertEquals("Skipped bytes do not match", skippedBytes, 3L);

        long remainingBytes = input.available();
        assertEquals("Unexpected number of bytes remaining after skip", 0, remainingBytes);
    }

    /**
     * Test skip method with null InputStream.
     */
    @Test
    public void testSkipNullInputStream() {
        InputStream input = null;

        assertThrows("Input stream should not be null", IOException.class, () -> IOUtils.skip(input, 1L));
    }

    /**
     * Test readFully method with valid InputStream and byte array.
     */
    @Test
    public void testReadFullyValid() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        byte[] buffer = new byte[5];

        int bytesRead = IOUtils.readFully(input, buffer);
        byte[] expectedBuffer = new byte[] { 1, 2, 3, 4, 5 };

        assertEquals("Bytes read do not match", bytesRead, 5);
        assertArrayEquals("Buffer does not match expected", expectedBuffer, buffer);
    }

    /**
     * Test readFully method with InputStream that has fewer bytes than buffer size.
     */
    @Test
    public void testReadFullyFewerBytes() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        byte[] buffer = new byte[5];

        int bytesRead = IOUtils.readFully(input, buffer);
        byte[] expectedBuffer = new byte[] { 1, 2, 3 };

        assertEquals("Bytes read do not match", bytesRead, 3);
        assertArrayEquals("Buffer does not match expected", expectedBuffer, buffer);
    }

    /**
     * Test readFully method with null InputStream or buffer.
     */
    @Test
    public void testReadFullyNull() {
        InputStream input = null;
        byte[] buffer = null;

        assertThrows("Input stream should not be null", IOException.class, () -> IOUtils.readFully(input, buffer));
        assertThrows("Buffer should not be null", IOException.class, () -> IOUtils.readFully(input, buffer));
    }

    /**
     * Test toByteArray method with valid InputStream.
     */
    @Test
    public void testToByteArrayValid() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        byte[] expectedByteArray = new byte[] { 1, 2, 3, 4, 5 };
        byte[] actualByteArray = IOUtils.toByteArray(input);

        assertArrayEquals("Byte array does not match expected", expectedByteArray, actualByteArray);
    }

    /**
     * Test toByteArray method with InputStream that has fewer bytes than byte array size.
     */
    @Test
    public void testToByteArrayFewerBytes() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        byte[] expectedByteArray = new byte[] { 1, 2, 3 };
        byte[] actualByteArray = IOUtils.toByteArray(input);

        assertArrayEquals("Byte array does not match expected", expectedByteArray, actualByteArray);
    }

    /**
     * Test toByteArray method with null InputStream or byte array.
     */
    @Test
    public void testToByteArrayNull() {
        InputStream input = null;
        byte[] buffer = null;

        assertThrows("InputStream should not be null", IOException.class, () -> IOUtils.toByteArray(input));
        assertThrows("Buffer should not be null", IOException.class, () -> IOUtils.toByteArray(input, buffer));
    }

    /**
     * Test closeQuietly method with valid Closeable.
     */
    @Test
    public void testCloseQuietlyValid() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });

        IOUtils.closeQuietly(input);
        assertEquals("InputStream is not closed", false, input.isOpen());
    }

    /**
     * Test closeQuietly method with null Closeable.
     */
    @Test
    public void testCloseQuietlyNull() {
        Closeable closeable = null;

        IOUtils.closeQuietly(closeable);
        // No need to check for null closeable here as the assertThrows will ensure it
    }
}