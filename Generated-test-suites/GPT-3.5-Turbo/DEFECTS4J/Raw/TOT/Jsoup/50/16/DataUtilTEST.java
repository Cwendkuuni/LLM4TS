package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.UnsupportedCharsetException;

import static org.junit.Assert.*;

public class DataUtilTest {
    
    private File testFile;
    
    @Before
    public void setUp() {
        testFile = new File("test.html"); // Assuming we have a test HTML file in the same directory as the test class
    }
    
    @Test
    public void testLoad_file_validCharsetAndBaseUri_returnsDocument() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(testFile, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testLoad_file_invalidCharset_throwsIOException() {
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        
        assertThrows(IOException.class, () -> {
            DataUtil.load(testFile, charsetName, baseUri);
        });
    }
    
    @Test
    public void testLoad_file_nullBaseUri_returnsDocumentWithEmptyBaseUri() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = null;
        
        Document document = DataUtil.load(testFile, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals("", document.baseUri());
    }
    
    @Test
    public void testLoad_inputStream_validCharsetAndBaseUri_returnsDocument() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testLoad_inputStream_charsetNotSupported_throwsUnsupportedCharsetException() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        
        assertThrows(UnsupportedCharsetException.class, () -> {
            DataUtil.load(inputStream, charsetName, baseUri);
        });
    }
    
    @Test
    public void testLoad_inputStream_nullBaseUri_returnsDocumentWithEmptyBaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = null;
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals("", document.baseUri());
    }
    
    @Test
    public void testLoad_inputStreamAndParser_validCharsetAndBaseUri_returnsDocument() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.xml");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testLoad_inputStreamAndParser_charsetNotSupported_throwsUnsupportedCharsetException() {
        InputStream inputStream = getClass().getResourceAsStream("test.xml");
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        
        assertThrows(UnsupportedCharsetException.class, () -> {
            DataUtil.load(inputStream, charsetName, baseUri, parser);
        });
    }
    
    @Test
    public void testLoad_inputStreamAndParser_nullBaseUri_returnsDocumentWithEmptyBaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.xml");
        String charsetName = "UTF-8";
        String baseUri = null;
        Parser parser = Parser.xmlParser();
        
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
        assertEquals("", document.baseUri());
    }
    
    @Test
    public void testCrossStreams_validInputStreamAndOutputStream_noExceptionsThrown() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        OutputStream outputStream = new ByteArrayOutputStream();
        
        assertDoesNotThrow(() -> {
            DataUtil.crossStreams(inputStream, outputStream);
        });
    }
    
    @Test
    public void testParseByteData_withCharsetAndBaseUri_returnsDocument() {
        ByteBuffer byteData = ByteBuffer.wrap("<html></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }
    
    @Test
    public void testParseByteData_missingCharsetInMetaTag_returnsDocumentWithDefaultCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html\"></head></html>".getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
        assertEquals("UTF-8", document.outputSettings().charset());
    }
    
    @Test
    public void testReadToByteBuffer_validInputStreamAndMaxSize_returnsByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        int maxSize = 1024;
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);
        
        assertNotNull(byteBuffer);
        assertEquals(maxSize, byteBuffer.capacity());
    }
    
    @Test
    public void testReadFileToByteBuffer_existingFile_returnsByteBuffer() throws IOException {
        File file = new File("test.html");
        
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        
        assertNotNull(byteBuffer);
    }
    
    @Test
    public void testEmptyByteBuffer_returnsEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }
    
    @Test
    public void testGetCharsetFromContentType_withValidContentType_returnsCharset() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentType_withInvalidCharset_returnsNull() {
        String contentType = "text/html; charset=InvalidCharset";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
    
    @Test
    public void testMimeBoundary_returnsValidMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
}