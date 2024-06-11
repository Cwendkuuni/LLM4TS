package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class IOUtilsTest {
    @Test
    public void testCopy() throws IOException {
        // Test copy with default buffer size
        String testString = "This is a test string";
        InputStream input = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        OutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        assertEquals(testString.length(), count);
        assertEquals(testString, output.toString());

        // Test copy with custom buffer size
        input = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        output = new ByteArrayOutputStream();
        int bufferSize = 4;
        count = IOUtils.copy(input, output, bufferSize);
        assertEquals(testString.length(), count);
        assertEquals(testString, output.toString());

        // Test copy with empty input stream
        input = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
        output = new ByteArrayOutputStream();
        count = IOUtils.copy(input, output);
        assertEquals(0, count);
        assertEquals("", output.toString());
    }
    
    @Test
    public void testSkip() throws IOException {
        // Test skipping bytes
        String testString = "This is a test string";
        InputStream input = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        long numToSkip = 10;
        long skipped = IOUtils.skip(input, numToSkip);
        assertEquals(numToSkip, skipped);
        
        // Test skipping more bytes than available
        input = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        numToSkip = 100;
        skipped = IOUtils.skip(input, numToSkip);
        assertEquals(testString.length(), skipped);
        
        // Test skipping bytes on empty input stream
        input = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
        numToSkip = 10;
        skipped = IOUtils.skip(input, numToSkip);
        assertEquals(0, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        // Test reading fully into buffer
        String testString = "This is a test string";
        InputStream input = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        byte[] buffer = new byte[testString.length()];
        int bytesRead = IOUtils.readFully(input, buffer);
        assertEquals(testString.length(), bytesRead);
        assertEquals(testString, new String(buffer, StandardCharsets.UTF_8));

        // Test reading partially into buffer
        input = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        buffer = new byte[testString.length() + 10];
        bytesRead = IOUtils.readFully(input, buffer, 5, testString.length());
        assertEquals(testString.length(), bytesRead);
        assertEquals(testString, new String(buffer, 5, testString.length(), StandardCharsets.UTF_8));

        // Test reading fully on empty input stream
        input = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
        buffer = new byte[10];
        bytesRead = IOUtils.readFully(input, buffer);
        assertEquals(0, bytesRead);
        assertEquals("", new String(buffer, StandardCharsets.UTF_8));
    }

    @Test
    public void testToByteArray() throws IOException {
        // Test converting input stream to byte array
        String testString = "This is a test string";
        InputStream input = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        byte[] byteArray = IOUtils.toByteArray(input);
        assertEquals(testString, new String(byteArray, StandardCharsets.UTF_8));

        // Test converting empty input stream to byte array
        input = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
        byteArray = IOUtils.toByteArray(input);
        assertEquals("", new String(byteArray, StandardCharsets.UTF_8));
    }

    @Test
    public void testCloseQuietly() {
        // Test closing a Closeable
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException("Test IO Exception");
            }
        };
        IOUtils.closeQuietly(closeable);
        assertTrue(true); // If no exception is thrown, the test is considered as passed
        
        // Test closing a null Closeable
        closeable = null;
        IOUtils.closeQuietly(closeable);
        assertTrue(true); // If no exception is thrown, the test is considered as passed
    }
}