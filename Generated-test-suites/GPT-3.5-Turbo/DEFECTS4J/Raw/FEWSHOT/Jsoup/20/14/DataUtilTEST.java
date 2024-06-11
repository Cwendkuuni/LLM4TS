package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;

public class DataUtilTest {
    
    private Parser mockParser;
    
    @Before
    public void setUp() {
        mockParser = mock(Parser.class);
    }
    
    @Test
    public void testLoadFileWithCharsetNameAndBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        FileInputStream mockStream = mock(FileInputStream.class);
        ByteBuffer mockByteData = mock(ByteBuffer.class);
        Document mockDocument = mock(Document.class);
        
        when(mockStream.read(any(byte[].class))).thenReturn(-1);
        when(mockParser.parseInput(anyString(), anyString())).thenReturn(mockDocument);
        
        Document result = DataUtil.load(file, charsetName, baseUri);
        
        verify(mockStream, times(1)).close();
        verify(mockParser, times(1)).parseInput(anyString(), anyString());
        assertEquals(mockDocument, result);
    }
    
    @Test
    public void testLoadInputStreamWithCharsetNameAndBaseUri() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        ByteBuffer mockByteData = mock(ByteBuffer.class);
        Document mockDocument = mock(Document.class);
        
        when(mockByteData.toString()).thenReturn("test");
        when(mockParser.parseInput(anyString(), anyString())).thenReturn(mockDocument);
        
        Document result = DataUtil.load(inputStream, charsetName, baseUri);
        
        verify(mockParser, times(1)).parseInput(anyString(), anyString());
        assertEquals(mockDocument, result);
    }
    
    @Test
    public void testLoadInputStreamWithCharsetNameBaseUriAndParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        ByteBuffer mockByteData = mock(ByteBuffer.class);
        Document mockDocument = mock(Document.class);
        
        when(mockByteData.toString()).thenReturn("test");
        when(mockParser.parseInput(anyString(), anyString())).thenReturn(mockDocument);
        
        Document result = DataUtil.load(inputStream, charsetName, baseUri, mockParser);
        
        verify(mockParser, times(1)).parseInput(anyString(), anyString());
        assertEquals(mockDocument, result);
    }
    
    @Test
    public void testParseByteDataWithCharsetNameBaseUriAndParser() {
        ByteBuffer byteData = ByteBuffer.allocate(0);
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Document mockDocument = mock(Document.class);
        
        when(mockParser.parseInput(anyString(), anyString())).thenReturn(mockDocument);
        
        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, mockParser);
        
        verify(mockParser, times(1)).parseInput(anyString(), anyString());
        assertEquals(mockDocument, result);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        ByteBuffer result = DataUtil.readToByteBuffer(inputStream);
        
        assertEquals(ByteBuffer.wrap("test".getBytes()), result);
    }
    
    @Test
    public void testGetCharsetFromContentTypeWithContentTypeContainingCharset() {
        String contentType = "text/html; charset=UTF-8";
        
        String result = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", result);
    }
    
    @Test
    public void testGetCharsetFromContentTypeWithContentTypeNotContainingCharset() {
        String contentType = "text/html";
        
        String result = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(result);
    }
}