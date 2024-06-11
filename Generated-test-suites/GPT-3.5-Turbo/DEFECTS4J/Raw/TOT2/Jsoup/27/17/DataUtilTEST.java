import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws Exception {
        File testFile = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = DataUtil.load(testFile, charset, baseUri);
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testLoadFromInputStream() throws Exception {
        String html = "<html><head></head><body><p>Hello, World!</p></body></html>";
        InputStream stream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = DataUtil.load(stream, charset, baseUri);
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.select("p").text());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws Exception {
        String html = "<html><head></head><body><p>Hello, World!</p></body></html>";
        InputStream stream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(stream, charset, baseUri, parser);
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.select("p").text());
    }

    @Test
    public void testParseByteData() {
        byte[] data = "test".getBytes(StandardCharsets.UTF_8);
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(ByteBuffer.wrap(data), charset, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        byte[] data = "test".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(stream);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}