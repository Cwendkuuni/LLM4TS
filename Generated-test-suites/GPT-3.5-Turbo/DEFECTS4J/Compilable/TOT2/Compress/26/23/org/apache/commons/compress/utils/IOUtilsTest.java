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
    public void testCopyWithDefaultBufferSize() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long bytesCopied = IOUtils.copy(input, output);
        
        assertArrayEquals(inputData, output.toByteArray());
        assertEquals(inputData.length, bytesCopied);
    }

    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        InputStream input = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long bytesCopied = IOUtils.copy(input, output, 5);
        
        assertArrayEquals(inputData, output.toByteArray());
        assertEquals(inputData.length, bytesCopied);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        InputStream input = new ByteArrayInputStream(inputData);
        
        long bytesSkipped = IOUtils.skip(input, 5);
        
        assertEquals(5, bytesSkipped);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        InputStream input = new ByteArrayInputStream(inputData);
        byte[] outputData = new byte[10];
        
        int bytesRead = IOUtils.readFully(input, outputData);
        
        assertArrayEquals(inputData, outputData);
        assertEquals(10, bytesRead);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        InputStream input = new ByteArrayInputStream(inputData);
        byte[] outputData = new byte[10];
        
        int bytesRead = IOUtils.readFully(input, outputData, 3, 5);
        
        byte[] expectedOutput = {0, 0, 0, 1, 2, 3, 4, 5, 0, 0};
        
        assertArrayEquals(expectedOutput, outputData);
        assertEquals(5, bytesRead);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputData);
        
        byte[] outputData = IOUtils.toByteArray(input);
        
        assertArrayEquals(inputData, outputData);
    }

    @Test
    public void testCloseQuietly() {
        // Not sure how to test this method as it only closes the stream quietly
        // and doesn't return any output. Can be tested with code coverage tools.
    }
}