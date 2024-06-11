package org.jsoup.helper;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.parser.CharsetDetector;
import org.jsoup.parser.ContentType;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFromFileWithInvalidFile() throws IOException {
        File file = new File("non-existent-file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String charsetName = null;
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testLoadFromInputStreamWithInvalidInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Test</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteData);
        Assert.assertEquals(htmlContent.length(), byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }
}