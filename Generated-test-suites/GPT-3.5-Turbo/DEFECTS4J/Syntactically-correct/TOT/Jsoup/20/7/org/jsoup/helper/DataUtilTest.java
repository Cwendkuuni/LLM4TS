package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoad_File_Success() {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            
            // Assert
            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
            assertEquals("Test HTML", document.title());
            assertEquals(1, document.getElementsByTag("body").size());
        } catch (IOException e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testLoad_File_IOException() {
        // Arrange
        File file = new File("nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            
            // Assert
            fail("IOException should be thrown.");
        } catch (IOException e) {
            // Pass
        }
    }

    @Test
    public void testLoad_InputStream_Success() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("Test HTML".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            
            // Assert
            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
            assertEquals("Test HTML", document.title());
            assertEquals(1, document.getElementsByTag("body").size());
        } catch (IOException e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testLoad_InputStream_IOException() {
        // Arrange
        InputStream inputStream = new FileInputStream("nonexistent.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        // Act
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            
            // Assert
            fail("IOException should be thrown.");
        } catch (IOException e) {
            // Pass
        }
    }

    @Test
    public void testLoad_InputStream_WithParser_Success() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("Test XML".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/xml";
        Parser parser = Parser.xmlParser();
        
        // Act
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            
            // Assert
            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
            assertEquals("Test XML", document.title());
            assertEquals(1, document.getElementsByTag("root").size());
        } catch (IOException e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testLoad_InputStream_WithParser_IOException() {
        // Arrange
        InputStream inputStream = new FileInputStream("nonexistent.xml");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/xml";
        Parser parser = Parser.xmlParser();
        
        // Act
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            
            // Assert
            fail("IOException should be thrown.");
        } catch (IOException e) {
            // Pass
        }
    }

    @Test
    public void testParseByteData_WithCharsetFromMeta_Success() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("<meta charset=\"ISO-8859-1\"><title>Test Page</title>".getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals("Test Page", document.title());
    }

    @Test
    public void testParseByteData_WithCharsetFromContentType_Success() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("Test Content".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals("Test Content", document.title());
    }

    @Test
    public void testParseByteData_WithoutCharset_Success() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("<title>Test Page</title>".getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals("Test Page", document.title());
    }

    @Test
    public void testReadToByteBuffer_Success() {
        // Arrange
        byte[] buffer = "Test Buffer".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
        
        // Act
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            
            // Assert
            assertNotNull(byteData);
            assertEquals(buffer.length, byteData.remaining());
        } catch (IOException e) {
            fail("Exception should not be thrown.");
        }
    }

    @Test
    public void testGetCharsetFromContentType_Success() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_NoCharset() {
        // Arrange
        String contentType = "text/html";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_EmptyCharset() {
        // Arrange
        String contentType = "text/html; charset=";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertNull(charset);
    }
    
}