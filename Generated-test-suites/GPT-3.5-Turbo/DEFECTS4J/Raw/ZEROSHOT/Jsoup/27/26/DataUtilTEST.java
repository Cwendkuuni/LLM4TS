import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class DataUtilTest {

    // Test load(File in, String charsetName, String baseUri)
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "https://www.example.com");
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
    }

    // Test load(InputStream in, String charsetName, String baseUri)
    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        InputStream stream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(stream, "UTF-8", "https://www.example.com");
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("Test", doc.title());
    }

    // Test load(InputStream in, String charsetName, String baseUri, Parser parser)
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><element>Test</element></root>";
        InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(stream, "UTF-8", "https://www.example.com", Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("root", doc.tagName());
    }

    // Test parseByteData(ByteBuffer byteData, String charsetName, String baseUri, Parser parser)
    @Test
    public void testParseByteData() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "https://www.example.com", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("Test", doc.title());
    }

    // Test readToByteBuffer(InputStream inStream)
    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        InputStream stream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(stream);
        assertNotNull(byteData);
        assertEquals(html, new String(byteData.array(), StandardCharsets.UTF_8));
    }

    // Test getCharsetFromContentType(String contentType)
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
}