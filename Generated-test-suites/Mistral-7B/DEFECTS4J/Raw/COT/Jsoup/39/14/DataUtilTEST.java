package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Locale;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

/**
 * Test class for DataUtil methods.
 *
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html"); // Replace with the actual file path
        Document document = DataUtil.load(file, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html"); // Replace with the actual file path
        Document document = DataUtil.load(file, "UTF-8");
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html"); // Replace with the actual file path
        Document document = DataUtil.load(file, "InvalidCharset");
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "Test content.";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.load(inputStream, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "Test content.";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, "UTF-8");
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "Test content.";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document document = DataUtil.load(inputStream, "InvalidCharset");
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlContent = "Test content.";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String htmlContent = "Test content.";
        byte[] htmlBytes = htmlContent.getBytes();
        ByteBuffer byteData = ByteBuffer.wrap(htmlBytes);
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String htmlContent = "Test content.";
        String charsetName = "UTF-8";
        byte[] htmlBytes = htmlContent.getBytes();
        ByteBuffer byteData = ByteBuffer.wrap(htmlBytes);
        Document document = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithInvalidCharsetName() {
        String charsetName = "InvalidCharset";
        ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
        assertThrows(IOException.class, () -> DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser()));
    }

    @Test(expected = UnsupportedCharsetException.class)
    public void testParseByteDataWithUnsupportedCharset() {
        String charsetName = "InvalidCharset";
        byte[] htmlBytes = "Test content".getBytes();
        ByteBuffer byteData = ByteBuffer.wrap(htmlBytes);
        assertThrows(UnsupportedCharsetException.class, () -> DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser()));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "Test content.";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String htmlContent = "Test content.";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);
        assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}