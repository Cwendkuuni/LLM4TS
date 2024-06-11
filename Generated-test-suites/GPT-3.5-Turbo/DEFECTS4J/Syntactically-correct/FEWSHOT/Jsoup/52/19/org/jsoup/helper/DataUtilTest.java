package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoad() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertEquals("", doc.title());
    }

    @Test
    public void testLoadWithInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document doc = DataUtil.load(in, charsetName, baseUri);
        assertEquals("", doc.title());
    }

    @Test
    public void testLoadWithParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.load(in, charsetName, baseUri, parser);
        assertEquals("", doc.title());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(in, out);
        assertEquals("", out.toString());
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertEquals("", doc.title());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inStream = new FileInputStream("input.txt");
        int maxSize = 0;
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, maxSize);
        assertEquals("", new String(byteData.array(), Charset.forName("UTF-8")));
    }

    @Test
    public void testReadToByteBufferCapped() throws IOException {
        InputStream inStream = new FileInputStream("input.txt");
        int maxSize = 100;
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, maxSize);
        assertEquals("", new String(byteData.array(), Charset.forName("UTF-8")));
    }

    @Test
    public void testReadToByteBufferEmpty() throws IOException {
        InputStream inStream = new FileInputStream("empty.txt");
        int maxSize = 0;
        
        ByteBuffer byteData = DataUtil.readToByteBuffer(inStream, maxSize);
        assertEquals("", new String(byteData.array(), Charset.forName("UTF-8")));
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.txt");
        
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        assertEquals("", new String(byteData.array(), Charset.forName("UTF-8")));
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        assertEquals(0, byteData.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap("".getBytes());
        String charsetName = "UTF-8";
        
        String charset = DataUtil.detectCharsetFromBom(byteData, charsetName);
        assertEquals("UTF-8", charset);
    }
}