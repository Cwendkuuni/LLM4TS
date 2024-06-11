import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Test loading a file and parsing it to a Document
        // Typical use case
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        // Test loading input stream and parsing it to a Document
        // Typical use case
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        // Test loading an input stream with a specified parser and parsing it to a Document
        // Typical use case
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testParseByteData() {
        // Test parsing byte data with specified charset and base URI
        // Edge case: null charsetName
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() {
        // Test reading input stream to ByteBuffer
        // Typical use case
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteData);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test extracting charset from content type header
        // Edge case: null content type
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);

        // Error scenario: invalid content type
        contentType = "invalid_content_type";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);

        // Typical use case
        contentType = "text/html; charset=UTF-8";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}