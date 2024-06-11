import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, "UTF-8", baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, "UTF-8", baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, "UTF-8", baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    // Edge case: Charset name is null
    @Test
    public void testParseByteDataCharsetNull() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("test data".getBytes());
        Document document = DataUtil.parseByteData(byteBuffer, null, "http://example.com", Parser.htmlParser());
        assertNotNull(document);
    }

    // Edge case: Charset name is specified
    @Test
    public void testParseByteDataCharsetSpecified() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("test data".getBytes());
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
    }

    // Error scenario: Unable to extract charset from content type
    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String charset = DataUtil.getCharsetFromContentType("text/html");
        assertEquals(null, charset);
    }

    // Error scenario: Unsupported charset
    @Test
    public void testGetCharsetFromContentTypeUnsupportedCharset() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=unsupportedCharset");
        assertEquals(null, charset);
    }

}