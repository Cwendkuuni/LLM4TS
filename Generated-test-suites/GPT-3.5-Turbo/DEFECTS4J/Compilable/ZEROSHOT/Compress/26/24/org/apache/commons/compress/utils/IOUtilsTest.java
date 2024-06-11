package org.apache.commons.compress.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class IOUtilsTest {

    /* Test copy method */

    @Test
    public void testCopy() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        byte[] outputBytes = output.toByteArray();
        assertEquals(inputBytes.length, outputBytes.length);
        assertArrayEquals(inputBytes, outputBytes);
        assertEquals(inputBytes.length, count);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output, 4096);
        byte[] outputBytes = output.toByteArray();
        assertEquals(inputBytes.length, outputBytes.length);
        assertArrayEquals(inputBytes, outputBytes);
        assertEquals(inputBytes.length, count);
    }

    /* Test skip method */

    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        long skipped = IOUtils.skip(input, 6);
        assertEquals(6, skipped);
    }

    @Test
    public void testSkipWithUnavailableBytes() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        long skipped = IOUtils.skip(input, 20);
        assertEquals(inputBytes.length, skipped);
    }

    /* Test readFully method */

    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(input, buffer);
        assertEquals(5, bytesRead);
        byte[] expectedBytes = {'H', 'e', 'l', 'l', 'o'};
        assertArrayEquals(expectedBytes, buffer);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(input, buffer, 1, 3);
        assertEquals(3, bytesRead);
        byte[] expectedBytes = {0, 'H', 'e', 'l', 0};
        assertArrayEquals(expectedBytes, buffer);
    }

    /* Test toByteArray method */

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = IOUtils.toByteArray(input);
        assertArrayEquals(inputBytes, outputBytes);
    }

    /* Test closeQuietly method */

    @Test
    public void testCloseQuietly() {
        // Unable to test as it only swallows IOException
    }

}