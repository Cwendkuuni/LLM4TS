import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(JUnit4.class)
public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharsetAndBaseUri() throws Exception {
        File inputFile = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputFile, charset, baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidFile() throws Exception {
        File inputFile = new File("invalidFile.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(inputFile, charset, baseUri);
    }

    @Test
    public void testLoadInputStreamWithValidCharsetAndBaseUri() throws Exception {
        String inputStreamContent = "HTML content...";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName("UTF-8")));
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charset, baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidInputStream() throws Exception {
        InputStream inputStream = null;
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(inputStream, charset, baseUri);
    }

    @Test
    public void testLoadInputStreamWithNullCharsetAndBaseUri() throws Exception {
        String inputStreamContent = "HTML content...";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName("UTF-8")));
        Document document = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithValidCharsetAndBaseUri() throws Exception {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        ByteBuffer byteData = ByteBuffer.allocate(100);
        byteData.put("HTML content...".getBytes(Charset.forName(charset)));
        byteData.flip();
        Document document = DataUtil.parseByteData(byteData, charset, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithNullCharsetAndBaseUri() throws Exception {
        ByteBuffer byteData = ByteBuffer.allocate(100);
        byteData.put("HTML content...".getBytes(Charset.forName("UTF-8")));
        byteData.flip();
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBufferWithValidInputStream() throws Exception {
        String inputStreamContent = "HTML content...";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName("UTF-8")));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithNullInputStream() throws Exception {
        ByteBuffer byteData = DataUtil.readToByteBuffer(null);
        Assert.assertNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentTypeWithValidContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }
}