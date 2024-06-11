package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class DataUtilTest {
    private static final String BASE_URI = "https://www.example.com/";

    @Test
    public void testLoadFromFile() {
        File file = new File("test.html");
        try {
            Document doc = DataUtil.load(file, null, BASE_URI);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoadFromInputStream() {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getResourceAsStream("test.html");
            assertNotNull(inputStream);

            Document doc = DataUtil.load(inputStream, null, BASE_URI);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    fail("IOException thrown: " + e.getMessage());
                }
            }
        }
    }

    @Test
    public void testLoadFromInputStreamWithCustomParser() {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getResourceAsStream("test.xml");
            assertNotNull(inputStream);

            Document doc = DataUtil.load(inputStream, null, BASE_URI, Parser.xmlParser());
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    fail("IOException thrown: " + e.getMessage());
                }
            }
        }
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><meta charset=\"UTF-8\"></head><body></body></html>".getBytes());
        Document doc = DataUtil.parseByteData(byteData, null, BASE_URI, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.charset().name());
    }

    @Test
    public void testParseByteDataWithCharset() {
        ByteBuffer byteData = ByteBuffer.wrap("<html><head><meta charset=\"ISO-8859-1\"></head><body></body></html>".getBytes());
        Document doc = DataUtil.parseByteData(byteData, "ISO-8859-1", BASE_URI, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("ISO-8859-1", doc.charset().name());
    }

    @Test
    public void testReadToByteBuffer() {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getResourceAsStream("test.txt");
            assertNotNull(inputStream);

            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteData);

            String content = new String(byteData.array());
            assertTrue(content.contains("Lorem ipsum dolor sit amet"));
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    fail("IOException thrown: " + e.getMessage());
                }
            }
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertNotNull(charset);
        assertEquals("UTF-8", charset);

        charset = DataUtil.getCharsetFromContentType("text/html");
        assertNull(charset);

        charset = DataUtil.getCharsetFromContentType("text/html; charset=EUC-JP");
        assertNotNull(charset);
        assertEquals("EUC-JP", charset);
    }
}