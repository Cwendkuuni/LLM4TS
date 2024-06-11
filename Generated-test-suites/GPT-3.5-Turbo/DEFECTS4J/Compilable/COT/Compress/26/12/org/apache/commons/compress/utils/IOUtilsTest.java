package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        // Test case 1
        InputStream input1 = new ByteArrayInputStream("Hello, World!".getBytes());
        ByteArrayOutputStream output1 = new ByteArrayOutputStream();
        long count1 = IOUtils.copy(input1, output1);
        assertEquals(count1, 13);
        assertEquals("Hello, World!", output1.toString());

        // Test case 2
        InputStream input2 = new ByteArrayInputStream("Test".getBytes());
        ByteArrayOutputStream output2 = new ByteArrayOutputStream();
        long count2 = IOUtils.copy(input2, output2, 5);
        assertEquals(count2, 4);
        assertEquals("Test", output2.toString());
    }

    @Test
    public void testSkip() throws IOException {
        // Test case 1
        InputStream input1 = new ByteArrayInputStream("Hello, World!".getBytes());
        long skipped1 = IOUtils.skip(input1, 6);
        assertEquals(skipped1, 6);

        // Test case 2
        InputStream input2 = new ByteArrayInputStream("Hello, World!".getBytes());
        long skipped2 = IOUtils.skip(input2, 20);
        assertEquals(skipped2, 13);
    }

    @Test
    public void testReadFully() throws IOException {
        // Test case 1
        InputStream input1 = new ByteArrayInputStream("Hello".getBytes());
        byte[] buffer1 = new byte[5];
        int count1 = IOUtils.readFully(input1, buffer1);
        assertEquals(count1, 5);
        assertEquals("Hello", new String(buffer1));

        // Test case 2
        InputStream input2 = new ByteArrayInputStream("Hello".getBytes());
        byte[] buffer2 = new byte[3];
        int count2 = IOUtils.readFully(input2, buffer2, 1, 2);
        assertEquals(count2, 2);
        assertEquals("\0He", new String(buffer2));
    }

    @Test
    public void testToByteArray() throws IOException {
        // Test case 1
        InputStream input1 = new ByteArrayInputStream("Hello".getBytes());
        byte[] bytes1 = IOUtils.toByteArray(input1);
        assertEquals("Hello", new String(bytes1));

        // Test case 2
        InputStream input2 = new ByteArrayInputStream("Test".getBytes());
        byte[] bytes2 = IOUtils.toByteArray(input2);
        assertEquals("Test", new String(bytes2));
    }

    @Test
    public void testCloseQuietly() {
        // Test case 1
        InputStream input1 = new ByteArrayInputStream("Hello".getBytes());
        IOUtils.closeQuietly(input1);
        // No assertion, just testing for no exception

        // Test case 2
        InputStream input2 = null;
        IOUtils.closeQuietly(input2);
        // No assertion, just testing for no exception
    }
}

