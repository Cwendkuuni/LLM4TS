package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "https://www.example.com";

    private File testFile;
    private FileInputStream testFileStream;
    private InputStream testInputStream;
    private ByteBuffer testByteBuffer;

    @Before
    public void setUp() throws FileNotFoundException {
        testFile = new File("test.html");
        testFileStream = new FileInputStream(testFile);
        testInputStream = new ByteArrayInputStream("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>".getBytes());
        testByteBuffer = ByteBuffer.wrap("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>".getBytes());
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Document document = DataUtil.load(testFile, CHARSET_NAME, BASE_URI);
        assertEquals("Test", document.title());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        Document document = DataUtil.load(testInputStream, CHARSET_NAME, BASE_URI);
        assertEquals("Test", document.title());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        Document document = DataUtil.load(testInputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        assertEquals("Test", document.title());
    }

    @Test
    public void testParseByteDataWithCharset() {
        Document document = DataUtil.parseByteData(testByteBuffer, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        assertEquals("Test", document.title());
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        Document document = DataUtil.parseByteData(testByteBuffer, null, BASE_URI, Parser.htmlParser());
        assertEquals("Test", document.title());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testFileStream);
        assertEquals(testByteBuffer, byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}