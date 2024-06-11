package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><head><title>Test</title></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("<html><head><title>Test</title></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteData() {
        // Test for default charset
        Document document = DataUtil.parseByteData(null, null, "http://example.com", Parser.htmlParser());
        assertNotNull(document);

        // Test for specified charset
        Document document2 = DataUtil.parseByteData(null, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document2);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] data = "<html><head><title>Test</title></head><body></body></html>".getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(data);
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");

        assertNotNull(document);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        String invalidContentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(invalidContentType);
        assertEquals(null, charset);
    }
}