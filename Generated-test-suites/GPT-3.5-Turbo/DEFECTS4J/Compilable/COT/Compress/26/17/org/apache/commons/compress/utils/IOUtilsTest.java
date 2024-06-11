package org.apache.commons.compress.utils;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class IOUtilsTest {

    @Test
    public void testCopy() {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        try {
            long result = IOUtils.copy(input, output);
            assertEquals("Expected 4 bytes to be copied", 4, result);
            assertEquals("Expected output to be 'test'", "test", output.toString());
        } catch (IOException e) {
            fail("Failed to copy input to output");
        }
    }

    @Test
    public void testCopyWithBufferSize() {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        try {
            long result = IOUtils.copy(input, output, 2);
            assertEquals("Expected 4 bytes to be copied", 4, result);
            assertEquals("Expected output to be 'test'", "test", output.toString());
        } catch (IOException e) {
            fail("Failed to copy input to output");
        }
    }

    @Test
    public void testSkip() {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        try {
            long result = IOUtils.skip(input, 2);
            assertEquals("Expected 2 bytes to be skipped", 2, result);
            assertEquals("Expected input to be positioned at 'st'", 's', input.read());
            assertEquals("Expected input to be positioned at 'st'", 't', input.read());
        } catch (IOException e) {
            fail("Failed to skip bytes");
        }
    }

    @Test
    public void testReadFully() {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] buffer = new byte[2];
        try {
            int result = IOUtils.readFully(input, buffer);
            assertEquals("Expected 2 bytes to be read", 2, result);
            assertEquals("Expected buffer to be 'te'", 't', buffer[0]);
            assertEquals("Expected buffer to be 'te'", 'e', buffer[1]);
        } catch (IOException e) {
            fail("Failed to read bytes");
        }
    }

    @Test
    public void testReadFullyWithOffsetAndLen() {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] buffer = new byte[4];
        try {
            int result = IOUtils.readFully(input, buffer, 1, 2);
            assertEquals("Expected 2 bytes to be read", 2, result);
            assertEquals("Expected buffer to be 'tst'", 't', buffer[1]);
            assertEquals("Expected buffer to be 'tst'", 's', buffer[2]);
        } catch (IOException e) {
            fail("Failed to read bytes");
        }
    }

    @Test
    public void testToByteArray() {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        try {
            byte[] result = IOUtils.toByteArray(input);
            assertArrayEquals("Expected byte array to be 'test'", new byte[]{'t', 'e', 's', 't'}, result);
        } catch (IOException e) {
            fail("Failed to convert input to byte array");
        }
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException("Test exception");
            }
        };
        IOUtils.closeQuietly(closeable);
        // No assertion as exception is thrown and swallowed
    }
}