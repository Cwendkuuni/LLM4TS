import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoad_withValidFile_ShouldReturnDocument() {
        File file = new File("test.html");
        try {
            Document document = DataUtil.load(file, StandardCharsets.UTF_8.name(), "https://example.com");
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException thrown");
        }
    }

    @Test
    public void testLoad_withInvalidFile_ShouldThrowIOException() {
        File file = new File("invalid.html");
        try {
            DataUtil.load(file, StandardCharsets.UTF_8.name(), "https://example.com");
            Assert.fail("IOException not thrown");
        } catch (IOException e) {
            Assert.assertEquals("File not found", e.getMessage());
        }
    }

    @Test
    public void testLoad_withValidInputStream_ShouldReturnDocument() {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        try {
            Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "https://example.com");
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException thrown");
        }
    }

    @Test
    public void testLoad_withInvalidInputStream_ShouldThrowIOException() {
        InputStream inputStream = getClass().getResourceAsStream("/invalid.html");
        try {
            DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "https://example.com");
            Assert.fail("IOException not thrown");
        } catch (IOException e) {
            Assert.assertEquals("Invalid input stream", e.getMessage());
        }
    }

    @Test
    public void testLoad_withValidInputStreamAndParser_ShouldReturnDocument() {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        try {
            Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "https://example.com", Parser.htmlParser());
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException thrown");
        }
    }

    @Test
    public void testParseByteData_withValidData_ShouldReturnDocument() {
        String data = "<html><head></head><body><h1>Test</h1></body></html>";
        byte[] byteData = data.getBytes(StandardCharsets.UTF_8);
        Document document = DataUtil.parseByteData(ByteBuffer.wrap(byteData), StandardCharsets.UTF_8.name(), "https://example.com", Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("Test", document.select("h1").text());
    }

    @Test
    public void testGetCharsetFromContentType_withValidContentType_ShouldReturnCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_withInvalidContentType_ShouldReturnNull() {
        String contentType = "text/html; charset=INVALID";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

}