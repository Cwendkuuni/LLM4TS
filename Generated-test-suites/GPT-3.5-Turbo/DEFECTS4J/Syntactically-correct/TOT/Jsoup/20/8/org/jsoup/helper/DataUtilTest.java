package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void loadFile_validFile_success() {
        // Arrange
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        String expectedTitle = "Test HTML";
        
        // Act
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            
            // Assert
            Assert.assertEquals(expectedTitle, document.title());
        } catch (IOException e) {
            Assert.fail("Exception should not be thrown.");
        }
    }

    @Test
    public void loadFile_invalidFilePath_exceptionThrown() {
        // Arrange
        File file = new File("invalid.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";

        // Act and Assert
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            Assert.fail("Exception should be thrown.");
        } catch (IOException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void loadFile_nullCharset_defaultCharsetUsed() {
        // Arrange
        File file = new File("test.html");
        String charsetName = null;
        String baseUri = "https://example.com";
        String expectedTitle = "Test HTML";

        // Act
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            
            // Assert
            Assert.assertEquals(expectedTitle, document.title());
        } catch (IOException e) {
            Assert.fail("Exception should not be thrown.");
        }
    }
    
    @Test
    public void loadInputStream_validInputStream_success() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("<html><head><title>Test HTML</title></head><body></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        String expectedTitle = "Test HTML";
        
        // Act
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            
            // Assert
            Assert.assertEquals(expectedTitle, document.title());
        } catch (IOException e) {
            Assert.fail("Exception should not be thrown.");
        }
    }

    @Test
    public void loadInputStream_nullCharset_defaultCharsetUsed() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("<html><head><title>Test HTML</title></head><body></body></html>".getBytes());
        String charsetName = null;
        String baseUri = "https://example.com";
        String expectedTitle = "Test HTML";
        
        // Act
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            
            // Assert
            Assert.assertEquals(expectedTitle, document.title());
        } catch (IOException e) {
            Assert.fail("Exception should not be thrown.");
        }
    }
    
    @Test
    public void loadInputStream_invalidInputStream_exceptionThrown() {
        // Arrange
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        
        // Act and Assert
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail("Exception should be thrown.");
        } catch (IOException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void loadInputStream_validParser_success() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("<html><head><title>Test HTML</title></head><body></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        String expectedTitle = "Test HTML";
        
        // Act
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            
            // Assert
            Assert.assertEquals(expectedTitle, document.title());
        } catch (IOException e) {
            Assert.fail("Exception should not be thrown.");
        }
    }

    @Test
    public void loadInputStream_invalidParser_exceptionThrown() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("<html><head><title>Test HTML</title></head><body></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = null;
        
        // Act and Assert
        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
            Assert.fail("Exception should be thrown.");
        } catch (IOException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void parseByteData_validData_success() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><title>Test HTML</title></head><body></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        String expectedTitle = "Test HTML";
        
        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        Assert.assertEquals(expectedTitle, document.title());
    }

    @Test
    public void parseByteData_nullCharsetName_nullCharsetNameUsed() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><title>Test HTML</title></head><body></body></html>".getBytes());
        String charsetName = null;
        String baseUri = "https://example.com";
        Parser parser = Parser.htmlParser();
        String expectedTitle = "Test HTML";
        
        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        Assert.assertEquals(expectedTitle, document.title());
    }
    
    @Test
    public void readToByteBuffer_validInputStream_success() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("<html><head><title>Test HTML</title></head><body></body></html>".getBytes());
        int bufferSize = 0x20000;

        // Act
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            
            // Assert
            Assert.assertEquals(bufferSize, byteData.capacity());
        } catch (IOException e) {
            Assert.fail("Exception should not be thrown.");
        }
    }

    @Test
    public void readToByteBuffer_invalidInputStream_exceptionThrown() {
        // Arrange
        InputStream inputStream = null;
        
        // Act and Assert
        try {
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            Assert.fail("Exception should be thrown.");
        } catch (IOException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void getCharsetFromContentType_validContentType_charsetExtracted() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        Assert.assertEquals(expectedCharset, charset);
    }

    @Test
    public void getCharsetFromContentType_invalidContentType_nullReturned() {
        // Arrange
        String contentType = "text/html";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        Assert.assertNull(charset);
    }

    @Test
    public void getCharsetFromContentType_nullContentType_nullReturned() {
        // Arrange
        String contentType = null;
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        Assert.assertNull(charset);
    }

    @Test
    public void getCharsetFromContentType_emptyContentType_nullReturned() {
        // Arrange
        String contentType = "";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        Assert.assertNull(charset);
    }
}