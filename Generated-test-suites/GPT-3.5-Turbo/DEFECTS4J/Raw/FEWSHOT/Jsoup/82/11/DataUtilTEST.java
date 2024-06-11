package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        Document document = DataUtil.load(new File("test.html"), StandardCharsets.UTF_8.name(), "");
        assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        
        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "");
        assertNotNull(document);
    }
    
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        
        Document document = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), "", Parser.xmlParser());
        assertNotNull(document);
    }
    
    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals("Hello World!", outputStream.toString(StandardCharsets.UTF_8));
    }
    
    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        
        Document document = DataUtil.parseInputStream(inputStream, StandardCharsets.UTF_8.name(), "");
        assertNotNull(document);
    }
    
    @Test
    public void testReadToByteBuffer() throws IOException {
        String text = "Hello World!";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertEquals(text, StandardCharsets.UTF_8.decode(byteBuffer).toString());
    }
    
    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(DataUtil.boundaryLength, boundary.length());
    }
    
    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
    
    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(charset, validatedCharset);
    }
    
    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.capacity());
    }
    
    @Test
    public void testDetectCharsetFromBom() throws IOException {
        byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bom);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(byteInputStream, 1024);
        
        DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);
        assertNotNull(bomCharset);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(true, bomCharset.offset);
    }
}