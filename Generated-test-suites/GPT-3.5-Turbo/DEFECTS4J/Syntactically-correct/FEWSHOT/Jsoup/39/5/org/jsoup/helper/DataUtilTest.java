package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File("test.html");

        try {
            Document document = DataUtil.load(file, charsetName, baseUri);

            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoadInputStream() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("test.html");

        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);

            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Parser parser = Parser.htmlParser();

        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

            assertNotNull(document);
            assertEquals(baseUri, document.baseUri());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("Hello World".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        int maxSize = 1024;
        InputStream inputStream = getClass().getResourceAsStream("test.html");

        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

            assertNotNull(byteBuffer);
            assertEquals(maxSize, byteBuffer.capacity());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testReadToByteBuffer() {
        InputStream inputStream = getClass().getResourceAsStream("test.html");

        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

            assertNotNull(byteBuffer);
            assertEquals(inputStream.available(), byteBuffer.capacity());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
    
}