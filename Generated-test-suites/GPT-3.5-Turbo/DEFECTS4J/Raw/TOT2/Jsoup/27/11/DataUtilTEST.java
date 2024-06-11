package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() {
        File testFile = new File("testFile.txt");
        try {
            Document document = DataUtil.load(testFile, "UTF-8", "http://example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testLoadInputStream() {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes(Charset.forName("UTF-8")));
        try {
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testLoadInputStreamParser() {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes(Charset.forName("UTF-8")));
        Parser parser = Parser.htmlParser();
        try {
            Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", parser);
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("test data".getBytes(Charset.forName("UTF-8")));
        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", "http://example.com", Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes(Charset.forName("UTF-8")));
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteBuffer);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}