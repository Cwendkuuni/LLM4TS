package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Parser.HtmlParser;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("gb2312", DataUtil.getCharsetFromContentType("text/html; charset=gb2312"));
        assertEquals("EUC-JP", DataUtil.getCharsetFromContentType("text/html; charset=EUC-JP"));
        assertNull(DataUtil.getCharsetFromContentType("text/html"));
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String html = "<html><head><meta charset=\"gb2312\"></head><body><p>Content</p></body></html>";
        ByteArrayInputStream in = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(in, null, null);
        assertEquals(html, doc.html());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String html = "<html><head><meta charset=\"gb2312\"></head><body><p>Content</p></body></html>";
        ByteArrayInputStream in = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(in, "gb2312", null);
        assertEquals(html, doc.html());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String html = "<html><head><meta charset=\"gb2312\"></head><body><p>Content</p></body></html>";
        ByteArrayInputStream in = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(in, null, null);
        assertEquals(html, doc.html());

        Element meta = doc.selectFirst("meta[charset]");
        assertNotNull(meta);
        assertEquals("gb2312", meta.attr("charset"));
    }

    @Test
    public void testParseByteDataWithDetectedUtf8Charset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><p>Content</p></body></html>";
        ByteArrayInputStream in = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(in, null, null);
        assertEquals(html, doc.html());
    }

    @Test
    public void testParseByteDataWithDetectedInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"InvalidCharset\"></head><body><p>Content</p></body></html>";
        ByteArrayInputStream in = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(in, null, null);
        assertEquals(html, doc.html());

        Element meta = doc.selectFirst("meta[charset]");
        assertNull(meta);
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String filename = "DataUtilTest.java";
        File file = new File(filename);
        Document doc = DataUtil.load(file, null, null);
        FileInputStream inputStream = new FileInputStream(file);
        assertEquals(inputStream.readAllBytes(), doc.body().bytes());
        inputStream.close();
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String filename = "DataUtilTest.java";
        File file = new File(filename);
        Document doc = DataUtil.load(file, "UTF-8", null);
        FileInputStream inputStream = new FileInputStream(file);
        assertEquals(inputStream.readAllBytes(), doc.body().bytes());
        inputStream.close();
    }

    @Test
    public void testLoadFileWithDetectedCharset() throws IOException {
        String filename = "DataUtilTest.java";
        File file = new File(filename);
        Document doc = DataUtil.load(file, null, null);
        FileInputStream inputStream = new FileInputStream(file);
        assertEquals(inputStream.readAllBytes(), doc.body().bytes());
        inputStream.close();

        Element meta = doc.selectFirst("meta[charset]");
        assertNotNull(meta);
        assertEquals("UTF-8", meta.attr("charset"));
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String html = "<html><head><meta charset=\"gb2312\"></head><body><p>Content</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals(html, doc.html());
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String html = "<html><head><meta charset=\"gb2312\"></head><body><p>Content</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "gb2312", null);
        assertEquals(html, doc.html());
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithDetectedCharset() throws IOException {
        String html = "<html><head><meta charset=\"gb2312\"></head><body><p>Content</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals(html, doc.html());
        inputStream.close();

        Element meta = doc.selectFirst("meta[charset]");
        assertNotNull(meta);
        assertEquals("gb2312", meta.attr("charset"));
    }

    @Test
    public void testLoadInputStreamWithDetectedUtf8Charset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><p>Content</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals(html, doc.html());
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithDetectedInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"InvalidCharset\"></head><body><p>Content</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals(html, doc.html());
        inputStream.close();

        Element meta = doc.selectFirst("meta[charset]");
        assertNull(meta);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><head><meta charset=\"gb2312\"></head><body><p>Content</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, null, null, HtmlParser.instance());
        assertEquals(html, doc.html());
        inputStream.close();
    }
}