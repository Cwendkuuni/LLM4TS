package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

public class DataUtilTest {
    
    @Test
    public void testLoad() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            Assert.assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadInputStream() {
        InputStream in = new ByteArrayInputStream("test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = DataUtil.load(in, charsetName, baseUri);
            Assert.assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadInputStreamParser() {
        InputStream in = new ByteArrayInputStream("test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        try {
            Document doc = DataUtil.load(in, charsetName, baseUri, parser);
            Assert.assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        Assert.assertNotNull(doc);
    }
    
    @Test
    public void testReadToByteBuffer() {
        InputStream in = new ByteArrayInputStream("test data".getBytes());
        int maxSize = 0;
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(in, maxSize);
            Assert.assertNotNull(byteData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadToByteBufferCapped() {
        InputStream in = new ByteArrayInputStream("test data".getBytes());
        int maxSize = 10;
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(in, maxSize);
            Assert.assertNotNull(byteData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeNonSupportedCharset() {
        String contentType = "text/html; charset=invalid-charset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeUpperCaseCharset() {
        String contentType = "text/html; charset=utf-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }
    
}