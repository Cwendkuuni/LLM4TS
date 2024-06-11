package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {
    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, null, null);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", document.html());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null);
        assertEquals(html, document.html());
    }

    @Test
    public void testLoadFromInputStreamWithCharset() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_16));
        Document document = DataUtil.load(inputStream, "UTF-16", null);
        assertEquals(html, document.html());
    }

    @Test
    public void testParseByteDataWithCharset() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        byte[] byteData = html.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.wrap(byteData);
        Document document = DataUtil.parseByteData(buffer, "UTF-8", null, Parser.htmlParser());
        assertEquals(html, document.html());
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        byte[] byteData = html.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.wrap(byteData);
        Document document = DataUtil.parseByteData(buffer, null, null, Parser.htmlParser());
        assertEquals(html, document.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(html, new String(byteData.array(), StandardCharsets.UTF_8));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}