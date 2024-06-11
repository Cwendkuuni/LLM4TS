package org.apache.commons.compress.utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class IOUtilsTest {

    private ByteArrayOutputStream output;
    private ByteArrayInputStream input;

    @Before
    public void setUp() {
        output = new ByteArrayOutputStream();
        input = new ByteArrayInputStream("Test String".getBytes());
    }

    @After
    public void tearDown() {
        output = null;
        input = null;
    }

    @Test
    public void testCopy() throws IOException {
        long count = IOUtils.copy(input, output);
        assertEquals(11, count);
        assertEquals("Test String", output.toString());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        long count = IOUtils.copy(input, output, 8024);
        assertEquals(11, count);
        assertEquals("Test String", output.toString());
    }

    @Test
    public void testSkip() throws IOException {
        long skipped = IOUtils.skip(input, 5);
        assertEquals(5, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(input, buffer);
        assertEquals(5, bytesRead);
        assertEquals("Test ", new String(buffer));
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = IOUtils.readFully(input, buffer, 2, 5);
        assertEquals(5, bytesRead);
        assertEquals("\u0000\u0000Test\u0000\u0000", new String(buffer));
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] bytes = IOUtils.toByteArray(input);
        assertEquals("Test String", new String(bytes));
    }

    @Test
    public void testCloseQuietly() {
        IOUtils.closeQuietly(null);
        // Test does not throw exception
    }
}