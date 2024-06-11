package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = mock(File.class);
        FileInputStream inStream = mock(FileInputStream.class);
        when(file.getPath()).thenReturn("test/path/file.html");
        when(file.getName()).thenReturn("file.html");
        when(file.length()).thenReturn(100L);
        when(file.exists()).thenReturn(true);
        when(file.canRead()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(inStream.read(any(byte[].class))).thenReturn(5, 5, 5, 5, 5, 5, -1);
        DataUtil.load(file, "UTF-8", "http://example.com");
        verify(inStream, times(14)).read(any(byte[].class));
        verify(inStream, times(1)).close();
    }

    @Test(expected = IOException.class)
    public void testLoadFileThrowsIOException() throws IOException {
        File file = mock(File.class);
        FileInputStream inStream = mock(FileInputStream.class);
        when(file.exists()).thenReturn(true);
        when(file.canRead()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(inStream.read(any(byte[].class))).thenThrow(IOException.class);
        DataUtil.load(file, "UTF-8", "http://example.com");
        verify(inStream, times(1)).close();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = mock(InputStream.class);
        when(in.read(any(byte[].class))).thenReturn(5, 5, 5, 5, 5, 5, -1);
        DataUtil.load(in, "UTF-8", "http://example.com");
        verify(in, times(14)).read(any(byte[].class));
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamThrowsIOException() throws IOException {
        InputStream in = mock(InputStream.class);
        when(in.read(any(byte[].class))).thenThrow(IOException.class);
        DataUtil.load(in, "UTF-8", "http://example.com");
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream in = mock(InputStream.class);
        Parser parser = mock(Parser.class);
        when(in.read(any(byte[].class))).thenReturn(5, 5, 5, 5, 5, 5, -1);
        DataUtil.load(in, "UTF-8", "http://example.com", parser);
        verify(in, times(14)).read(any(byte[].class));
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithParserThrowsIOException() throws IOException {
        InputStream in = mock(InputStream.class);
        Parser parser = mock(Parser.class);
        when(in.read(any(byte[].class))).thenThrow(IOException.class);
        DataUtil.load(in, "UTF-8", "http://example.com", parser);
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);
        Charset charset = mock(Charset.class);
        Document doc = mock(Document.class);
        when(parser.parseInput(any(String.class), any(String.class))).thenReturn(doc);
        when(parser.xmlParser()).thenReturn(parser);
        when(doc.select(anyString())).thenReturn(null);
        when(doc.outputSettings()).thenReturn(null);
        when(doc.toString()).thenReturn("doc");
        when(charset.decode(byteData)).thenReturn(charset);
        when(charset.toString()).thenReturn("charset");
        when(charsetName.equalsIgnoreCase(anyString())).thenReturn(true);
        when(byteData.rewind()).thenReturn(null);
        DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        verify(parser, times(1)).parseInput(any(String.class), any(String.class));
        verify(parser, times(1)).xmlParser();
        verify(doc, times(1)).outputSettings();
        verify(doc, times(1)).toString();
        verify(charset, times(1)).decode(byteData);
        verify(charset, times(1)).toString();
        verify(charset, times(1)).equalsIgnoreCase(anyString());
        verify(byteData, times(1)).rewind();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = mock(InputStream.class);
        ByteArrayOutputStream outStream = mock(ByteArrayOutputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        byte[] bufferMock = new byte[0x20000];
        int read = 5;
        when(inStream.read(any(byte[].class))).thenReturn(read, read, read, read, read, read, -1);
        when(outStream.toByteArray()).thenReturn(bufferMock);
        when(byteData.wrap(any(byte[].class))).thenReturn(byteData);
        DataUtil.readToByteBuffer(inStream);
        verify(inStream, times(14)).read(any(byte[].class));
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferThrowsIOException() throws IOException {
        InputStream inStream = mock(InputStream.class);
        ByteArrayOutputStream outStream = mock(ByteArrayOutputStream.class);
        when(inStream.read(any(byte[].class))).thenThrow(IOException.class);
        DataUtil.readToByteBuffer(inStream);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        Matcher matcher = mock(Matcher.class);
        when(matcher.find()).thenReturn(true);
        when(matcher.group(1)).thenReturn("UTF-8");
        when(contentType.matches(anyString())).thenReturn(true);
        when(DataUtil.getCharsetFromContentType(contentType)).thenReturn(null);
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
        verify(matcher, times(1)).find();
        verify(matcher, times(1)).group(1);
        verify(contentType, times(1)).matches(anyString());
    }
    
}