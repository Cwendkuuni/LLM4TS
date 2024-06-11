package org.apache.commons.compress.utils;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class IOUtilsTest {

    @Test
    public void testCopyDefaultBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        assertEquals(4, count);
        assertEquals("test", output.toString());
    }

    @Test
    public void testCopyCustomBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output, 2);
        assertEquals(4, count);
        assertEquals("test", output.toString());
    }

    @Test
    public void testSkipWithBytesToSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        long skipped = IOUtils.skip(input, 2);
        assertEquals(2, skipped);
    }

    @Test
    public void testSkipWithZeroBytesToSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        long skipped = IOUtils.skip(input, 0);
        assertEquals(0, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] buffer = new byte[4];
        int count = IOUtils.readFully(input, buffer);
        assertEquals(4, count);
        assertArrayEquals("test".getBytes(), buffer);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] buffer = new byte[6];
        int count = IOUtils.readFully(input, buffer, 1, 4);
        assertEquals(4, count);
        assertArrayEquals(new byte[]{0, 't', 'e', 's', 't', 0}, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] byteArray = IOUtils.toByteArray(input);
        assertArrayEquals("test".getBytes(), byteArray);
    }

    @Test
    public void testCloseQuietlyWithNull() {
        IOUtils.closeQuietly(null); // no exception should be thrown
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException("Test exception");
            }
        };
        IOUtils.closeQuietly(closeable); // no exception should be thrown
    }
}