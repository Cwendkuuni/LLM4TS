package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

    private static final String BASE_URI = "http://www.example.com";
    private static final String CHARSET_NAME = "UTF-8";

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, CHARSET_NAME, BASE_URI);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName(CHARSET_NAME)));
        Document document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName(CHARSET_NAME)));
        Document document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap(
                "<html><body><h1>Hello, World!</h1></body></html>".getBytes(Charset.forName(CHARSET_NAME)));
        Document document = DataUtil.parseByteData(byteData, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName(CHARSET_NAME)));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charsetName = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(CHARSET_NAME, charsetName);
    }

}