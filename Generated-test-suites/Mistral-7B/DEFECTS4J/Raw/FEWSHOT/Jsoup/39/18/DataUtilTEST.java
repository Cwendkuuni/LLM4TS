package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class DataUtilTest {

    @Test
    public void testLoad() throws Exception {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertNotNull(doc);
    }

    @Test
    public void testLoadWithDefaultCharset() throws Exception {
        String html = "<html><head></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("ISO-8859-1"));
        Document doc = DataUtil.load(inputStream, null, null);
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertNotNull(doc);
    }

    @Test
    public void testLoadWithBaseUri() throws Exception {
        String html = "<html><head></head><body><a href=\"about.html\">About</a></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        Element link = doc.select("a").first();
        assertEquals("about.html", link.attr("href"));
    }

    @Test
    public void testLoadWithParser() throws Exception {
        String xml = "<root><child>Hello World</child></root>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("child").text());
    }

    @Test
    public void testParseByteData() throws Exception {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws Exception {
        String html = "<html><head><meta charset=\"ISO-8859-1\"></head><body><p>Hello World</p></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithDetectedCharsetAndParser() throws Exception {
        String xml = "<root><child>Hello World</child></root>";
        ByteBuffer byteData = ByteBuffer.wrap(xml.getBytes("UTF-8"));
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.xmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("child").text());
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals("UTF-8", Charset.isSupported(byteData.asCharBuffer().charset().name()));
        assertNotEquals(0, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType("text/html; charset=ISO-8859-1"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html"));
        assertNull(DataUtil.getCharsetFromContentType("text/plain"));
    }
}