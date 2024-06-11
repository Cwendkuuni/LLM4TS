package org.jsoup.helper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals(baseUri, document.baseUri());
        assertEquals(html, document.html());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"gb2312\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "https://example.com";
        Document document = assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
        assertNull(document);
    }

    @Test
    public void testLoadDocumentFromInputStreamWithNullBaseUri() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = null;
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNull(document.baseUri());
        assertEquals(html, document.html());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertEquals(baseUri, document.baseUri());
        assertEquals(html, document.html());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"gb2312\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "https://example.com";
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser()));
        assertNull(DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser()));
    }

    @Test
    public void testParseInputStreamWithNullBaseUri() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = null;
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertNull(document.baseUri());
        assertEquals(html, document.html());
    }

    @Test
    public void testParseInputStreamWithXmlParser() throws IOException {
        String xml = "<xml version=\"1.0\" encoding=\"UTF-8\"><root><child>Hello World</child></root></xml>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "https://example.com";
        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.xmlParser());
        assertEquals(baseUri, document.baseUri());
        assertEquals(xml, document.xml().toString());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        assertEquals(html.length(), byteBuffer.limit());
        assertEquals(html.getBytes(StandardCharsets.UTF_8), byteBuffer.array());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, html.length() + 100);
        assertEquals(html.length(), byteBuffer.limit());
        assertEquals(html.getBytes(StandardCharsets.UTF_8), byteBuffer.array());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html; charset=gb2312";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals("UTF-8", DataUtil.validateCharset(charset));

        charset = "GB2312";
        assertEquals("GB2312", DataUtil.validateCharset(charset));

        charset = "ISO-8859-1";
        assertEquals("ISO-8859-1", DataUtil.validateCharset(charset));

        charset = "invalidCharset";
        assertNull(DataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Random random = new Random();
        assertEquals(32, boundary.length());
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(Character.isAlphabetic(boundary.charAt(i)) || Character.isDigit(boundary.charAt(i)) || boundary.charAt(i) == '-');
        }
    }

    @Test
    public void testDetectCharsetFromBom() {
        String bomUtf32 = "\u0000\u0000\u00FE\u00FF";
        ByteBuffer byteBufferUtf32 = ByteBuffer.wrap(bomUtf32.getBytes(StandardCharsets.UTF_8));
        BomCharset bomCharsetUtf32 = DataUtil.detectCharsetFromBom(byteBufferUtf32);
        assertEquals("UTF-32", bomCharsetUtf32.charset);
        assertTrue(bomCharsetUtf32.offset);

        String bomUtf16BE = "\u00FE\u00FF\u0000\u0000";
        ByteBuffer byteBufferUtf16BE = ByteBuffer.wrap(bomUtf16BE.getBytes(StandardCharsets.UTF_8));
        BomCharset bomCharsetUtf16BE = DataUtil.detectCharsetFromBom(byteBufferUtf16BE);
        assertEquals("UTF-16", bomCharsetUtf16BE.charset);
        assertFalse(bomCharsetUtf16BE.offset);

        String bomUtf8 = "\u00EF\u00BB\u00BF";
        ByteBuffer byteBufferUtf8 = ByteBuffer.wrap(bomUtf8.getBytes(StandardCharsets.UTF_8));
        BomCharset bomCharsetUtf8 = DataUtil.detectCharsetFromBom(byteBufferUtf8);
        assertEquals("UTF-8", bomCharsetUtf8.charset);
        assertTrue(bomCharsetUtf8.offset);

        String invalidBom = "\u0000\u0001\ufffe\ufffe";
        ByteBuffer byteBufferInvalidBom = ByteBuffer.wrap(invalidBom.getBytes(StandardCharsets.UTF_8));
        assertNull(DataUtil.detectCharsetFromBom(byteBufferInvalidBom));
    }
}

class BomCharset {
    public final String charset;
    public final boolean offset;

    public BomCharset(String charset, boolean offset) {
        this.charset = charset;
        this.offset = offset;
    }
}