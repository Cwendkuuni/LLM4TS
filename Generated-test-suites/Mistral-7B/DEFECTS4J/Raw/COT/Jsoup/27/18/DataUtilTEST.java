package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.parser.Parser.HtmlParser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "testFile.html";
        File file = new File(filePath);
        Document doc = DataUtil.load(file, null, null);
        assertNotNull(doc);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadFileNotFound() throws IOException, FileNotFoundException {
        String filePath = "nonExistentFile.html";
        File file = new File(filePath);
        DataUtil.load(file, null, null);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null, new HtmlParser());
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException, IOException {
        String html = "<html><head><meta charset=\"UTF-16\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-16", null);
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, new HtmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, null, null, new HtmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        byte[] byteArray = byteData.array();
        assertEquals(html.getBytes("UTF-8").length, byteArray.length);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        String contentType2 = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType2);
        assertNull(charset);
    }
}