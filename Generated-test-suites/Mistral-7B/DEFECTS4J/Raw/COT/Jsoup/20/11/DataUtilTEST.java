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
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://test.com");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFromNullFile() {
        File file = null;
        try {
            Document doc = DataUtil.load(file, "UTF-8", "http://test.com");
            Assert.assertNull(doc);
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://test.com");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, null, "http://test.com");
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFromInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, "InvalidCharset", "http://test.com");
    }

    @Test
    public void testParseByteData() {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes());
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "http://test.com", Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes());
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        Document doc = DataUtil.parseByteData(byteData, null, "http://test.com", Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes());
        ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
        Document doc = DataUtil.parseByteData(byteData, "InvalidCharset", "http://test.com", Parser.htmlParser());
        Assert.assertNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(html.getBytes());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithNullInputStream() {
        InputStream inputStream = null;
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(DataUtil.defaultCharset, charset);
    }
}