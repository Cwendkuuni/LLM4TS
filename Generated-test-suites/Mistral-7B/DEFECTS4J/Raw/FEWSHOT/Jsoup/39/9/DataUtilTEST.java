package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=utf-8"));
        assertEquals("EUC-JP", DataUtil.getCharsetFromContentType("text/html; charset=EUC-JP"));
        assertEquals("GBK", DataUtil.getCharsetFromContentType("text/html; charset=gbk"));
        assertNull(DataUtil.getCharsetFromContentType("text/html"));
        assertNull(DataUtil.getCharsetFromContentType("text/html; charset="));
        assertNull(DataUtil.getCharsetFromContentType("text/html; charset=foo"));
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new File("src/test/resources/test.html").getInputStream());
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("Test Page", document.title());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new File("src/test/resources/test.html").getInputStream());
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("Test Page", document.title());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharsetAndParser() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new File("src/test/resources/test.html").getInputStream());
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.xmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals(0, document.select("html").size());
        assertEquals(0, document.select("head").size());
        assertEquals(0, document.select("title").size());
    }

    @Test
    public void testParseByteDataWithMetaCharset() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new File("src/test/resources/test-meta-charset.html").getInputStream());
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("Shift_JIS", document.outputSettings().charset());
        assertEquals("Test Page (Shift_JIS)", document.title());
    }

    @Test
    public void testParseByteDataWithInvalidMetaCharset() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new File("src/test/resources/test-invalid-meta-charset.html").getInputStream());
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("Test Page", document.title());
    }

    @Test
    public void testParseByteDataWithInvalidContentTypeCharset() throws IOException {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new File("src/test/resources/test-invalid-content-type-charset.html").getInputStream());
        Document document = DataUtil.parseByteData(byteData, "foo", null, Parser.htmlParser());
        assertEquals("UTF-8", document.outputSettings().charset());
        assertEquals("Test Page", document.title());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        File file = new File("src/test/resources/test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new FileInputStream(file), 1024);
        assertArrayEquals(new byte[1024], byteBuffer.array());
    }

    @Test
    public void testReadToByteBufferWithoutMaxSize() throws IOException {
        File file = new File("src/test/resources/test.html");
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new FileInputStream(file));
        assertEquals(509245, byteBuffer.limit());
    }
}