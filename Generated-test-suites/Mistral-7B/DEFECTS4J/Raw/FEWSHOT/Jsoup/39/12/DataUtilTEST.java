package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

/**
 * Comprehensive test for DataUtil class.
 *
 */
public class DataUtilTest {

    @Test
    public void testParseByteData() {
        String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><meta charset=\"GB2312\"></head><body><h1>Hello, World!</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));

        String charsetName = null;
        Document doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("Hello, World!", doc.select("h1").text());

        byteData.rewind();
        charsetName = "GB2312";
        doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals("GB2312", doc.outputSettings().charset());
        assertEquals("Hello, World!", doc.select("h1").text());

        byteData.rewind();
        charsetName = "UTF-8";
        doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("Hello, World!", doc.select("h1").text());

        byteData.rewind();
        doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals("UTF-8", doc.outputSettings().charset());
        assertEquals("Hello, World!", doc.select("h1").text());

        try {
            byteData.rewind();
            doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
            fail("Expected IllegalArgumentException on second call.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byteData.rewind();
        charsetName = "invalid-charset";
        doc = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
        assertNull(doc);
    }

    @Test
    public void testLoad() throws IOException {
        String filename = "test.html";
        File file = new File(getClass().getResource(filename).getFile());

        // Test with null charset
        Document doc1 = DataUtil.load(file, null, null);
        assertEquals(StandardCharsets.UTF_8.name(), doc1.outputSettings().charset());

        // Test with valid charset
        Document doc2 = DataUtil.load(file, "UTF-8", null);
        assertEquals("UTF-8", doc2.outputSettings().charset());

        // Test with invalid charset
        Document doc3 = null;
        try {
            doc3 = DataUtil.load(file, "invalid-charset", null);
            fail("Expected IOException.");
        } catch (IOException e) {
            // Expected
        }
        assertNull(doc3);

        // Test with custom base URI
        Document doc4 = DataUtil.load(file, "UTF-8", "http://example.com");
        assertEquals("http://example.com", doc4.baseUri());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        // Test with null charset
        Document doc1 = DataUtil.load(inputStream, null, null);
        assertEquals(StandardCharsets.UTF_8.name(), doc1.outputSettings().charset());

        // Test with valid charset
        Document doc2 = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("UTF-8", doc2.outputSettings().charset());

        // Test with invalid charset
        Document doc3 = null;
        try {
            doc3 = DataUtil.load(inputStream, "invalid-charset", null);
            fail("Expected IOException.");
        } catch (IOException e) {
            // Expected
        }
        assertNull(doc3);

        // Test with custom base URI
        Document doc4 = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertEquals("http://example.com", doc4.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        // Test with null charset
        Document doc1 = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertEquals(StandardCharsets.UTF_8.name(), doc1.outputSettings().charset());

        // Test with valid charset
        Document doc2 = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
        assertEquals("UTF-8", doc2.outputSettings().charset());

        // Test with invalid charset
        Document doc3 = null;
        try {
            doc3 = DataUtil.load(inputStream, "invalid-charset", null, Parser.xmlParser());
            fail("Expected IOException.");
        } catch (IOException e) {
            // Expected
        }
        assertNull(doc3);

        // Test with custom base URI and parser
        Document doc4 = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertEquals("http://example.com", doc4.baseUri());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteData1 = DataUtil.readToByteBuffer(inputStream);
        ByteBuffer byteData2 = DataUtil.readToByteBuffer(inputStream, 5);

        byte[] expectedBytes = html.getBytes(StandardCharsets.UTF_8);
        byte[] expectedSubBytes = new byte[] { 68, 69, 73, 70, 65, 0 };

        byte[] bytes1 = byteData1.array();
        byte[] bytes2 = byteData2.array();

        assertArrayEquals(expectedBytes, bytes1);
        assertArrayEquals(expectedSubBytes, bytes2);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);

        contentType = "text/html; charset=GB2312";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("GB2312", charset);

        contentType = "text/html; charset=invalid-charset";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testCharsetPattern() {
        Pattern pattern = DataUtil.charsetPattern;

        Matcher matcher1 = pattern.matcher("Content-Type: text/html; charset=UTF-8");
        assertTrue(matcher1.find());
        assertEquals("UTF-8", matcher1.group(1));

        Matcher matcher2 = pattern.matcher("Content-Type: text/html; charset=GB2312");
        assertTrue(matcher2.find());
        assertEquals("GB2312", matcher2.group(1));

        Matcher matcher3 = pattern.matcher("Content-Type: text/html; charset=invalid-charset");
        assertFalse(matcher3.find());
    }
}