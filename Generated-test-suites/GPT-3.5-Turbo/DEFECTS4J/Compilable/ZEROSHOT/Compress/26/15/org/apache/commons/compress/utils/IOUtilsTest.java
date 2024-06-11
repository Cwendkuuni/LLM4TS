package org.apache.commons.compress.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        assertEquals(4, count);
        assertEquals("Test", output.toString());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output, 10);
        assertEquals(4, count);
        assertEquals("Test", output.toString());
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        long skipped = IOUtils.skip(input, 1);
        assertEquals(1, skipped);
        assertEquals("est", input.toString());
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        byte[] buffer = new byte[4];
        int read = IOUtils.readFully(input, buffer);
        assertEquals(4, read);
        assertEquals("Test", new String(buffer));
    }

    @Test
    public void testReadFullyWithOffsetAndLen() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        byte[] buffer = new byte[4];
        int read = IOUtils.readFully(input, buffer, 0, 4);
        assertEquals(4, read);
        assertEquals("Test", new String(buffer));
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        byte[] bytes = IOUtils.toByteArray(input);
        assertEquals("Test", new String(bytes));
    }

    @Test
    public void testCloseQuietly() {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        IOUtils.closeQuietly(input);
        // no assertion, just checking for any exceptions
    }
}