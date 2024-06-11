package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class DataUtilTest {

    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "http://example.com";

    private DataUtil dataUtil;

    @Before
    public void setUp() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        try {
            Document doc = dataUtil.load(file, CHARSET_NAME, BASE_URI);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        try {
            Document doc = dataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Parser parser = Parser.htmlParser();
        try {
            Document doc = dataUtil.load(inputStream, CHARSET_NAME, BASE_URI, parser);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        Parser parser = Parser.htmlParser();
        Document doc = dataUtil.parseByteData(byteData, CHARSET_NAME, BASE_URI, parser);
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        int maxSize = 4;
        try {
            ByteBuffer byteData = dataUtil.readToByteBuffer(inputStream, maxSize);
            assertNotNull(byteData);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        try {
            ByteBuffer byteData = dataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteData);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}