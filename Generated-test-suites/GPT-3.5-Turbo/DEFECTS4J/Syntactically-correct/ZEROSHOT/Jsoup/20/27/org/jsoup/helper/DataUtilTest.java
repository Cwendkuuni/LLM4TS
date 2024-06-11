package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
    
    @Test
    public void testLoadFromFile() throws IOException {
        File file = mock(File.class);
        FileInputStream inStream = mock(FileInputStream.class);
        
        when(file.getName()).thenReturn("test.html");
        when(file.length()).thenReturn(100L);
        when(file.canRead()).thenReturn(true);
        when(file.exists()).thenReturn(true);
        when(new FileInputStream(file)).thenReturn(inStream);        
        
        Document document = DataUtil.load(file, "UTF-8", "https://www.example.com");
        
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        // perform other assertions
        
        inStream.close();
    }
    
    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        
        Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com");
        
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        // perform other assertions
        
        inputStream.close();
    }
    
    @Test
    public void testLoadFromInputStreamWithCustomParser() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        Parser parser = mock(Parser.class);
        
        Document document = DataUtil.load(inputStream, "UTF-8", "https://www.example.com", parser);
        
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        // perform other assertions
        
        inputStream.close();
    }
    
    @Test
    public void testParseByteDataWithCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(100);
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = mock(Parser.class);
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        // perform other assertions
    }
    
    @Test
    public void testParseByteDataWithoutCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(100);
        String charsetName = null;
        String baseUri = "https://www.example.com";
        Parser parser = mock(Parser.class);
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(document);
        assertEquals("https://www.example.com", document.baseUri());
        // perform other assertions
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = mock(InputStream.class);
        
        byte[] buffer = new byte[DataUtil.bufferSize];
        when(inStream.read(buffer)).thenReturn(DataUtil.bufferSize);
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream);
        
        assertNotNull(byteData);
        // perform other assertions
        
        inStream.close();
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", charset);
    }
}