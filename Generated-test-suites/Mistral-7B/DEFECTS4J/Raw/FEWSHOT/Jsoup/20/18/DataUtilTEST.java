package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws Exception {
        String filePath = "path/to/your/file.html";
        Document document = DataUtil.load(new File(filePath), null, "");
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws Exception {
        String filePath = "path/to/your/file.html";
        String charsetName = "UTF-8";
        Document document = DataUtil.load(new File(filePath), charsetName, "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws Exception {
        String content = "Your HTML content here...";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        Document document = DataUtil.load(inputStream, null, "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws Exception {
        String content = "Your HTML content here...";
        String charsetName = "UTF-8";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        Document document = DataUtil.load(inputStream, charsetName, "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws Exception {
        String content = "Your HTML content here...";
        String charsetName = "UTF-8";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        Parser parser = Parser.xmlParser(); // Use a different Parser here, for example xmlParser()
        Document document = DataUtil.load(inputStream, charsetName, "", parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes());
        Document document = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        String charsetName = "UTF-8";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes());
        Document document = DataUtil.parseByteData(byteData, charsetName, "", Parser.htmlParser());
        assertNotNull(document);
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