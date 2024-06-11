import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
    	// Test loading a file to a Document
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertEquals("Loaded document should not be null", doc != null);
        // Additional test cases for file loading edge cases and exceptions could be added here
    }

    @Test
    public void testLoadInputStream() {
        // Test parsing a Document from an input stream
        InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertEquals("Parsed document should not be null", doc != null);
        // Additional test cases for input stream parsing edge cases and exceptions could be added here
    }

    @Test
    public void testLoadAlternateParser() {
        // Test parsing a Document from an input stream using an alternate parser
        InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertEquals("Parsed document should not be null", doc != null);
        // Additional test cases for alternate parser edge cases and exceptions could be added here
    }

    @Test
    public void testCrossStreams() {
        // Test writing input stream to output stream
        InputStream input = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        DataUtil.crossStreams(input, output);
        assertEquals("Output stream should not be empty", output.size() > 0);
        // Additional test cases for cross streams edge cases and exceptions could be added here
    }

    @Test
    public void testReadToByteBuffer() {
        // Test reading input stream into a byte buffer
        InputStream input = new ByteArrayInputStream("test".getBytes());
        ByteBuffer buffer = DataUtil.readToByteBuffer(input, 10);
        assertEquals("Buffer should not be empty", buffer.capacity() > 0);
        // Additional test cases for reading to byte buffer edge cases and exceptions could be added here
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test getting charset from content type
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("Charset should be UTF-8", "UTF-8", charset);
        // Additional test cases for content type edge cases and exceptions could be added here
    }

    // Additional test cases for other methods in DataUtil class could be added here

}