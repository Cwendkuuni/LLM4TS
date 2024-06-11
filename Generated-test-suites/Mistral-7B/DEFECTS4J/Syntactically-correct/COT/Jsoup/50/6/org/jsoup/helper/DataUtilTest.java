package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
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

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace with the actual file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String baseUri = "http://example.com";
        DataUtil.load(inputStream, null, baseUri);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("test.html")); // Replace with the actual file path
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String charsetName = "UTF-8";
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("test.html")); // Replace with the actual file path
        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("test.html")); // Replace with the actual file path
        String charsetName = "InvalidCharset";
        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertFalse(boundary.isEmpty());
    }
}