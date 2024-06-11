import static org.junit.Assert.*;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class IOUtilsTest {

    @Test
    public void testCopyMethod() throws IOException {
        // Basic test case for IOUtils.copy() method
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        IOUtils.copy(inputStream, outputStream);
        assertArrayEquals("Test Data".getBytes(), outputStream.toByteArray());
    }

    // ... test cases for copy() method with edge cases and exception handling ...

    @Test
    public void testCopyMethodWithCustomBufferSize() throws IOException {
        // Basic test case for IOUtils.copy() method with custom buffer size
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        long length = IOUtils.copy(inputStream, outputStream, 1024);
        assertArrayEquals("Test Data".getBytes(), outputStream.toByteArray());
        assertEquals(23, length); // 23 bytes = size of "Test Data"
    }

    // ... test cases for copy() method with edge cases and exception handling (continued) ...

    @Test
    public void testSkipMethod() throws IOException {
        // IOUtils.skip() method test
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        long bytesToSkip = 10;
        long skippedBytes = IOUtils.skip(inputStream, bytesToSkip);
        assertEquals(bytesToSkip, skippedBytes);
        assertArrayEquals("Test Data".getBytes(), inputStream.readAllBytes());
    }

    // ... test cases for skip() method with edge cases and exception handling ...

    @Test
    public void testSkipMoreBytesThanInputStream() throws IOException {
        // Edge case: Testing when the number of bytes to skip is greater than the input stream size
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        long bytesToSkip = inputStream.available() + 10;
        long skippedBytes = IOUtils.skip(inputStream, bytesToSkip);
        assertEquals(inputStream.available(), skippedBytes);
    }

    // ... test cases for skip() method with edge cases and exception handling (continued) ...

    @Test
    public void testReadFullyMethod() throws IOException {
        // IOUtils.readFully() method test
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        byte[] buffer = new byte[10];
        int bytesRead = IOUtils.readFully(inputStream, buffer);
        assertArrayEquals("Test Data".substring(0, bytesRead).getBytes(), buffer, 0, bytesRead);
    }

    // ... test cases for readFully() method with edge cases and exception handling ...

    @Test
    public void testReadFullyMoreBytesThanStreamSize() throws IOException {
        // Edge case: Testing when the number of bytes to read is greater than the input stream size
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        byte[] buffer = new byte[inputStream.available() + 10];
        int bytesRead = IOUtils.readFully(inputStream, buffer);
        assertEquals(inputStream.available(), bytesRead);
    }

    // ... test cases for readFully() method with edge cases and exception handling (continued) ...

    @Test
    public void testCloseQuietly() {
        // Testing IOUtils.closeQuietly() method
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.closeQuietly(outputStream);
    }

    // ... test cases for closeQuietly() method with edge cases and exception handling ...

    @Test
    public void testToByteArray() throws IOException {
        // IOUtils.toByteArray() method test
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        byte[] result = IOUtils.toByteArray(inputStream);
        assertArrayEquals("Test Data".getBytes(), result);
    }

    // ... test cases for toByteArray() method with edge cases and exception handling ...
}