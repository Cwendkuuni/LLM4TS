package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

import static org.junit.Assert.*;

public class DataUtilTest {
    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "https://www.example.com";

    private DataUtil dataUtil;

    @Before
    public void setUp() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFromFile() {
        File file = new File("data.html");
        try {
            Document document = dataUtil.load(file, CHARSET_NAME, BASE_URI);
            assertNotNull(document);
        } catch (IOException e) {
            fail("Failed to load document from file");
        }
    }

    @Test
    public void testLoadFromInputStream() {
        InputStream inputStream = getClass().getResourceAsStream("data.html");
        try {
            Document document = dataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
            assertNotNull(document);
        } catch (IOException e) {
            fail("Failed to load document from input stream");
        }
    }

    @Test
    public void testLoadFromInputStreamWithParser() {
        InputStream inputStream = getClass().getResourceAsStream("data.html");
        try {
            Document document = dataUtil.load(inputStream, CHARSET_NAME, BASE_URI, Parser.htmlParser());
            assertNotNull(document);
        } catch (IOException e) {
            fail("Failed to load document from input stream with parser");
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.allocate(1024);
        String data = "<html><head></head><body><h1>Hello World</h1></body></html>";
        byteData.put(data.getBytes());
        byteData.flip();
        Document document = dataUtil.parseByteData(byteData, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() {
        byte[] buffer = new byte[1024];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
        try {
            ByteBuffer byteData = dataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteData);
        } catch (IOException e) {
            fail("Failed to read to byte buffer");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }
}