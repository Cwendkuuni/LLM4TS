package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {
    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document result = DataUtil.load(file, charsetName, baseUri);
        
        Document expected = Jsoup.parse(file, charsetName, baseUri);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document result = DataUtil.load(in, charsetName, baseUri);
        
        Document expected = Jsoup.parse(in, charsetName, baseUri);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document result = DataUtil.load(in, charsetName, baseUri, parser);
        
        Document expected = Jsoup.parse(in, charsetName, baseUri, parser);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        
        Assert.assertArrayEquals("test".getBytes(), out.toByteArray());
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        Document expected = Jsoup.parse(new String(byteData.array()), baseUri, parser);
        expected.outputSettings().charset(charsetName);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        int maxSize = 100;
        ByteBuffer result = DataUtil.readToByteBuffer(inStream, maxSize);
        
        byte[] buffer = new byte[maxSize];
        inStream.read(buffer);
        ByteBuffer expected = ByteBuffer.wrap(buffer);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        FileInputStream inStream = new FileInputStream("test.html");
        ByteBuffer result = DataUtil.readToByteBuffer(inStream);
        
        File file = new File("test.html");
        byte[] buffer = new byte[(int) file.length()];
        inStream.read(buffer);
        ByteBuffer expected = ByteBuffer.wrap(buffer);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer result = DataUtil.readFileToByteBuffer(file);
        
        FileInputStream inStream = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inStream.read(buffer);
        ByteBuffer expected = ByteBuffer.wrap(buffer);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer result = DataUtil.emptyByteBuffer();
        
        ByteBuffer expected = ByteBuffer.allocate(0);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html; charset=EUC-JP";
        String contentType3 = "text/html";
        
        String result1 = DataUtil.getCharsetFromContentType(contentType1);
        String result2 = DataUtil.getCharsetFromContentType(contentType2);
        String result3 = DataUtil.getCharsetFromContentType(contentType3);
        
        String expected1 = "UTF-8";
        String expected2 = "EUC-JP";
        String expected3 = null;
        
        Assert.assertEquals(expected1, result1);
        Assert.assertEquals(expected2, result2);
        Assert.assertEquals(expected3, result3);
    }
    
    @Test
    public void testMimeBoundary() {
        String result = DataUtil.mimeBoundary();
        
        Assert.assertNotNull(result);
        Assert.assertEquals(DataUtil.boundaryLength, result.length());
    }
}