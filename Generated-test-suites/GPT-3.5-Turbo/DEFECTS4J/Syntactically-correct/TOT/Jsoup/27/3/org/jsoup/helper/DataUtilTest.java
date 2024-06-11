package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.charset.Charset;

class DataUtilTest {
    private File testFile;
    private String baseUri;
    
    @BeforeEach
    public void setUp() {
        testFile = new File("testfile.html");
        baseUri = "https://www.example.com";
    }
    
    @Test
    public void testLoad_file() throws IOException {
        Document doc = DataUtil.load(testFile, "UTF-8", baseUri);
        assertNotNull(doc);
        
        // test if doc contains desired elements
        
    }
    
    @Test
    public void testLoad_inputStream() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testfile.html");
        assertNotNull(inputStream);
        
        Document doc = DataUtil.load(inputStream, "UTF-8", baseUri);
        assertNotNull(doc);
        
        // test if doc contains desired elements
        
    }
    
    @Test
    public void testLoad_inputStream_withParser() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testfile.html");
        assertNotNull(inputStream);
        
        Document doc = DataUtil.load(inputStream, "UTF-8", baseUri, Parser.xmlParser());
        assertNotNull(doc);
        
        // test if doc contains desired elements
        
    }
    
    @Test
    public void testParseByteData_withCharset() {
        Charset charset = Charset.forName("UTF-8");
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        ByteBuffer byteData = ByteBuffer.wrap("Test data".getBytes());
        
        Document doc = DataUtil.parseByteData(byteData, charset.displayName(), baseUri, parser);
        assertNotNull(doc);
        
        // test if doc contains desired elements
        
    }
    
    @Test
    public void testParseByteData_withoutCharset() {
        Charset defaultCharset = Charset.forName("UTF-8");
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        ByteBuffer byteData = ByteBuffer.wrap("Test data".getBytes());
        
        Document doc = DataUtil.parseByteData(byteData, null, baseUri, parser);
        assertNotNull(doc);
        
        // test if doc contains desired elements
        
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testfile.html");
        assertNotNull(inputStream);
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
        
        // test if byteData matches expected values
        
    }
    
    @Test
    public void testGetCharsetFromContentType_withSupportedCharset() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_withUnsupportedCharset() {
        String contentType = "text/html; charset=ISO-8859-1";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}