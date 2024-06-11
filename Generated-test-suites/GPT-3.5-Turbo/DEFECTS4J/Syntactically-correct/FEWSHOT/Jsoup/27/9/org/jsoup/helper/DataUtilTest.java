package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoad() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com/";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("http://www.example.com/", doc.baseUri());
    }

    @Test
    public void testLoad_InputStream() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com/";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("http://www.example.com/", doc.baseUri());
    }

    @Test
    public void testLoad_InputStream_Parser() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com/";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("http://www.example.com/", doc.baseUri());
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("Test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com/";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("http://www.example.com/", doc.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new FileInputStream("test.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
        assertTrue(byteData.capacity() > 0);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }

}