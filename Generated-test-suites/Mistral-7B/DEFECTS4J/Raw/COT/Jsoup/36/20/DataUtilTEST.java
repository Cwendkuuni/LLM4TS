package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil
 */
public class DataUtilTest {

    private static Document doc;
    private static ByteArrayOutputStream byteStream;
    private static ByteArrayInputStream inputStream;

    @BeforeClass
    public static void setUp() {
        byteStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(byteStream.toByteArray());
    }

    @Test
    public void testLoad_File_NullCharset_Normal() throws IOException {
        File testFile = new File("test.html");
        Document document = DataUtil.load(testFile, null, "");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoad_File_GB2312_Charset() throws IOException {
        File testFile = new File("test.html");
        String charset = "GB2312";
        Document document = DataUtil.load(testFile, charset, "");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoad_InputStream_NullCharset_Normal() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, null, "");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoad_InputStream_UTF8_Charset() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoad_InputStream_GB2312_Charset() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charset = "GB2312";
        Document document = DataUtil.load(inputStream, charset, "");
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoad_File_InvalidFile() throws IOException {
        File testFile = new File("test.abcd");
        Document document = DataUtil.load(testFile, null, "");
    }

    @Test(expected = IOException.class)
    public void testParseByteData_NullCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, null, baseUri, parser);
        Assert.fail();
    }

    @Test
    public void testParseByteData_UTF8_Charset() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, "UTF-8", baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteData_GB2312_Charset() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        String charset = "GB2312";
        Document document = DataUtil.parseByteData(byteData, charset, baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer_MaxSize_Zero() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Assert.assertEquals(inputStream.available(), byteBuffer.limit());
    }

    @Test
    public void testReadToByteBuffer_MaxSize_MoreThanZero() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        int maxSize = 1;
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        Assert.assertEquals(maxSize, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType_ValidContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("EUC-JP", charset);
    }

    @Test
    public void testGetCharsetFromContentType_InvalidContentType() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_InvalidCharset() {
        String contentType = "text/html; charset=X-INVALID-CHARSET";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testParseByteData_BOM_UTF8_Charset() throws IOException {
        String html = "\ufeff<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, "UTF-8", baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteData_BOM_GB2312_Charset() throws IOException {
        String html = "\ufeff<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        String baseUri = "";
        Parser parser = Parser.htmlParser();
        String charset = "GB2312";
        Document document = DataUtil.parseByteData(byteData, charset, baseUri, parser);
        Assert.assertNotNull(document);
    }
}