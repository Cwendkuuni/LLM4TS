package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {
  
    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
        // Add more assertions for the loaded document if needed
    }
    
    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(in, "UTF-8", "");
        assertNotNull(doc);
        // Add more assertions for the loaded document if needed
    }
    
    @Test
    public void testLoadFromInputStreamUsingParser() throws IOException {
        String html = "<html></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(in, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        // Add more assertions for the loaded document if needed
    }
    
    @Test
    public void testParseByteData() {
        String html = "<html></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(Charset.forName("UTF-8")));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
        // Add more assertions for the parsed document if needed
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        assertNotNull(byteData);
        // Add more assertions for the byte data if needed
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
        // Add more assertions for different content types if needed
    }
    
}