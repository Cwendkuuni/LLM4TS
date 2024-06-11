package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "https://www.example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream input = new FileInputStream("test.html");
        Document doc = DataUtil.load(input, "UTF-8", "https://www.example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoadWithParser() throws IOException {
        InputStream input = new FileInputStream("test.html");
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(input, "UTF-8", "https://www.example.com", parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "https://www.example.com", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] testData = "test data".getBytes();
        InputStream input = new ByteArrayInputStream(testData);
        ByteBuffer byteData = DataUtil.readToByteBuffer(input);
        assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}