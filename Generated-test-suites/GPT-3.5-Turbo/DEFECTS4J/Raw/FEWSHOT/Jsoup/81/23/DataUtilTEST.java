package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("test.html");
        Document doc = DataUtil.load(inputFile, "UTF-8", "http://example.com");
        assertNotNull(doc);
        assertEquals(1, doc.select("test").size());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.title());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String xml = "<root><element>value</element></root>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("value", doc.select("element").text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "Hello, World!";
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(bytes);
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String output = outputStream.toString();
        assertEquals(input, output);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.select("h1").text());
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
        assertEquals(null, charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }
}