package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class IOUtilsTest {

    @Test
    public void testCopy() throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        assertEquals(5, count);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, output.toByteArray());
    }

    @Test
    public void testCopyWithBufferSize() throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int bufferSize = 2;
        long count = IOUtils.copy(input, output, bufferSize);
        assertEquals(5, count);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, output.toByteArray());
    }

    @Test
    public void testSkip() throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        long numToSkip = 2;
        long skipped = IOUtils.skip(input, numToSkip);
        assertEquals(2, skipped);
        assertEquals(3, input.read());
    }

    @Test
    public void testReadFully() throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] buffer = new byte[5];
        int bytesread = IOUtils.readFully(input, buffer);
        assertEquals(5, bytesread);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, buffer);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] buffer = new byte[3];
        int offset = 1;
        int length = 2;
        int bytesread = IOUtils.readFully(input, buffer, offset, length);
        assertEquals(2, bytesread);
        assertArrayEquals(new byte[]{0, 1, 2}, buffer);
    }

    @Test
    public void testToByteArray() throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] result = IOUtils.toByteArray(input);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, result);
    }

    @Test
    public void testCloseQuietly() throws Exception {
        Closeable closeable = new Closeable() {
            boolean closed = false;
            public void close() throws IOException {
                closed = true;
            }
        };
        IOUtils.closeQuietly(closeable);
        assertTrue(closeable.closed);
    }
}