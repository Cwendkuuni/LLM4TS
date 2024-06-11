package org.jsoup.helper;

import static org.junit.Assert.*;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

public class DataUtilTest {
    @Test
    public void testLoadFile() {
        try {
            File file = new File("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Document doc = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(doc);
            assertEquals(baseUri, doc.baseUri());
        } catch (IOException e) {
            fail("IOException caught");
        }
    }

    @Test
    public void testLoadInputStream() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            assertNotNull(doc);
            assertEquals(baseUri, doc.baseUri());
        } catch (IOException e) {
            fail("IOException caught");
        }
    }

    @Test
    public void testLoadInputStreamParser() {
        try {
            InputStream inputStream = new FileInputStream("test.xml");
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.xmlParser();
            Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
            assertNotNull(doc);
            assertEquals(baseUri, doc.baseUri());
        } catch (IOException e) {
            fail("IOException caught");
        }
    }

    @Test
    public void testParseByteData() {
        try {
            ByteBuffer byteData = ByteBuffer.wrap("test data".getBytes());
            String charsetName = "UTF-8";
            String baseUri = "http://example.com";
            Parser parser = Parser.htmlParser();
            Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
            assertNotNull(doc);
            assertEquals(baseUri, doc.baseUri());
        } catch (Exception e) {
            fail("Exception caught");
        }
    }

    @Test
    public void testReadToByteBuffer() {
        try {
            InputStream inputStream = new FileInputStream("test.html");
            ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
            assertNotNull(byteData);
            assertNotEquals(0, byteData.limit());
        } catch (IOException e) {
            fail("IOException caught");
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
}