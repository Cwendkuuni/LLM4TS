package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoad_withCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoad_withoutCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, null, "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoad_withInputStream_withCharset() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoad_withInputStream_withoutCharset() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.load(in, null, "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testLoad_withInputStream_withCharset_andParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com", parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteData_withCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(doc);
    }

    @Test
    public void testParseByteData_withoutCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        OutputStream out = new FileOutputStream("output.txt");
        
        DataUtil.crossStreams(in, out);
        
        File outputFile = new File("output.txt");
        assertTrue(outputFile.exists());
    }

    @Test
    public void testReadToByteBuffer_withMaxSize() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        int maxSize = 1024;
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in, maxSize);
        
        assertNotNull(byteBuffer);
        assertEquals(maxSize, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBuffer_withoutMaxSize() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(in);
        
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.txt");
        
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        
        assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        
        assertNotNull(boundary);
        assertEquals(32, boundary.length());
    }
}