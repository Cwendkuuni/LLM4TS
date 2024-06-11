import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws Exception {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws Exception {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inputStream, "UTF-8", "", parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithCharset() throws Exception {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithoutCharset() throws Exception {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 100);
        assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithUnlimitedSize() throws Exception {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() throws Exception {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() throws Exception {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromNullContentType() throws Exception {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}