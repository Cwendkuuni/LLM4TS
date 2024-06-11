package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testFile.html");
        Document document = DataUtil.load(file, null, "");
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("testFile.html");
        Document document = DataUtil.load(file, "UTF-8", "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}