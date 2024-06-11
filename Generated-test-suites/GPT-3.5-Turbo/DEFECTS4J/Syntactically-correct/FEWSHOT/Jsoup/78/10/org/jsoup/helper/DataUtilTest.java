package org.jsoup.helper;

import static org.junit.Assert.*;
import org.junit.Test;

public class DataUtilTest {
    
    @Test
    public void testLoadFile() {
        try {
            String filePath = "test.html";
            String charsetName = "UTF-8";
            String baseUri = "http://www.example.com";
            
            Document doc = DataUtil.load(new File(filePath), charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://www.example.com";
            
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testLoadInputStreamWithParser() {
        try {
            InputStream inputStream = new FileInputStream("test.xml");
            String charsetName = "UTF-8";
            String baseUri = "http://www.example.com";
            Parser parser = Parser.xmlParser();
            
            Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testCrossStreams() {
        try {
            InputStream in = new ByteArrayInputStream("test".getBytes());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            
            DataUtil.crossStreams(in, out);
            
            assertEquals("test", out.toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testParseInputStream() {
        try {
            InputStream input = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://www.example.com";
            Parser parser = Parser.htmlParser();
            
            Document doc = DataUtil.parseInputStream(input, charsetName, baseUri, parser);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inStream = new FileInputStream("test.html");
            int maxSize = 1024;
            
            ByteBuffer buffer = DataUtil.readToByteBuffer(inStream, maxSize);
            assertNotNull(buffer);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testReadToByteBufferWithZeroMaxSize() {
        try {
            InputStream inStream = new FileInputStream("test.html");
            int maxSize = 0;
            
            ByteBuffer buffer = DataUtil.readToByteBuffer(inStream, maxSize);
            assertNotNull(buffer);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testReadToByteBufferFromFile() {
        try {
            File file = new File("test.html");
            
            ByteBuffer buffer = DataUtil.readFileToByteBuffer(file);
            assertNotNull(buffer);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer buffer = DataUtil.emptyByteBuffer();
        assertEquals(0, buffer.limit());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalid";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
    
    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("UTF-8", validatedCharset);
    }
    
    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "invalid";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }
    
    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertNotNull(mimeBoundary);
        assertEquals(DataUtil.boundaryLength, mimeBoundary.length());
    }
    
}