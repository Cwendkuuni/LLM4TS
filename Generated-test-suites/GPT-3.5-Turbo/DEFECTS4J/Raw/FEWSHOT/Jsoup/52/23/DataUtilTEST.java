package org.jsoup.helper;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DataUtilTest {
 
    @Test
    public void testLoadFile() {
        try {
            Document doc = DataUtil.load(new File("test.html"), "UTF-8", "http://example.com");
            
            assertNotNull(doc);
            assertEquals("http://example.com", doc.baseUri());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            
            assertNotNull(doc);
            assertEquals("http://example.com", doc.baseUri());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            
            assertNotNull(doc);
            assertEquals("http://example.com", doc.baseUri());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testCrossStreams() {
        try {
            InputStream in = getClass().getResourceAsStream("test.txt");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            
            DataUtil.crossStreams(in, out);
            
            byte[] expected = "This is a test file.".getBytes();
            byte[] actual = out.toByteArray();
            
            assertArrayEquals(expected, actual);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>Hello world!</h1></body></html>".getBytes());
        
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", Parser.htmlParser());
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("<html><body><h1>Hello world!</h1></body></html>", doc.outerHtml());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
        
    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap("".getBytes());
        String charsetName = "UTF-8";
        
        String detectedCharset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        
        assertEquals(charsetName, detectedCharset);
    }
}