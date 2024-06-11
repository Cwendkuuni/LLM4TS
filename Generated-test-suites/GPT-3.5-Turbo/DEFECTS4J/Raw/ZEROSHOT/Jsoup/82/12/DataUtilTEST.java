package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
    private static final File TEST_FILE = new File("test.html");
    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "http://example.com";
    private Document document;

    @Before
    public void setUp() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        FileWriter fileWriter = new FileWriter(TEST_FILE);
        fileWriter.write(html);
        fileWriter.close();
    }

    @Test
    public void testLoadFile() throws IOException {
        document = DataUtil.load(TEST_FILE, CHARSET_NAME, BASE_URI);
        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Hello World!", document.select("h1").first().text());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE);
        document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Hello World!", document.select("h1").first().text());
    }

    @Test
    public void testLoadInputStreamParser() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE);
        Parser parser = Parser.htmlParser();
        document = DataUtil.load(inputStream, CHARSET_NAME, BASE_URI, parser);
        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Hello World!", document.select("h1").first().text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new ByteArrayInputStream("Hello World!".getBytes());
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        String result = out.toString();
        Assert.assertEquals("Hello World!", result);
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE);
        document = DataUtil.parseInputStream(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        Assert.assertEquals("Test", document.title());
        Assert.assertEquals("Hello World!", document.select("h1").first().text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream(TEST_FILE);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        Assert.assertTrue(byteBuffer.capacity() > 0);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals("UTF-8", validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        Assert.assertEquals("UTF-8", bomCharset.charset);
        Assert.assertTrue(bomCharset.offset);
    }
}