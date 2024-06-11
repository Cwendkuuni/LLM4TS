package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

public class DataUtilTest {
    private static final String BASE_URI = "https://www.example.com";
    private static final String CHARSET_NAME = "UTF-8";
    
    private DataUtil dataUtil;
    private File testFile;
    private InputStream testInputStream;
    
    @Before
    public void setUp() throws IOException {
        dataUtil = new DataUtil();
        testFile = new File("test.html");
        testInputStream = new ByteArrayInputStream("<html><body><h1>Test</h1></body></html>".getBytes());
    }
    
    @Test
    public void testLoadFromFile() throws IOException {
        Document doc = dataUtil.load(testFile, CHARSET_NAME, BASE_URI);
        
        assertEquals(Jsoup.parse(testFile, CHARSET_NAME, BASE_URI).html(), doc.html());
    }
    
    @Test
    public void testLoadFromInputStream() throws IOException {
        Document doc = dataUtil.load(testInputStream, CHARSET_NAME, BASE_URI);
        
        assertEquals(Jsoup.parse(testInputStream, CHARSET_NAME, BASE_URI).html(), doc.html());
    }
    
    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        Parser parser = Parser.xmlParser();
        Document doc = dataUtil.load(testInputStream, CHARSET_NAME, BASE_URI, parser);
        
        assertEquals(Jsoup.parse(testInputStream, CHARSET_NAME, BASE_URI, parser).html(), doc.html());
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = dataUtil.crossStreams(testInputStream, outputStream);
        
        assertEquals(Jsoup.parse(testInputStream, CHARSET_NAME, BASE_URI).html(), Jsoup.parse(new ByteArrayInputStream(outputStream.toByteArray()), CHARSET_NAME, BASE_URI).html());
    }
    
    @Test
    public void testParseInputStream() throws IOException {
        Document doc = dataUtil.parseInputStream(testInputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        
        assertEquals(Jsoup.parse(testInputStream, CHARSET_NAME, BASE_URI).html(), doc.html());
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteBuffer buffer = dataUtil.readToByteBuffer(testInputStream, 1024);
        
        assertEquals(Charset.forName(CHARSET_NAME).decode(buffer).toString(), "<html><body><h1>Test</h1></body></html>");
    }
    
    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        ByteBuffer buffer = dataUtil.readToByteBuffer(testInputStream);
        
        assertEquals(Charset.forName(CHARSET_NAME).decode(buffer).toString(), "<html><body><h1>Test</h1></body></html>");
    }
    
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        ByteBuffer buffer = dataUtil.readFileToByteBuffer(testFile);
        
        assertEquals(Charset.forName(CHARSET_NAME).decode(buffer).toString(), "<html><body><h1>Test</h1></body></html>");
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = dataUtil.emptyByteBuffer();
        
        assertEquals(buffer.capacity(), 0);
        assertNull(buffer.array());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        
        assertEquals(charset, "UTF-8");
    }
    
    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String contentType = "text/html";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String boundary = dataUtil.mimeBoundary();
        
        assertEquals(boundary.length(), 32);
    }
    
    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer bomByteBuffer = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        DataUtil.BomCharset bomCharset = dataUtil.detectCharsetFromBom(bomByteBuffer);
        
        assertEquals(bomCharset.charset, "UTF-8");
        assertEquals(bomCharset.offset, 3);
    }
}