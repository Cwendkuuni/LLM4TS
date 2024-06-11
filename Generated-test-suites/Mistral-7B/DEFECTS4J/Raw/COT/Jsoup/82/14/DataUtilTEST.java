package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class DataUtilTest {
    private Document doc;
    private Parser parser;

    @Before
    public void setUp() {
        parser = Parser.htmlParser();
    }

    

    // Test for load(File) method
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace with the path to your test HTML file
        Document expectedDoc = parser.parse(file);
        Document actualDoc = DataUtil.load(file, null, null);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    // Test for load(InputStream) method
    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document expectedDoc = parser.parse(new InputSource(inputStream));
        Document actualDoc = DataUtil.load(inputStream, null, null);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    // Test for load(InputStream, String, String) method
    @Test
    public void testLoadInputStreamWithCharsetAndBaseUri() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document expectedDoc = parser.parse(new InputSource(inputStream));
        String charset = "UTF-8";
        String baseUri = "https://example.com";
        Document actualDoc = DataUtil.load(inputStream, charset, baseUri);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    // Test for load(InputStream, String, String, Parser) method
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Parser parserXml = Parser.xmlParser();
        Document expectedDoc = parserXml.parseInput(new InputSource(inputStream));
        Document actualDoc = DataUtil.load(inputStream, null, null, parserXml);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    // Test for parseInputStream(InputStream, String, String, Parser) method
    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document expectedDoc = parser.parseInput(new InputSource(inputStream));
        Document actualDoc = DataUtil.parseInputStream(inputStream, null, null, parser);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    // Test for crossStreams method
    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        DataUtil.crossStreams(inputStream, outputStream);
        String expectedData = "Test data";
        String actualData = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        Assert.assertEquals(expectedData, actualData);
    }

    // Test for detectCharsetFromBom method
    @Test
    public void testDetectCharsetFromBom() {
        String testDataUtf8 = "\u00EF\u00BB\u00BFTest UTF-8";
        ByteBuffer utf8Bom = ByteBuffer.wrap(testDataUtf8.getBytes(StandardCharsets.UTF_8));
        BomCharset expectedBomCharsetUtf8 = new BomCharset("UTF-8", true);
        BomCharset actualBomCharsetUtf8 = DataUtil.detectCharsetFromBom(utf8Bom);
        Assert.assertEquals(expectedBomCharsetUtf8, actualBomCharsetUtf8);

        String testDataUtf16 = "\uFEFFTest UTF-16";
        ByteBuffer utf16Bom = ByteBuffer.wrap(testDataUtf16.getBytes(StandardCharsets.UTF_16LE));
        BomCharset expectedBomCharsetUtf16 = new BomCharset("UTF-16", false);
        BomCharset actualBomCharsetUtf16 = DataUtil.detectCharsetFromBom(utf16Bom);
        Assert.assertEquals(expectedBomCharsetUtf16, actualBomCharsetUtf16);

        String testDataUtf32 = "\u0000\u0000\u00FE\u00FFTest UTF-32";
        ByteBuffer utf32Bom = ByteBuffer.wrap(testDataUtf32.getBytes(StandardCharsets.UTF_32BE));
        BomCharset expectedBomCharsetUtf32 = new BomCharset("UTF-32", false);
        BomCharset actualBomCharsetUtf32 = DataUtil.detectCharsetFromBom(utf32Bom);
        Assert.assertEquals(expectedBomCharsetUtf32, actualBomCharsetUtf32);

        ByteBuffer emptyBom = ByteBuffer.allocate(0);
        BomCharset actualBomCharsetEmpty = DataUtil.detectCharsetFromBom(emptyBom);
        Assert.assertNull(actualBomCharsetEmpty);
    }

    
}