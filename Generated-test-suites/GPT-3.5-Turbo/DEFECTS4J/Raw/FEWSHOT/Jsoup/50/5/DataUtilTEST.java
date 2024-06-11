package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;

public class DataUtilTest {
    
    private DataUtil dataUtil;
    private File file;
    private InputStream inputStream;
    private ByteBuffer byteBuffer;
    private String charsetName;
    private String baseUri;
    private Parser parser;
    private Document document;
    
    @Before
    public void setup() {
        dataUtil = new DataUtil();
        file = new File("test.html");
        inputStream = mock(InputStream.class);
        byteBuffer = mock(ByteBuffer.class);
        charsetName = "UTF-8";
        baseUri = "https://www.example.com";
        parser = mock(Parser.class);
        document = mock(Document.class);
    }
    
    @Test
    public void testLoadWithFile() throws IOException {
        when(dataUtil.readFileToByteBuffer(file)).thenReturn(byteBuffer);
        when(dataUtil.parseByteData(byteBuffer, charsetName, baseUri, Parser.htmlParser())).thenReturn(document);
        
        Document result = dataUtil.load(file, charsetName, baseUri);
        
        assertEquals(document, result);
    }
    
    @Test
    public void testLoadWithInputStream() throws IOException {
        when(dataUtil.readToByteBuffer(inputStream)).thenReturn(byteBuffer);
        when(dataUtil.parseByteData(byteBuffer, charsetName, baseUri, Parser.htmlParser())).thenReturn(document);
        
        Document result = dataUtil.load(inputStream, charsetName, baseUri);
        
        assertEquals(document, result);
    }
    
    @Test
    public void testLoadWithInputStreamAndParser() throws IOException {
        when(dataUtil.readToByteBuffer(inputStream)).thenReturn(byteBuffer);
        when(dataUtil.parseByteData(byteBuffer, charsetName, baseUri, parser)).thenReturn(document);
        
        Document result = dataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertEquals(document, result);
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        InputStream inStream = mock(InputStream.class);
        OutputStream outStream = mock(OutputStream.class);
        byte[] buffer = new byte[DataUtil.bufferSize];
        int len = 100;
        
        when(inStream.read(buffer)).thenReturn(len, -1);
        
        dataUtil.crossStreams(inStream, outStream);
        
        // Verify that the correct methods were called
        assertEquals(buffer, outStream);
    }
    
    @Test
    public void testParseByteDataWithCharsetNull() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        String docData = "test";
        String foundCharset = "UTF-8";
        
        when(byteData.rewind()).thenReturn(byteData);
        when(byteData.toString()).thenReturn(docData);
        when(parser.parseInput(docData, baseUri)).thenReturn(document);
        when(document.select("meta[http-equiv=content-type], meta[charset]")).thenReturn(mock(Element.class));
        when(element.hasAttr("http-equiv")).thenReturn(true);
        when(element.attr("content")).thenReturn("text/html; charset=EUC-JP");
        when(dataUtil.getCharsetFromContentType("text/html; charset=EUC-JP")).thenReturn(foundCharset);
        when(Charset.forName(foundCharset).decode(byteData).toString()).thenReturn(docData);
        when(element.attr("charset")).thenReturn("utf-8");
        when(Charset.isSupported("utf-8")).thenReturn(true);
        when(dataUtil.parseByteData(byteData, foundCharset, baseUri, parser)).thenReturn(document);
        
        Document result = dataUtil.parseByteData(byteData, null, baseUri, parser);
        
        assertEquals(document, result);
    }
    
    @Test
    public void testParseByteDataWithCharsetNameNotNull() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        String docData = "test";
        
        when(byteData.toString()).thenReturn(docData);
        when(parser.parseInput(docData, baseUri)).thenReturn(document);
        when(document.outputSettings()).thenReturn(mock(OutputSettings.class));
        when(outputSettings.charset(charsetName)).thenReturn(outputSettings);
        when(outputSettings.charset()).thenReturn(charsetName);
        
        Document result = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertEquals(document, result);
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inStream = mock(InputStream.class);
        int maxSize = 1000;
        byte[] buffer = new byte[DataUtil.bufferSize];
        ByteArrayOutputStream outStream = mock(ByteArrayOutputStream.class);
        byte[] byteArray = new byte[maxSize];
        
        when(inStream.read(buffer)).thenReturn(maxSize, -1);
        when(Validate.isTrue(maxSize >= 0, "maxSize must be 0 (unlimited) or larger")).thenReturn(true);
        when(outStream.toByteArray()).thenReturn(byteArray);
        when(ByteBuffer.wrap(byteArray)).thenReturn(byteBuffer);
        
        ByteBuffer result = dataUtil.readToByteBuffer(inStream, maxSize);
        
        assertEquals(byteBuffer, result);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = mock(InputStream.class);
        byte[] buffer = new byte[DataUtil.bufferSize];
        ByteArrayOutputStream outStream = mock(ByteArrayOutputStream.class);
        byte[] byteArray = new byte[DataUtil.bufferSize];
        
        when(inStream.read(buffer)).thenReturn(DataUtil.bufferSize, -1);
        when(outStream.toByteArray()).thenReturn(byteArray);
        when(ByteBuffer.wrap(byteArray)).thenReturn(byteBuffer);
        
        ByteBuffer result = dataUtil.readToByteBuffer(inStream);
        
        assertEquals(byteBuffer, result);
    }
    
    @Test
    public void testReadFileToByteBuffer() throws IOException {
        RandomAccessFile randomAccessFile = mock(RandomAccessFile.class);
        byte[] bytes = new byte[DataUtil.bufferSize];
        
        when(randomAccessFile.length()).thenReturn((long) DataUtil.bufferSize);
        when(randomAccessFile.readFully(bytes)).thenReturn(bytes);
        when(RandomAccessFile(file, "r")).thenReturn(randomAccessFile);
        when(ByteBuffer.wrap(bytes)).thenReturn(byteBuffer);
        
        ByteBuffer result = dataUtil.readFileToByteBuffer(file);
        
        assertEquals(byteBuffer, result);
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer result = dataUtil.emptyByteBuffer();
        
        assertEquals(0, result.capacity());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String result = dataUtil.getCharsetFromContentType(contentType);
        
        assertEquals("UTF-8", result);
    }
    
    @Test
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=INVALID";
        
        String result = dataUtil.getCharsetFromContentType(contentType);
        
        assertNull(result);
    }
    
    @Test
    public void testMimeBoundary() {
        int boundaryLength = 32;
        String mimeBoundaryChars = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        
        String result = dataUtil.mimeBoundary();
        
        assertEquals(boundaryLength, result.length());
        for (int i = 0; i < result.length(); i++) {
            assertTrue(mimeBoundaryChars.contains(result.charAt(i)));
        }
    }
}