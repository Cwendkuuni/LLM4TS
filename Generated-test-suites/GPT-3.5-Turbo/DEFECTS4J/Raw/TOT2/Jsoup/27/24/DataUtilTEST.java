package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        
        try {
            Document document = DataUtil.load(file, charset, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        
        try {
            Document document = DataUtil.load(inputStream, charset, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        try {
            Document document = DataUtil.load(inputStream, charset, baseUri, parser);
            assertNotNull(document);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testLoadWithNullCharsetName() {
        File file = new File("test.html");
        String charset = null;
        String baseUri = "http://example.com";
        
        try {
            Document document = DataUtil.load(file, charset, baseUri);
            assertNotNull(document);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testParseByteDataWithCharsetDetection() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("test data".getBytes());
        String charset = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteBuffer, charset, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("test data".getBytes());
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteBuffer, charset, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() {
        byte[] bytes = "test data".getBytes();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            assertArrayEquals(bytes, byteBuffer.array());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

}