package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.Assert.*;

public class DataUtilTest {
    @Test
    public void testLoad_File_CharsetName_BaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }
    
    @Test
    public void testLoad_InputStream_CharsetName_BaseUri() throws IOException {
        InputStream in = this.getClass().getResourceAsStream("/test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(in, charsetName, baseUri);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }
    
    @Test
    public void testLoad_InputStream_CharsetName_BaseUri_Parser() throws IOException {
        InputStream in = this.getClass().getResourceAsStream("/test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = this.getClass().getResourceAsStream("/test.html");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(in, out);
        
        assertEquals(2071, out.toByteArray().length);
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset().name());
    }
    
    @Test
    public void testReadToByteBuffer_InputStream_MaxSize() throws IOException {
        InputStream in = this.getClass().getResourceAsStream("/test.html");
        int maxSize = 100;
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, maxSize);
        
        assertEquals(100, byteBuffer.limit());
    }
    
    @Test
    public void testReadToByteBuffer_InputStream() throws IOException {
        InputStream in = this.getClass().getResourceAsStream("/test.html");
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in);
        
        assertEquals(2071, byteBuffer.limit());
    }
    
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File(this.getClass().getResource("/test.html").getFile());
        
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        
        assertEquals(2071, byteBuffer.limit());
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        
        assertEquals(0, byteBuffer.limit());
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
        
        assertEquals(32, mimeBoundary.length());
    }
}