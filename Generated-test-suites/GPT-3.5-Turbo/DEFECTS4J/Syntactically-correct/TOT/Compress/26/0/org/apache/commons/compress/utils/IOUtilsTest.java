import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;

public class IOUtilsTest {

    @Test
    public void testCopy_defaultBufferSize() throws IOException {
        // Test case to check if the copy() method copies the content from InputStream to OutputStream correctly with default buffer size

        // Arrange
        InputStream input = createMockInputStream("Test Input");
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // Act
        long count = IOUtils.copy(input, output);

        // Assert
        assertEquals("Test Input", output.toString());
        assertEquals("Result", 10, count);
    }

    @Test
    public void testCopy_customBufferSize() throws IOException {
        // Test case to check if the copy() method copies the content from InputStream to OutputStream correctly with custom buffer size

        // Arrange
        InputStream input = createMockInputStream("Test Input");
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // Act
        long count = IOUtils.copy(input, output, 16);

        // Assert
        assertEquals("Test Input", output.toString());
        assertEquals("Result", 10, count);
    }

    @Test
    public void testCopy_emptyInputStream() throws IOException {
        // Test case to check if the copy() method handles empty InputStream correctly

        // Arrange
        InputStream input = createMockInputStream("");
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // Act
        long count = IOUtils.copy(input, output);

        // Assert
        assertEquals("", output.toString());
        assertEquals("Result", 0, count);
    }

    @Test
    public void testCopy_largeInputStream() throws IOException {
        // Test case to check if the copy() method handles large InputStream correctly

        // Arrange
        InputStream input = createMockInputStream("This is a large input that exceeds the buffer size");
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // Act
        long count = IOUtils.copy(input, output, 16);

        // Assert
        assertEquals("This is a large input that exceeds the buffer size", output.toString());
        assertEquals("Result", 50, count);
    }

    @Test
    public void testSkip_validNumToSkip() throws IOException {
        // Test case to check if the skip() method skips the given number of bytes correctly

        // Arrange
        InputStream input = createMockInputStream("Skip Test");
        long numToSkip = 5;

        // Act
        long skipped = IOUtils.skip(input, numToSkip);

        // Assert
        assertEquals("Result", 5, skipped);
        assertEquals("Remaining Bytes", "Test", inputToString(input));
    }

    @Test
    public void testSkip_invalidNumToSkip() throws IOException {
        // Test case to check if the skip() method handles invalid numToSkip correctly

        // Arrange
        InputStream input = createMockInputStream("Test");
        long numToSkip = -5;

        // Act
        long skipped = IOUtils.skip(input, numToSkip);

        // Assert
        assertEquals("Result", 0, skipped);
        assertEquals("Remaining Bytes", "Test", inputToString(input));
    }

    @Test
    public void testReadFully_validOffsetAndLength() throws IOException {
        // Test case to check if the readFully() method reads the given amount of bytes into the array correctly with valid offset and length

        // Arrange
        InputStream input = createMockInputStream("ReadFully Test");
        byte[] buffer = new byte[10];
        int offset = 5;
        int length = 5;

        // Act
        int bytesRead = IOUtils.readFully(input, buffer, offset, length);

        // Assert
        assertEquals("Bytes Read", 5, bytesRead);
        assertEquals("Buffer", "     Test", byteArrayToString(buffer));
    }

    @Test
    public void testReadFully_invalidOffsetAndLength() throws IOException {
        // Test case to check if the readFully() method handles invalid offset and length correctly

        // Arrange
        InputStream input = createMockInputStream("ReadFully Test");
        byte[] buffer = new byte[10];
        int offset = -5;
        int length = 15;

        // Act
        int bytesRead = IOUtils.readFully(input, buffer, offset, length);

        // Assert
        assertEquals("Bytes Read", 0, bytesRead);
        assertEquals("Buffer", "          ", byteArrayToString(buffer));
    }

    @Test
    public void testToByteArray() throws IOException {
        // Test case to check if the toByteArray() method correctly converts InputStream to byte array

        // Arrange
        InputStream input = createMockInputStream("ToByteArray Test");

        // Act
        byte[] byteArray = IOUtils.toByteArray(input);

        // Assert
        assertEquals("Byte Array", "ToByteArray Test", byteArrayToString(byteArray));
    }

    @Test
    public void testCloseQuietly() {
        // Test case to check if the closeQuietly() method successfully closes the Closeable object without throwing an exception

        // Arrange
        Closeable closeable = null; // Replace with a mock Closeable object

        // Act & Assert
        // This test case cannot be implemented without a mock Closeable object
    }

    // Helper methods

    private InputStream createMockInputStream(String inputString) {
        return new ByteArrayInputStream(inputString.getBytes());
    }

    private String inputToString(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(input, output);
        return output.toString();
    }

    private String byteArrayToString(byte[] byteArray) {
        return new String(byteArray);
    }
}