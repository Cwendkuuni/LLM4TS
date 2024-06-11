package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DataUtilTest {

    @Test
    public void testLoadFile_ValidFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);

        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadFile_InvalidFile() throws IOException {
        File file = new File("invalid.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        DataUtil.load(file, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream_ValidInputStream() throws IOException {
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        assertNotNull(document);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStream_InvalidInputStream() throws IOException {
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream_WithParser() throws IOException {
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        assertNotNull(document);
    }

    @Test
    public void testParseByteData_WithCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(100);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset().name());
    }

    @Test
    public void testParseByteData_WithoutCharset() {
        ByteBuffer byteData = ByteBuffer.allocate(100);
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testReadToByteBuffer_ValidInputStream() throws IOException {
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        assertNotNull(byteData);
        assertTrue(byteData.remaining() > 0);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBuffer_InvalidInputStream() throws IOException {
        InputStream inputStream = null;

        DataUtil.readToByteBuffer(inputStream);
    }

    @Test
    public void testGetCharsetFromContentType_WithCharset() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_WithoutCharset() {
        String contentType = "text/html";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }
}