package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testFile.html");
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(file, null, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("testFile.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        File file = new File("testFile.html");
        InputStream inputStream = new FileInputStream(file);
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(inputStream, null, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        File file = new File("testFile.html");
        InputStream inputStream = new FileInputStream(file);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String docData = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String docData = "<html><head></head><body></body></html>";
        String charsetName = "UTF-8";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=ISO-8859-1";
        String expectedCharset = "ISO-8859-1";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);
    }
}