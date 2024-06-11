
package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);

        assertEquals("http://example.com", document.baseUri());
        assertTrue(document.hasText());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream in = new FileInputStream("file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(in, charsetName, baseUri);

        assertEquals("http://example.com", document.baseUri());
        assertTrue(document.hasText());
        in.close();
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.load(in, charsetName, baseUri, parser);

        assertEquals("http://example.com", document.baseUri());
        assertTrue(document.hasText());
        in.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        DataUtil.crossStreams(in, out);

        assertEquals("test", out.toString());
        in.close();
        out.close();
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head></head><body></body></html>".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals("http://example.com", document.baseUri());
        assertTrue(document.hasText());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream in = new FileInputStream("file.html");
        int maxSize = 2048;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, maxSize);

        assertTrue(byteBuffer.capacity() <= maxSize);
        in.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = new FileInputStream("file.html");

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in);

        assertTrue(byteBuffer.capacity() > 0);
        in.close();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("file.html");

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        assertTrue(byteBuffer.capacity() > 0);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNull() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.allocate(4);
        byteData.put((byte) 0xEF);
        byteData.put((byte) 0xBB);
        byteData.put((byte) 0xBF);
        byteData.put((byte) 0x41);

        String charsetName = "UTF-8";

        String charset = DataUtil.detectCharsetFromBom(byteData, charsetName);

        assertEquals("UTF-8", charset);
    }
}
