package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "https://example.com");
        assertNotNull(doc);
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(in, "UTF-8", "https://example.com");
        assertNotNull(doc);
    }
    
    @Test
    public void testLoadInputStreamParser() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(in, "UTF-8", "https://example.com", Parser.xmlParser());
        assertNotNull(doc);
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        String input = "Hello World";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        assertEquals(input, out.toString());
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        byteData.put("Hello".getBytes());
        byteData.flip();
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "https://example.com", Parser.xmlParser());
        assertNotNull(doc);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        String input = "Hello World";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        assertEquals(input, new String(byteData.array()));
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String input = "Hello World";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(in, 5);
        assertEquals("Hello", new String(byteData.array()));
    }
    
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteData);
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        assertNotNull(byteData);
        assertEquals(0, byteData.limit());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }
}