package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, null, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
        assertEquals("<html><body></body></html>", document.html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithParser() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals("<html><body></body></html>", document.xml().asXml());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source content";
        String destination = "Destination content";

        InputStream inputStream = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        assertArrayEquals(source.getBytes(StandardCharsets.UTF_8), outputStream.toByteArray());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("<html><body></body></html>", document.html());
    }

    @Test
    public void testBomCharset() throws IOException {
        String utf8Bom = "\uFEFFfoo";
        String utf16Bom = "\uFEFF\u0000foo";
        String utf32Bom = "\uFEFF\u0000\u0000foo";

        ByteBuffer utf8ByteBuffer = ByteBuffer.wrap(utf8Bom.getBytes(StandardCharsets.UTF_8));
        ByteBuffer utf16ByteBuffer = ByteBuffer.wrap(utf16Bom.getBytes(StandardCharsets.UTF_16LE));
        ByteBuffer utf32ByteBuffer = ByteBuffer.wrap(utf32Bom.getBytes(StandardCharsets.UTF_32BE));

        DataUtil.BomCharset utf8BomCharset = DataUtil.detectCharsetFromBom(utf8ByteBuffer);
        DataUtil.BomCharset utf16BomCharset = DataUtil.detectCharsetFromBom(utf16ByteBuffer);
        DataUtil.BomCharset utf32BomCharset = DataUtil.detectCharsetFromBom(utf32ByteBuffer);

        assertEquals("UTF-8", utf8BomCharset.charset);
        assertFalse(utf8BomCharset.offset);

        assertEquals("UTF-16", utf16BomCharset.charset);
        assertFalse(utf16BomCharset.offset);

        assertEquals("UTF-32", utf32BomCharset.charset);
        assertTrue(utf32BomCharset.offset);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html; charset=ISO-8859-1";
        String contentType3 = "text/html";

        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType1));
        assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType(contentType2));
        assertNull(DataUtil.getCharsetFromContentType(contentType3));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("utf-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\t"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\n"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\r"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\r\n"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\t\r\n"));

        assertNull(DataUtil.validateCharset(""));
        assertNull(DataUtil.validateCharset(null));
        assertNull(DataUtil.validateCharset("invalidCharset"));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);

        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(Character.isAlphabetic(boundary.charAt(i)) || Character.isDigit(boundary.charAt(i)) || boundary.charAt(i) == '-');
        }
    }
}