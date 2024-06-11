import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<!DOCTYPE html><html><head></head><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testParseByteData() {
        String html = "<!DOCTYPE html><html><head></head><body><h1>Hello World!</h1></body></html>";
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        Document document = DataUtil.parseByteData(ByteBuffer.wrap(bytes), "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<!DOCTYPE html><html><head></head><body><h1>Hello World!</h1></body></html>";
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(bytes);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(bytes.length, byteBuffer.array().length);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}