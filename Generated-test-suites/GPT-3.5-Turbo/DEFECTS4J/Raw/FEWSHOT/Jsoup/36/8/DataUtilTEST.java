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
import java.nio.charset.StandardCharsets;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, null, "");

        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithCharset() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "");

        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, "");

        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, "UTF-8", "");

        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(inputStream, null, "", parser);

        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithCharsetAndParser() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(inputStream, "UTF-8", "", parser);

        assertNotNull(document);
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithNullCharset() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteBuffer, null, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithCharset() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        int maxSize = 1024;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "application/json";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=ISO-8859-1";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }
}