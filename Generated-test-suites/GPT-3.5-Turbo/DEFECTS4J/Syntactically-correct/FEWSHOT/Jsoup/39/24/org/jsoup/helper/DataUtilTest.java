package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataUtilTest {
    private static final File testFile = new File("test.html");

    @Test
    public void testLoadFile() throws IOException {
        FileInputStream inStream = mock(FileInputStream.class);
        when(inStream.read()).thenReturn(1, -1);

        Document document = DataUtil.load(testFile, "UTF-8", "http://example.com");
        assertEquals("UTF-8", document.outputSettings().charset().displayName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test(expected = IOException.class)
    public void testLoadFileIOException() throws IOException {
        FileInputStream inStream = mock(FileInputStream.class);
        when(inStream.read()).thenThrow(new IOException());

        DataUtil.load(testFile, "UTF-8", "http://example.com");
    }

    @Test
    public void testLoadInputStream() throws IOException {
        ByteBuffer byteBuffer = mock(ByteBuffer.class);
        InputStream inStream = mock(InputStream.class);
        when(inStream.read()).thenReturn(1, -1);
        when(byteBuffer.array()).thenReturn("testData".getBytes());

        Document document = DataUtil.load(inStream, "UTF-8", "http://example.com");
        assertEquals("UTF-8", document.outputSettings().charset().displayName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamIOException() throws IOException {
        inputStream = mock(InputStream.class);
        when(inputStream.read()).thenThrow(new IOException());

        DataUtil.load(inputStream, "UTF-8", "http://example.com");
    }

    @Test
    public void testLoadAlternateParser() throws IOException {
        ByteBuffer byteBuffer = mock(ByteBuffer.class);
        InputStream inStream = mock(InputStream.class);
        when(inStream.read()).thenReturn(1, -1);
        when(byteBuffer.array()).thenReturn("testData".getBytes());

        Parser parser = Parser.xmlParser();
        Document document = DataUtil.load(inStream, "UTF-8", "http://example.com", parser);
        assertEquals("UTF-8", document.outputSettings().charset().displayName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test(expected = IOException.class)
    public void testLoadAlternateParserIOException() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read()).thenThrow(new IOException());

        Parser parser = Parser.xmlParser();
        DataUtil.load(inputStream, "UTF-8", "http://example.com", parser);
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", parser);
        assertEquals("UTF-8", document.outputSettings().charset().displayName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, null, "http://example.com", parser);
        assertNull(document.outputSettings().charset());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read()).thenReturn(1, -1);

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals("testData", new String(byteData.array()));
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read()).thenReturn(1, -1);

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);
        assertEquals("testData", new String(byteData.array()));
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferIOException() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read()).thenThrow(new IOException());

        DataUtil.readToByteBuffer(inputStream);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=Invalid-Charset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}