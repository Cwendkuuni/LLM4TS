package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFileDocument() throws IOException {
        File testFile = new File("test.html"); // Replace with the path to your test file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileDocumentWithInvalidFile() throws IOException {
        File testFile = new File("invalidFile.txt"); // Replace with the path to a non-existent file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(testFile, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStreamDocument() throws IOException {
        String html = "Your HTML content here..."; // Replace with your HTML content
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(testInputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamDocumentWithNullCharset() throws IOException {
        String html = "Your HTML content here..."; // Replace with your HTML content
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        DataUtil.load(testInputStream, charsetName, baseUri);
    }

    @Test
    public void testParseByteDataDocument() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String html = "Your HTML content here..."; // Replace with your HTML content
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(html.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBufferInputStream() throws IOException {
        String html = "Your HTML content here..."; // Replace with your HTML content
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(html.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(testInputStream);
        assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalidCharset";
        String expectedCharset = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

}