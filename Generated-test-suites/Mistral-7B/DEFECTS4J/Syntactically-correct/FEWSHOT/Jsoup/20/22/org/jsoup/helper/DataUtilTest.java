package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser.ParserType;

import org.junit.Before;
import org.junit.Test;

public class DataUtilTest {

    private InputStream inputStream;
    private Document document;

    @Before
    public void setUp() {
        inputStream = new ByteArrayInputStream("<html><head></head><body></body></html>".getBytes());
        document = mock(Document.class);
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html");
        when(document.outputSettings()).thenReturn(new org.jsoup.nodes.Document.OutputSettings());

        DataUtil.load(file, null, "http://test.com/");

        verifyLoadDocumentCall("UTF-8", inputStream, document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        when(document.outputSettings()).thenReturn(new org.jsoup.nodes.Document.OutputSettings());

        DataUtil.load(file, "UTF-16", "http://test.com/");

        verifyLoadDocumentCall("UTF-16", inputStream, document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        when(document.outputSettings()).thenReturn(new org.jsoup.nodes.Document.OutputSettings());

        DataUtil.load(new ByteArrayInputStream("test".getBytes()), null, "http://test.com/");

        verifyLoadDocumentCall("UTF-8", inputStream, document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        when(document.outputSettings()).thenReturn(new org.jsoup.nodes.Document.OutputSettings());

        DataUtil.load(new ByteArrayInputStream("test".getBytes()), "UTF-16", "http://test.com/");

        verifyLoadDocumentCall("UTF-16", inputStream, document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        when(document.outputSettings()).thenReturn(new org.jsoup.nodes.Document.OutputSettings());

        Parser parser = mock(Parser.class);
        when(parser.parseInput("test", "http://test.com/")).thenReturn(document);

        DataUtil.load(new ByteArrayInputStream("test".getBytes()), null, "http://test.com/", parser);

        verifyLoadDocumentCall("UTF-8", inputStream, document);
        verify(parser).parseInput("test", "http://test.com/");
    }

    @Test
    public void testLoadFileWithMetaCharset() throws IOException {
        File file = new File("test.html");
        when(document.outputSettings()).thenReturn(new org.jsoup.nodes.Document.OutputSettings());
        Element meta = mock(Element.class);
        when(document.select("meta[http-equiv=content-type], meta[charset]")).thenReturn(meta);
        when(meta.attr("content")).thenReturn("text/html; charset=UTF-16");
        when(meta.attr("charset")).thenReturn("");

        DataUtil.load(file, null, "http://test.com/");

        verifyLoadDocumentCall("UTF-16", inputStream, document);
    }

    @Test
    public void testLoadFileWithInvalidMetaCharset() throws IOException {
        File file = new File("test.html");
        when(document.outputSettings()).thenReturn(new org.jsoup.nodes.Document.OutputSettings());
        Element meta = mock(Element.class);
        when(document.select("meta[http-equiv=content-type], meta[charset]")).thenReturn(meta);
        when(meta.attr("content")).thenReturn("text/html; charset=xxxx");
        when(meta.attr("charset")).thenReturn("");

        DataUtil.load(file, null, "http://test.com/");

        verifyLoadDocumentCall("UTF-8", inputStream, document);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());

        Document document = DataUtil.parseByteData(byteData, null, "http://test.com/", ParserType.HTML);

        verifyLoadDocumentCall("UTF-8", inputStream, document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());

        Document document = DataUtil.parseByteData(byteData, "UTF-16", "http://test.com/", ParserType.HTML);

        verifyLoadDocumentCall("UTF-16", inputStream, document);
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());

        DataUtil.parseByteData(byteData, "xxxx", "http://test.com/", ParserType.HTML);
    }

    private void verifyLoadDocumentCall(String charsetName, InputStream inputStream, Document document) throws IOException {
        ByteBuffer byteData = readToByteBuffer(inputStream);
        Document expectedDocument = new Parser().parseInput(Charset.forName(charsetName).decode(byteData).toString(), "http://test.com/");
        expectedDocument.outputSettings().charset(charsetName);
        assertEquals(expectedDocument, document);
    }
}