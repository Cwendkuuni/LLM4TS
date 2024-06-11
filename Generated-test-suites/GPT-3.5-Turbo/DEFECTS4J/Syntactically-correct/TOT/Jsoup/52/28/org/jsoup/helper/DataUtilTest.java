import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.DataUtil;

public class DataUtilTest {

// Testing load(File in, String charsetName, String baseUri) method
    @Test
    public void testLoadFile() {
        try {
            Document doc = DataUtil.load(new File("example.html"), "UTF-8", "http://example.com");
            assertNotNull(doc);
            // Add more assertions for typical use cases and edge cases
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadFileInvalidFile() {
        try {
            Document doc = DataUtil.load(new File("nonexistent.html"), "UTF-8", "http://example.com");
            fail("IOException should be thrown");
        } catch (IOException e) {
            // Expected exception
        }
    }

// Testing load(InputStream in, String charsetName, String baseUri) method
    @Test
    public void testLoadInputStream() {
        try {
            InputStream in = getClass().getResourceAsStream("example.html");
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
            assertNotNull(doc);
            // Add more assertions for typical use cases and edge cases
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStreamInvalidStream() {
        try {
            InputStream in = null; // Invalid input stream
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
            fail("IOException should be thrown");
        } catch (IOException e) {
            // Expected exception
        }
    }

// Testing load(InputStream in, String charsetName, String baseUri, Parser parser) method
    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream in = getClass().getResourceAsStream("example.html");
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(doc);
            // Add more assertions for typical use cases and edge cases
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParserInvalidStream() {
        try {
            InputStream in = null; // Invalid input stream
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.htmlParser());
            fail("IOException should be thrown");
        } catch (IOException e) {
            // Expected exception
        }
    }

// Testing crossStreams(InputStream in, OutputStream out) method
    @Test
    public void testCrossStreams() {
        try {
            InputStream in = getClass().getResourceAsStream("example.html");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataUtil.crossStreams(in, out);
            // Add assertions to verify the correctness of the output stream
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCrossStreamsInvalidInputStream() {
        try {
            InputStream in = null; // Invalid input stream
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataUtil.crossStreams(in, out);
            fail("IOException should be thrown");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testCrossStreamsInvalidOutputStream() {
        try {
            InputStream in = getClass().getResourceAsStream("example.html");
            OutputStream out = null; // Invalid output stream
            DataUtil.crossStreams(in, out);
            fail("IOException should be thrown");
        } catch (IOException e) {
            // Expected exception
        }
    }

// Add similar test cases for the remaining methods in the DataUtil class

}