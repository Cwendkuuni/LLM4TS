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

    private static final String BASE_URI = "https://www.example.com/";

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", BASE_URI);
        
        assertNotNull(doc);
        // Add assertions here to test the loaded document
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><body><h1>Hello, world!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        
        Document doc = DataUtil.load(inputStream, "UTF-8", BASE_URI);
        
        assertNotNull(doc);
        // Add assertions here to test the loaded document
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String xml = "<root><element>Value</element></root>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        
        Document doc = DataUtil.load(inputStream, "UTF-8", BASE_URI, Parser.xmlParser());
        
        assertNotNull(doc);
        // Add assertions here to test the loaded document
    }
    
    @Test
    public void testParseByteDataWithCharset() {
        byte[] bytes = "<html><body><h1>Hello, world!</h1></body></html>".getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteData = ByteBuffer.wrap(bytes);
        
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", BASE_URI, Parser.htmlParser());
        
        assertNotNull(doc);
        // Add assertions here to test the parsed document
    }
    
    @Test
    public void testParseByteDataWithoutCharset() {
        byte[] bytes = "<html><body><h1>Hello, world!</h1></body></html>".getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteData = ByteBuffer.wrap(bytes);
        
        Document doc = DataUtil.parseByteData(byteData, null, BASE_URI, Parser.htmlParser());
        
        assertNotNull(doc);
        // Add assertions here to test the parsed document
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String input = "<html><body><h1>Hello, world!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        
        assertNotNull(byteData);
        // Add assertions here to test the byte data
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

    @Test
    public void testGetCharsetFromContentTypeInvalidCharset() {
        String contentType = "text/html; charset=invalid";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
}