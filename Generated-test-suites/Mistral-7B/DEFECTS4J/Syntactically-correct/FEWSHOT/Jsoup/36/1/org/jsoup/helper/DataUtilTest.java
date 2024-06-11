package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

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
    public void testLoadFileWithDefaultCharset() throws Exception {
        String filePath = "src/test/resources/sample.html";
        File file = new File(filePath);
        String expectedCharset = "UTF-8";
        Document doc = DataUtil.load(file, null);
        assertNotNull(doc);
        assertEquals(expectedCharset, doc.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws Exception {
        String filePath = "src/test/resources/sample.html";
        File file = new File(filePath);
        String charsetName = "ISO-8859-1";
        Document doc = DataUtil.load(file, charsetName);
        assertNotNull(doc);
        assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws Exception {
        String htmlContent = "Sample HTML content.";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document doc = DataUtil.load(inputStream, null);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws Exception {
        String htmlContent = "Sample HTML content.";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("ISO-8859-1"));
        Document doc = DataUtil.load(inputStream, "ISO-8859-1");
        assertNotNull(doc);
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws Exception {
        String charsetName = "UTF-8";
        String htmlContent = "Sample HTML content.";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(charsetName));
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws Exception {
        String charsetName = "ISO-8859-1";
        String htmlContent = "Sample HTML content.";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws Exception {
        String htmlContent = "<html><head><meta http-equiv='Content-Type' content='text/html;charset=ISO-8859-1'></head><body>Sample HTML content.</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String expectedCharset = "EUC-JP";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeInvalid() {
        String contentType = "text/html; charset=invalid";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}