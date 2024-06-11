package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testParseByteData() {
        String defaultCharset = DataUtil.defaultCharset;
        ByteBuffer byteData = ByteBuffer.allocate(1024).put("This is a test".getBytes(StandardCharsets.UTF_8));
        byteData.flip();

        // Determine charset from meta tag
        String docData = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser()).body().text();
        assertSame(Parser.htmlParser(), DataUtil.parseByteData(byteData.slice(), docData.trim(), null, Parser.htmlParser()).parser());

        Element meta = new Element("meta");
        meta.attr("http-equiv", "content-type");
        meta.attr("content", "text/html; charset=gb2312");
        Document doc = new Document();
        doc.appendElement(meta);
        String charsetFromMeta = DataUtil.getCharsetFromContentType(meta.attr("content"));
        byteData.rewind();
        docData = Charset.forName(charsetFromMeta).decode(byteData).toString();
        assertSame(Parser.htmlParser(), DataUtil.parseByteData(byteData.slice(), docData, null, Parser.htmlParser()).parser());

        // Determine charset from charset header
        meta.attr("http-equiv", "content-type");
        meta.attr("content", "text/html; charset=" + defaultCharset);
        docData = Charset.forName(defaultCharset).decode(byteData).toString();
        assertSame(Parser.htmlParser(), DataUtil.parseByteData(byteData.slice(), docData, null, Parser.htmlParser()).parser());

        // Determine charset from file extension
        meta.attr("http-equiv", "content-type");
        meta.attr("content", "application/ octet-stream; charset=utf-16");
        docData = Charset.forName("utf-16").decode(byteData).toString();
        assertSame(Parser.htmlParser(), DataUtil.parseByteData(byteData.slice(), docData, null, Parser.htmlParser()).parser());

        // Unsupported charset
        meta.attr("http-equiv", "content-type");
        meta.attr("content", "application/ octet-stream; charset=xyz");
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData.slice(), docData, null, Parser.htmlParser()));

        // BOM
        byteData.put(0, (byte) UNICODE_BOM);
        byteData.flip();
        docData = Charset.forName(defaultCharset).decode(byteData).toString();
        assertSame(Parser.htmlParser(), DataUtil.parseByteData(byteData.slice(), docData, null, Parser.htmlParser()).parser());
    }

    @Test
    public void testLoadFile() throws IOException {
        File in = new File("test.html");
        Document doc = DataUtil.load(in, null, null);
        assertNotNull(doc);
        assertEquals("text/html", doc.outputSettings().contentType());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "This is a test";
        InputStream in = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(in, null, null);
        assertNotNull(doc);
        assertEquals("text/html", doc.outputSettings().contentType());
        assertEquals(testData, doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String testData = "This is a test";
        InputStream in = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(in, "UTF-16", null);
        assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithBaseUri() throws IOException {
        String testData = "<html><body><a href=\"test.html\">Link</a></body></html>";
        InputStream in = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(in, null, "http://example.com/");
        assertNotNull(doc);
        Element link = doc.select("a").first();
        assertEquals("test.html", link.attr("href"));
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String testData = "<html><body><script>alert('Hello world!')</script></body></html>";
        InputStream in = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(in, null, null, Parser.xmlParser());
        assertEquals("text/xml", doc.outputSettings().contentType());
        assertTrue(doc.body().text().isEmpty());
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        InputStream in = new ByteArrayInputStream("Hello world".getBytes(StandardCharsets.UTF_8));
        DataUtil.crossStreams(in, out);
        assertEquals("Hello world", out.toString());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        String testData = "Hello world";
        InputStream in = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        assertEquals(testData.length(), byteData.limit());
        byte[] bytes = new byte[testData.length()];
        byteData.get(bytes);
        assertEquals(testData, new String(bytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File in = new File("test.html");
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(in);
        assertEquals(in.length(), byteData.limit());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        assertEquals(0, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("gb2312", DataUtil.getCharsetFromContentType("text/html; charset=gb2312"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("utf-16", DataUtil.getCharsetFromContentType("application/ octet-stream; charset=utf-16"));
        assertEquals(null, DataUtil.getCharsetFromContentType("application/ octet-stream"));
    }

    @Test
    public void testMimeBoundary() {
        assertEquals(boundaryLength, DataUtil.mimeBoundary().length());
        String boundary = DataUtil.mimeBoundary();
        for (int i = 0; i < boundaryLength; i++) {
            assertEquals(mimeBoundaryChars[i], boundary.charAt(i));
        }
    }
}