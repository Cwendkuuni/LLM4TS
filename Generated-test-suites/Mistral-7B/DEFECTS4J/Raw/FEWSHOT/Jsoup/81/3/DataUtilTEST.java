package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testParseInputStream() throws IOException {
        String xmlContent = "<html><head><meta charset=\"UTF-8\"/></head><body></body></html>";
        InputStream xmlInputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(xmlInputStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals(xmlContent, document.html());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String xmlContent = "<html><head><meta charset=\"invalidCharset\"/></head><body></body></html>";
        InputStream xmlInputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(xmlInputStream, "invalidCharset", null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals(xmlContent, document.html()); // Charset was ignored and defaulted to UTF-8
    }

    @Test
    public void testParseInputStreamWithEmptyContent() throws IOException {
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);

        Document document = DataUtil.parseInputStream(emptyInputStream, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals("<html></html>", document.html());
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.getProperty("line.separator") + "<html><body></body></html>";
        InputStream xmlInputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(xmlInputStream, null, null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals(xmlContent, document.html());
    }

    @Test
    public void testParseInputStreamWithBOM_UTF16() throws IOException {
        String xmlContent = "ＦＥＥＴＨＡＴＧＯＶＥＮＥＴＡＴＡ４ＳＡＢＩＴＥＴＡＡＴＡＡＡＢＯＮＤＣＥＴＡＴＡＡＡＢＯＭＢＡＡＡＢＯＭＢＡＡＡＢＯＭＢＡＡＡＢＯＭＢＡＡＡＢ"; // UTF-16 BOM
        InputStream xmlInputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_16LE));

        Document document = DataUtil.parseInputStream(xmlInputStream, null, null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals(xmlContent, document.html());
    }

    @Test
    public void testParseInputStreamWithBOM_UTF32() throws IOException {
        String xmlContent = "\ufeff<html><body></body></html>"; // UTF-32 BOM
        InputStream xmlInputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_32BE));

        Document document = DataUtil.parseInputStream(xmlInputStream, null, null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals(xmlContent, document.html());
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertEquals(boundary.length(), 32);
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(Character.isAlphabetic(boundary.charAt(i)) || boundary.charAt(i) == '-');
        }
    }
}

class ByteArrayInputStream extends InputStream {
    private final byte[] data;

    public ByteArrayInputStream(byte[] data) {
        this.data = data;
    }

    @Override
    public int read() throws IOException {
        return data.length > 0 ? data[0] : -1;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (data.length > off + len) {
            System.arraycopy(data, off, b, off, len);
            return len;
        } else {
            System.arraycopy(data, 0, b, off, data.length);
            return data.length;
        }
    }
}