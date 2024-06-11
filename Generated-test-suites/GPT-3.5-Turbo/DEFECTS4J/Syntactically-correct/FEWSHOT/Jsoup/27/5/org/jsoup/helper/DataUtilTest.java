
package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException, URISyntaxException {
        String testFilePath = "/testFile.html";
        ClassLoader classLoader = getClass().getClassLoader();
        File testFile = new File(classLoader.getResource(testFilePath).toURI());

        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(testFile, charsetName, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertNotNull(document.body());
    }

    @Test
    public void testLoad_InputSteam() throws IOException, URISyntaxException {
        String testFilePath = "/testFile.html";
        ClassLoader classLoader = getClass().getClassLoader();
        File testFile = new File(classLoader.getResource(testFilePath).toURI());

        FileInputStream inStream = new FileInputStream(testFile);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inStream, charsetName, baseUri);

        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertNotNull(document.body());
    }

    @Test
    public void testLoad_InputStream_CustomParser() throws IOException, URISyntaxException {
        String testFilePath = "/testFile.html";
        ClassLoader classLoader = getClass().getClassLoader();
        File testFile = new File(classLoader.getResource(testFilePath).toURI());

        FileInputStream inStream = new FileInputStream(testFile);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inStream, charsetName, baseUri, parser);

        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertNotNull(document.body());
    }

    @Test
    public void testParseByteData_NullCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><meta charset=\"UTF-8\"></head><body></body></html>".getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertNotNull(document.body());
    }

    @Test
    public void testParseByteData_WithCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><meta charset=\"UTF-8\"></head><body></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertNotNull(document.body());
    }

    @Test
    public void testParseByteData_WithoutCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><meta charset=\"UTF-8\"></head><body></body></html>".getBytes());
        String charsetName = "ISO-8859-1";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        Assert.assertNotNull(document);
        Assert.assertEquals("http://example.com", document.baseUri());
        Assert.assertNotNull(document.body());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testFilePath = "/testFile.html";
        ClassLoader classLoader = getClass().getClassLoader();
        File testFile = new File(classLoader.getResource(testFilePath).toURI());

        FileInputStream inStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);

        Assert.assertNotNull(byteData);
        Assert.assertTrue(byteData.limit() > 0);
    }

    @Test
    public void testGetCharsetFromContentType_WithCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_WithoutCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertNull(charset);
    }
}
