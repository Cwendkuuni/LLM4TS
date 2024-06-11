package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("testFile.html"); // Replace with a valid file path
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileIOException() throws IOException {
        File file = new File("nonExistentFile.html"); // Replace with a non-existent file path
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body>Test Input Stream</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamIOException() throws IOException {
        String testData = "Test Input Stream Data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
    }

    @Test
    public void testLoadInputStreamWithAlternateParser() throws IOException {
        String testData = "<?xml version=\"1.0\">\n<html><head><meta charset=\"UTF-8\"></head><body>Test Input Stream</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testData = "Test Stream Data";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(Charset.forName("UTF-8")));
        DataUtil.crossStreams(inputStream, outputStream);
        String actualData = new String(outputStream.toByteArray(), Charset.forName("UTF-8"));
        assertEquals(testData, actualData);
    }

    @Test
    public void testParseByteData() {
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body>Test Byte Data</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        String testData = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<html><body>Test Byte Data</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(Charset.forName("UTF-8")));
        String charsetName = null;
        String baseUri = "http://test.com";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals("ISO-8859-1", document.outputSettings().charset());
        assertNotNull(document);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=ISO-8859-1";
        String expectedCharset = "ISO-8859-1";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeInvalid() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(boundary.length(), 32);
    }

}