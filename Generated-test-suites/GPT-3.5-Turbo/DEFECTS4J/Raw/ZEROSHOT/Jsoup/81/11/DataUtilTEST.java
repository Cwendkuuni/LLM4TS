package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DataUtilTest {

    private static final String BASE_URI = "http://example.com";

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, null, BASE_URI);
        
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        Document document = DataUtil.load(inputStream, null, BASE_URI);
        
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.xml");
        Document document = DataUtil.load(inputStream, null, BASE_URI, Parser.xmlParser());
        
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.txt");
        OutputStream outputStream = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(inputStream, outputStream);
        
        assertNotNull(outputStream);
        assertEquals("This is a test file.", outputStream.toString());
    }
    
    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        Document document = DataUtil.parseInputStream(inputStream, null, BASE_URI, Parser.htmlParser());
        
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromInvalidContentType() {
        String contentType = "text/html; charset=invalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.txt");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        
        assertNotNull(byteBuffer);
        assertEquals(20, byteBuffer.limit());
    }
    
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.txt");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        
        assertNotNull(byteBuffer);
        assertEquals(20, byteBuffer.limit());
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.limit());
    }
    
    @Test
    public void testParseInputStreamWithBomCharset() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test_bom.xml");
        Document document = DataUtil.parseInputStream(inputStream, null, BASE_URI, Parser.xmlParser());
        
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", BASE_URI, Parser.htmlParser());
        
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }
    
    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test.html");
        Document document = DataUtil.parseInputStream(inputStream, "invalidCharset", BASE_URI, Parser.htmlParser());
        
        assertNull(document);
    }
    
    @Test
    public void testParseInputStreamEmptyBody() throws IOException {
        InputStream inputStream = null;
        Document document = DataUtil.parseInputStream(inputStream, null, BASE_URI, Parser.htmlParser());
        
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }
}