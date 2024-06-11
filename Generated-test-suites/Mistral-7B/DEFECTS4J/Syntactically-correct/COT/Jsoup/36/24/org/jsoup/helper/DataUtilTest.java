package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException, HelperException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html");
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithIOException() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("nonExistentFile.html");
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException, HelperException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String testData = "<html><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithIOException() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException, HelperException {
        String charsetName = null;
        String baseUri = "http://example.com";
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        byteData.put("Test data".getBytes());
        byteData.flip();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test(expected = HelperException.class)
    public void testParseByteDataWithInvalidCharset() throws IOException, HelperException {
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        byteData.put("Test data".getBytes());
        byteData.flip();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException, HelperException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        byteData.put("Test data".getBytes(Charset.forName("UTF-8")));
        byteData.flip();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(testData.length(), byteData.remaining());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testData = "Test data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, testData.length() + 1);
        assertEquals(testData.length(), byteData.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}