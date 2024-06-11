package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {
    private Parser parser;
    private File file;
    private FileInputStream fileInputStream;
    private InputStream inputStream;

    @Before
    public void setup() throws IOException {
        parser = mock(Parser.class);
        file = mock(File.class);
        fileInputStream = mock(FileInputStream.class);
        inputStream = mock(InputStream.class);

        when(fileInputStream.read(any(byte[].class))).thenReturn(-1);
        when(inputStream.read(any(byte[].class))).thenReturn(-1);

        when(parser.parseInput(anyString(), anyString())).thenReturn(mock(Document.class));
    }

    @Test
    public void testLoadFromFile() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        when(fileInputStream.read(any(byte[].class))).thenReturn(10);

        Document document = DataUtil.load(file, charset, baseUri);

        verify(fileInputStream).close();
        verify(parser).parseInput(anyString(), eq(baseUri));
        assertEquals(mock(Document.class), document);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        when(inputStream.read(any(byte[].class))).thenReturn(10);

        Document document = DataUtil.load(inputStream, charset, baseUri);

        verify(inputStream).close();
        verify(parser).parseInput(anyString(), eq(baseUri));
        assertEquals(mock(Document.class), document);
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        when(inputStream.read(any(byte[].class))).thenReturn(10);

        Document document = DataUtil.load(inputStream, charset, baseUri, parser);

        verify(inputStream).close();
        verify(parser).parseInput(anyString(), eq(baseUri));
        assertEquals(mock(Document.class), document);
    }

    @Test
    public void testParseByteDataWithCharsetFromMetaTag() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        parser = Parser.htmlParser();

        when(byteData.rewind()).thenReturn(byteData);
        when(byteData.toString()).thenReturn("<meta charset=\"UTF-16\">");
        when(byteData.charAt(0)).thenReturn((char)65279);

        Document document = DataUtil.parseByteData(byteData, null, "http://example.com", parser);

        verify(parser).parseInput(anyString(), eq("http://example.com"));
        assertEquals(mock(Document.class), document);
    }

    @Test
    public void testParseByteDataWithCharsetFromContentType() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        parser = Parser.htmlParser();

        when(byteData.toString()).thenReturn("text/html; charset=UTF-16");

        Document document = DataUtil.parseByteData(byteData, null, "http://example.com", parser);

        verify(parser).parseInput(anyString(), eq("http://example.com"));
        assertEquals(mock(Document.class), document);
    }

    @Test
    public void testReadToByteBufferFromInputStreamWithMaxSize() throws IOException {
        int maxSize = 1024;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(fileInputStream, maxSize);

        verify(fileInputStream).read(any(byte[].class));
        verify(fileInputStream).close();
        assertEquals(mock(ByteBuffer.class), byteBuffer);
    }

    @Test
    public void testReadToByteBufferFromInputStream() throws IOException {
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(fileInputStream);

        verify(fileInputStream).read(any(byte[].class));
        verify(fileInputStream).close();
        assertEquals(mock(ByteBuffer.class), byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }
}