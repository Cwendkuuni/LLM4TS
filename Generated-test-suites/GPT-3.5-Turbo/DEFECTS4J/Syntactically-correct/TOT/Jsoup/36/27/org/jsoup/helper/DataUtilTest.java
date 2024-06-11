import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "https://www.example.com");

        assertNotNull(document);
        // TODO: Assert document content
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com");

        assertNotNull(document);
        // TODO: Assert document content
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String xml = "<xml><root><element>Test</element></root></xml>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com", Parser.xmlParser());

        assertNotNull(document);
        // TODO: Assert document content
    }

    @Test
    public void testParseByteDataWithCharset() {
        // Test case for when charset is provided
        byte[] byteData = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>".getBytes(StandardCharsets.UTF_8);
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(ByteBuffer.wrap(byteData), charsetName, baseUri, parser);

        assertNotNull(document);
        // TODO: Assert document content
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        // Test case for when charset is not provided
        byte[] byteData = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>".getBytes(StandardCharsets.UTF_8);
        String charsetName = null;
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(ByteBuffer.wrap(byteData), charsetName, baseUri, parser);

        assertNotNull(document);
        // TODO: Assert document content
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] testData = "This is a test".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteBuffer);
        assertEquals(testData.length, byteBuffer.limit());
        // TODO: Assert byte buffer content
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test case for valid content type with supported charset
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);

        // Test case for invalid charset
        contentType = "text/html; charset=INVALID";

        charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }
}