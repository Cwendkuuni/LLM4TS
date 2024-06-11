import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataNullCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataNonNullCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

}