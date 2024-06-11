package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws Exception {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        // Act
        Document doc = DataUtil.load(file, charsetName, baseUri);
        
        // Assert
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testLoadInputStream() throws Exception {
        // Arrange
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        // Act
        Document doc = DataUtil.load(in, charsetName, baseUri);
        
        // Assert
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        // Arrange
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        
        // Assert
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testCrossStreams() throws Exception {
        // Arrange
        InputStream in = getClass().getResourceAsStream("test.html");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        // Act
        DataUtil.crossStreams(in, out);
        
        // Assert
        assertTrue(out.size() > 0);
    }
    
    @Test
    public void testParseByteData() throws Exception {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("<html></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() throws Exception {
        // Arrange
        InputStream in = getClass().getResourceAsStream("test.html");
        int maxSize = 1024;
        
        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, maxSize);
        
        // Assert
        assertTrue(byteBuffer.limit() <= maxSize);
    }
    
    @Test
    public void testReadToByteBuffer() throws Exception {
        // Arrange
        InputStream in = getClass().getResourceAsStream("test.html");
        
        // Act
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in);
        
        // Assert
        assertTrue(byteBuffer.limit() > 0);
    }
    
    @Test
    public void testReadFileToByteBuffer() throws Exception {
        // Arrange
        File file = new File("test.html");
        
        // Act
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        
        // Assert
        assertTrue(byteBuffer.limit() > 0);
    }
    
    @Test
    public void testEmptyByteBuffer() throws Exception {
        // Act
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        
        // Assert
        assertEquals(0, byteBuffer.limit());
    }
    
    @Test
    public void testGetCharsetFromContentType() throws Exception {
        // Arrange
        String contentType = "text/html; charset=UTF-8";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testMimeBoundary() throws Exception {
        // Act
        String boundary = DataUtil.mimeBoundary();
        
        // Assert
        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }
    
    @Test
    public void testDetectCharsetFromBom() throws Exception {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, '<', 'h', 't', 'm', 'l', '>', '<', '/', 'h', 't', 'm', 'l', '>'});
        String charsetName = "UTF-8";
        
        // Act
        String charset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        
        // Assert
        assertEquals("UTF-8", charset);
    }
}