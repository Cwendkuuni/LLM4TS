package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;

import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        try {
            Document document = DataUtil.load(new File("test.html"), "UTF-8", "http://example.com");
            assertNotNull(document);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            FileInputStream fileInputStream = new FileInputStream("test.html");
            Document document = DataUtil.load(fileInputStream, "UTF-8", "http://example.com");
            assertNotNull(document);
            fileInputStream.close();
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
  
    @Test
    public void testLoadInputStreamParser() {
        try {
            FileInputStream fileInputStream = new FileInputStream("test.html");
            Document document = DataUtil.load(fileInputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(document);
            fileInputStream.close();
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            FileInputStream fileInputStream = new FileInputStream("test.html");
            ByteBuffer byteData = DataUtil.readToByteBuffer(fileInputStream);
            assertNotNull(byteData);
            fileInputStream.close();
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