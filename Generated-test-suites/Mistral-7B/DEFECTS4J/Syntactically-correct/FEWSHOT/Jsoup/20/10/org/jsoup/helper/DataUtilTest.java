package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test.html content".getBytes());
        Document doc = DataUtil.load(inputStream, null, "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test.html content".getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDetectedCharset() throws IOException {
        String contentType = "text/html; charset=UTF-8";
        InputStream inputStream = new ByteArrayInputStream("test.html content".getBytes());
        Parser parser = mock(Parser.class);
        Document doc = mock(Document.class);
        when(parser.parseInput(anyString(), anyString())).thenReturn(doc);
        when(doc.select("meta[http-equiv=content-type], meta[charset]").first()).thenReturn(mockMetaElement("UTF-8"));

        Document loadedDoc = DataUtil.load(inputStream, null, "", parser);
        assertNotNull(loadedDoc);
        assertEquals(doc, loadedDoc);
    }

    private Element mockMetaElement(String charset) {
        Element metaElement = mock(Element.class);
        when(metaElement.attr("charset")).thenReturn(charset);
        return metaElement;
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String docData = "test.html content";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String docData = "test.html content";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String docData = "test.html content\uFFFE";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(doc);

        Element metaElement = mock(Element.class);
        when(metaElement.attr("charset")).thenReturn("UTF-8");
        when(doc.select("meta[http-equiv=content-type], meta[charset]").first()).thenReturn(metaElement);

        Document parsedDoc = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(parsedDoc);
        assertEquals(doc, parsedDoc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test.html content".getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}