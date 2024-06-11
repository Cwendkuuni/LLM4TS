import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        String filePath = "test.html";
        File file = new File(filePath);
        String charsetName = "UTF-8";
        String baseUri = "/test";

        Document doc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "/test";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "invalidCharset";
        String baseUri = "/test";

        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithNullCharset() {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "/test";

        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithEmptyCharset() {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "";
        String baseUri = "/test";

        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithUnsupportedCharset() {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "x-custom";
        String baseUri = "/test";

        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseByteData() {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "/test";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "invalidCharset";
        String baseUri = "/test";
        Parser parser = Parser.htmlParser();

        Document doc = null;
        try {
            doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        Assert.assertNull(doc);
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        String htmlContent = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "/test";
        Parser parser = Parser.htmlParser();

        Document doc = null;
        try {
            doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        Assert.assertNull(doc);
    }

    @Test
    public void testCrossStreams() {
        String inputContent = "Test content";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String outputContent = outputStream.toString();
        Assert.assertEquals(inputContent, outputContent);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals(DataUtil.defaultCharset, charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        Assert.assertEquals(32, boundary.length());
    }

}