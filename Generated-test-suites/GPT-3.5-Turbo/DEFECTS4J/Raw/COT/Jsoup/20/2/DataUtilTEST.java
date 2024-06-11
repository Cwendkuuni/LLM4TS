package org.jsoup.helper;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;

import java.io.*;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() {
        try {
            Document doc = DataUtil.load(new File("test.html"), "UTF-8", "http://example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testLoadInputStream() {
        try {
            InputStream in = new FileInputStream("test.html");
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
            in.close();
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream in = new FileInputStream("test.html");
            Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.htmlParser());
            in.close();
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNull(doc);
    }
    
    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inStream = new FileInputStream("test.html");
            ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
            inStream.close();
            assertNotNull(byteData);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
        
        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

}