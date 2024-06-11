package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        File file = new File("test.html"); // add the path to your test file here
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithNullCharset() throws IOException {
        File file = new File("test.html"); // add the path to your test file here
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, null, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, null, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String charsetName = "INVALID_CHARSET";
        String baseUri = "http://example.com";
        assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser()));
    }

    @Test
    public void testLoadInputStreamWithEmptyCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String charsetName = "";
        String baseUri = "http://example.com";
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser()));
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"></head><body>Test</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithBOM() throws IOException {
        String htmlContent = "\uFEFF<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));
        String charsetName = "INVALID_CHARSET";
        String baseUri = "http://example.com";
        assertThrows(IOException.class, () -> DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser()));
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputSource = "Source";
        String outputTarget = "Target";
        InputStream inputStream = new ByteArrayInputStream(inputSource.getBytes("UTF-8"));
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(inputSource, outputStream.toString("UTF-8"));
    }
}