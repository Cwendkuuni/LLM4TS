package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadDocument() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadDocumentWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"invalidCharset\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "invalidCharset", null);
        assertNull(document);
    }

    @Test
    public void testLoadDocumentWithNoCharset() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
    }

    @Test
    public void testLoadDocumentWithXmlDeclaration() throws IOException {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset());
        Element xmlDeclaration = document.childNode(0);
        assertNotNull(xmlDeclaration);
        assertEquals("UTF-8", xmlDeclaration.attr("encoding"));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"invalidCharset\"></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "invalidCharset", null, Parser.htmlParser());
        assertNull(document);
    }

    @Test
    public void testParseInputStreamWithNoCharset() throws IOException {
        String html = "<html><head></head><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithXmlDeclaration() throws IOException {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><body></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
        assertNotNull(document);
        Element xmlDeclaration = document.childNode(0);
        assertNotNull(xmlDeclaration);
        assertEquals("UTF-8", xmlDeclaration.attr("encoding"));
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Hello, World!";
        String destination = "Output.txt";
        InputStream inputStream = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new FileOutputStream(destination);
        DataUtil.crossStreams(inputStream, outputStream);
        String content = new String(new ByteArrayInputStream(new File(destination).readAllBytes()).readAllBytes(), StandardCharsets.UTF_8);
        assertEquals(source, content);
    }
}