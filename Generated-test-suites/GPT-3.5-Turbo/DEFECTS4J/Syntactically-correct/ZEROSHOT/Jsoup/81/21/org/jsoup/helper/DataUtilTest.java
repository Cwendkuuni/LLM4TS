package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class DataUtilTest {

    private DataUtil dataUtil;

    @Before
    public void setup() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoad_File_CharsetName_BaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Document document = dataUtil.load(file, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoad_InputStream_CharsetName_BaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Document document = dataUtil.load(inputStream, charsetName, baseUri);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testLoad_InputStream_CharsetName_BaseUri_Parser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        Document document = dataUtil.load(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        ByteBuffer outputBuffer = ByteBuffer.allocate(1024);
        dataUtil.crossStreams(inputStream, outputBuffer);
        
        assertNotNull(outputBuffer);
        assertEquals(1024, outputBuffer.limit());
    }

    @Test
    public void testParseInputStream_Input_CharsetName_BaseUri_Parser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        Document document = dataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testReadToByteBuffer_Input_MaxSize() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        int maxSize = 1024;
        ByteBuffer byteBuffer = dataUtil.readToByteBuffer(inputStream, maxSize);
        
        assertNotNull(byteBuffer);
        assertEquals(maxSize, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBuffer_Input() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("input.txt");
        ByteBuffer byteBuffer = dataUtil.readToByteBuffer(inputStream);
        
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.limit() > 0);
    }

    @Test
    public void testReadFileToByteBuffer_File() throws IOException {
        File file = new File("test.txt");
        ByteBuffer byteBuffer = dataUtil.readFileToByteBuffer(file);
        
        assertNotNull(byteBuffer);
        assertTrue(byteBuffer.limit() > 0);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = dataUtil.emptyByteBuffer();
        
        assertNotNull(byteBuffer);
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = dataUtil.mimeBoundary();
        
        assertNotNull(mimeBoundary);
        assertEquals(32, mimeBoundary.length());
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = dataUtil.validateCharset(charset);
        
        assertNotNull(validatedCharset);
        assertEquals(charset, validatedCharset);
    }

    @Test
    public void testParseInputStream_WithCharsetDetection() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String baseUri = "https://www.example.com";
        Parser parser = Parser.htmlParser();
        Document document = dataUtil.parseInputStream(inputStream, null, baseUri, parser);
        
        assertNotNull(document);
        assertEquals(baseUri, document.baseUri());
    }
}