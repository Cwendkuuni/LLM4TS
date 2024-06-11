package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DataUtilTest {
    @Test
    public void testLoadFromFile() throws IOException {
        File testFile = new File("test.html");
        assertNotNull(DataUtil.load(testFile, "UTF-8", "http://example.com"));
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        assertNotNull(DataUtil.load(inputStream, "UTF-8", "http://example.com"));
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        assertNotNull(DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser()));
    }

    @Test
    public void testLoadWithNullInputStream() throws IOException {
        assertNull(DataUtil.load((InputStream) null, "UTF-8", "http://example.com"));
    }

    @Test
    public void testLoadWithNullInputStreamAndParser() throws IOException {
        assertNull(DataUtil.load((InputStream) null, "UTF-8", "http://example.com", Parser.htmlParser()));
    }

    @Test
    public void testParseByteData() {
        // Test case for null charsetName
        ByteBuffer byteData = ByteBuffer.wrap("Test data".getBytes());
        String charsetName = null;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        assertNotNull(DataUtil.parseByteData(byteData, charsetName, baseUri, parser));
        
        // Test case for non-null charsetName
        charsetName = "UTF-8";
        assertNotNull(DataUtil.parseByteData(byteData, charsetName, baseUri, parser));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        assertNotNull(DataUtil.readToByteBuffer(inputStream));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}