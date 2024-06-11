package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class IOUtilsTest {
    @Test
    public void testCopy() {
        try {
            byte[] inputBytes = "This is a test".getBytes();
            InputStream input = new ByteArrayInputStream(inputBytes);
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            // Test with default buffer size
            long count = IOUtils.copy(input, output);
            assertEquals(inputBytes.length, count);
            byte[] outputBytes = output.toByteArray();
            assertArrayEquals(inputBytes, outputBytes);

            // Test with different buffer size
            input.reset();
            output.reset();
            int bufferSize = 1024;
            count = IOUtils.copy(input, output, bufferSize);
            assertEquals(inputBytes.length, count);
            outputBytes = output.toByteArray();
            assertArrayEquals(inputBytes, outputBytes);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testSkip() {
        try {
            byte[] inputBytes = "This is a test".getBytes();
            InputStream input = new ByteArrayInputStream(inputBytes);
            long numToSkip = 5;

            long skipped = IOUtils.skip(input, numToSkip);
            assertEquals(numToSkip, skipped);

            // Test skipping past end of stream
            skipped = IOUtils.skip(input, inputBytes.length);
            assertEquals(inputBytes.length - numToSkip, skipped);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadFully() {
        try {
            byte[] inputBytes = "This is a test".getBytes();
            InputStream input = new ByteArrayInputStream(inputBytes);
            byte[] buffer = new byte[inputBytes.length];

            // Test with buffer size equal to inputBytes length
            int bytesRead = IOUtils.readFully(input, buffer);
            assertEquals(inputBytes.length, bytesRead);
            assertArrayEquals(inputBytes, buffer);

            // Test with smaller buffer size
            input.reset();
            buffer = new byte[inputBytes.length - 5];
            bytesRead = IOUtils.readFully(input, buffer);
            assertEquals(buffer.length, bytesRead);
            byte[] expectedOutput = new byte[buffer.length];
            System.arraycopy(inputBytes, 0, expectedOutput, 0, buffer.length);
            assertArrayEquals(expectedOutput, buffer);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testToByteArray() {
        try {
            byte[] inputBytes = "This is a test".getBytes();
            InputStream input = new ByteArrayInputStream(inputBytes);

            byte[] outputBytes = IOUtils.toByteArray(input);
            assertArrayEquals(inputBytes, outputBytes);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCloseQuietly() {
        try {
            Closeable c = new Closeable() {
                @Override
                public void close() throws IOException {
                    throw new IOException("Test IOException");
                }
            };
            
            IOUtils.closeQuietly(c);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
}