package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {
    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertEquals("Test Document", doc.title());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><head><title>Test Document</title></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertEquals("Test Document", doc.title());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals("Hello World", outputStream.toString());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><title>Test Document</title></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        assertEquals("Test Document", doc.title());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><title>Test Document</title></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        String result = new String(byteBuffer.array(), StandardCharsets.UTF_8);
        assertEquals(html, result);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        String result = new String(byteBuffer.array(), StandardCharsets.UTF_8);
        assertEquals("Test Document", result);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);
    }
}