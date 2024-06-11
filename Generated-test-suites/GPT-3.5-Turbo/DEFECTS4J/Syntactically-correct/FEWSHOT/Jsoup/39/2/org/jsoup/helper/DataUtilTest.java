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

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        // Add more assertions if applicable
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body><p>Hello, World!</p></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        // Add more assertions if applicable
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><body><p>Hello, World!</p></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(in, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        // Add more assertions if applicable
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(100);
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        // Add more assertions if applicable
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream in = new ByteArrayInputStream(data);

        ByteBuffer byteData = DataUtil.readToByteBuffer(in, 3);
        assertNotNull(byteData);
        assertEquals(3, byteData.limit());
        // Add more assertions if applicable
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream in = new ByteArrayInputStream(data);

        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        assertNotNull(byteData);
        assertEquals(5, byteData.limit());
        // Add more assertions if applicable
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
        
        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
        // Add more assertions if applicable
    }
}