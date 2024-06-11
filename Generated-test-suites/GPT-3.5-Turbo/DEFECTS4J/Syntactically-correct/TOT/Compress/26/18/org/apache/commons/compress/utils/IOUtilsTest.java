import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class IOUtilsTest {
    
    private ByteArrayInputStream inputStream;
    private ByteArrayOutputStream outputStream;
    
    @Before
    public void setup() {
        String testString = "This is a test string";
        inputStream = new ByteArrayInputStream(testString.getBytes());
        outputStream = new ByteArrayOutputStream();
    }
    
    @Test
    public void testCopyWithDefaultBufferSize() {
        try {
            long count = IOUtils.copy(inputStream, outputStream);
            assertEquals("The count of copied bytes should be equal to the size of the test string", inputStream.available(), count);
            assertEquals("The content of the output stream should be equal to the test string", "This is a test string", outputStream.toString());
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
    
    @Test
    public void testCopyWithSpecifiedBufferSize() {
        try {
            long count = IOUtils.copy(inputStream, outputStream, 2048);
            assertEquals("The count of copied bytes should be equal to the size of the test string", inputStream.available(), count);
            assertEquals("The content of the output stream should be equal to the test string", "This is a test string", outputStream.toString());
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
    
    @Test
    public void testSkip() {
        try {
            long skipped = IOUtils.skip(inputStream, 5);
            assertEquals("The skipped bytes should be equal to the skipped parameter", 5, skipped);
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
    
    @Test
    public void testReadFully() {
        byte[] buffer = new byte[10];
        try {
            int bytesRead = IOUtils.readFully(inputStream, buffer);
            assertEquals("The number of bytes read should be equal to the buffer size", 10, bytesRead);
            assertEquals("The contents of the buffer should be equal to the first 10 characters of the test string", "This is a ", new String(buffer));
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
    
    @Test
    public void testReadFullyWithOffsetAndLength() {
        byte[] buffer = new byte[20];
        try {
            int bytesRead = IOUtils.readFully(inputStream, buffer, 5, 10);
            assertEquals("The number of bytes read should be equal to the length parameter", 10, bytesRead);
            assertEquals("The contents of the buffer from offset 5 should be equal to the first 10 characters of the test string", "     This i", new String(buffer));
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
    
    @Test
    public void testToByteArray() {
        try {
            byte[] byteArray = IOUtils.toByteArray(inputStream);
            assertEquals("The length of the byte array should be equal to the size of the test string", "This is a test string".length(), byteArray.length);
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
    
    @Test
    public void testCloseQuietly() {
        IOUtils.closeQuietly(inputStream);
        assertNull("The input stream should be null after closing it", inputStream);
    }
}