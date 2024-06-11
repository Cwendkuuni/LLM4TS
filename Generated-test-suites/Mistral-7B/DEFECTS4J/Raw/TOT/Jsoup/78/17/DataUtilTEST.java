package org.jsoup.helper.test;

import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;

public class DataUtilTest {

    @org.junit.Test
    public void testLoadFileWithValidCharset() throws IOException {
        File file = new File("testFile.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);

        // Test document is not null
        Assert.assertNotNull(document);

        // Test document content
        Assert.assertEquals("Test content", document.title());
    }

    @org.junit.Test
    public void testLoadFileWithoutCharset() throws IOException {
        File file = new File("testFileWithoutCharset.html");
        String charsetName = null;
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);

        // Test document is not null
        Assert.assertNotNull(document);

        // Test document uses default charset
        Assert.assertEquals("UTF-8", document.outputSettings().charset());
    }

    @org.junit.Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String testData = "Test content";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());

        // Test document is not null
        Assert.assertNotNull(document);

        // Test document content
        Assert.assertEquals("Test content", document.title());
    }

    @org.junit.Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testData = "Test content";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("ISO-8859-1"));
        String charsetName = "ISO-8859-1";
        String baseUri = "http://example.com";

        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
            Assert.fail();
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @org.junit.Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String testData = "Test content";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        String charsetName = null;
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());

        // Test document uses default charset (UTF-8)
        Assert.assertEquals("UTF-8", document.outputSettings().charset());
    }

    @org.junit.Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String testData = "Test content";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

        // Test document is not null
        Assert.assertNotNull(document);

        // Test document content
        Assert.assertEquals("Test content", document.title());
    }

    @org.junit.Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String testData = "Test content";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("ISO-8859-1"));
        String charsetName = "UTF-8"; // Incorrect charset
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        try {
            Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
            Assert.fail();
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @org.junit.Test
    public void testParseInputStreamWithNullCharset() throws IOException {
        String testData = "Test content";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

        // Test document uses default charset (UTF-8)
        Assert.assertEquals("UTF-8", document.outputSettings().charset());
    }

    @org.junit.Test
    public void testCrossStreams() throws IOException {
        String testData = "Test content";
        String inputData = "Test input";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] outputData = outputStream.toByteArray();
        Assert.assertEquals(testData, new String(outputData, "UTF-8"));
    }
}