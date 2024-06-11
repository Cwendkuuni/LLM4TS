import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class IOUtilsTest {

    private final byte[] testData = "Hello, World!".getBytes();

    @Test
    public void testCopy() {
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(testData);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            long bytesCopied = IOUtils.copy(input, output);
            assertEquals(testData.length, bytesCopied);
            assertEquals("Hello, World!", output.toString());
        } catch (IOException e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testCopyWithBufferSize() {
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(testData);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            long bytesCopied = IOUtils.copy(input, output, 5);
            assertEquals(testData.length, bytesCopied);
            assertEquals("Hello, World!", output.toString());
        } catch (IOException e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testSkip() {
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(testData);
            long bytesSkipped = IOUtils.skip(input, 5);
            assertEquals(5, bytesSkipped);
        } catch (IOException e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testReadFully() {
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(testData);
            byte[] buffer = new byte[5];
            int bytesRead = IOUtils.readFully(input, buffer);
            assertEquals(5, bytesRead);
            assertEquals("Hello", new String(buffer));
        } catch (IOException e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testReadFullyWithOffsetAndLength() {
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(testData);
            byte[] buffer = new byte[5];
            int bytesRead = IOUtils.readFully(input, buffer, 0, 3);
            assertEquals(3, bytesRead);
            assertEquals("Hel", new String(buffer, 0, bytesRead));
        } catch (IOException e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testToByteArray() {
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(testData);
            byte[] result = IOUtils.toByteArray(input);
            assertEquals(testData.length, result.length);
            assertTrue(java.util.Arrays.equals(result, testData));
        } catch (IOException e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testCloseQuietly() {
        try {
            Closeable closeable = new Closeable() {
                @Override
                public void close() throws IOException {
                    throw new IOException("Test exception");
                }
            };
            IOUtils.closeQuietly(closeable);
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }
}