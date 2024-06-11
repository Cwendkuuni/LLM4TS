package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File inputFile = new File("sample.html");
        Document doc = DataUtil.load(inputFile, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File inputFile = new File("sample.html");
        Document doc = DataUtil.load(inputFile, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File inputFile = new File("sample.html");
        Document doc = DataUtil.load(inputFile, "invalidCharset", null);
        fail("Expected IOException but got " + doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document doc = DataUtil.load(inputStream, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document doc = DataUtil.load(inputStream, "invalidCharset", null);
        fail("Expected IOException but got " + doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null, DataUtil.Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithNullByteData() {
        ByteBuffer byteData = null;
        String charsetName = "UTF-8";
        String baseUri = null;
        Parser parser = null;
        try {
            DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
            fail("Expected IllegalArgumentException but got null");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseByteDataWithNullCharsetName() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        String charsetName = null;
        String baseUri = null;
        Parser parser = null;
        try {
            DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
            fail("Expected IllegalArgumentException but got null");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseByteDataWithValidData() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, null);
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(htmlContent.getBytes().length, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}