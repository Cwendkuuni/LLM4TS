package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadDocument() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(input, "UTF-8", null);
        assertEquals("<html><head><meta charset=\"UTF-8\"><title>Untitled Document</title></head><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadDocumentWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"invalidCharset\"></head><body><h1>Test</h1></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(input, "invalidCharset", null);
        // If the charset is not found, it should use the default charset
        assertEquals("<html><head><meta charset=\"UTF-8\"><title>Untitled Document</title></head><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadDocumentWithNoCharset() throws IOException {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(input, null, null);
        // If no charset is specified, it should use the default charset
        assertEquals("<html><head><title>Untitled Document</title></head><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(input, "UTF-8", null, Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"><title>Untitled Document</title></head><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"invalidCharset\"></head><body><h1>Test</h1></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(input, "invalidCharset", null, Parser.htmlParser());
        // If the charset is not found, it should use the default charset
        assertEquals("<html><head><meta charset=\"UTF-8\"><title>Untitled Document</title></head><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testParseInputStreamWithNoCharset() throws IOException {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        // If no charset is specified, it should use the default charset
        assertEquals("<html><head><title>Untitled Document</title></head><body><h1>Test</h1></body></html>", doc.html());
    }

    @Test
    public void testParseInputStreamWithXmlParser() throws IOException {
        String xml = "<root><element>Test</element></root>";
        InputStream input = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(input, "UTF-8", null, Parser.xmlParser());
        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        Element element = root.element(0);
        assertEquals("Test", element.text());
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        String utf8Bom = "\uFEFFTest";
        InputStream input = new ByteArrayInputStream(utf8Bom.getBytes(StandardCharsets.UTF_8));
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(input, 4));
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);

        String utf16Bom = "\uFEFF\uFFFETest";
        input = new ByteArrayInputStream(utf16Bom.getBytes(StandardCharsets.UTF_16LE));
        bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(input, 6));
        assertEquals("UTF-16", bomCharset.charset);
        assertFalse(bomCharset.offset);

        String utf32Bom = "\uFEFF\uFFFE\u0000Test";
        input = new ByteArrayInputStream(utf32Bom.getBytes(StandardCharsets.UTF_32BE));
        bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(input, 12));
        assertEquals("UTF-32", bomCharset.charset);
        assertFalse(bomCharset.offset);

        String noBom = "Test";
        input = new ByteArrayInputStream(noBom.getBytes(StandardCharsets.UTF_8));
        bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(input, 0));
        assertNull(bomCharset);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        assertEquals("UTF-8", charset1);

        String contentType2 = "text/html";
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        assertEquals(null, charset2);

        String contentType3 = "text/html; charset=gb2312";
        String charset3 = DataUtil.getCharsetFromContentType(contentType3);
        assertEquals("gb2312", charset3);
    }

    @Test
    public void testValidateCharset() {
        String charset1 = "UTF-8";
        String charset2 = "GB2312";
        String charset3 = "Invalid";
        String charset4 = "";

        assertEquals(charset1, DataUtil.validateCharset(charset1));
        assertEquals(charset2, DataUtil.validateCharset(charset2));
        assertNull(DataUtil.validateCharset(charset3));
        assertNull(DataUtil.validateCharset(charset4));
    }

    @Test
    public void testMimeBoundary() {
        String expected = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String boundary = DataUtil.mimeBoundary();
        assertEquals(expected, boundary);
    }
}