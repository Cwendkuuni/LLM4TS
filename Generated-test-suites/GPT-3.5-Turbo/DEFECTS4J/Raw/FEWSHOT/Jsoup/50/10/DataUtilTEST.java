package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.helper.DataUtil;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
    private static final Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*(?:\"|')?([^\\s,;\"']*)");
    static final String defaultCharset = "UTF-8";
    private static final int bufferSize = 0x20000;
    private static final int UNICODE_BOM = 0xFEFF;
    private static final char[] mimeBoundaryChars =
            "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    static final int boundaryLength = 32;

    @Test
    public void testLoad_file() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "";
        
        Document document = DataUtil.load(file, charsetName, baseUri);
    
        assertNotNull(document);
        // Add more assertions here
    }

    @Test
    public void testLoad_inputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(document);
        // Add more assertions here
    }

    @Test
    public void testLoad_inputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(document);
        // Add more assertions here
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        // Add assertions here
    }

    @Test
    public void testParseByteData_defaultCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("some html data".getBytes());
        String charsetName = null;
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        // Add more assertions here
    }

    @Test
    public void testParseByteData_specifiedCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("some html data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        // Add more assertions here
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteBuffer);
        // Add assertions here
    }

    @Test
    public void testReadToByteBuffer_withMaxSize() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        int maxSize = 1024;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        assertNotNull(byteBuffer);
        // Add assertions here
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        assertNotNull(byteBuffer);
        // Add assertions here
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
        // Add more assertions here
    }

    @Test
    public void testGetCharsetFromContentType_foundCharsetInMeta() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_foundCharsetInCharsetAttr() {
        String contentType = "text/html; charset=\"UTF-8\"";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_charsetNotFound() {
        String contentType = "text/html";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertNotNull(boundary);
        assertEquals(boundaryLength, boundary.length());
        // Add more assertions here
    }
}