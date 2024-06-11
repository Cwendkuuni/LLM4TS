import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(document);
            // Add more assertions to validate the document
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream() {
        String html = "<html><body><h1>Title</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(document);
            // Add more assertions to validate the document
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        String html = "<html><body><h1>Title</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(document);
            // Add more assertions to validate the document
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        String data = "Sample data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        int maxSize = 20;
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
            assertNotNull(byteBuffer);
            // Add more assertions to validate the byte buffer
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() {
        String data = "Sample data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteBuffer);
            // Add more assertions to validate the byte buffer
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);

        contentType = "text/html; charset=ISO-8859-1";
        expectedCharset = "ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);

        // Test unsupported charset
        contentType = "text/html; charset=invalidCharset";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);

        // Test invalid content type format
        contentType = "text/html;charset=invalidCharset";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}