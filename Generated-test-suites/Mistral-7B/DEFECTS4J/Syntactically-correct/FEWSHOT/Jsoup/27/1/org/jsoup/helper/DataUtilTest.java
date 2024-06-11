package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {
    private static final String HTML_CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final String HTML_WITH_GB2312_META = "<html lang=\"zh-CN\" xmlns=\"http://www.w3.org/1999/xhtml\">" +
            "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"/></head>" +
            "<body><h1>Test</h1></body></html>";

    private Document utf8Html;
    private Document gb2312Html;

    @Before
    public void setUp() {
        utf8Html = parseHtml("UTF-8.html");
        gb2312Html = parseHtml("GB2312.html");
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws Exception {
        Document document = DataUtil.load(new File("UTF-8.html"), null, null);
        assertEquals(utf8Html, document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws Exception {
        Document document = DataUtil.load(new File("UTF-8.html"), "UTF-8", null);
        assertEquals(utf8Html, document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(HTML_CONTENT_TYPE.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, null, null);
        assertEquals(utf8Html, document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(HTML_CONTENT_TYPE.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals(utf8Html, document);
    }

    @Test
    public void testLoadInputStreamAndDetectCharset() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(HTML_WITH_GB2312_META.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, null, null);
        assertEquals(gb2312Html, document);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        ByteBuffer byteData = readByteBuffer("UTF-8.html");
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals(utf8Html, document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        ByteBuffer byteData = readByteBuffer("UTF-8.html");
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertEquals(utf8Html, document);
    }

    @Test
    public void testParseByteDataAndDetectCharset() {
        String charset = "gb2312";
        ByteBuffer byteData = readByteBuffer("GB2312.html");
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals(gb2312Html, document);
        assertEquals(charset, document.outputSettings().charset());
    }

    private Document parseHtml(String fileName) throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/" + fileName);
        Document document = DataUtil.load(inputStream, null, null);
        inputStream.close();
        return document;
    }

    private ByteBuffer readByteBuffer(String fileName) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/" + fileName);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        inputStream.close();
        return byteData;
    }
}