package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private String baseUri;
    private Parser parser;

    @Before
    public void setUp() {
        baseUri = "http://example.com";
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, null, baseUri);
        assertEquals("Expected document.baseUri to match baseUri", baseUri, document.baseUri());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><head><title>Test</title></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, baseUri);
        assertEquals("Expected document.baseUri to match baseUri", baseUri, document.baseUri());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String html = "<html><head><title>Test</title></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, baseUri, parser);
        assertEquals("Expected document.baseUri to match baseUri", baseUri, document.baseUri());
    }

    @Test
    public void testParseByteData() {
        String html = "<html><head><title>Test</title></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseByteData(byteBuffer, null, baseUri, parser);
        assertEquals("Expected document.baseUri to match baseUri", baseUri, document.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><title>Test</title></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertEquals("html", StandardCharsets.UTF_8.decode(byteBuffer).toString());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}