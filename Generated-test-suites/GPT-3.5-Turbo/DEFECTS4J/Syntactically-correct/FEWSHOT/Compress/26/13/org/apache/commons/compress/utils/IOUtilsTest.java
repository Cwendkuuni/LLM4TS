package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        // Test with small input stream
        InputStream inputStream1 = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        long bytesCopied1 = IOUtils.copy(inputStream1, outputStream1);
        assertEquals(3, bytesCopied1);
        assertArrayEquals(new byte[]{1, 2, 3}, outputStream1.toByteArray());

        // Test with large input stream
        InputStream inputStream2 = new ByteArrayInputStream(new byte[1000]);
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        long bytesCopied2 = IOUtils.copy(inputStream2, outputStream2);
        assertEquals(1000, bytesCopied2);
        assertEquals(1000, outputStream2.toByteArray().length);
    }

    @Test
    public void testSkip() throws IOException {
        // Create input stream with known content
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});

        // Skip 2 bytes
        long bytesSkipped1 = IOUtils.skip(inputStream, 2);
        assertEquals(2, bytesSkipped1);

        // Read remainder of stream
        byte[] remainingBytes1 = new byte[3];
        int bytesRead1 = inputStream.read(remainingBytes1);
        assertEquals(3, bytesRead1);
        assertArrayEquals(new byte[]{3, 4, 5}, remainingBytes1);

        // Create new input stream with known content
        InputStream inputStream2 = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});

        // Skip entire stream
        long bytesSkipped2 = IOUtils.skip(inputStream2, 5);
        assertEquals(5, bytesSkipped2);

        // Attempt to read from skipped stream
        byte[] remainingBytes2 = new byte[3];
        int bytesRead2 = inputStream2.read(remainingBytes2);
        assertEquals(-1, bytesRead2);
    }

    @Test
    public void testReadFully() throws IOException {
        // Create input stream with known content
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});

        // Read 3 bytes into byte array
        byte[] buffer1 = new byte[3];
        int bytesRead1 = IOUtils.readFully(inputStream, buffer1);
        assertEquals(3, bytesRead1);
        assertArrayEquals(new byte[]{1, 2, 3}, buffer1);

        // Read remaining 2 bytes into byte array
        byte[] buffer2 = new byte[2];
        int bytesRead2 = IOUtils.readFully(inputStream, buffer2);
        assertEquals(2, bytesRead2);
        assertArrayEquals(new byte[]{4, 5}, buffer2);

        // Create new input stream with known content
        InputStream inputStream2 = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});

        // Attempt to read more bytes than available in stream
        byte[] buffer3 = new byte[6];
        int bytesRead3 = IOUtils.readFully(inputStream2, buffer3);
        assertEquals(5, bytesRead3);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, buffer3);
    }

    @Test
    public void testToByteArray() throws IOException {
        // Create input stream with known content
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});

        // Convert input stream to byte array
        byte[] byteArray = IOUtils.toByteArray(inputStream);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, byteArray);

        // Create new input stream with no content
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);

        // Convert empty input stream to byte array
        byte[] emptyByteArray = IOUtils.toByteArray(emptyInputStream);
        assertArrayEquals(new byte[0], emptyByteArray);
    }

    @Test
    public void testCloseQuietly() {
        // Test with null Closeable
        IOUtils.closeQuietly(null);

        // Wrap in a Closeable and test with IOException
        Closeable closeableWithException = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
        IOUtils.closeQuietly(closeableWithException);

        // Test with a Closeable throwing RuntimeException
        Closeable closeableWithRuntimeException = new Closeable() {
            @Override
            public void close() {
                throw new RuntimeException();
            }
        };
        IOUtils.closeQuietly(closeableWithRuntimeException);

        // Test with a normal Closeable
        Closeable closeable = new Closeable() {
            boolean isClosed = false;

            @Override
            public void close() throws IOException {
                isClosed = true;
            }
        };
        IOUtils.closeQuietly(closeable);
        assertTrue(((Closeable) closeable).isClosed());
    }

}