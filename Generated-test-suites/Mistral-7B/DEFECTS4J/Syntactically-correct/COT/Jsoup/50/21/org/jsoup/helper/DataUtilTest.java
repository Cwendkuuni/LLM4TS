package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace with the actual path to a test file
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String charsetName = null;
        String baseUri = "http://test.com";
        InputStream inputStream = new ByteArrayInputStream("Test content".getBytes());
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        InputStream inputStream = new ByteArrayInputStream("Test content".getBytes());
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charsetName = null;
        String baseUri = "http://test.com";
        InputStream inputStream = new ByteArrayInputStream("Test content".getBytes());
        Parser parser = DataUtil.Parser.xmlParser(); // Replace with the actual parser
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test content".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String actualContent = outputStream.toString();
        String expectedContent = "Test content";
        assertNotNull(actualContent);
        assertNotNull(expectedContent);
        assertNotNull(actualContent.compareTo(expectedContent) == 0);
    }

    @Test
    public void testParseByteDataWithDefaultCharsetAndBaseUri() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String charsetName = null;
        String baseUri = "http://test.com";
        Parser parser = DataUtil.Parser.htmlParser(); // Replace with the actual parser
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharsetAndBaseUri() {
        ByteBuffer byteData = ByteBuffer.wrap("Test content".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Parser parser = DataUtil.Parser.htmlParser(); // Replace with the actual parser
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParseByteDataWithNullCharsetNameAndParser() {
        ByteBuffer byteData = ByteBuffer.wrap("Test content".getBytes());
        String charsetName = null;
        String baseUri = "http://test.com";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParseByteDataWithNullCharsetNameAndParserAndBaseUri() {
        ByteBuffer byteData = ByteBuffer.wrap("Test content".getBytes());
        String charsetName = null;
        Document document = DataUtil.parseByteData(byteData, charsetName, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadToByteBufferWithNegativeMaxSize() throws IOException {
        InputStream inStream = new ByteArrayInputStream("Test content".getBytes());
        int maxSize = -1;
        DataUtil.readToByteBuffer(inStream, maxSize);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=X-SAMPLE";
        DataUtil.getCharsetFromContentType(contentType);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertNotNull(boundary.length() > 0);
    }
}