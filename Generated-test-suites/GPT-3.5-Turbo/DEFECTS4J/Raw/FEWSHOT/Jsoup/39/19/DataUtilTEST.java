package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {
    @Test
    public void testLoad_file() throws IOException {
        File file = mock(File.class);
        FileInputStream inStream = mock(FileInputStream.class);

        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canRead()).thenReturn(true);
        when(file.getPath()).thenReturn("test.html");
        when(file.getAbsolutePath()).thenReturn("C:\\test.html");
        when(file.getName()).thenReturn("test.html");

        when(inStream.read(any(byte[].class))).thenReturn(-1);
        whenNew(FileInputStream.class).withArguments(file).thenReturn(inStream);

        Document doc = DataUtil.load(file, null, "");
        assertEquals("", doc.text());
    }

    @Test
    public void testLoad_inputStream() throws IOException {
        InputStream in = mock(InputStream.class);
        ByteBuffer byteData = ByteBuffer.allocate(10);

        when(in.read(any(byte[].class))).thenReturn(-1);
        whenNew(ByteBuffer.class).withArguments(10).thenReturn(byteData);

        Document doc = DataUtil.load(in, null, "");
        assertEquals("", doc.text());
    }

    @Test
    public void testLoad_inputStreamWithParser() throws IOException {
        InputStream in = mock(InputStream.class);
        ByteBuffer byteData = ByteBuffer.allocate(10);
        Parser parser = mock(Parser.class);

        when(in.read(any(byte[].class))).thenReturn(-1);
        whenNew(ByteBuffer.class).withArguments(10).thenReturn(byteData);

        Document doc = DataUtil.load(in, null, "", parser);
        assertEquals("", doc.text());
        verify(parser, times(1)).parseInput(anyString(), eq(""));
    }

    @Test
    public void testParseByteData_noCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        Parser parser = mock(Parser.class);
        Document doc = mock(Document.class);

        when(byteData.rewind()).thenReturn(byteData);
        when(parser.parseInput(anyString(), eq(""))).thenReturn(doc);

        Document result = DataUtil.parseByteData(byteData, null, "", parser);

        assertEquals(doc, result);
        verify(parser, times(1)).parseInput(anyString(), eq(""));
        verifyNoMoreInteractions(parser);
        verifyNoInteractions(doc);
    }

    @Test
    public void testParseByteData_withCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(10);
        Parser parser = mock(Parser.class);
        Document doc = mock(Document.class);

        when(byteData.rewind()).thenReturn(byteData);
        when(parser.parseInput(anyString(), eq(""))).thenReturn(doc);

        Document result = DataUtil.parseByteData(byteData, "UTF-8", "", parser);

        assertEquals(doc, result);
        verify(parser, times(1)).parseInput(anyString(), eq(""));
        verifyNoMoreInteractions(parser);
        verifyNoInteractions(doc);
    }

    @Test
    public void testReadToByteBuffer_withMaxSize() throws IOException {
        InputStream inStream = mock(InputStream.class);
        ByteArrayOutputStream outStream = mock(ByteArrayOutputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);

        whenNew(ByteArrayOutputStream.class).withArguments(DataUtil.bufferSize).thenReturn(outStream);
        when(inStream.read(any(byte[].class))).thenReturn(-1);
        when(byteData.wrap(any(byte[].class))).thenReturn(byteData);

        ByteBuffer result = DataUtil.readToByteBuffer(inStream, 100);

        assertEquals(byteData, result);
        verify(outStream, times(1)).toByteArray();
    }

    @Test
    public void testReadToByteBuffer_noMaxSize() throws IOException {
        InputStream inStream = mock(InputStream.class);
        ByteArrayOutputStream outStream = mock(ByteArrayOutputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);

        whenNew(ByteArrayOutputStream.class).withArguments(DataUtil.bufferSize).thenReturn(outStream);
        when(inStream.read(any(byte[].class))).thenReturn(-1);
        when(byteData.wrap(any(byte[].class))).thenReturn(byteData);

        ByteBuffer result = DataUtil.readToByteBuffer(inStream, 0);

        assertEquals(byteData, result);
        verify(outStream, times(1)).toByteArray();
    }

    @Test
    public void testReadToByteBuffer_withContent() throws IOException {
        InputStream inStream = mock(InputStream.class);
        ByteArrayOutputStream outStream = mock(ByteArrayOutputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);

        whenNew(ByteArrayOutputStream.class).withArguments(DataUtil.bufferSize).thenReturn(outStream);
        when(inStream.read(any(byte[].class))).thenReturn(10).thenReturn(-1);
        when(byteData.wrap(any(byte[].class))).thenReturn(byteData);

        DataUtil.readToByteBuffer(inStream, 0);

        verify(outStream, times(1)).write(any(byte[].class), eq(0), eq(10));
        verify(outStream, times(1)).toByteArray();
    }

    @Test
    public void testGetCharsetFromContentType_null() {
        String result = DataUtil.getCharsetFromContentType(null);
        assertNull(result);
    }

    @Test
    public void testGetCharsetFromContentType_charsetFound() {
        String result = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", result);
    }

    @Test
    public void testGetCharsetFromContentType_charsetNotFound() {
        String result = DataUtil.getCharsetFromContentType("text/html");
        assertNull(result);
    }
}