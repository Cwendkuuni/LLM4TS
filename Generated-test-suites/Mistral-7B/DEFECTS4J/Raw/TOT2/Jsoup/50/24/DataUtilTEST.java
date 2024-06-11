import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, null, null);
        // Check if document is not null
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String html = "<html><head><meta charset='UTF-8'></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, null, null);
        // Check if document is not null
        Assert.assertNotNull(document);
        // Check if charset is correct
        Assert.assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String html = "<html><head><meta charset='UTF-8'></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, "ISO-8859-1", null);
        // Check if document is not null
        Assert.assertNotNull(document);
        // Check if charset is not UTF-8
        Assert.assertNotEquals("UTF-8", document.outputSettings().charset());
        // Check if charset is ISO-8859-1
        Assert.assertEquals("ISO-8859-1", document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String xml = "<xml><root></root></xml>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        // Check if document is not null
        Assert.assertNotNull(document);
        // Check if document is an XML document
        Assert.assertEquals("xml", document.tagName());
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);
        // Check if output file is not empty
        Assert.assertFalse(new File("output.txt").length() == 0);

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String html = "<html><head><meta charset='UTF-8'></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-8"));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        // Check if document is not null
        Assert.assertNotNull(document);
        // Check if charset is correct
        Assert.assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithMetaCharset() throws IOException {
        String html = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-8"));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        // Check if document is not null
        Assert.assertNotNull(document);
        // Check if charset is ISO-8859-1
        Assert.assertEquals("ISO-8859-1", document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithContentTypeHeader() throws IOException {
        String html = "<html><head><meta charset='UTF-8'><meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-8"));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        // Check if document is not null
        Assert.assertNotNull(document);
        // Check if charset is ISO-8859-1
        Assert.assertEquals("ISO-8859-1", document.outputSettings().charset());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        // Check if charset is correct
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html; charset=ISO-8859-1";
        charset = DataUtil.getCharsetFromContentType(contentType);
        // Check if charset is correct
        Assert.assertEquals("ISO-8859-1", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        // Check if charset is null
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        // Check if boundary length is correct
        Assert.assertEquals(32, boundary.length());
    }
}