package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {
    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteBuffer byteData = Charset.forName("UTF-8").encode(Charset.forName("UTF-8").decode(html.getBytes()));
        String charsetName = "UTF-8";
        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals(html, doc.body().html());

        String charsetHeader = "text/html; charset=GBK";
        html = "<html><head><meta charset=\"GBK\"></head><body></body></html>";
        byteData = Charset.forName("UTF-8").encode(Charset.forName("UTF-8").decode(html.getBytes()));
        charsetName = null;
        doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals(html, doc.body().html());
        assertEquals("GBK", doc.outputSettings().charset());

        String invalidCharsetName = "INVALID_CHARSET";
        byteData = Charset.forName("UTF-8").encode(Charset.forName("UTF-8").decode(html.getBytes()));
        charsetName = invalidCharsetName;
        doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals(html, doc.body().html());
        assertNull(doc.outputSettings().charset());
    }

    @Test
    public void testLoad() throws IOException {
        String filePath = "src/test/resources/test.html";
        Document doc = DataUtil.load(new File(filePath), null, null);
        assertEquals("test.html", doc.title());

        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        doc = DataUtil.load(inputStream, null, null);
        assertEquals("test.html", doc.title());

        File file = new File(filePath);
        InputStream inputStreamFromFile = new FileInputStream(file);
        doc = DataUtil.load(inputStreamFromFile, null, null);
        assertEquals("test.html", doc.title());
    }

    @Test
    public void testLoadWithCharset() throws IOException {
        String filePath = "src/test/resources/test_gbk.html";
        Document doc = DataUtil.load(new File(filePath), "GBK", null);
        assertEquals("test_gbk.html", doc.title());
        assertEquals("GBK", doc.outputSettings().charset());

        InputStream inputStream = getClass().getResourceAsStream("/test_gbk.html");
        doc = DataUtil.load(inputStream, "GBK", null);
        assertEquals("test_gbk.html", doc.title());
        assertEquals("GBK", doc.outputSettings().charset());
    }

    @Test
    public void testLoadWithBaseUri() throws IOException {
        String filePath = "src/test/resources/test.html";
        String baseUri = "https://example.com/base";
        Document doc = DataUtil.load(new File(filePath), null, baseUri);
        assertEquals("test.html", doc.title());
        Element link = doc.select("link[href=../test2.html]").first();
        assertEquals("https://example.com/base/test2.html", link.attr("abs:href"));

        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        doc = DataUtil.load(inputStream, null, baseUri);
        assertEquals("test.html", doc.title());
        link = doc.select("link[href=../test2.html]").first();
        assertEquals("https://example.com/base/test2.html", link.attr("abs:href"));
    }

    @Test
    public void testLoadWithParser() throws IOException {
        String filePath = "src/test/resources/test.xml";
        Document doc = DataUtil.load(new File(filePath), null, null, Parser.xmlParser());
        assertEquals("test", doc.title());

        InputStream inputStream = getClass().getResourceAsStream("/test.xml");
        doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertEquals("test", doc.title());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><body></body></html>";
        ByteBuffer byteData = Charset.forName("UTF-8").encode(Charset.forName("UTF-8").decode(html.getBytes()));

        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        ByteBuffer readByteData = DataUtil.readToByteBuffer(inputStream);
        assertEquals(byteData, readByteData);

        File file = new File("test.html");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.writeBytes(html.getBytes());
        randomAccessFile.close();
        byteData = DataUtil.readFileToByteBuffer(file);
        readByteData = DataUtil.readToByteBuffer(new FileInputStream(file));
        assertEquals(byteData, readByteData);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();
        byte[] expectedBytes = new byte[0];
        byte[] actualBytes = new byte[emptyByteBuffer.limit()];
        emptyByteBuffer.get(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType1));

        String contentType2 = "text/html; charset=gbk";
        assertEquals("gbk", DataUtil.getCharsetFromContentType(contentType2));

        String contentType3 = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType3));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        int boundaryLength = boundary.length();
        assertFalse(boundary.contains("--"));
        assertFalse(boundary.contains("-"));
        assertTrue(boundary.matches("\\-[_0-9a-zA-Z]+"));
        assertEquals(boundaryLength, DataUtil.boundaryLength);
    }
}