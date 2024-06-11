package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
    private File file;
    private InputStream inputStream;
    private Parser parser;

    @Before
    public void setUp() {
        file = mock(File.class);
        inputStream = mock(InputStream.class);
        parser = mock(Parser.class);
    }

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        FileInputStream fileInputStream = mock(FileInputStream.class);
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document expected = mock(Document.class);

        when(file.getAbsolutePath()).thenReturn("test.html");
        whenNew(FileInputStream.class).withArguments(file).thenReturn(fileInputStream);
        when(DataUtil.readToByteBuffer(fileInputStream)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser())).thenReturn(expected);

        Document actual = DataUtil.load(file, charsetName, baseUri);

        verify(file).getAbsolutePath();
        verifyNew(FileInputStream.class).withArguments(file);
        verify(fileInputStream).close();
        verify(DataUtil).readToByteBuffer(fileInputStream);
        verify(DataUtil).parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals(expected, actual);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document expected = mock(Document.class);

        when(DataUtil.readToByteBuffer(inputStream)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser())).thenReturn(expected);

        Document actual = DataUtil.load(inputStream, charsetName, baseUri);

        verify(DataUtil).readToByteBuffer(inputStream);
        verify(DataUtil).parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals(expected, actual);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        ByteBuffer byteData = mock(ByteBuffer.class);
        Document expected = mock(Document.class);

        when(DataUtil.readToByteBuffer(inputStream)).thenReturn(byteData);
        when(DataUtil.parseByteData(byteData, charsetName, baseUri, parser)).thenReturn(expected);

        Document actual = DataUtil.load(inputStream, charsetName, baseUri, parser);

        verify(DataUtil).readToByteBuffer(inputStream);
        verify(DataUtil).parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseByteDataWithCharsetNull() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        String charsetName = null;
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        String docData = "Test Data";
        Document doc = mock(Document.class);
        Element meta = mock(Element.class);

        when(Charset.forName(DataUtil.defaultCharset).decode(byteData)).thenReturn(Charset.forName(DataUtil.defaultCharset).newDecoder().decode(byteData).toString());
        when(parser.parseInput(docData, baseUri)).thenReturn(doc);
        when(doc.select("meta[http-equiv=content-type], meta[charset]")).thenReturn(Collections.singletonList(meta));
        when(meta.hasAttr("http-equiv")).thenReturn(true);
        when(meta.attr("content")).thenReturn("text/html; charset=UTF-8");

        Document actual = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        verify(Charset).forName(DataUtil.defaultCharset);
        verify(Charset.forName(DataUtil.defaultCharset)).decode(byteData);
        verify(parser).parseInput(docData, baseUri);
        verify(doc).select("meta[http-equiv=content-type], meta[charset]");
        verify(meta).hasAttr("http-equiv");
        verify(meta).attr("content");
        verify(doc).outputSettings().charset(DataUtil.defaultCharset);
        assertEquals(doc, actual);
    }

    @Test
    public void testParseByteDataWithCharsetNotNull() {
        ByteBuffer byteData = mock(ByteBuffer.class);
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Parser parser = Parser.htmlParser();
        String docData = "Test Data";
        Document doc = mock(Document.class);
        Element meta = mock(Element.class);

        when(Charset.forName(charsetName).decode(byteData)).thenReturn(Charset.forName(charsetName).newDecoder().decode(byteData).toString());
        when(parser.parseInput(docData, baseUri)).thenReturn(doc);
        when(doc.select("meta[http-equiv=content-type], meta[charset]")).thenReturn(Collections.singletonList(meta));
        when(meta.hasAttr("http-equiv")).thenReturn(true);
        when(meta.attr("content")).thenReturn("text/html; charset=UTF-8");

        Document actual = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        verify(Charset).forName(charsetName);
        verify(Charset.forName(charsetName)).decode(byteData);
        verify(parser).parseInput(docData, baseUri);
        verify(doc).select("meta[http-equiv=content-type], meta[charset]");
        verify(meta).hasAttr("http-equiv");
        verify(meta).attr("content");
        verify(doc).outputSettings().charset(charsetName);
        assertEquals(doc, actual);
    }

    @Test
    public void testGetCharsetFromContentTypeWithCharsetNotSupported() {
        String contentType = "text/html; charset=Invalid-Charset";
        String expected = null;

        String actual = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCharsetFromContentTypeWithEmptyContentType() {
        String contentType = "";
        String expected = null;

        String actual = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetCharsetFromContentTypeWithValidCharset() {
        String contentType = "text/html; charset=UTF-8";
        String expected = "UTF-8";

        String actual = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expected, actual);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] buffer = new byte[DataUtil.bufferSize];
        ByteArrayOutputStream outStream = mock(ByteArrayOutputStream.class);
        int read = 1024;
        ByteBuffer expected = mock(ByteBuffer.class);

        when(inputStream.read(buffer)).thenReturn(read);
        whenNew(ByteArrayOutputStream.class).withArguments(DataUtil.bufferSize).thenReturn(outStream);
        doNothing().when(outStream).write(buffer, 0, read);
        when(ByteBuffer.wrap(outStream.toByteArray())).thenReturn(expected);

        ByteBuffer actual = DataUtil.readToByteBuffer(inputStream);

        verify(inputStream, atLeastOnce()).read(buffer);
        verifyNew(ByteArrayOutputStream.class).withArguments(DataUtil.bufferSize);
        verify(outStream).write(buffer, 0, read);
        verify(ByteBuffer).wrap(outStream.toByteArray());
        assertEquals(expected, actual);
    }
}