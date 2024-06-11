
package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] inputBytes = "Test Copy".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long count = IOUtils.copy(input, output);

        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, output.toByteArray());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] inputBytes = "Test Copy".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        int bufferSize = 16;
        long count = IOUtils.copy(input, output, bufferSize);

        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, output.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = "Test Skip".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        long numToSkip = 5;
        long skipped = IOUtils.skip(input, numToSkip);

        assertEquals(numToSkip, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = "Test Read Fully".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[16];

        int bytesRead = IOUtils.readFully(input, buffer);

        assertEquals(inputBytes.length, bytesRead);
        assertArrayEquals(inputBytes, buffer);
    }

    @Test
    public void testReadFullyWithOffset() throws IOException {
        byte[] inputBytes = "Test Read Fully".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[16];
        int offset = 4;
        int length = 10;

        int bytesRead = IOUtils.readFully(input, buffer, offset, length);

        assertEquals(length, bytesRead);
        assertArrayEquals("t Read Ful".getBytes(), buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = "Test To Byte Array".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        byte[] resultBytes = IOUtils.toByteArray(input);

        assertArrayEquals(inputBytes, resultBytes);
    }

    @Test
    public void testCloseQuietly() {
        // No exception expected
        IOUtils.closeQuietly(null);
    }
}
