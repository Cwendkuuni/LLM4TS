package org.apache.commons.compress.utils;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("test string".getBytes());
        OutputStream output = new ByteArrayOutputStream();

        long count = IOUtils.copy(input, output);

        assertNotNull(output.toString());
        assertEquals(count, 11);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("test string".getBytes());
        OutputStream output = new ByteArrayOutputStream();

        int bufferSize = 8;
        long count = IOUtils.copy(input, output, bufferSize);

        assertNotNull(output.toString());
        assertEquals(count, 11);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("test string".getBytes());

        long numToSkip = 5;
        long skipped = IOUtils.skip(input, numToSkip);

        assertEquals(skipped, 5);
        assertEquals(input.available(), 6);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("test string".getBytes());
        byte[] buffer = new byte[5];

        int bytesRead = IOUtils.readFully(input, buffer);

        assertEquals(new String(buffer), "test ");
        assertEquals(bytesRead, 5);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        InputStream input = new ByteArrayInputStream("test string".getBytes());
        byte[] buffer = new byte[5];

        int offset = 2;
        int length = 3;
        int bytesRead = IOUtils.readFully(input, buffer, offset, length);

        assertEquals(new String(buffer), "  st ");
        assertEquals(bytesRead, 3);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("test string".getBytes());

        byte[] byteArray = IOUtils.toByteArray(input);

        assertNotNull(byteArray);
        assertEquals(new String(byteArray), "test string");
    }

    @Test
    public void testCloseQuietly() {
        Closeable c = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };

        IOUtils.closeQuietly(c);
    }
}