package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add assertions
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
        // Add assertions
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String xml = "<root><element>Value</element></root>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(document);
        // Add assertions
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "Hello, World!";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        String output = outputStream.toString();
        assertEquals(input, output);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
        // Add assertions
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String input = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertEquals(input.length(), byteBuffer.remaining());
        // Add assertions
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String input = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 5);
        assertEquals(5, byteBuffer.remaining());
        // Add assertions
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        assertNotNull(byteBuffer);
        // Add assertions
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
        assertEquals(0, byteBuffer.limit());
        assertEquals(0, byteBuffer.position());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String charset = DataUtil.getCharsetFromContentType("text/html");
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=U_T-F/8");
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = DataUtil.validateCharset("UTF-8");
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = DataUtil.validateCharset("U_T-F/8");
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }
}