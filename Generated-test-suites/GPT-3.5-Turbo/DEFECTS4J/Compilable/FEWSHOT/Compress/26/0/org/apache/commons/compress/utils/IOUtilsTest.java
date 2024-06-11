package org.apache.commons.compress.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        assertEquals("Hello, World!", output.toString());
        assertEquals(inputBytes.length, count);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int bufferSize = 5;
        long count = IOUtils.copy(input, output, bufferSize);
        assertEquals("Hello, World!", output.toString());
        assertEquals(inputBytes.length, count);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        long numToSkip = 7;
        long skipped = IOUtils.skip(input, numToSkip);
        String remaining = "";
        int nextByte;
        while ((nextByte = input.read()) != -1) {
            remaining += (char) nextByte;
        }
        assertEquals("World!", remaining);
        assertEquals(numToSkip, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] output = new byte[5];
        int bytesRead = IOUtils.readFully(input, output);
        assertEquals("Hello", new String(output));
        assertEquals(5, bytesRead);
    }

    @Test
    public void testReadFullyWithOffset() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] output = new byte[10];
        int bytesRead = IOUtils.readFully(input, output, 2, 5);
        assertEquals("\u0000\u0000Hello\u0000\u0000\u0000", new String(output));
        assertEquals(5, bytesRead);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] output = IOUtils.toByteArray(input);
        assertArrayEquals(inputBytes, output);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        IOUtils.closeQuietly(input);
        // No assertion, just verifying that it doesn't throw an exception
    }
}