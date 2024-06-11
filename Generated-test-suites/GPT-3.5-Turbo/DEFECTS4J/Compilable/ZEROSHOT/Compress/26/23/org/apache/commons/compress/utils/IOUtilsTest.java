package org.apache.commons.compress.utils;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.utils.IOUtils;
import java.io.*;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        byte[] result = output.toByteArray();

        assertEquals(5, count);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, result);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6});
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output, 3);
        byte[] result = output.toByteArray();

        assertEquals(6, count);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6}, result);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        long skipped = IOUtils.skip(input, 3);

        assertEquals(3, skipped);
    }

    @Test
    public void testSkipWithZeroSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        long skipped = IOUtils.skip(input, 0);

        assertEquals(0, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] buffer = new byte[5];
        int count = IOUtils.readFully(input, buffer);

        assertEquals(5, count);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, buffer);
    }

    @Test
    public void testReadFullyWithOffsetAndLen() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] buffer = new byte[5];
        int count = IOUtils.readFully(input, buffer, 1, 3);

        assertEquals(3, count);
        assertArrayEquals(new byte[]{0, 1, 2, 0, 0}, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] result = IOUtils.toByteArray(input);

        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, result);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        IOUtils.closeQuietly(input);
        assertTrue(true); // no exception occurred
    }
}