package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

public class DataUtilTest {
    private final Parser parser = Parser.htmlParser();

    @Test
    public void testLoad() throws IOException {
        File file = mock(File.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document doc = mock(Document.class);

        FileInputStream inStream = mock(FileInputStream.class);
        whenNew(FileInputStream.class).withArguments(file).thenReturn(inStream);
        when(DataUtil.readToByteBuffer(inStream)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, parser)).thenReturn(doc);

        Document result = DataUtil.load(file, charsetName, baseUri);

        assertEquals(doc, result);
        verify(inStream).close();
    }

    @Test
    public void testLoad_Stream() throws IOException {
        InputStream in = mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document doc = mock(Document.class);

        when(DataUtil.readToByteBuffer(in)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, parser)).thenReturn(doc);

        Document result = DataUtil.load(in, charsetName, baseUri);

        assertEquals(doc, result);
    }

    @Test
    public void testLoad_StreamWithParser() throws IOException {
        InputStream in = mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document doc = mock(Document.class);

        when(DataUtil.readToByteBuffer(in)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, parser)).thenReturn(doc);

        Document result = DataUtil.load(in, charsetName, baseUri, parser);

        assertEquals(doc, result);
    }

    @Test
    public void testParseByteData_WithCharset() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        String docData = "test";
        Document doc = mock(Document.class);

        when(byteData.toString()).thenReturn(docData);
        when(parser.parseInput(docData, baseUri)).thenReturn(doc);

        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals(doc, result);
    }

    @Test
    public void testParseByteData_WithoutCharset() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        String charsetName = null;
        String baseUri = "http://example.com";
        String docData = "test";
        Document doc = mock(Document.class);
        Element meta = mock(Element.class);
        Charset charset = Charset.forName(DataUtil.defaultCharset);

        when(byteData.toString()).thenReturn(docData);
        when(parser.parseInput(docData, baseUri)).thenReturn(doc);
        when(doc.select("meta[http-equiv=content-type], meta[charset]")).thenReturn(new Elements(meta));
        when(meta.attr("charset")).thenReturn("charset=UTF-8");
        when(meta.attr("http-equiv")).thenReturn("Content-Type");
        when(meta.attr("content")).thenReturn("text/html;charset=gb2312");
        when(meta.hasAttr("charset")).thenReturn(true);
        when(Charset.isSupported(meta.attr("charset"))).thenReturn(true);
        when(Charset.forName(meta.attr("charset"))).thenReturn(charset);

        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals(doc, result);
        assertEquals(charset, charset.forName(charsetName));
    }

    @Test
    public void testParseByteData_WithUnsupportedCharset() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        String charsetName = null;
        String baseUri = "http://example.com";
        String docData = "test";
        Document doc = mock(Document.class);
        Element meta = mock(Element.class);

        when(byteData.toString()).thenReturn(docData);
        when(parser.parseInput(docData, baseUri)).thenReturn(doc);
        when(doc.select("meta[http-equiv=content-type], meta[charset]")).thenReturn(new Elements(meta));
        when(meta.attr("charset")).thenReturn("charset=UTF-8");
        when(meta.attr("http-equiv")).thenReturn("Content-Type");
        when(meta.attr("content")).thenReturn("text/html;charset=gb2312");
        when(meta.hasAttr("charset")).thenReturn(true);
        when(Charset.isSupported(meta.attr("charset"))).thenReturn(false);

        Document result = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals(doc, result);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = mock(InputStream.class);
        int maxSize = 0;
        byte[] buffer = mock(byte[].class);
        ByteArrayOutputStream outStream = mock(ByteArrayOutputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);

        whenNew(ByteArrayOutputStream.class).withArguments(DataUtil.bufferSize).thenReturn(outStream);
        when(inStream.read(buffer)).thenReturn(-1);
        when(byteData.array()).thenReturn(buffer);

        ByteBuffer result = DataUtil.readToByteBuffer(inStream, maxSize);

        assertEquals(byteData, result);
    }

    @Test
    public void testReadToByteBuffer_WithMaxSize() throws IOException {
        InputStream inStream = mock(InputStream.class);
        int maxSize = 100;
        byte[] buffer = mock(byte[].class);
        ByteArrayOutputStream outStream = mock(ByteArrayOutputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);

        whenNew(ByteArrayOutputStream.class).withArguments(DataUtil.bufferSize).thenReturn(outStream);
        when(inStream.read(buffer)).thenReturn(50).thenReturn(50).thenReturn(-1);
        when(byteData.array()).thenReturn(buffer);

        ByteBuffer result = DataUtil.readToByteBuffer(inStream, maxSize);

        assertEquals(byteData, result);
    }

    @Test
    public void testReadToByteBuffer_WithNegativeMaxSize() throws IOException {
        InputStream inStream = mock(InputStream.class);
        int maxSize = -1;
        byte[] buffer = mock(byte[].class);
        ByteArrayOutputStream outStream = mock(ByteArrayOutputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);

        whenNew(ByteArrayOutputStream.class).withArguments(DataUtil.bufferSize).thenReturn(outStream);
        when(inStream.read(buffer)).thenReturn(-1);
        when(byteData.array()).thenReturn(buffer);

        ByteBuffer result = DataUtil.readToByteBuffer(inStream, maxSize);

        assertEquals(byteData, result);
    }

    @Test
    public void testReadToByteBuffer_IOException() throws IOException {
        InputStream inStream = mock(InputStream.class);
        int maxSize = 0;

        when(inStream.read(any(byte[].class))).thenThrow(new IOException());

        assertThrows(IOException.class, () -> DataUtil.readToByteBuffer(inStream, maxSize));
    }

    @Test
    public void testGetCharsetFromContentType_NotFound() {
        String contentType = null;

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertNull(result);
    }

    @Test
    public void testGetCharsetFromContentType_Found() {
        String contentType = "text/html;charset=EUC-JP";
        String expectedCharset = "EUC-JP";

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, result);
    }

    @Test
    public void testGetCharsetFromContentType_Supported() {
        String contentType = "text/html;charset=utf-8";
        String expectedCharset = "UTF-8";

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, result);
    }

    @Test
    public void testGetCharsetFromContentType_Unsupported() {
        String contentType = "text/html;charset=invalidCharset";

        String result = DataUtil.getCharsetFromContentType(contentType);

        assertNull(result);
    }   
}