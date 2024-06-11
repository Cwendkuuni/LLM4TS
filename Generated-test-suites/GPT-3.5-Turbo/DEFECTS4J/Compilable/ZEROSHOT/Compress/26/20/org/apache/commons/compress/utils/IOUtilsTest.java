package org.apache.commons.compress.utils;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class IOUtilsTest {

    private InputStream input;
    private OutputStream output;

    @Before
    public void setUp() {
        input = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        output = new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        };
    }

    @Test
    public void testCopyDefaultBufferSize() {
        try {
            long count = IOUtils.copy(input, output);
            assertEquals(0, count);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCopyCustomBufferSize() {
        try {
            long count = IOUtils.copy(input, output, 8024);
            assertEquals(0, count);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSkip() {
        try {
            long skipped = IOUtils.skip(input, 100);
            assertEquals(100, skipped);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testReadFully() {
        byte[] buffer = new byte[10];
        try {
            int bytesRead = IOUtils.readFully(input, buffer);
            assertEquals(10, bytesRead);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testReadFullyWithOffsetAndLength() {
        byte[] buffer = new byte[10];
        try {
            int bytesRead = IOUtils.readFully(input, buffer, 0, 10);
            assertEquals(10, bytesRead);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testToByteArray() {
        byte[] result = new byte[0];
        try {
            result = IOUtils.toByteArray(input);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
        assertNotNull(result);
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {

            }
        };
        try {
            IOUtils.closeQuietly(closeable);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}