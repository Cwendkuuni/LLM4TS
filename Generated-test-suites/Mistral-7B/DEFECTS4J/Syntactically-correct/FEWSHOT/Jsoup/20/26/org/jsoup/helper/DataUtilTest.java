package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File inputFile = new File("testInputFile.html");
        Document doc = DataUtil.load(inputFile, null, "http://example.com/");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File inputFile = new File("testInputFile.html");
        Document doc = DataUtil.load(inputFile, "UTF-8", "http://example.com/");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputData = "<html><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputData.getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, null, "http://example.com/");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String inputData = "<html><body><h1>Test</h1></body></html>";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(inputData.getBytes(StandardCharsets.UTF_8.name()));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com/");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDetectedCharset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("<html><head><meta charset='ISO-8859-1'><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8.name()));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.load(inputStream, null, "http://example.com/");
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String inputData = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(inputData.getBytes(StandardCharsets.UTF_8.name()));
        Document doc = DataUtil.parseByteData(byteData, null, "http://example.com/", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String inputData = "<html><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(inputData.getBytes(StandardCharsets.UTF_8.name()));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com/", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        String inputData = "<html><head><meta charset='ISO-8859-1'><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(inputData.getBytes(StandardCharsets.UTF_8.name()));
        Document doc = DataUtil.parseByteData(byteData, null, "http://example.com/", Parser.htmlParser());
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputData = "Test Data";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8.name()));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(inputData.length(), byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = null;
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}