import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadWithFile() {
        File file = new File("test.html");

        try {
            Document document = DataUtil.load(file, StandardCharsets.UTF_8.name(), "");
            assertNotNull(document);
            assertEquals("Title", document.title());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoadWithInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "");
            assertNotNull(document);
            assertEquals("Title", document.title());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoadWithParser() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "", Parser.htmlParser());
            assertNotNull(document);
            assertEquals("Title", document.title());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testParseByteDataWithCharsetNull() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("<html><head><title>Test</title></head><body></body></html>".getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";

        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, Parser.htmlParser());

        assertNotNull(document);
        assertEquals("Test", document.title());
    }

    @Test
    public void testParseByteDataWithCharsetName() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("<html><head><title>Test</title></head><body></body></html>".getBytes());
        String charsetName = StandardCharsets.UTF_8.name();
        String baseUri = "http://example.com";

        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, Parser.htmlParser());

        assertNotNull(document);
        assertEquals("Test", document.title());
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

            assertNotNull(byteBuffer);
            assertTrue(byteBuffer.capacity() > 0);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentTypeWithCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
}