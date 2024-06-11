package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(JUnit4.class)
public class DataUtilTest {

    private static Document doc;
    private static final String TEST_CHARSET = "UTF-8";

    @BeforeClass
    public static void setup() {
        // Replace with the appropriate base URI for your test environment
        String baseUri = "http://example.com";

        // Create a sample HTML file for testing
        String htmlContent = "<html><head><title>Test</title></head><body></body></html>";
        try (FileOutputStream outputStream = new FileOutputStream("test.html")) {
            outputStream.write(htmlContent.getBytes(TEST_CHARSET));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, TEST_CHARSET, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><head><title>Test</title></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(TEST_CHARSET));
        Document doc = DataUtil.load(inputStream, TEST_CHARSET, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlContent = "<html><head><title>Test</title></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(TEST_CHARSET));
        Document doc = DataUtil.load(inputStream, TEST_CHARSET, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceText = "This is a test";
        String targetText = "";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceText.getBytes(TEST_CHARSET));

        DataUtil.crossStreams(inputStream, outputStream);
        targetText = new String(outputStream.toByteArray());

        assertEquals(sourceText, targetText);
    }

    @Test
    public void testParseByteDataWithUtf8Charset() {
        String htmlContent = "<html><head><title>Test</title></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(TEST_CHARSET));

        Document doc = DataUtil.parseByteData(byteData, TEST_CHARSET, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() {
        String htmlContent = "<html><head><title>Test</title></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes("InvalidCharset".toCharArray()));

        Document doc = null;
        try {
            doc = DataUtil.parseByteData(byteData, "InvalidCharset", baseUri, Parser.htmlParser());
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        assertNull(doc);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String htmlContent = "<html><head><title>Test</title></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes("ISO-8859-1".toCharArray()));

        Document doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        String htmlContent = "<html><head><title>Test</title></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes("UTF-8".toCharArray()));

        Element meta = new Element("meta");
        meta.attr("http-equiv", "content-type");
        meta.attr("content", "text/html; charset=gb2312");

        Document doc = null;
        try {
            doc = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());

            Element detectedCharsetElement = doc.selectFirst("meta[http-equiv=content-type]");
            assertEquals("gb2312", detectedCharsetElement.attr("content").substring(0, 5));

            doc.select("title").text(0).equals("Test");
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull(doc);
    }

    // Add more test cases here as needed for other methods in the DataUtil class.

    @AfterClass
    public static void teardown() {
        // Clean up the test file created earlier
        File file = new File("test.html");
        file.delete();
    }
}