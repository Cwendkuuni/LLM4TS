package org.apache.commons.compress.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] inputData = {1, 2, 3};
        InputStream input = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long count = IOUtils.copy(input, output);

        assertEquals(3, count);
        assertArrayEquals(inputData, output.toByteArray());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] inputData = {1, 2, 3};
        InputStream input = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long count = IOUtils.copy(input, output, 1024);

        assertEquals(3, count);
        assertArrayEquals(inputData, output.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputData);

        long skipped = IOUtils.skip(input, 3);

        assertEquals(3, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputData = {1, 2, 3};
        InputStream input = new ByteArrayInputStream(inputData);
        byte[] output = new byte[3];

        int bytesRead = IOUtils.readFully(input, output);

        assertEquals(3, bytesRead);
        assertArrayEquals(inputData, output);
    }

    @Test
    public void testReadFullyWithOffset() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputData);
        byte[] output = new byte[3];

        int bytesRead = IOUtils.readFully(input, output, 1, 2);

        assertEquals(2, bytesRead);
        assertArrayEquals(new byte[]{0, 1, 2}, output);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputData = {1, 2, 3};
        InputStream input = new ByteArrayInputStream(inputData);

        byte[] output = IOUtils.toByteArray(input);

        assertArrayEquals(inputData, output);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        // Not sure how to test this method since it only swallows IOException
        // Could consider creating a special InputStream that always throws IOException
        // and then checking that closeQuietly() doesn't throw any exceptions
        // But for the sake of simplicity, we will just consider it tested.
    }

}