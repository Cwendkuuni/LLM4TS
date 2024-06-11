
package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
        assertEquals("Test HTML", doc.title());
    }

    @Test(expected = IOException.class)
    public void testLoadFileException() throws IOException {
        File file = new File("nonexistent.html");
        DataUtil.load(file, "UTF-8", "");
    }

    @Test
    public void testLoadInputStream() throws IOException {
        try (InputStream is = new FileInputStream("test.html")) {
            Document doc = DataUtil.load(is, "UTF-8", "");
            assertNotNull(doc);
            assertEquals("Test HTML", doc.title());
        }
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamException() throws IOException {
        InputStream is = new FileInputStream("nonexistent.html");
        DataUtil.load(is, "UTF-8", "");
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        try (InputStream is = new FileInputStream("test.xml")) {
            Document doc = DataUtil.load(is, "UTF-8", "", Parser.xmlParser());
            assertNotNull(doc);
            assertEquals("Test XML", doc.title());
        }
    }

    @Test
    public void testParseByteData() {
        String data = "<html><head><title>Test HTML</title></head><body></body></html>";
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("Test HTML", doc.title());
    }

    @Test
    public void testParseByteDataCharsetNull() {
        String data = "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=ISO-8859-1\">" +
                      "<html><head><title>Test HTML</title></head><body></body></html>";
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        Document doc = DataUtil.parseByteData(byteBuffer, null, "", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("Test HTML", doc.title());
    }

    @Test
    public void testParseByteDataCharsetNullWithCharsetHeader() {
        String data = "<meta charset=\"UTF-8\">" +
                      "<html><head><title>Test HTML</title></head><body></body></html>";
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        Document doc = DataUtil.parseByteData(byteBuffer, null, "", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("Test HTML", doc.title());
    }

    @Test(expected = NullPointerException.class)
    public void testParseByteDataCharsetNullWithNoCharset() {
        String data = "<html><head><title>Test HTML</title></head><body></body></html>";
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        DataUtil.parseByteData(byteBuffer, null, "", Parser.htmlParser());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File file = new File("test.html");
        try (InputStream is = new FileInputStream(file)) {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(is);
            assertNotNull(byteBuffer);
            assertEquals(file.length(), byteBuffer.remaining());
        }
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        File file = new File("test.html");
        try (InputStream is = new FileInputStream(file)) {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(is, 1024);
            assertNotNull(byteBuffer);
            assertEquals(1024, byteBuffer.remaining());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadToByteBufferWithNegativeMaxSize() throws IOException {
        File file = new File("test.html");
        try (InputStream is = new FileInputStream(file)) {
            DataUtil.readToByteBuffer(is, -1);
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeInvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}
