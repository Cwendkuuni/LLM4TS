import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() {
        File file = new File("test.html");
        try {
            Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadFromInputStream() {
        InputStream in = new ByteArrayInputStream("test data".getBytes());
        try {
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        InputStream in = new ByteArrayInputStream("test data".getBytes());
        try {
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() {
        InputStream in = new ByteArrayInputStream("test data".getBytes());
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(in);
            assertNotNull(byteData);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}