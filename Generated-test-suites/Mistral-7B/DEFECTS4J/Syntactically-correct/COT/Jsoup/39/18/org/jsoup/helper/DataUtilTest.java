package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File inputFile = new File("sample.html");
        Document doc = DataUtil.load(inputFile, null, "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File inputFile = new File("sample.html");
        Document doc = DataUtil.load(inputFile, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File inputFile = new File("sample.html");
        DataUtil.load(inputFile, "INVALID_CHARSET", "");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String sampleHtml = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(sampleHtml.getBytes());
        Document doc = DataUtil.load(inputStream, null, "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String sampleHtml = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(sampleHtml.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String sampleHtml = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(sampleHtml.getBytes());
        DataUtil.load(inputStream, "INVALID_CHARSET", "");
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithUnsupportedParser() throws IOException {
        String sampleHtml = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(sampleHtml.getBytes("UTF-8"));
        DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String sampleHtml = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(sampleHtml.getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String sampleHtml = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(sampleHtml.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test(expected = HelperException.class)
    public void testParseByteDataWithInvalidCharset() {
        String sampleHtml = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(sampleHtml.getBytes());
        Document doc = DataUtil.parseByteData(byteData, "INVALID_CHARSET", "", Parser.htmlParser());
    }

    @Test
    public void testReadToByteBufferFromInputStream() throws IOException {
        String sampleHtml = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(sampleHtml.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferFromInputStreamWithMaxSize() throws IOException {
        String sampleHtml = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(sampleHtml.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);
        assertNotNull(byteData);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferFromInputStreamWithNegativeMaxSize() throws IOException {
        String sampleHtml = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(sampleHtml.getBytes());
        DataUtil.readToByteBuffer(inputStream, -1);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=INVALID_CHARSET";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=UTF-16BE";
        DataUtil.getCharsetFromContentType(contentType);
    }
}