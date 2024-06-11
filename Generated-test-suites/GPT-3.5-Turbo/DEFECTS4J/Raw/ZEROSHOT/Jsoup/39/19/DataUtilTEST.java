package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    @Test
    public void testLoadWithFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testLoadWithInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testLoadWithInputStreamAndParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(100);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        int maxSize = 1024;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        assertNotNull(byteBuffer);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
}