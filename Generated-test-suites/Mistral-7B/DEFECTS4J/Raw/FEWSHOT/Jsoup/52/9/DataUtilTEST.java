package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.jsoup.helper.DataUtil.getCharsetFromContentType;
import static org.jsoup.helper.DataUtil.load;
import static org.jsoup.helper.DataUtil.mimeBoundary;
import static org.jsoup.helper.DataUtil.parseByteData;
import static org.jsoup.helper.DataUtil.readFileToByteBuffer;
import static org.jsoup.helper.DataUtil.validateCharset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html");
        Document doc = load(file, charsetName, baseUri);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String testData = "Test data.";
        outputStream.write(testData.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(testData, doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String testData = "<html><body>Test data.</body></html>";
        outputStream.write(testData.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = load(inputStream, charsetName, baseUri, Parser.xmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("<html><body>Test data.</body></html>", doc.toString());
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("testInput.html");
        File outputFile = new File("testOutput.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);
        Document doc = load(outputFile, charsetName, baseUri);
        Assert.assertNotNull(doc);

        inputStream.close();
        outputStream.close();
        outputFile.delete();
    }

    @Test
    public void testParseByteData() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        String testData = "Test data.";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(doc);
        Assert.assertEquals(testData, doc.body().text());

        charsetName = null;
        byteData = ByteBuffer.wrap("<?xml encoding='ISO-8859-1'?>Test data.".getBytes(StandardCharsets.UTF_8));
        doc = parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(doc);
        Assert.assertEquals("<xml encoding='ISO-8859-1'>Test data.", doc.toString());

        charsetName = "invalidCharset";
        byteData = ByteBuffer.wrap("<?xml encoding='invalidCharset'?>Test data.".getBytes(StandardCharsets.UTF_8));
        doc = parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNull(doc);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html; charset=ISO-8859-1";
        charset = getCharsetFromContentType(contentType);
        Assert.assertEquals("ISO-8859-1", charset);

        contentType = "text/html";
        charset = getCharsetFromContentType(contentType);
        Assert.assertEquals(null, charset);

        contentType = "text/html; charset=x-invalid";
        charset = getCharsetFromContentType(contentType);
        Assert.assertEquals(null, charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(boundary.length(), DataUtil.boundaryLength);
    }

    @Test
    public void testDetectCharsetFromBom() {
        String charsetName = null;
        ByteBuffer byteData = DataUtil.emptyByteBuffer();

        byteData.put((byte) 0xFF);
        byteData.put((byte) 0xFE);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0x00);
        byteData.flip();
        charsetName = DataUtil.detectCharsetFromBom(byteData, charsetName);
        Assert.assertEquals("UTF-16", charsetName);

        byteData.clear();
        byteData.put((byte) 0x00);
        byteData.put((byte) 0x00);
        byteData.put((byte) 0xFE);
        byteData.put((byte) 0xFF);
        byteData.flip();
        charsetName = DataUtil.detectCharsetFromBom(byteData, charsetName);
        Assert.assertEquals("UTF-32", charsetName);

        byteData.clear();
        byteData.put((byte) 0xEF);
        byteData.put((byte) 0xBB);
        byteData.put((byte) 0xBF);
        byteData.flip();
        charsetName = DataUtil.detectCharsetFromBom(byteData, charsetName);
        Assert.assertEquals("UTF-8", charsetName);

        byteData.clear();
        charsetName = DataUtil.detectCharsetFromBom(byteData, charsetName);
        Assert.assertEquals(null, charsetName);
    }

    @Test
    public void testValidateCharset() {
        Assert.assertEquals("UTF-8", validateCharset("UTF-8"));
        Assert.assertEquals("UTF-16", validateCharset("UTF-16"));
        Assert.assertEquals("UTF-32", validateCharset("UTF-32"));
        Assert.assertEquals("ISO-8859-1", validateCharset("ISO-8859-1"));
        Assert.assertEquals("GB2312", validateCharset("GB2312"));
        Assert.assertEquals(null, validateCharset(null));
        Assert.assertEquals(null, validateCharset(""));
        Assert.assertEquals(null, validateCharset("x-invalid"));
    }
}