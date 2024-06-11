package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

public class DataUtilTest {
  
    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            
            Document doc = DataUtil.load(file, charsetName, baseUri);
            
            assertNotNull(doc);
            assertEquals(baseUri, doc.baseUri());
        } catch (IOException e) {
            fail("IO Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            
            assertNotNull(doc);
            assertEquals(baseUri, doc.baseUri());
        } catch (IOException e) {
            fail("IO Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.htmlParser();
            
            Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
            
            assertNotNull(doc);
            assertEquals(baseUri, doc.baseUri());
        } catch (IOException e) {
            fail("IO Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testParseByteDataWithNullCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<html></html>".getBytes(Charset.forName("UTF-8")));
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testParseByteDataWithSpecificCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<html></html>".getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("test.html");
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            
            assertNotNull(byteData);
        } catch (IOException e) {
            fail("IO Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
}