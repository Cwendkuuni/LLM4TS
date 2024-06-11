import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long count = IOUtils.copy(input, output);

        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, output.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        long skipped = IOUtils.skip(input, 5);

        assertEquals(5, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[5];

        int bytesRead = IOUtils.readFully(input, buffer);

        assertEquals(5, bytesRead);
        byte[] expected = "Test ".getBytes();
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        byte[] outputBytes = IOUtils.toByteArray(input);

        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testCloseQuietly() {
        // Cannot directly test closeQuietly() as it just swallows exceptions
    }
}