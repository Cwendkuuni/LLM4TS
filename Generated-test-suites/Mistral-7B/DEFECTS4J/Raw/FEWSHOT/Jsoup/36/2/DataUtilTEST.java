package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser.HtmlParser;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws Exception {
        String xmlData = "<html><head><meta charset=\"UTF-8\"/><title>Test</title></head><body>Hello world</body></html>";
        File testFile = new File("test.html");
        FileUtils.writeStringToFile(testFile, xmlData, "UTF-8");

        Document doc = DataUtil.load(testFile, "UTF-8", null);
        assertEquals("Hello world", doc.body().text());
        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadStreamWithDefaultCharset() throws Exception {
        String xmlData = "<html><head><meta charset=\"UTF-8\"/><title>Test</title></head><body>Hello world</body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals("Hello world", doc.body().text());
    }

    @Test
    public void testLoadStreamWithSpecifiedCharset() throws Exception {
        String xmlData = "<html><head><meta charset=\"UTF-8\"/><title>Test</title></head><body>Hello world</body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("Hello world", doc.body().text());

        inputStream = new ByteArrayInputStream(xmlData.getBytes("GBK")); // Unsupported charset

        try {
            doc = DataUtil.load(inputStream, "GBK", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadStreamWithParser() throws Exception {
        String xmlData = "<html><head><meta charset=\"UTF-8\"/><title>Test</title></head><body>Hello world</body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, null, null, new HtmlParser());
        assertEquals("Hello world", doc.body().text());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String xmlData = "<html><head><meta charset=\"UTF-8\"/><title>Test</title></head><body>Hello world</body></html>";
        ByteBuffer byteData = Charset.forName("UTF-8").decode(xmlData.getBytes("UTF-8"));

        Document doc = DataUtil.parseByteData(byteData, null, null, new HtmlParser());
        assertEquals("Hello world", doc.body().text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String xmlData = "<html><head><meta charset=\"UTF-8\"/><title>Test</title></head><body>Hello world</body></html>";
        ByteBuffer byteData = Charset.forName("UTF-8").decode(xmlData.getBytes("UTF-8"));

        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, new HtmlParser());
        assertEquals("Hello world", doc.body().text());

        byteData = ByteBuffer.wrap(xmlData.getBytes("GBK")); // Unsupported charset

        try {
            doc = DataUtil.parseByteData(byteData, "GBK", null, new HtmlParser());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        String xmlData = "<html><head><meta charset=\"UTF-8\"/><title>Test</title></head><body>Hello world</body></html>";
        InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes("UTF-8"));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(xmlData.length(), byteData.limit());

        inputStream = new ByteArrayInputStream(xmlData.getBytes("GBK")); // Unsupported charset

        try {
            byteData = DataUtil.readToByteBuffer(inputStream);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

}