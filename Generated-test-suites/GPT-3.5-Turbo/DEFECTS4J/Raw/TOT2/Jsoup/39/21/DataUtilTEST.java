package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFileCharsetBaseUri() throws IOException {
        File testFile = new File("test.html");
        Document doc = DataUtil.load(testFile, "UTF-8", "http://example.com");
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testLoadInputStreamCharsetBaseUri() throws IOException {
        String html = "<html><head><title>Test</title></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes());
        
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testLoadInputStreamCharsetBaseUriParser() throws IOException {
        String html = "<html><head><title>Test</title></head><body></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes());
        
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.xmlParser());
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("xml", doc.parser().settings().get("parser"));
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String content = "Test content";
        InputStream in = new ByteArrayInputStream(content.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, content.length());
        
        assertEquals(content, new String(byteBuffer.array()));
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        String content = "Test content";
        InputStream in = new ByteArrayInputStream(content.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in);
        
        assertEquals(content, new String(byteBuffer.array()));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
}