package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class IOUtilsTest {

    @Test
    public void testCopyDefaultBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        assertEquals(4, count);
        assertEquals("Test", output.toString());
    }

    @Test
    public void testCopyCustomBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output, 2);
        assertEquals(4, count);
        assertEquals("Test", output.toString());
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        long skipped = IOUtils.skip(input, 2);
        assertEquals(2, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        byte[] b = new byte[4];
        int bytesRead = IOUtils.readFully(input, b);
        assertEquals(4, bytesRead);
        assertArrayEquals("Test".getBytes(), b);
    }

    @Test
    public void testReadFullyWithOffset() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        byte[] b = new byte[6];
        int bytesRead = IOUtils.readFully(input, b, 1, 4);
        assertEquals(4, bytesRead);
        assertArrayEquals(new byte[]{0, 'T', 'e', 's', 't', 0}, b);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        byte[] byteArray = IOUtils.toByteArray(input);
        assertArrayEquals("Test".getBytes(), byteArray);
    }

    @Test
    public void testCloseQuietly() {
        Closeable c = null;
        // no exception expected
        IOUtils.closeQuietly(c);
    }
}