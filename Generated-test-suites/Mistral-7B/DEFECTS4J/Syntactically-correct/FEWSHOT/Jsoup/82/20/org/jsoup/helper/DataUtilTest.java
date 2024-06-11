package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadDocument() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());

        String charset = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charset);

        charset = DataUtil.getCharsetFromContentType("text/html; charset=gb2312");
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertNotNull(boundary);
        assertEquals(boundary.length(), DataUtil.boundaryLength);
    }

    @Test
    public void testDetectCharsetFromBom() {
        String utf32Hex = "FF FE 00 00";
        String utf16LEHex = "FE FF 00 00";
        String utf8Hex = "EF BBBF";

        ByteBuffer utf32ByteBuffer = ByteBuffer.allocate(utf32Hex.length());
        utf32ByteBuffer.put(new byte[1][1][utf32Hex.getBytes(StandardCharsets.UTF_8)]);
        BomCharset utf32Charset = DataUtil.detectCharsetFromBom(utf32ByteBuffer);

        ByteBuffer utf16LEByteBuffer = ByteBuffer.allocate(utf16LEHex.length());
        utf16LEByteBuffer.put(new byte[1][1][utf16LEHex.getBytes(StandardCharsets.UTF_8)]);
        BomCharset utf16LECharset = DataUtil.detectCharsetFromBom(utf16LEByteBuffer);

        ByteBuffer utf8ByteBuffer = ByteBuffer.allocate(utf8Hex.length());
        utf8ByteBuffer.put(new byte[1][1][utf8Hex.getBytes(StandardCharsets.UTF_8)]);
        BomCharset utf8Charset = DataUtil.detectCharsetFromBom(utf8ByteBuffer);

        assertEquals("UTF-32", utf32Charset.charset);
        assertFalse(utf32Charset.offset);
        assertEquals("UTF-16", utf16LECharset.charset);
        assertFalse(utf16LECharset.offset);
        assertEquals("UTF-8", utf8Charset.charset);
        assertTrue(utf8Charset.offset);
    }

    private static void writeToByteBuffer(ByteBuffer byteBuffer, byte[] bytes) {
        byteBuffer.clear();
        byteBuffer.put(bytes);
        byteBuffer.flip();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024 * 32);

        assertEquals(html.length(), byteBuffer.limit());

        InputStream recoveredInputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteBuffer recoveredByteBuffer = DataUtil.readToByteBuffer(recoveredInputStream, 1024 * 32);

        assertEquals(html.length(), recoveredByteBuffer.limit());
        assertEquals(html, new String(byteBuffer.array()));
        assertEquals(html, new String(recoveredByteBuffer.array()));
    }
}

class TestParser extends Parser {
    @Override
    public Document parseInput(String source, String baseUri) {
        Document document = super.parseInput(source, baseUri);
        document.outputSettings().charset(StandardCharsets.UTF_8);
        return document;
    }
}