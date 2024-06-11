package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.regex.Matcher;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = mock(File.class);
        FileInputStream inStream = mock(FileInputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document doc = mock(Document.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canRead()).thenReturn(true);
        when(file.getAbsolutePath()).thenReturn("/path/to/file");
        when(new FileInputStream(file)).thenReturn(inStream);
        when(DataUtil.readToByteBuffer(inStream)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser())).thenReturn(doc);

        Document result = DataUtil.load(file, charsetName, baseUri);

        assertEquals(doc, result);
    }

    @Test
    public void testLoadFileIOException() throws IOException {
        File file = mock(File.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canRead()).thenReturn(true);
        when(file.getAbsolutePath()).thenReturn("/path/to/file");
        when(new FileInputStream(file)).thenThrow(new FileNotFoundException());

        Document result = null;
        try {
            result = DataUtil.load(file, charsetName, baseUri);
        } catch (IOException e) {
            // Expected IOException
        }

        assertNull(result);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = mock(InputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document doc = mock(Document.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        when(DataUtil.readToByteBuffer(in)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser())).thenReturn(doc);

        Document result = DataUtil.load(in, charsetName, baseUri);

        assertEquals(doc, result);   
    }

    @Test
    public void testLoadInputStreamIOException() throws IOException {
        InputStream in = mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        when(DataUtil.readToByteBuffer(in)).thenThrow(new IOException());

        Document result = null;
        try {
            result = DataUtil.load(in, charsetName, baseUri);
        } catch (IOException e) {
            // Expected IOException
        }

        assertNull(result);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream in = mock(InputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document doc = mock(Document.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        
        when(DataUtil.readToByteBuffer(in)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, parser)).thenReturn(doc);

        Document result = DataUtil.load(in, charsetName, baseUri, parser);

        assertEquals(doc, result);   
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = mock(Document.class);
        
        when(byteData.rewind()).thenReturn(byteData);
        when(byteData.toString()).thenReturn("document data");
        when(parser.parseInput("document data", baseUri)).thenReturn(doc);

        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals(doc, result);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = mock(InputStream.class);
        byte[] buffer = new byte[DataUtil.bufferSize];
        ByteArrayOutputStream outStream = mock(ByteArrayOutputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        int read = 0;
        
        when(inStream.read(buffer)).thenReturn(read).thenReturn(-1);
        when(outStream.toByteArray()).thenReturn(buffer);
        when(ByteBuffer.wrap(buffer)).thenReturn(byteData);

        ByteBuffer result = DataUtil.readToByteBuffer(inStream);

        assertEquals(byteData, result);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        Matcher m = mock(Matcher.class);
        
        when(m.group(1)).thenReturn("UTF-8");
        when(contentType.matches("(?i)\\bcharset=\\s*\"?([^\\s;\"]*)")).thenReturn(true);
        when(DataUtil.charsetPattern.matcher(contentType)).thenReturn(m);

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", result);
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";
        
        String result = DataUtil.getCharsetFromContentType(contentType);

        assertNull(result);
    }
    
}