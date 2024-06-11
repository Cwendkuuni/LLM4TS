package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws Exception {
        String filePath = "path/to/your/file.html";
        File file = new File(filePath);
        String charsetName = null;
        String baseUri = "http://example.com/";

        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws Exception {
        String filePath = "path/to/your/file.html";
        File file = new File(filePath);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws Exception {
        String inputStreamContent = "Your input stream content here";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        String charsetName = null;
        String baseUri = "http://example.com/";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws Exception {
        String inputStreamContent = "Your input stream content here";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws Exception {
        String inputStreamContent = "Your input stream content here";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = Parser.xmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws Exception {
        String documentContent = "Your document content here";
        ByteBuffer byteData = ByteBuffer.wrap(documentContent.getBytes());
        String charsetName = null;
        String baseUri = "http://example.com/";

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws Exception {
        String documentContent = "Your document content here";
        ByteBuffer byteData = ByteBuffer.wrap(documentContent.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws Exception {
        String documentContent = "Your document content here";
        ByteBuffer byteData = ByteBuffer.wrap(documentContent.getBytes());
        String charsetName = null;
        String baseUri = "http://example.com/";

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);

        // Assuming that the detected charset is "GBK", add the following assertions for testing:
        assertEquals("GBK", document.outputSettings().charset());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = null;
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}