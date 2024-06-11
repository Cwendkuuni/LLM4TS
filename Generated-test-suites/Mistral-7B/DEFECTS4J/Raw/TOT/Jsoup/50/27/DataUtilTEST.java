package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class DataUtilTest {

    private static final String TEST_FILE = "testFile.html";
    private static final String TEST_CHARSET = "UTF-16";
    private static final String TEST_BASE_URI = "http://example.com";

    private Document docFromFile;
    private Document docFromInputStream;
    private Document docFromByteBuffer;

    @Before
    public void setup() throws IOException {
        File file = new File(TEST_FILE);
        docFromFile = DataUtil.load(file, TEST_CHARSET, TEST_BASE_URI);

        InputStream inputStream = new FileInputStream(file);
        docFromInputStream = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(new FileInputStream(file), byteArrayOutputStream);
        ByteBuffer byteBufferFromFile = DataUtil.readFileToByteBuffer(file);
        docFromByteBuffer = DataUtil.parseByteData(byteBufferFromFile, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());

        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        docFromByteBuffer = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
    }

    @Test
    public void testLoadFromFile() {
        Assert.assertNotNull(docFromFile);
    }

    @Test
    public void testLoadFromInputStream() {
        Assert.assertNotNull(docFromInputStream);
        Assert.assertEquals(docFromFile.body().text(), docFromInputStream.body().text());
    }

    @Test
    public void testLoadFromByteBuffer() {
        Assert.assertNotNull(docFromByteBuffer);
        Assert.assertEquals(docFromFile.body().text(), docFromByteBuffer.body().text());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String invalidCharset = "INVALID_CHARSET";
        String expectedErrorMessage = String.format("Unsupported charset: %s", invalidCharset);
        try {
            DataUtil.parseByteData(byteData, invalidCharset, TEST_BASE_URI, Parser.htmlParser());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(expectedErrorMessage, e.getMessage());
        }
    }

    @Test
    public void testParseByteDataWithNoCharsetSpecified() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        Document doc = DataUtil.parseByteData(byteData, null, TEST_BASE_URI, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String charset = "UTF-8";
        Document doc = DataUtil.parseByteData(byteData, charset, TEST_BASE_URI, Parser.htmlParser());
        Assert.assertNotNull(doc);
    }

    @Test
    public void testDetectCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, detectedCharset);
    }

    @Test
    public void testDetectCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=INVALID_CHARSET";
        String expectedCharset = null;
        String detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, detectedCharset);
    }

    @Test
    public void testDetectCharsetFromContentTypeWithNoCharsetSpecified() {
        String contentType = "text/html";
        String expectedCharset = DataUtil.defaultCharset;
        String detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, detectedCharset);
    }

    @Test
    public void testDetectCharsetFromContentTypeWithInvalidMetaTag() {
        String contentType = "text/html";
        String invalidMetaTag = "<meta charset=INVALID_CHARSET>";
        Document doc = new org.jsoup.nodes.Document();
        doc.appendElement(new org.jsoup.nodes.Element("html"));
        doc.getElementsByTag("html").first().appendElement(new org.jsoup.nodes.Element("head"));
        doc.getElementsByTag("head").first().appendElement(new org.jsoup.nodes.Element("meta", invalidMetaTag));
        String detectedCharset = DataUtil.getCharsetFromContentType(contentType + "\n" + doc.html());
        String expectedCharset = DataUtil.defaultCharset;
        Assert.assertEquals(expectedCharset, detectedCharset);
    }

    @Test
    public void testDetectCharsetFromContentTypeWithValidMetaTag() {
        String contentType = "text/html";
        String validMetaTag = "<meta charset=UTF-8>";
        Document doc = new org.jsoup.nodes.Document();
        doc.appendElement(new org.jsoup.nodes.Element("html"));
        doc.getElementsByTag("html").first().appendElement(new org.jsoup.nodes.Element("head"));
        doc.getElementsByTag("head").first().appendElement(new org.jsoup.nodes.Element("meta", validMetaTag));
        String detectedCharset = DataUtil.getCharsetFromContentType(contentType + "\n" + doc.html());
        String expectedCharset = "UTF-8";
        Assert.assertEquals(expectedCharset, detectedCharset);
    }
}