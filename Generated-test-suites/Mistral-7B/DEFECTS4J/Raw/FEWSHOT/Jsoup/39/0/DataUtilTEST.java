package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=utf-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8; foo=bar"));
        assertNull(DataUtil.getCharsetFromContentType("text/html"));
        assertNull(DataUtil.getCharsetFromContentType("text/html; charset="));
        assertNull(DataUtil.getCharsetFromContentType("text/html; charset=foo"));
        assertNull(DataUtil.getCharsetFromContentType("text/html; charset=foo bar"));
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello World</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());

        String invalidHtml = "<html><head><meta charset=\"invalidCharset\"></head><body><div>Hello World</div></body></html>";
        inputStream = new ByteArrayInputStream(invalidHtml.getBytes("UTF-8"));
        doc = DataUtil.load(inputStream, null, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());

        String htmlWithBom = "\uFEFF<html><head><meta charset=\"UTF-8\"></head><body><div>Hello World</div></body></html>";
        inputStream = new ByteArrayInputStream(htmlWithBom.getBytes("UTF-8"));
        doc = DataUtil.load(inputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());

        String invalidHtmlWithBom = "\uFEFF<html><head><meta charset=\"invalidCharset\"></head><body><div>Hello World</div></body></html>";
        inputStream = new ByteArrayInputStream(invalidHtmlWithBom.getBytes("UTF-8"));
        doc = DataUtil.load(inputStream, null, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());

        String charsetMeta = "<html><head><meta charset=\"ISO-8859-1\"></head><body><div>Hello World</div></body></html>";
        inputStream = new ByteArrayInputStream(charsetMeta.getBytes("UTF-8"));
        doc = DataUtil.load(inputStream, null, null, Parser.htmlParser());
        assertEquals("ISO-8859-1", doc.outputSettings().charset());

        String charsetLink = "<html><head><link rel=\"shortcut icon\" href=\"favicon.ico\"><link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\"><meta charset=\"UTF-8\"></head><body><div>Hello World</div></body></html>";
        inputStream = new ByteArrayInputStream(charsetLink.getBytes("UTF-8"));
        doc = DataUtil.load(inputStream, null, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());

        String noCharset = "<html><head></head><body><div>Hello World</div></body></html>";
        inputStream = new ByteArrayInputStream(noCharset.getBytes("UTF-8"));
        doc = DataUtil.load(inputStream, null, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "src/test/resources/DataUtilTest.java";
        File file = new File(filePath);
        Document doc = DataUtil.load(file, null, null);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithCharset() throws IOException {
        String filePath = "src/test/resources/DataUtilTest.java";
        File file = new File(filePath);
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello World</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithCharsetNull() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><div>Hello World</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, null, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String xml = "<root><element>Hello World</element></root>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        Document doc = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("Hello World", doc.rootElement().elementText());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String data = "Hello World";
        InputStream inputStream = new ByteArrayInputStream(data.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        byte[] expected = data.getBytes("UTF-8");
        byte[] actual = new byte[byteBuffer.limit()];
        byteBuffer.get(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String data = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.";
        InputStream inputStream = new ByteArrayInputStream(data.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 5);

        assertTrue(byteBuffer.limit() <= 5);

        byte[] expected = new byte[5];
        byteBuffer.get(expected);
        String actual = Charset.forName("UTF-8").decode(byteBuffer).toString();
        assertEquals(new String(expected), actual.substring(0, 5));
    }

    @Test
    public void testReadToByteBufferWithUnlimitedMaxSize() throws IOException {
        String data = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog.";
        InputStream inputStream = new ByteArrayInputStream(data.getBytes("UTF-8"));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        byte[] expected = data.getBytes("UTF-8");
        byte[] actual = new byte[byteBuffer.limit()];
        byteBuffer.get(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testLoadWithNullFile() {
        try {
            DataUtil.load(null, null, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadWithNullInputStream() {
        try {
            DataUtil.load(null, null, null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseByteDataWithNullCharsetName() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        String data = "Hello World";
        byteBuffer.put(data.getBytes("UTF-8"));
        byteBuffer.flip();

        try {
            DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseByteDataWithNullParser() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        String data = "Hello World";
        byteBuffer.put(data.getBytes("UTF-8"));
        byteBuffer.flip();

        try {
            Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadWithNullBaseUri() {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><a href=\"/test.html\">Test</a></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));

        Document doc1 = DataUtil.load(inputStream, "UTF-8", null);
        Document doc2 = DataUtil.load(inputStream, "UTF-8", "");

        assertSame(doc1, doc2);
    }

    @Test
    public void testLoadWithEmptyBaseUri() {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><a href=\"test.html\">Test</a></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));

        Document doc1 = DataUtil.load(inputStream, "UTF-8", "");
        Document doc2 = DataUtil.load(inputStream, "UTF-8", null);

        assertSame(doc1, doc2);
    }

    @Test
    public void testLoadWithBaseUri() {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><a href=\"test.html\">Test</a></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));

        Document doc1 = DataUtil.load(inputStream, "UTF-8", "/base/url");
        Document doc2 = DataUtil.load(inputStream, "UTF-8", "http://example.com/base/url");

        String expectedUrl1 = "/base/url/test.html";
        String expectedUrl2 = "http://example.com/base/url/test.html";

        assertEquals(expectedUrl1, doc1.baseUri());
        assertEquals(expectedUrl2, doc2.baseUri());
    }
}