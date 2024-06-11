import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
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
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFromFileWithValidArguments() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithValidArguments() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithNullCharsetName() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String baseUri = "http://test.com";
        try {
            Document doc = DataUtil.load(inputStream, null, baseUri);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Charset name cannot be null.", e.getMessage());
        }
    }

    @Test
    public void testLoadFromInputStreamWithEmptyCharsetName() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String baseUri = "http://test.com";
        try {
            Document doc = DataUtil.load(inputStream, "", baseUri);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Charset name cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void testLoadFromInputStreamWithInvalidCharsetName() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String baseUri = "http://test.com";
        try {
            Document doc = DataUtil.load(inputStream, "invalidCharset", baseUri);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().contains("Invalid charset name"));
        }
    }

    @Test
    public void testLoadFromInputStreamWithNullBaseUri() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        try {
            Document doc = DataUtil.load(inputStream, charsetName, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Base URI cannot be null.", e.getMessage());
        }
    }

    @Test
    public void testLoadFromInputStreamWithEmptyBaseUri() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithValidArguments() throws IOException {
        String html = "<html><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithNullCharsetName() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body></body></html>".getBytes("UTF-8"));
        String baseUri = "http://test.com";
        try {
            Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Charset name cannot be null.", e.getMessage());
        }
    }

    @Test
    public void testParseByteDataWithEmptyCharsetName() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body></body></html>".getBytes("UTF-8"));
        String baseUri = "http://test.com";
        try {
            Document doc = DataUtil.parseByteData(byteData, "", baseUri, Parser.htmlParser());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Charset name cannot be empty.", e.getMessage());
        }
    }

    @Test
    public void testParseByteDataWithInvalidCharsetName() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body></body></html>".getBytes("UTF-8"));
        String baseUri = "http://test.com";
        try {
            Document doc = DataUtil.parseByteData(byteData, "invalidCharset", baseUri, Parser.htmlParser());
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().contains("Invalid charset name"));
        }
    }

    @Test
    public void testReadToByteBufferWithValidArguments() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithNullInputStream() {
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("InputStream cannot be null.", e.getMessage());
        }
    }
}