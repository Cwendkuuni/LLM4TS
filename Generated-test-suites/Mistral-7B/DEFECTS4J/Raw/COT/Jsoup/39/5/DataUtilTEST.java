package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#load(File, String, String)}.
     */
    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        File inputFile = new File("test.html");
        Document expectedDoc = Parser.htmlParser().parseInput(new File("test.html"), baseUri);
        Document actualDoc = DataUtil.load(inputFile, charsetName, baseUri);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    /**
     * Test method for {@link DataUtil#load(File, String, String)} with incorrect charset.
     */
    @Test(expected = IOException.class)
    public void testLoadFileWithIncorrectCharset() throws IOException {
        String charsetName = "invalid_charset";
        String baseUri = "http://example.com/";
        File inputFile = new File("test.html");
        DataUtil.load(inputFile, charsetName, baseUri);
    }

    /**
     * Test method for {@link DataUtil#load(File, String, String)} with null charset.
     */
    @Test
    public void testLoadFileWithNullCharset() throws IOException {
        String baseUri = "http://example.com/";
        File inputFile = new File("test.html");
        Document actualDoc = DataUtil.load(inputFile, null, baseUri);
        Document expectedDoc = Parser.htmlParser().parseInput(new File("test.html"), baseUri);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)}.
     */
    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        String testData = "Test data...";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(Charset.forName(charsetName)));
        Document expectedDoc = Parser.htmlParser().parseInput(new ByteArrayInputStream(testData.getBytes(Charset.forName(charsetName))), baseUri);
        Document actualDoc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String)} with null charset.
     */
    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String baseUri = "http://example.com/";
        InputStream inputStream = new ByteArrayInputStream("Test data...".getBytes());
        Document actualDoc = DataUtil.load(inputStream, null, baseUri);
        Document expectedDoc = Parser.htmlParser().parseInput(new ByteArrayInputStream("Test data...".getBytes()), baseUri);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    /**
     * Test method for {@link DataUtil#load(InputStream, String, String, Parser)} with htmlParser.
     */
    @Test
    public void testLoadInputStreamWithHtmlParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        String testData = "Test data...";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(Charset.forName(charsetName)));
        Document expectedDoc = Parser.htmlParser().parseInput(new ByteArrayInputStream(testData.getBytes(Charset.forName(charsetName))), baseUri);
        Document actualDoc = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with default charset.
     */
    @Test
    public void testParseByteDataWithDefaultCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();
        Document expectedDoc = parser.parseInput(new ByteArrayInputStream("Test data...".getBytes()), baseUri);
        Document actualDoc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with specified charset.
     */
    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'T', 'e', 's', 't', ' ', 'd', 'a', 't', 'a', 0x3B, 'a', ' ', 't', 'e', 's', 't', ' ', 'd', 'a', 't', 'a'});
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();
        Document expectedDoc = parser.parseInput(new ByteArrayInputStream("Test data...".getBytes(Charset.forName("UTF-8"))), baseUri);
        Document actualDoc = DataUtil.parseByteData(byteData, "UTF-8", baseUri, parser);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with null charset.
     */
    @Test
    public void testParseByteDataWithNullCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();
        Document actualDoc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        Document expectedDoc = parser.parseInput(new ByteArrayInputStream(new byte[0]), baseUri);
        Assert.assertEquals(expectedDoc, actualDoc);
    }

    /**
     * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} with invalid charset.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithInvalidCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        String baseUri = "http://example.com/";
        Parser parser = Parser.htmlParser();
        DataUtil.parseByteData(byteData, "invalid_charset", baseUri, parser);
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)} with unlimited maxSize.
     */
    @Test
    public void testReadToByteBufferWithUnlimitedMaxSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test data...".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        byte[] expectedData = "Test data...".getBytes();
        byte[] actualData = new byte[byteData.limit()];
        byteData.get(actualData);
        Assert.assertArrayEquals(expectedData, actualData);
    }

    /**
     * Test method for {@link DataUtil#readToByteBuffer(InputStream, int)} with limited maxSize.
     */
    @Test
    public void testReadToByteBufferWithLimitedMaxSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test data...".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);
        byte[] expectedData = new byte[10];
        byteData.get(expectedData);
        String expectedString = new String(expectedData, 0, expectedData.length);
        Assert.assertEquals("Test data...", expectedString);
        inputStream.close();
    }
}