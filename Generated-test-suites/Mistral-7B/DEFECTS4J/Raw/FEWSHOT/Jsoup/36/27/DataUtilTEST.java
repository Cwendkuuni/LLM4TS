package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {
    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testFile.html");
        Document doc = DataUtil.load(file, null, "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("testFile.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        File file = new File("testFile.html");
        ByteArrayInputStream inputStream = new ByteArrayInputStream("testContent".getBytes());
        Document doc = DataUtil.load(inputStream, null, "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        File file = new File("testFile.html");
        ByteArrayInputStream inputStream = new ByteArrayInputStream("testContent".getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        File file = new File("testFile.html");
        ByteArrayInputStream inputStream = new ByteArrayInputStream("testContent".getBytes());
        Document doc = DataUtil.load(inputStream, null, "", Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String testContent = "testContent";
        ByteBuffer byteData = ByteBuffer.wrap(testContent.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String testContent = "testContent";
        ByteBuffer byteData = ByteBuffer.wrap(testContent.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File file = new File("testFile.html");
        FileInputStream inputStream = new FileInputStream(file);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 0);
        assertNotNull(byteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeUnknown() {
        String contentType = "text/html; charset=unknown";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}