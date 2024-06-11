package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.helper.DataUtil;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = mock(File.class);
        FileInputStream inStream = mock(FileInputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document doc = mock(Document.class);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canRead()).thenReturn(true);
        when(file.getAbsolutePath()).thenReturn("path/to/file");
        when(file.getPath()).thenReturn("path/to/file");
        when(file.getCanonicalPath()).thenReturn("path/to/file");

        when(inStream.read(any(byte[].class))).thenReturn(-1);
        when(inStream.getChannel()).thenReturn(mock(FileChannel.class));

        when(DataUtil.readToByteBuffer(inStream)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser())).thenReturn(doc);

        Document result = DataUtil.load(file, charsetName, baseUri);

        assertEquals(doc, result);
        verify(file, times(1)).exists();
        verify(file, times(1)).isFile();
        verify(file, times(1)).canRead();
        verify(file, times(1)).getAbsolutePath();
        verify(file, times(1)).getPath();
        verify(file, times(1)).getCanonicalPath();
        verify(inStream, times(1)).read(any(byte[].class));
        verify(inStream, times(1)).close();
        verify(DataUtil, times(1)).readToByteBuffer(inStream);
        verify(DataUtil, times(1)).parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inStream = mock(InputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document doc = mock(Document.class);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        when(inStream.read(any(byte[].class))).thenReturn(-1);
        when(inStream.getChannel()).thenReturn(mock(FileChannel.class));

        when(DataUtil.readToByteBuffer(inStream)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser())).thenReturn(doc);

        Document result = DataUtil.load(inStream, charsetName, baseUri);

        assertEquals(doc, result);
        verify(inStream, times(1)).read(any(byte[].class));
        verify(inStream, times(1)).close();
        verify(DataUtil, times(1)).readToByteBuffer(inStream);
        verify(DataUtil, times(1)).parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        InputStream inStream = mock(InputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document doc = mock(Document.class);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.xmlParser();

        when(inStream.read(any(byte[].class))).thenReturn(-1);
        when(inStream.getChannel()).thenReturn(mock(FileChannel.class));

        when(DataUtil.readToByteBuffer(inStream)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, parser)).thenReturn(doc);

        Document result = DataUtil.load(inStream, charsetName, baseUri, parser);

        assertEquals(doc, result);
        verify(inStream, times(1)).read(any(byte[].class));
        verify(inStream, times(1)).close();
        verify(DataUtil, times(1)).readToByteBuffer(inStream);
        verify(DataUtil, times(1)).parseByteData(byteData, charsetName, baseUri, parser);
    }

    @Test
    public void testParseByteDataWithNoCharset() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document doc = mock(Document.class);
        String charsetName = null;
        String baseUri = "http://www.example.com";

        when(byteData.rewind()).thenReturn(null);
        when(Charset.forName(DataUtil.defaultCharset).decode(byteData).toString()).thenReturn(mockData);
        when(parser.parseInput(mockData, baseUri)).thenReturn(doc);
        when(doc.select("meta[http-equiv=content-type], meta[charset]")).thenReturn(mockElements);
        when(meta.hasAttr("http-equiv")).thenReturn(true);
        when(meta.attr("content")).thenReturn("text/html; charset=UTF-8");
        when(DataUtil.getCharsetFromContentType(meta.attr("content"))).thenReturn("UTF-8");
        when(meta.attr("charset")).thenReturn("");
        when(Charset.forName("UTF-8").decode(byteData).toString()).thenReturn(mockData);
        when(parser.parseInput(mockData, baseUri)).thenReturn(doc);
        when(meta.attr("charset")).thenReturn("");
        when(doc.outputSettings().charset("UTF-8")).thenReturn(null);

        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        assertEquals(doc, result);
        verify(byteData, times(1)).rewind();
        verify(Charset.forName(DataUtil.defaultCharset), times(1)).decode(byteData).toString();
        verify(parser, times(1)).parseInput(mockData, baseUri);
        verify(doc, times(1)).select("meta[http-equiv=content-type], meta[charset]");
        verify(meta, times(1)).hasAttr("http-equiv");
        verify(meta, times(1)).attr("content");
        verify(DataUtil, times(1)).getCharsetFromContentType(meta.attr("content"));
        verify(meta, times(1)).attr("charset");
        verify(Charset.forName("UTF-8"), times(1)).decode(byteData).toString();
        verify(parser, times(1)).parseInput(mockData, baseUri);
        verify(meta, times(1)).attr("charset");
        verify(doc, times(1)).outputSettings().charset("UTF-8");
    }

    @Test
    public void testParseByteDataWithCharset() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document doc = mock(Document.class);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        when(byteData.rewind()).thenReturn(null);
        when(Charset.forName(charsetName).decode(byteData).toString()).thenReturn(mockData);
        when(parser.parseInput(mockData, baseUri)).thenReturn(doc);
        when(doc.outputSettings().charset(charsetName)).thenReturn(null);

        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        assertEquals(doc, result);
        verify(byteData, times(1)).rewind();
        verify(Charset.forName(charsetName), times(1)).decode(byteData).toString();
        verify(parser, times(1)).parseInput(mockData, baseUri);
        verify(doc, times(1)).outputSettings().charset(charsetName);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = mock(InputStream.class);
        ByteArrayOutputStream outStream = mock(ByteArrayOutputStream.class);
        byte[] buffer = new byte[DataUtil.bufferSize];
        int read = -1;
        ByteBuffer byteData = ByteBuffer.wrap(buffer);

        when(inStream.read(buffer)).thenReturn(read);
        when(outStream.toByteArray()).thenReturn(null);

        ByteBuffer result = DataUtil.readToByteBuffer(inStream);

        assertEquals(byteData, result);
        verify(inStream, times(1)).read(buffer);
        verify(inStream, times(1)).close();
        verify(outStream, times(1)).toByteArray();
    }

    @Test
    public void testGetCharsetFromContentTypeWithCharset() {
        String contentType = "text/html; charset=ISO-8859-1";
        String expectedCharset = "ISO-8859-1";

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, result);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String contentType = "text/html";
        String expectedCharset = null;

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, result);
    }

    @Test
    public void testGetCharsetFromContentTypeWithTypeAndNoCharset() {
        String contentType = "text/html; charset=";
        String expectedCharset = null;

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, result);
    }
}