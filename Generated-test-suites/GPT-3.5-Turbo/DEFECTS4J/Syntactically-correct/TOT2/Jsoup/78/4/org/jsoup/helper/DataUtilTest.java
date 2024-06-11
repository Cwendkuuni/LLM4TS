import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head></head><body><p>Hello, World!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertEquals("Hello, World!", document.select("p").first().text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String xml = "<root></root>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertEquals("root", document.tagName());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String text = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes());
        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(text, new String(buffer.array()));
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String text = "This text is longer than the maximum size specified";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes());
        ByteBuffer buffer = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals(10, buffer.array().length);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

}