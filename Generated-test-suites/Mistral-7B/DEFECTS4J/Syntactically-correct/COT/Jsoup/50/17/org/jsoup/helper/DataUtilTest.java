package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html"); // replace with actual file path
        Document doc = DataUtil.load(file, null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html"); // replace with actual file path
        Document doc = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test content".getBytes());
        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String data = "test content";
        InputStream inputStream = new ByteArrayInputStream(data.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String data = "test content";
        InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("testInput.txt");
        File outputFile = new File("testOutput.txt");

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertTrue(outputFile.length() > 0);
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharset() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String invalidCharset = "INVALID_CHARSET";
        DataUtil.parseByteData(byteData, invalidCharset, null, Parser.htmlParser());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=INVALID_CHARSET";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertTrue(boundary.length() == 32);
    }
}