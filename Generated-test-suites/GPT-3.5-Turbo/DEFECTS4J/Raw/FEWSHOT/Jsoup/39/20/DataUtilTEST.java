package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class DataUtilTest {
    @Before
    public void setup() {
        // Some setup code for mock objects
    }
    
    @Test
    public void testLoadFromFile() throws IOException {
        File file = mock(File.class);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        FileInputStream inStream = mock(FileInputStream.class);
        whenNew(FileInputStream.class).withArguments(file).thenReturn(inStream);
        
        ByteBuffer byteData = mock(ByteBuffer.class);
        when(DataUtil.readToByteBuffer(inStream)).thenReturn(byteData);
        
        Document expected = mock(Document.class);
        Parser parser = Parser.htmlParser();
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, parser)).thenReturn(expected);
        
        Document result = DataUtil.load(file, charsetName, baseUri);
        
        verify(inStream).close();
        assertEquals(expected, result);
    }
    
    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inStream = mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        
        ByteBuffer byteData = mock(ByteBuffer.class);
        when(DataUtil.readToByteBuffer(inStream)).thenReturn(byteData);
        
        Document expected = mock(Document.class);
        Parser parser = Parser.htmlParser();
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, parser)).thenReturn(expected);
        
        Document result = DataUtil.load(inStream, charsetName, baseUri);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inStream = mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = mock(Parser.class);
        
        ByteBuffer byteData = mock(ByteBuffer.class);
        when(DataUtil.readToByteBuffer(inStream)).thenReturn(byteData);
        
        Document expected = mock(Document.class);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, parser)).thenReturn(expected);
        
        Document result = DataUtil.load(inStream, charsetName, baseUri, parser);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testParseByteDataWithCharset() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        
        String docData = "html content";
        String docDataWithBOM = "html content with BOM";
        String expectedCharset = "UTF-8";
        
        Document expected = mock(Document.class);
        when(parser.parseInput(docData, baseUri)).thenReturn(expected);
        
        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertEquals(expected, result);
        
        when(byteData.rewind()).thenReturn(byteData);
        when(byteData.remaining()).thenReturn(7);
        when(byteData.getChar(0)).thenReturn((char)65279); // UTF-8 BOM
        
        when(parser.parseInput(docDataWithBOM, baseUri)).thenReturn(expected);
        
        result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertEquals(expected, result);
        verify(byteData).rewind();
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = mock(InputStream.class);
        int maxSize = 1024;
        
        byte[] buffer = new byte[DataUtil.bufferSize];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(DataUtil.bufferSize);
        
        int read = 0;
        int remaining = maxSize;
        int bytesRead = 0;
        
        while (true) {
            read = inStream.read(buffer);
            if (read == -1) break;
            if (maxSize > 0) {
                if (read > remaining) {
                    outStream.write(buffer, 0, remaining);
                    break;
                }
                remaining -= read;
            }
            outStream.write(buffer, 0, read);
            bytesRead += read;
        }
        
        byte[] expectedBytes = outStream.toByteArray();
        when(DataUtil.readToByteBuffer(inStream, maxSize)).thenCallRealMethod();
        when(inStream.read(buffer)).thenReturn(read).thenReturn(-1);
        whenNew(ByteArrayOutputStream.class).withArguments(DataUtil.bufferSize).thenReturn(outStream);
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, maxSize);
        
        assertEquals(expectedBytes.length, byteData.capacity());
        assertEquals(expectedBytes.length, byteData.remaining());
        
        byte[] resultBytes = new byte[byteData.remaining()];
        byteData.get(resultBytes);

        assertArrayEquals(expectedBytes, resultBytes);
    }
    
    @Test
    public void testReadToByteBufferUnlimitedSize() throws IOException {
        InputStream inStream = mock(InputStream.class);
        
        byte[] buffer = new byte[DataUtil.bufferSize];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(DataUtil.bufferSize);
        
        int read = 0;
        int bytesRead = 0;
        
        while (true) {
            read = inStream.read(buffer);
            if (read == -1) break;
            outStream.write(buffer, 0, read);
            bytesRead += read;
        }
        
        byte[] expectedBytes = outStream.toByteArray();
        when(DataUtil.readToByteBuffer(inStream, 0)).thenCallRealMethod();
        when(inStream.read(buffer)).thenReturn(read).thenReturn(-1);
        whenNew(ByteArrayOutputStream.class).withArguments(DataUtil.bufferSize).thenReturn(outStream);
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, 0);
        
        assertEquals(expectedBytes.length, byteData.capacity());
        assertEquals(expectedBytes.length, byteData.remaining());
        
        byte[] resultBytes = new byte[byteData.remaining()];
        byteData.get(resultBytes);

        assertArrayEquals(expectedBytes, resultBytes);
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String expectedCharset1 = "UTF-8";
        
        String contentType2 = "text/html; charset=ISO-8859-1";
        String expectedCharset2 = "ISO-8859-1";
        
        String contentType3 = "text/html";
        String expectedCharset3 = null;
        
        String contentType4 = null;
        String expectedCharset4 = null;
        
        String contentType5 = "text/html; charset=";
        String expectedCharset5 = null;
        
        String contentType6 = "text/html; charset=invalidCharset";
        String expectedCharset6 = null;
        
        String result1 = DataUtil.getCharsetFromContentType(contentType1);
        String result2 = DataUtil.getCharsetFromContentType(contentType2);
        String result3 = DataUtil.getCharsetFromContentType(contentType3);
        String result4 = DataUtil.getCharsetFromContentType(contentType4);
        String result5 = DataUtil.getCharsetFromContentType(contentType5);
        String result6 = DataUtil.getCharsetFromContentType(contentType6);
        
        assertEquals(expectedCharset1, result1);
        assertEquals(expectedCharset2, result2);
        assertEquals(expectedCharset3, result3);
        assertEquals(expectedCharset4, result4);
        assertEquals(expectedCharset5, result5);
        assertEquals(expectedCharset6, result6);
    }
}