package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Test load method
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertEquals("<html><head></head><body></body></html>", document.html());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // Test load method
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals("<html><head></head><body></body></html>", document.html());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Test load method with parser
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertEquals("<html><head></head><body></body></html>", document.html());
    }

    @Test
    public void testParseByteDataWithCharset() {
        String html = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Test parseByteData method with charset
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals("<html><head></head><body></body></html>", document.html());
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        String html = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        // Test parseByteData method without charset
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals("<html><head></head><body></body></html>", document.html());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        int maxSize = html.length();

        // Test readToByteBuffer method with maxSize
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertEquals(html, new String(byteData.array(), StandardCharsets.UTF_8));
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        // Test readToByteBuffer method without maxSize
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(html, new String(byteData.array(), StandardCharsets.UTF_8));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        // Test getCharsetFromContentType method
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}