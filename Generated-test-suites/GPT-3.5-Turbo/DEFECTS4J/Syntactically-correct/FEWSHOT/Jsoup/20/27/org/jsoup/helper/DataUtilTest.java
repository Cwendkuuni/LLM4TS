import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {
    @Test
    public void testLoadFromFile() throws IOException {
        String baseUri = "http://example.com";
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-16", baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String baseUri = "http://example.com";
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        Document doc = DataUtil.load(inputStream, "UTF-8", baseUri);
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String baseUri = "http://example.com";
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        Document doc = DataUtil.load(inputStream, "UTF-8", baseUri, Parser.xmlParser());
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseByteDataWithCharset() {
        String baseUri = "http://example.com";
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello, World!</h1></body></html>";
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteData = ByteBuffer.wrap(bytes);
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        String baseUri = "http://example.com";
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body><h1>Hello, World!</h1></body></html>";
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteData = ByteBuffer.wrap(bytes);
        Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
        assertEquals(bytes.length, byteData.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}