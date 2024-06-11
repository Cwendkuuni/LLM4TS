package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "");
        
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body><p>Hello, World!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        
        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.html());
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><body><p>Hello, World!</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        
        Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());
        
        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.html());
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("Hello, World!".getBytes());
        byteBuffer.flip();
        
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());
        
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello, World!", document.text());
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        byte[] bytes = "Hello, World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 5);
        
        Assert.assertEquals(5, byteBuffer.remaining());
    }
    
    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        byte[] bytes = "Hello, World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        
        Assert.assertEquals(bytes.length, byteBuffer.remaining());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        Assert.assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String contentType = "text/html;";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        Assert.assertNull(charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=invalid";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        Assert.assertNull(charset);
    }
}