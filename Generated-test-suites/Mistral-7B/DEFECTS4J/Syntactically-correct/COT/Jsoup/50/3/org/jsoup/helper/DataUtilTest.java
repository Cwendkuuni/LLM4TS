package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStreamWithDefaultCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-16\"/></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-16"));
        String charsetName = null;
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStreamWithSpecifiedCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-16\"/></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-16"));
        String charsetName = "UTF-16";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String html = "<html><head><meta charset=\"UTF-16\"/></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-16"));
        String charsetName = "UTF-16";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser(); // Change this to test other parsers
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("testInput.txt");
        File outputFile = new File("testOutput.txt");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        assert(outputFile.length() > 0);

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String html = "<html><head><meta charset=\"UTF-16\"/></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-16"));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        assertThrows(IOException.class, () -> DataUtil.parseByteData(byteData, charsetName, baseUri, parser));
    }

    @Test
    public void testParseByteDataWithInvalidCharsetName() {
        String html = "<html><head><meta charset=\"UTF-16\"/></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-16"));
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        assertThrows(IllegalCharsetNameException.class, () -> DataUtil.parseByteData(byteData, charsetName, baseUri, parser));
    }

    @Test
    public void testParseByteDataWithEmptyContent() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        assertNotNull(DataUtil.parseByteData(byteData, charsetName, baseUri, parser));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-16";
        String expectedCharset = "UTF-16";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharsetName() {
        String contentType = "text/html; charset=invalidCharset";
        String expectedCharset = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        int length = boundary.length();

        assertTrue(length >= boundaryLength);
    }
}