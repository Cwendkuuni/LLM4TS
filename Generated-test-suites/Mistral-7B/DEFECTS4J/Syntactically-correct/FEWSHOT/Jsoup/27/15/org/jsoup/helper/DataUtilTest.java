package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test cases for DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("path/to/file.html");
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, null, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("path/to/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "html content";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, null, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "html content";
        String charsetName = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        String htmlContent = "html content";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        when(parser.parseInput(eq("html content"), eq("http://example.com"))).thenReturn(new Document());
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String charsetName = null;
        ByteBuffer byteData = ByteBuffer.allocate(10);
        byteData.put("html content".getBytes());
        byteData.flip();
        Document document = DataUtil.parseByteData(byteData, charsetName, "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String charsetName = "UTF-8";
        ByteBuffer byteData = ByteBuffer.allocate(10);
        byteData.put("html content".getBytes());
        byteData.flip();
        Document document = DataUtil.parseByteData(byteData, charsetName, "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}