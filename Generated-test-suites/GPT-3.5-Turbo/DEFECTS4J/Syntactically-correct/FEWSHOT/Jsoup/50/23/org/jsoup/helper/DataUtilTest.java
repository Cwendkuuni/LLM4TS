package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "https://www.example.com";
        Document doc = DataUtil.load(file, charset, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = "https://www.example.com";
        Document doc = DataUtil.load(in, charset, baseUri);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        String charset = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(in, charset, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        InputStream in = new FileInputStream("input.txt");
        OutputStream out = new FileOutputStream("output.txt");
        
        DataUtil.crossStreams(in, out);
        
        out.close();
        
        File outputFile = new File("output.txt");
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);
    }
    
    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        
        assertNotNull(doc);
        assertEquals(baseUri, doc.baseUri());
    }
    
    @Test
    public void testReadToByteBufferWithMaxSize() {
        InputStream inStream = new FileInputStream("input.txt");
        int maxSize = 1024;
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream, maxSize);
        
        assertNotNull(byteBuffer);
        assertEquals(maxSize, byteBuffer.capacity());
    }
    
    @Test
    public void testReadToByteBuffer() {
        InputStream inStream = new FileInputStream("input.txt");
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inStream);
        
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() > 0);
    }
    
    @Test
    public void testReadFileToByteBuffer() {
        File file = new File("test.html");
        
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.capacity() > 0);
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();
        
        assertNotNull(emptyByteBuffer);
        assertEquals(0, emptyByteBuffer.capacity());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        
        assertNull(charset);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
}