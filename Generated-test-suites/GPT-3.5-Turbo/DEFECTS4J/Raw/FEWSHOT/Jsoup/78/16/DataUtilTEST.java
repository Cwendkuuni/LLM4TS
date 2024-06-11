package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {
    
    private String baseUri;
    private Parser parser;
    
    @Before
    public void setUp() {
        baseUri = "http://example.com";
        parser = Parser.htmlParser();
    }
    
    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testLoadFromInputStream() throws IOException {
        FileInputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        
        in.close();
    }
    
    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        FileInputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", baseUri, parser);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        
        in.close();
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        OutputStream out = new FileOutputStream("output.txt");
        
        DataUtil.crossStreams(in, out);
        
        in.close();
        out.close();
    }
    
    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.parseInputStream(in, "UTF-8", baseUri, parser);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
        
        in.close();
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        File file = new File("test.html");
        FileInputStream in = new FileInputStream(file);
        int maxSize = (int) file.length();
        
        ByteBuffer buffer = DataUtil.readToByteBuffer(in, maxSize);
        
        assertNotNull(buffer);
        assertEquals(maxSize, buffer.limit());
        
        in.close();
    }
    
    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        File file = new File("test.html");
        FileInputStream in = new FileInputStream(file);
        
        ByteBuffer buffer = DataUtil.readToByteBuffer(in);
        
        assertNotNull(buffer);
        assertEquals((int) file.length(), buffer.limit());
        
        in.close();
    }
    
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        
        ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);
        
        assertNotNull(buffer);
        assertEquals((int) file.length(), buffer.limit());
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        
        assertNotNull(buffer);
        assertEquals(0, buffer.limit());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeNoContentType() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeInvalidCharset() {
        String contentType = "text/html; charset=invalid";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }
    
    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 0x61 });
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        
        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(3, bomCharset.offset);
    }
    
}