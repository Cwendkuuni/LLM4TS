import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() {
        // Typical use case
        // Test loading a file into Document
    }

    @Test
    public void testLoadFromInputStream() {
        // Typical use case
        // Test loading from input stream into Document
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        // Typical use case
        // Test loading from input stream into Document with custom parser
    }

    @Test
    public void testCrossStreams() {
        // Typical use case
        // Test writing input stream to output stream
    }

    @Test
    public void testReadToByteBuffer() {
        // Edge case
        // Test reading input stream into byte buffer with maxSize set to 0
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Edge case
        // Test getting charset from content type header
    }

    @Test
    public void testValidateCharset() {
        // Edge case
        // Test validating charset
    }

    @Test
    public void testMimeBoundary() {
        // Typical use case
        // Test creating random mime boundary string
    }

    @Test
    public void testDetectCharsetFromBom() {
        // Error scenario
        // Test detecting charset from BOM
    }

}