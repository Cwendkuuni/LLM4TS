package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoad_withValidFileAndCharsetAndBaseUri_returnsDocument() throws IOException {
        // Arrange
        File file = new File("path/to/valid/file.html");
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        
        // Act
        Document document = DataUtil.load(file, charset, baseUri);
        
        // Assert
        Assert.assertNotNull(document);
    }
    
    @Test(expected = IOException.class)
    public void testLoad_withInvalidFile_throwsIOException() throws IOException {
        // Arrange
        File file = new File("path/to/invalid/file.html");
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        
        // Act
        Document document = DataUtil.load(file, charset, baseUri);
    }
    
    @Test
    public void testLoad_withNullCharset_usesDefaultCharset() throws IOException {
        // Arrange
        File file = new File("path/to/valid/file.html");
        String charset = null;
        String baseUri = "http://www.example.com";
        
        // Act
        Document document = DataUtil.load(file, charset, baseUri);
        
        // Assert
        Assert.assertNotNull(document);
        Assert.assertEquals("UTF-8", document.charset().name());
    }
    
    @Test
    public void testLoad_withInputStreamAndCharsetAndBaseUri_returnsDocument() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("/valid/file.html");
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        
        // Act
        Document document = DataUtil.load(inputStream, charset, baseUri);
        
        // Assert
        Assert.assertNotNull(document);
    }
    
    @Test(expected = IOException.class)
    public void testLoad_withInvalidInputStream_throwsIOException() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("/invalid/file.html");
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        
        // Act
        Document document = DataUtil.load(inputStream, charset, baseUri);
    }
    
    @Test
    public void testLoad_withInputStreamAndCharsetAndBaseUriAndParser_returnsDocument() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("/valid/file.html");
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.load(inputStream, charset, baseUri, parser);
        
        // Assert
        Assert.assertNotNull(document);
    }
    
    @Test(expected = IOException.class)
    public void testLoad_withInvalidInputStreamAndCharsetAndBaseUriAndParser_throwsIOException() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("/invalid/file.html");
        String charset = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.load(inputStream, charset, baseUri, parser);
    }
    
    @Test
    public void testParseByteData_withCharsetAndBaseUriAndParser_returnsDocument() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testParseByteData_withNullCharsetAndBaseUriAndParser_returnsDocument() {
        // Arrange
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        String charsetName = null;
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        // Act
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        // Assert
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testReadToByteBuffer_withValidInputStream_returnsByteBuffer() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("/valid/file.html");
        
        // Act
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        
        // Assert
        Assert.assertNotNull(byteData);
    }
    
    @Test(expected = IOException.class)
    public void testReadToByteBuffer_withInvalidInputStream_throwsIOException() throws IOException {
        // Arrange
        InputStream inputStream = getClass().getResourceAsStream("/invalid/file.html");
        
        // Act
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
    }
    
    @Test
    public void testGetCharsetFromContentType_withValidContentType_returnsCharset() {
        // Arrange
        String contentType = "text/html; charset=UTF-8";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        Assert.assertNotNull(charset);
        Assert.assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_withInvalidContentType_returnsNull() {
        // Arrange
        String contentType = "text/html; encoding=UTF-8";
        
        // Act
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        Assert.assertNull(charset);
    }
}