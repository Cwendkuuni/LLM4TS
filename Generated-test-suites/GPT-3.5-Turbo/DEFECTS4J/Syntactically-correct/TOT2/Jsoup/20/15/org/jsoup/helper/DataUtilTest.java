import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
    public void testLoadFromFile() {
        try {
            File file = new File("test.html");
            Document document = DataUtil.load(file, "UTF-8", "http://example.com");
            assertNotNull(document);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoadFromInputStream() {
        try {
            String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
            InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
            Document document = DataUtil.load(inputStream, null, "http://example.com");
            assertNotNull(document);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        try {
            String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
            InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
            Document document = DataUtil.load(inputStream, null, "http://example.com", Parser.xmlParser());
            assertNotNull(document);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testParseByteData() {
        try {
            StringBuffer html = new StringBuffer("<html><head><meta charset=\"utf-8\"></head><body><p>Hello World!</p></body></html>");
            Document document = DataUtil.parseByteData(html.toString());
            assertNotNull(document);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            String content = "This is a test string";
            InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
            assertNotNull(DataUtil.readToByteBuffer(inputStream));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html; charset=ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}