
package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charset, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charset, baseUri);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.load(inputStream, charset, baseUri, parser);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseByteDataWithCharset() {
        // Create a ByteBuffer with some data
        ByteBuffer byteData = ByteBuffer.wrap("Test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseByteDataWithoutCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("Test data".getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        int maxSize = 1024;
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertNotNull(byteData);
        assertTrue(byteData.limit() <= maxSize);
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentTypeSupportedCharset() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeUnsupportedCharset() {
        String contentType = "text/html; charset=ISO-8859-1";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeInvalidContentType() {
        String contentType = "text/html; charset=";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}
