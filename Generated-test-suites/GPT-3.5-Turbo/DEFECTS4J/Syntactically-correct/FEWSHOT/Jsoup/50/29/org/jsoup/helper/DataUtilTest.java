
package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");

        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");

        assertEquals("http://example.com", doc.baseUri());
        assertTrue(doc.html().contains("Test Document"));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");

        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");

        assertEquals("http://example.com", doc.baseUri());
        assertTrue(doc.html().contains("Test Document"));
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");

        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());

        assertEquals("http://example.com", doc.baseUri());
        assertTrue(doc.html().contains("Test Document"));
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(new ByteArrayInputStream(byteBuffer.array()), outputStream);

        byte[] byteArray = outputStream.toByteArray();
        String data = new String(byteArray, "UTF-8");

        assertTrue(data.contains("Test Document"));
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><body><h1>Test Document</h1></body></html>".getBytes());
        String charsetName = "UTF-8";

        Document doc = DataUtil.parseByteData(byteData, charsetName, "http://example.com", Parser.htmlParser());

        assertEquals("http://example.com", doc.baseUri());
        assertTrue(doc.html().contains("Test Document"));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        assertTrue(byteBuffer.remaining() > 0);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertEquals(0, byteBuffer.remaining());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertEquals(DataUtil.boundaryLength, boundary.length());
    }
}
