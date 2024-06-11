import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class IOUtilsTest {

    // Test copy method with default buffer size
    @Test
    public void testCopyDefaultBufferSize() throws IOException {
        String inputString = "Test Input String";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        long bytesCopied = IOUtils.copy(inputStream, outputStream);
        assertEquals(inputString.length(), bytesCopied);
        assertEquals(inputString, outputStream.toString());
    }

    // Test copy method with custom buffer size
    @Test
    public void testCopyCustomBufferSize() throws IOException {
        String inputString = "Test Input String";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int bufferSize = 4;
        long bytesCopied = IOUtils.copy(inputStream, outputStream, bufferSize);
        assertEquals(inputString.length(), bytesCopied);
        assertEquals(inputString, outputStream.toString());
    }

    // Test skip method
    @Test
    public void testSkip() throws IOException {
        String inputString = "Test Input String";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        long numSkipped = IOUtils.skip(inputStream, 5);
        assertEquals(5, numSkipped);
        assertEquals("Input String", inputStream.toString());
    }

    // Test readFully method
    @Test
    public void testReadFully() throws IOException {
        String inputString = "Test Input String";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        byte[] buffer = new byte[inputString.length()];
        int bytesRead = IOUtils.readFully(inputStream, buffer);
        assertEquals(inputString.length(), bytesRead);
        assertEquals(inputString, new String(buffer));
    }

    // Test readFully method with offset and length specified
    @Test
    public void testReadFullyOffsetAndLength() throws IOException {
        String inputString = "Test Input String";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        byte[] buffer = new byte[inputString.length()];
        int bytesRead = IOUtils.readFully(inputStream, buffer, 5, 5);
        assertEquals(5, bytesRead);
        assertEquals("Input", new String(buffer, 5, 5));
    }

    // Test toByteArray method
    @Test
    public void testToByteArray() throws IOException {
        String inputString = "Test Input String";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        byte[] byteArray = IOUtils.toByteArray(inputStream);
        assertEquals(inputString, new String(byteArray));
    }

    // Test closeQuietly method
    @Test
    public void testCloseQuietly() {
        // Closeable resource
        // This test should not throw any exceptions
        Closeable closeable = () -> {};;
        IOUtils.closeQuietly(closeable);

        // Null resource
        // This test should not throw any exceptions
        Closeable nullCloseable = null;
        IOUtils.closeQuietly(nullCloseable);
    }

}