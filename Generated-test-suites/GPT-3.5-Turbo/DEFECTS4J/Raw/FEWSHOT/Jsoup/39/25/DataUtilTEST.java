
package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        
        assertNotNull(doc);
        // add assertions for the loaded document
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        
        assertNotNull(doc);
        // add assertions for the loaded document
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        
        assertNotNull(doc);
        // add assertions for the loaded document
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>Hello, World!</h1></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        // add assertions for the parsed document
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] data = "<html><body><h1>Hello, World!</h1></body></html>".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        
        assertNotNull(byteData);
        // add assertions for the byte data
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
        // add assertions for the charset
    }
}
