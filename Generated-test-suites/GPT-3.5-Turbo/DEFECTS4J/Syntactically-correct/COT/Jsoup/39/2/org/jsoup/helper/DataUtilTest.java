
import org.junit.Assert;
import org.junit.Test;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        // Test file loading
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            // Assert statements for verifying functionality
            Assert.assertNotNull(doc);
            Assert.assertEquals("https://example.com", doc.baseUri());
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStream() {
        // Test InputStream loading
        InputStream in = new ByteArrayInputStream("<html><body><h1>Hello World</h1></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        try {
            Document doc = DataUtil.load(in, charsetName, baseUri);
            // Assert statements for verifying functionality
            Assert.assertNotNull(doc);
            Assert.assertEquals("https://example.com", doc.baseUri());
            Assert.assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        // Test InputStream loading with custom parser
        InputStream in = new ByteArrayInputStream("<html><body><h1>Hello World</h1></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.xmlParser();
        try {
            Document doc = DataUtil.load(in, charsetName, baseUri, parser);
            // Assert statements for verifying functionality
            Assert.assertNotNull(doc);
            Assert.assertEquals("https://example.com", doc.baseUri());
            Assert.assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void testParseByteData() {
        // Test parsing byte data
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>Hello World</h1></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        // Assert statements for verifying functionality
        Assert.assertNotNull(doc);
        Assert.assertEquals("https://example.com", doc.baseUri());
        Assert.assertEquals("<html><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testReadToByteBuffer() {
        // Test reading input stream into byte buffer
        InputStream in = new ByteArrayInputStream("<html><body><h1>Hello World</h1></body></html>".getBytes());
        int maxSize = 1024;
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(in, maxSize);
            // Assert statements for verifying functionality
            Assert.assertNotNull(byteData);
            Assert.assertEquals("<html><body><h1>Hello World</h1></body></html>", new String(byteData.array()));
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Test getting charset from content type
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        // Assert statement for verifying functionality
        Assert.assertEquals("UTF-8", charset);
    }

}
