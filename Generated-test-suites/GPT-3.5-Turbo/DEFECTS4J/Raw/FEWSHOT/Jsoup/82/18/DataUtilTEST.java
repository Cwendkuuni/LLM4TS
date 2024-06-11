package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, "");
        assertEquals(doc.title(), "Test");
        file.delete();
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, null, "");
        assertEquals(doc.title(), "");

        inputStream.close();
    }

    @Test
    public void testLoadFromInputStreamWithCharset() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertEquals(doc.title(), "");

        inputStream.close();
    }

    @Test
    public void testLoadFromInputStreamWithCharsetAndParser() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());
        assertEquals(doc.title(), "");

        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        byte[] outputBytes = new byte[inputBytes.length];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        DataUtil.crossStreams(inputStream, System.out);
    }

    @Test
    public void testParseInputStreamWithCharsetAndParser() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        assertEquals(doc.title(), "");

        inputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, html.length());
        assertEquals(byteBuffer.limit(), html.length());

        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, html.length() / 2);
        assertTrue(byteBuffer.limit() <= html.length() / 2);

        inputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(charset, "UTF-8");
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalid";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(boundary.length(), DataUtil.boundaryLength);
    }
}