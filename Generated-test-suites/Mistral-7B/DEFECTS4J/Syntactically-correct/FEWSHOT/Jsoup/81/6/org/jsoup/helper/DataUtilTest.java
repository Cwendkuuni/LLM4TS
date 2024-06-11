package org.jsoup.helper;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("GBK", DataUtil.getCharsetFromContentType("text/html; charset=GBK"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertNull(DataUtil.getCharsetFromContentType("text/html"));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, DataUtil.Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>", doc.html());
    }

    @Test
    public void testParseInputStreamWithInvalidCharsetName() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        try {
            DataUtil.parseInputStream(inputStream, "invalidCharset", null, DataUtil.Parser.htmlParser());
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithNullEncoding() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>", doc.html());
    }

    @Test
    public void testParseInputStreamWithNoEncoding() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>", doc.html());

        inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_16LE));

        try {
            Document doc2 = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.htmlParser());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithBomUtf8() throws IOException {
        String html = "\uFEFF<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>", doc.html());
    }

    @Test
    public void testParseInputStreamWithBomUtf16() throws IOException {
        String html = "\uFEFF<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_16LE));

        try {
            Document doc = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.htmlParser());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithBomUtf32() throws IOException {
        String html = "\uFEFF<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_32BE));

        try {
            Document doc = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.htmlParser());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithXmlDeclaration() throws IOException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>", doc.html());
    }

    @Test
    public void testParseInputStreamWithXmlDeclarationAndInvalidCharset() throws IOException {
        String xml = "<?xml version=\"1.0\" encoding=\"invalidCharset\"?><html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));

        try {
            Document doc = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.htmlParser());
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithXmlDeclarationAndNoCharset() throws IOException {
        String xml = "<?xml version=\"1.0\"?><html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>", doc.html());
    }

    @Test
    public void testParseInputStreamWithXmlDeclarationAndValidCharset() throws IOException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.htmlParser());
        assertEquals("<html><head><meta charset=\"UTF-8\"></head><body><p>Hello World</p></body></html>", doc.html());
    }
}