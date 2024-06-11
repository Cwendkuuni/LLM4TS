
package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;

public class DataUtilTest {
    
    @Mock
    private File fileMock;
    
    @Mock
    private InputStream inputStreamMock;
    
    @Mock
    private ByteBuffer byteBufferMock;
    
    @Mock
    private Document documentMock;
    
    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testLoad_withFile() throws IOException {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.canRead()).thenReturn(true);
        when(fileMock.isFile()).thenReturn(true);
        
        when(inputStreamMock.available()).thenReturn(1);
        when(inputStreamMock.read(any(byte[].class), anyInt(), anyInt())).thenReturn(1, -1);
        
        when(byteBufferMock.array()).thenReturn(new byte[1]);
        
        when(documentMock.select(anyString())).thenReturn(null);
        
        Parser parser = Parser.htmlParser();
        
        // Act
        Document result = DataUtil.load(fileMock, charsetName, baseUri);
        
        // Assert
        assertEquals(documentMock, result);
        verify(fileMock).exists();
        verify(fileMock).canRead();
        verify(fileMock).isFile();
        verify(fileMock).length();
        verify(inputStreamMock).available();
        verify(inputStreamMock).read(any(byte[].class), anyInt(), anyInt());
        verify(byteBufferMock).array();
        verify(documentMock).select(anyString());
    }
    
    @Test
    public void testLoad_withInputStream() throws IOException {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        when(inputStreamMock.available()).thenReturn(1);
        when(inputStreamMock.read(any(byte[].class), anyInt(), anyInt())).thenReturn(1, -1);
        
        when(byteBufferMock.array()).thenReturn(new byte[1]);
        
        when(documentMock.select(anyString())).thenReturn(null);
        
        Parser parser = Parser.htmlParser();
        
        // Act
        Document result = DataUtil.load(inputStreamMock, charsetName, baseUri);
        
        // Assert
        assertEquals(documentMock, result);
        verify(inputStreamMock).available();
        verify(inputStreamMock).read(any(byte[].class), anyInt(), anyInt());
        verify(byteBufferMock).array();
        verify(documentMock).select(anyString());
    }
    
    @Test
    public void testLoad_withInputStreamAndParser() throws IOException {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        when(inputStreamMock.available()).thenReturn(1);
        when(inputStreamMock.read(any(byte[].class), anyInt(), anyInt())).thenReturn(1, -1);
        
        when(byteBufferMock.array()).thenReturn(new byte[1]);
        
        when(documentMock.select(anyString())).thenReturn(null);
        
        Parser parser = Parser.xmlParser();
        
        // Act
        Document result = DataUtil.load(inputStreamMock, charsetName, baseUri, parser);
        
        // Assert
        assertEquals(documentMock, result);
        verify(inputStreamMock).available();
        verify(inputStreamMock).read(any(byte[].class), anyInt(), anyInt());
        verify(byteBufferMock).array();
        verify(documentMock).select(anyString());
    }
    
    @Test
    public void testParseByteData_withNullCharset() {
        // Arrange
        String charsetName = null;
        String baseUri = "http://example.com";
        
        when(charsetPattern.matcher(anyString())).thenReturn(null);
        
        when(byteBufferMock.array()).thenReturn(new byte[1]);
        
        when(documentMock.select(anyString())).thenReturn(null);
        
        Parser parser = Parser.htmlParser();
        
        // Act
        Document result = DataUtil.parseByteData(byteBufferMock, charsetName, baseUri, parser);
        
        // Assert
        assertEquals(documentMock, result);
        verify(charsetPattern).matcher(anyString());
        verify(byteBufferMock).array();
        verify(documentMock).select(anyString());
    }
    
    @Test
    public void testParseByteData_withCharset() {
        // Arrange
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        when(charsetPattern.matcher(anyString())).thenReturn(null);
        
        when(byteBufferMock.array()).thenReturn(new byte[1]);
        
        when(documentMock.select(anyString())).thenReturn(null);
        
        Parser parser = Parser.htmlParser();
        
        // Act
        Document result = DataUtil.parseByteData(byteBufferMock, charsetName, baseUri, parser);
        
        // Assert
        assertEquals(documentMock, result);
        verify(charsetPattern).matcher(anyString());
        verify(byteBufferMock).array();
        verify(documentMock).select(anyString());
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        // Arrange
        byte[] buffer = new byte[DataUtil.bufferSize];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(DataUtil.bufferSize);
        int read = 1;
        
        when(inputStreamMock.read(any(byte[].class), anyInt(), anyInt())).thenReturn(1, -1);
        when(outStream.toByteArray()).thenReturn(buffer);
        
        // Act
        ByteBuffer result = DataUtil.readToByteBuffer(inputStreamMock);
        
        // Assert
        assertEquals(byteBufferMock, result);
        verify(inputStreamMock).read(any(byte[].class), anyInt(), anyInt());
        verify(outStream).toByteArray();
    }
    
    @Test
    public void testGetCharsetFromContentType_withNullContentType() {
        // Arrange
        String contentType = null;
        
        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testGetCharsetFromContentType_withCharset() {
        // Arrange
        String contentType = "text/html; charset=gb2312";
        
        // Act
        String result = DataUtil.getCharsetFromContentType(contentType);
        
        // Assert
        assertEquals("GB2312", result);
    }
}
