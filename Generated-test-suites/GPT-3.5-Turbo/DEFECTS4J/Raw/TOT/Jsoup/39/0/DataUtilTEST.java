
package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String CHARSET_EUC_JP = "EUC-JP";
    
    private File file;
    
    @Before
    public void setUp() {
        file = new File("test.html");
    }
    
    @Test
    public void testLoad_File_ValidCharset_ValidBaseUri_ReturnsDocument() throws IOException {
        // Arrange
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        FileUtils.writeStringToFile(file, html, StandardCharsets.UTF_8);
        
        // Act
        Document document = DataUtil.load(file, CHARSET_UTF8, "https://www.example.com");
        
        // Assert
        assertNotNull(document);
    }
    
    @Test
    public void testLoad_File_InvalidCharset_ValidBaseUri_ThrowsIOException() {
        // Arrange
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        FileUtils.writeStringToFile(file, html, StandardCharsets.UTF_8);
        
        // Act & Assert
        try {
            Document document = DataUtil.load(file, "INVALID_CHARSET", "https://www.example.com");
        } catch (IOException e) {
            // Expected IOException
            return;
        }
        
        // Fail if no exception is thrown
        fail("Should have thrown IOException");
    }
    
    @Test
    public void testLoad_InputSteam_ValidCharset_ValidBaseUri_ReturnsDocument() throws IOException {
        // Arrange
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        
        // Act
        Document document = DataUtil.load(inputStream, CHARSET_UTF8, "https://www.example.com");
        
        // Assert
        assertNotNull(document);
    }
    
    @Test
    public void testLoad_InputSteam_InvalidCharset_ValidBaseUri_ThrowsIOException() throws IOException {
        // Arrange
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        
        // Act & Assert
        try {
            Document document = DataUtil.load(inputStream, "INVALID_CHARSET", "https://www.example.com");
        } catch (IOException e) {
            // Expected IOException
            return;
        }
        
        // Fail if no exception is thrown
        fail("Should have thrown IOException");
    }
    
    @Test
    public void testLoad_InputSteam_ValidCharset_ValidBaseUri_SpecifiedParser_ReturnsDocument() throws IOException {
        // Arrange
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.load(inputStream, CHARSET_UTF8, "https://www.example.com", parser);
        
        // Assert
        assertNotNull(document);
    }
    
    @Test
    public void testParseByteData_ValidCharset_ValidBaseUri_ValidParser_ReturnsDocument() {
        // currently it is not possible to mock ByteBuffer in JUnit 4
        
        // Arrange
        String html = "<html><head><title>Test HTML</title></head><body><h1>Hello, World!</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));
        
        // Act
        Document document = DataUtil.parseByteData(byteData, CHARSET_UTF8, "https://www.example.com", Parser.htmlParser());
        
        // Assert
        assertNotNull(document);
    }
    
    @Test
    public void testGetCharsetFromContentType_ValidContentTypeWithCharset_ReturnsCharset() {
        // Arrange
        String contentType = "text/html; charset=EUC-JP";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertEquals(CHARSET_EUC_JP, charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_ValidContentTypeWithoutCharset_ReturnsNull() {
        // Arrange
        String contentType = "text/html";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertNull(charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_InvalidContentType_ReturnsNull() {
        // Arrange
        String contentType = "InvalidContent";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertNull(charset);
    }
    
    // Additional test cases for other methods...
}

