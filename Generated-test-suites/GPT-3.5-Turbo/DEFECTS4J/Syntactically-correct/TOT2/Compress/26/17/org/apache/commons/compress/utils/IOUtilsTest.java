import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class IOUtilsTest {

    @Test
    public void testCopy() {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            long bytesCopied = IOUtils.copy(input, output);
            assertArrayEquals(inputBytes, output.toByteArray());
            assertEquals(inputBytes.length, bytesCopied);
        } catch (IOException e) {
            fail("IOException thrown while copying input stream to output stream");
        }
    }

    @Test
    public void testSkip() {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        try {
            long bytesSkipped = IOUtils.skip(input, 2);
            assertEquals(2, bytesSkipped);
        } catch (IOException e) {
            fail("IOException thrown while skipping bytes in input stream");
        }
    }

    @Test
    public void testReadFully() {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[3];

        try {
            int bytesRead = IOUtils.readFully(input, outputBytes);
            assertArrayEquals(new byte[]{1, 2, 3}, outputBytes);
            assertEquals(3, bytesRead);
        } catch (IOException e) {
            fail("IOException thrown while reading fully from input stream");
        }
    }

    @Test
    public void testToByteArray() {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        try {
            byte[] outputBytes = IOUtils.toByteArray(input);
            assertArrayEquals(inputBytes, outputBytes);
        } catch (IOException e) {
            fail("IOException thrown while converting input stream to byte array");
        }
    }
    
    @Test
    public void testCloseQuietly() {
        // Not sure how to test this method as it only has side effects
        // such as closing a stream, without any return values or state changes
        // So, we just test if it doesn't throw any exceptions
        try {
            IOUtils.closeQuietly(null);
        } catch (Exception e) {
            fail("Exception thrown while closing Closeable");
        }
    }
}