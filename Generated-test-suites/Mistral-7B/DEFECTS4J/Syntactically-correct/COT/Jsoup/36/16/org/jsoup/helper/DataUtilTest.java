import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    private static Document doc;
    private static InputStream inputStream;
    private static File file;

    @BeforeClass
    public static void setUp() {
        // You can put any initialization code here.
    }

    @Test
    public void testLoadFile() throws IOException {
        file = new File("path/to/your/file.html");
        doc = DataUtil.load(file, "UTF-8", "https://example.com");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlString = "<html><body></body></html>";
        inputStream = new ByteArrayInputStream(htmlString.getBytes("UTF-8"));
        doc = DataUtil.load(inputStream, "UTF-8", "https://example.com");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlString = "<html><body></body></html>";
        inputStream = new ByteArrayInputStream(htmlString.getBytes("UTF-8"));
        doc = DataUtil.load(inputStream, "UTF-8", "https://example.com", Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlString = "<html><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlString.getBytes("UTF-8"));
        doc = DataUtil.parseByteData(byteData, "UTF-8", "https://example.com", Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() throws IOException {
        file = new File("path/to/your/file.html");
        DataUtil.load(file, null, "https://example.com");
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String htmlString = "<html><body></body></html>";
        inputStream = new ByteArrayInputStream(htmlString.getBytes("UTF-8"));
        DataUtil.load(inputStream, null, "https://example.com");
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        file = new File("path/to/your/file.html");
        DataUtil.load(file, "GBK_INVALID", "https://example.com");
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlString = "<html><body></body></html>";
        inputStream = new ByteArrayInputStream(htmlString.getBytes("UTF-8"));
        DataUtil.load(inputStream, "GBK_INVALID", "https://example.com");
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithEmptyContentType() {
        String contentType = "";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=GBK_INVALID";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlString = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlString.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteData);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithNullInputStream() throws IOException {
        DataUtil.readToByteBuffer(null);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String htmlString = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlString.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 1024);
        Assert.assertNotNull(byteData);
    }
}