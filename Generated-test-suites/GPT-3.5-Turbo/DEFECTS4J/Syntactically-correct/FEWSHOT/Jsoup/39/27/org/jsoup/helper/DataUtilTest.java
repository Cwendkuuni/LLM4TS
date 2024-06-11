package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {
    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new FileInputStream("index.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
        assertEquals(html, document.html());
        inputStream.close();
    }

    @Test
    public void testLoadFromInputStreamWithCharset() throws IOException {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new FileInputStream("index.html");
        Document document = DataUtil.load(inputStream, "ISO-8859-1", "http://example.com");
        assertNotNull(document);
        assertEquals(html, document.html());
        inputStream.close();
    }

    @Test
    public void testLoadFromFile() throws IOException {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        File file = new File("index.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    @Test
    public void testLoadFromFileWithCharset() throws IOException {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        File file = new File("index.html");
        Document document = DataUtil.load(file, "ISO-8859-1", "http://example.com");
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new FileInputStream("index.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(document);
        assertEquals(html, document.html());
        inputStream.close();
    }

    @Test
    public void testLoadFromInputStreamWithParserAndCharset() throws IOException {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new FileInputStream("index.html");
        Document document = DataUtil.load(inputStream, "ISO-8859-1", "http://example.com", Parser.xmlParser());
        assertNotNull(document);
        assertEquals(html, document.html());
        inputStream.close();
    }

    @Test
    public void testParseByteDataWithCharset() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(html);
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(html);
        Document document = DataUtil.parseByteData(byteBuffer, null, "http://example.com", Parser.htmlParser());
        assertNotNull(document);
        assertEquals(html, document.html());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        assertNotNull(charset1);
        assertEquals("UTF-8", charset1);

        String contentType2 = "text/html; charset=ISO-8859-1";
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        assertNotNull(charset2);
        assertEquals("ISO-8859-1", charset2);

        String contentType3 = "text/html";
        String charset3 = DataUtil.getCharsetFromContentType(contentType3);
        assertNull(charset3);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("index.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteBuffer);
        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithSize() throws IOException {
        InputStream inputStream = new FileInputStream("index.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertNotNull(byteBuffer);
        inputStream.close();
    }
}