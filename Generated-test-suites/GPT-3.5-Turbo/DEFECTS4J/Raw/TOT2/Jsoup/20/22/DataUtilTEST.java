import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, "UTF-8", baseUri);
        
        assertNotNull(doc);
        assertEquals("Document base URI should match", baseUri, doc.baseUri());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><head></head><body><p>Hello, world!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, "UTF-8", baseUri);
        
        assertNotNull(doc);
        assertEquals("Document base URI should match", baseUri, doc.baseUri());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String html = "<html><head></head><body><p>Hello, world!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, "UTF-8", baseUri, Parser.xmlParser());
        
        assertNotNull(doc);
        assertEquals("Document base URI should match", baseUri, doc.baseUri());
    }

    @Test
    public void testParseByteData() {
        // Edge case: testing with empty byte buffer
        Document doc = DataUtil.parseByteData(new ByteArrayInputStream(new byte[0]).asReadOnlyBuffer(), "UTF-8", "http://example.com", Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Typical case: contentType contains charset information
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        // Edge case: contentType without charset information
        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}