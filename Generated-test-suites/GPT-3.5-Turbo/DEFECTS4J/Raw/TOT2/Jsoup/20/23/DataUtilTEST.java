package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() {
        try {
            File file = new File("test.html");
            Document doc = DataUtil.load(file, "UTF-8", "http://example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testLoadFromInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
            assertNotNull(doc);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(doc);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testParseByteData() {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
            Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", "http://example.com", Parser.htmlParser());
            assertNotNull(doc);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            byte[] data = {65, 66, 67, 68, 69};
            InputStream inputStream = new ByteArrayInputStream(data);
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
            assertEquals(5, byteBuffer.limit());
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String charset = DataUtil.getCharsetFromContentType(null);
        assertNull(charset);
    }
}