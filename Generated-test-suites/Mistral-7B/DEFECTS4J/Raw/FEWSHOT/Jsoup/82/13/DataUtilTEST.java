package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Test class for DataUtil static methods.
 */
public class DataUtilTest {

    @Test
    public void testLoadDocument() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(input, "UTF-8", null);
        assertEquals("<html><head><meta charset=\"UTF-8\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>Document</title></head><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testLoadDocument_InvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"invalid-charset\"></head><body><h1>Hello World</h1></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(input, "invalid-charset", null);
        assertEquals("<html><head><meta charset=\"UTF-8\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>Document</title></head><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(input, "UTF-8", null, Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>Document</title></head><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testParseInputStream_NoCharset() throws IOException {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testParseInputStream_InvalidCharset_DetectedFromHtml() throws IOException {
        String html = "<html><head><meta charset=\"GBK\"></head><body><h1>Hello World</h1></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"GBK\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\"><title>Document</title></head><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testParseInputStream_InvalidCharset_DetectedFromMeta() throws IOException {
        String html = "<html><head><meta charset=\"ISO-8859-1\"></head><body><h1>Hello World</h1></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(input, null, null, Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"ISO-8859-1\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"><title>Document</title></head><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testParseInputStream_InvalidCharset_NotDetected() throws IOException {
        String html = "<html><head><meta charset=\"ISO-8859-1\"></head><body><h1>Hello World</h1></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(input, "UTF-8", null, Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>Document</title></head><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Hello World</h1></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(input, 0);
        assertEquals(html.length(), byteBuffer.limit());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(input, outputStream);
        InputStream testInput = new ByteArrayInputStream(outputStream.toByteArray());
        Document doc = DataUtil.parseInputStream(testInput, "UTF-8", null, Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>Document</title></head><body><h1>Hello World</h1></body></html>", doc.html());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertEquals("UTF-8", validatedCharset);

        charset = "";
        validatedCharset = DataUtil.validateCharset(charset);
        assertEquals(null, validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(32, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        InputStream input = new ByteArrayInputStream(new byte[]{0x00, 0x00, 0xFE, 0xFF});
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(input, 4));
        assertEquals("UTF-32", bomCharset.charset);
        assertFalse(bomCharset.offset);

        input = new ByteArrayInputStream(new byte[]{0xFF, 0xFE, 0x00, 0x00});
        bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(input, 4));
        assertEquals("UTF-16", bomCharset.charset);
        assertFalse(bomCharset.offset);

        input = new ByteArrayInputStream(new byte[]{0xEF, 0xBB, 0xBF});
        bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(input, 3));
        assertEquals("UTF-8", bomCharset.charset);
        assertTrue(bomCharset.offset);

        input = new ByteArrayInputStream(new byte[]{-1, -1, -1, -1});
        bomCharset = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(input, 4));
        assertEquals(null, bomCharset);
    }
}

class BomCharset {
    String charset;
    boolean offset;

    BomCharset(String charset, boolean offset) {
        this.charset = charset;
        this.offset = offset;
    }
}