package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testParseByteData() throws IOException {
        String testData = "This is a test";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals("This is a test", doc.body().text());
    }

    @Test
    public void testParseByteDataDetectCharset() throws IOException {
        String testData = "This is a test\u00E1\u00E9\u00ED";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals("This is a test\u00E1\u00E9\u00ED", doc.body().text());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithCharset() throws IOException {
        String testData = "This is a test";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals("This is a test", doc.body().text());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String testData = "This is a test";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        });
    }

    @Test
    public void testParseByteDataWithBom() throws IOException {
        String testData = "\uFEFFThis is a test";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals("This is a test", doc.body().text());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithMetaTag() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-16\"><title>Test</title></head><body>Test</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(StandardCharsets.UTF_8));

        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals("Test", doc.body().text());
        assertEquals("UTF-16", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = null;
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(file, charsetName, baseUri);

        assertEquals("Test file content", doc.body().text());
    }

    @Test
    public void testLoadFileWithCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(file, charsetName, baseUri);

        assertEquals("Test file content", doc.body().text());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";

        assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.load(file, charsetName, baseUri);
        });
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);

        assertEquals("This is a test", doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String testData = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);

        assertEquals("This is a test", doc.body().text());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testData = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";

        assertThrows(IllegalArgumentException.class, () -> {
            DataUtil.load(inputStream, charsetName, baseUri);
        });
    }

    @Test
    public void testLoadInputStreamWithBom() throws IOException {
        String testData = "\uFEFFThis is a test";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);

        assertEquals("This is a test", doc.body().text());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithMetaTag() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-16\"><title>Test</title></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);

        assertEquals("Test", doc.body().text());
        assertEquals("UTF-16", doc.outputSettings().charset());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithEmptyContentType() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(DataUtil.defaultCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithUppercaseCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertTrue(boundary.length() > 0);
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(DataUtil.mimeBoundaryChars.indexOf(boundary.charAt(i)) >= 0);
        }
    }
}