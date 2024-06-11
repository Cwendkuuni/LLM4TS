package org.jsoup.helper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class DataUtilTest {

    private static final String TEST_FILE_PATH = "src/test/resources/test.html";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "https://example.com";

    private static Document testDocument;

    @BeforeClass
    public static void setUp() throws IOException {
        File testFile = new File(TEST_FILE_PATH);
        testDocument = DataUtil.load(testFile, CHARSET_NAME, BASE_URI);
    }

    @Test
    public void testLoadFile() {
        assertNotNull(testDocument);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        FileInputStream inStream = new FileInputStream(TEST_FILE_PATH);
        Document loadedDocument = DataUtil.load(inStream, CHARSET_NAME, BASE_URI);
        assertNotNull(loadedDocument);
        inStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        FileInputStream inStream = new FileInputStream(TEST_FILE_PATH);
        Document loadedDocument = DataUtil.load(inStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        assertNotNull(loadedDocument);
        inStream.close();
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        Document parsedDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(parsedDocument);
        assertEquals(charsetName, parsedDocument.charset().name());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream inStream = new FileInputStream(TEST_FILE_PATH);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        assertNotNull(byteData);
        inStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
}