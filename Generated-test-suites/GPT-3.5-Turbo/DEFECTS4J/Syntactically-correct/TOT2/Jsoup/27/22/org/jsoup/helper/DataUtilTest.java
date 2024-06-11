import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws Exception {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws Exception {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        InputStream inputStream = new FileInputStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteData() throws Exception {
        String html = "<html><head></head><body><p>Hello, World!</p></body></html>";
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        Document doc = DataUtil.parseByteData(ByteBuffer.wrap(bytes), "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        File file = new File("test.html");
        FileInputStream inputStream = new FileInputStream(file);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() throws Exception {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        String contentTypeNoCharset = "text/html";
        String charsetNoCharset = DataUtil.getCharsetFromContentType(contentTypeNoCharset);
        assertEquals(null, charsetNoCharset);
    }

}