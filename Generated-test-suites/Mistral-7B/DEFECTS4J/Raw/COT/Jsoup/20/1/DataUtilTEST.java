import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace with the path to a valid HTML file
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamUtf8() throws IOException {
        String htmlContent = "HTML content here...";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamNullCharset() throws IOException {
        String htmlContent = "HTML content here...";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
        Document doc = DataUtil.load(inputStream, null, "https://example.com");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamCustomCharset() throws IOException {
        String htmlContent = "HTML content here...";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
        String charsetName = "GBK";
        String baseUri = "https://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlContent = "HTML content here...";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataInvalidCharset() throws IOException {
        String htmlContent = "HTML content here...";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(Charset.forName("UTF-8")));
        String charsetName = null;
        String baseUri = "https://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataCustomCharset() throws IOException {
        String htmlContent = "HTML content here...";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(Charset.forName("UTF-8")));
        String charsetName = "GBK";
        String baseUri = "https://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "HTML content here...";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("EUC-JP", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }
}