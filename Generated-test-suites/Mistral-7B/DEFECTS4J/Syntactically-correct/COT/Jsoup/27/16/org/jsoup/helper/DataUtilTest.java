package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test cases for DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("sample.html"); // Replace this with the actual path to your HTML file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, null, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidPath() throws IOException {
        File file = new File("invalid_path/sample.html"); // Replace this with the invalid path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(file, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "invalid_charset";
        String baseUri = "http://example.com";
        DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.allocate(html.length() + 1); // Add 1 for BOM
        byteData.put(html.getBytes());
        byteData.put((byte) 0xFFFE); // Add BOM
        byteData.flip();

        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.allocate(html.length());
        byteData.put(html.getBytes());
        byteData.flip();

        String charsetName = "invalid_charset";
        String baseUri = "http://example.com";
        Document document = null;
        try {
            document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        } catch (IOException e) {
            // Expected
        }
        assertNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(html.length() + 1, byteData.limit()); // Add 1 for BOM
    }

    @Test
    public void testReadToByteBufferWithInvalidInputStream() throws IOException {
        InputStream inputStream = null;
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(0, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

}