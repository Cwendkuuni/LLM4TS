package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;

public class DataUtilTest {
    @Test
    public void testLoad_fromFile() {
        File file = new File("test.html");
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("<html><body><h1>Hello World</h1></body></html>");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        try {
            Document document = DataUtil.load(file, "UTF-8", "");
            Element h1 = document.select("h1").first();
            assertEquals("Hello World", h1.text());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        file.delete();
    }
    
    @Test
    public void testLoad_fromInputStream() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());

        try {
            Document document = DataUtil.load(inputStream, "UTF-8", "");
            Element h1 = document.select("h1").first();
            assertEquals("Hello World", h1.text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoad_withParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());

        try {
            Document document = DataUtil.load(inputStream, "UTF-8", "", Parser.htmlParser());
            Element h1 = document.select("h1").first();
            assertEquals("Hello World", h1.text());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testCrossStreams() {
        String input = "Hello World";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            DataUtil.crossStreams(inputStream, outputStream);
            String output = outputStream.toString();
            assertEquals(input, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testParseByteData() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        Element h1 = document.select("h1").first();
        assertEquals("Hello World", h1.text());
    }
    
    @Test
    public void testReadToByteBuffer() {
        String input = "Hello World";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        
        try {
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
            String output = new String(byteBuffer.array());
            assertEquals(input, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadFileToByteBuffer() {
        File file = new File("test.txt");
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("Hello World");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        try {
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
            String output = new String(byteBuffer.array());
            assertEquals("Hello World", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        file.delete();
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
        
        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
        
        contentType = null;
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(32, boundary.length());
    }
}