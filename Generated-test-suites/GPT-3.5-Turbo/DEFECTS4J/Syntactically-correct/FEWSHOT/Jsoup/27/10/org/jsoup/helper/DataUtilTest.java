package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.Locale;

public class DataUtilTest {
    private File testFile;
    private String testFileCharset = "UTF-8";
    private String testFileBaseUri = "https://www.example.com";

    @Before
    public void setUp() {
        testFile = new File("test.html");
    }

    @Test
    public void testLoadFile() throws IOException {
        Document document = DataUtil.load(testFile, testFileCharset, testFileBaseUri);
        assertNotNull(document);
        assertEquals(testFileBaseUri, document.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        FileInputStream in = new FileInputStream(testFile);
        Document document = DataUtil.load(in, testFileCharset, testFileBaseUri);
        assertNotNull(document);
        assertEquals(testFileBaseUri, document.baseUri());
        in.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        FileInputStream in = new FileInputStream(testFile);
        Document document = DataUtil.load(in, testFileCharset, testFileBaseUri, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(testFileBaseUri, document.baseUri());
        in.close();
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream in = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        assertNotNull(byteData);
        in.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeNotFound() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}