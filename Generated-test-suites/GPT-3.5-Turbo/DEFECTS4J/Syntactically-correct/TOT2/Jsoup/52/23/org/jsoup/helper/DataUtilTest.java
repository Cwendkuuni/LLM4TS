import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() {
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document document = DataUtil.load(file, charset, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadFromInputStream() {
        String html = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document document = DataUtil.load(inputStream, charset, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        String html = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        try {
            Document document = DataUtil.load(inputStream, charset, baseUri, parser);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCrossStreams() {
        // Test for reading and writing streams
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DataUtil.crossStreams(inputStream, outputStream);
            assertEquals("test", outputStream.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test for parsing charset from content type
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    // More test cases for other methods can be added here

}