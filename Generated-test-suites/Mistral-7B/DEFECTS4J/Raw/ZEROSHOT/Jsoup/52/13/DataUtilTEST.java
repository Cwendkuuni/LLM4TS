package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * Tests for the DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("src/test/resources/DataUtilTest.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-16\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-16";
        String baseUri = null;
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = null;
        String baseUri = null;
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"X-PES-PD\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "X-PES-PD";
        String baseUri = null;
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            fail("Expected IOException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseByteData() throws IOException {
        String html = "<html><head><meta charset=\"UTF-16\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        String charsetName = "UTF-16";
        String baseUri = null;
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        String charsetName = null;
        String baseUri = null;
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("UTF-8", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"X-PES-PD\"></head><body></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(html.getBytes());
        String charsetName = "X-PES-PD";
        String baseUri = null;
        try {
            Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
            fail("Expected IOException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String input = "Hello World!";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        OutputStream out = new ByteArrayOutputStream();
        DataUtil.crossStreams(in, out);
        byte[] expectedOutput = "Hello World!".getBytes();
        assertEquals(expectedOutput.length, out.size());
        assertArrayEquals(expectedOutput, out.toByteArray());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-16";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-16", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-8";
        charset = charset.toUpperCase();
        assertEquals(charset, DataUtil.validateCharset(charset));

        charset = "UTF-8";
        try {
            charset = Charset.nameForCodePoint(0x1F00);
            assertEquals(charset, DataUtil.validateCharset(charset));
        } catch (IllegalCharsetNameException e) {
            fail("Expected valid charset");
        }

        charset = null;
        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertFalse(boundary.isEmpty());
        assertTrue(boundary.length() > 31);
    }
}