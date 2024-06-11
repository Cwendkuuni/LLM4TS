
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            Document document = DataUtil.load(new File("test.html"), "UTF-8", "http://example.com");
            assertNotNull(document);
            assertEquals("http://example.com", document.baseUri());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream in = new FileInputStream("test.html");
            Document document = DataUtil.load(in, "UTF-8", "http://example.com");
            assertNotNull(document);
            assertEquals("http://example.com", document.baseUri());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream in = new FileInputStream("test.html");
            Document document = DataUtil.load(in, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(document);
            assertEquals("http://example.com", document.baseUri());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    // Test edge case where charset is not specified
    @Test
    public void testParseByteDataWithNoCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><title>Test</title></head><body></body></html>".getBytes());
        Document document = DataUtil.parseByteData(byteData, null, "http://example.com", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    // Test edge case where charset is specified by content type header
    @Test
    public void testParseByteDataWithCharsetFromContentType() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><title>Test</title></head><body></body></html>".getBytes());
        Document document = DataUtil.parseByteData(byteData, "ISO-8859-1", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    // Test exception handling when reading stream with specified max size
    @Test
    public void testReadToByteBufferWithMaxSize() {
        try {
            FileInputStream inStream = new FileInputStream("test.html");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, 1024);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    // Test exception handling when reading stream without specifying max size
    @Test
    public void testReadToByteBufferWithoutMaxSize() {
        try {
            FileInputStream inStream = new FileInputStream("test.html");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    // Test extraction of charset from content type header
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=utf-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

}

