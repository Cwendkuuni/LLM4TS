package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, "UTF-8", "http://example.com");
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><body><h1>Hello world!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String xml = "<root><element>Value</element></root>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com", Parser.xmlParser());
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

}