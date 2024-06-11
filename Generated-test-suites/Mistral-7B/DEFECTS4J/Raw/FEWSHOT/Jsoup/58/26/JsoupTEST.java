package org.jsoup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().text());
        assertEquals("Test", doc.title());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head></head><body><img src=\"image.jpg\"></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertEquals("http://example.com/image.jpg", doc.imageUrl());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><head></head><body><img src=\"image.jpg\"></body></html>";
        Parser parser = new Parser();
        Document doc = Jsoup.parse(html, "", parser);
        assertEquals("image.jpg", doc.select("img").attr("src"));
    }

    @Test
    public void testParseEmpty() {
        Document doc = Jsoup.parse("", "");
        assertEquals("", doc.body().text());
    }

    @Test
    public void testConnect() {
        Connection con = Jsoup.connect("https://www.google.com");
        Document doc = con.get();
        assertEquals("Google", doc.title());
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8");
        assertEquals("Test", doc.title());
    }

    @Test
    public void testParseFileWithBaseUri() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8", file.getAbsolutePath());
        assertEquals("http://example.com/image.jpg", doc.imageUrl());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        String html = "<html><head></head><body><img src=\"image.jpg\"></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(inputStream, "UTF-8", baseUri);
        assertEquals("http://example.com/image.jpg", doc.imageUrl());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "");
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<p><img src=\"image.jpg\"></p>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("http://example.com/image.jpg", doc.imageUrl());
    }

    @Test
    public void testClean() {
        String html = "<script>alert('XSS')</script><p>Hello World</p>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(html, "", whitelist);
        assertTrue(cleanHtml.contains("Hello World"));
        assertNull(cleanHtml.matches(".*<script.*>.*"));
    }

    @Test
    public void testIsValid() {
        String html = "<script>alert('XSS')</script><p>Hello World</p>";
        Whitelist whitelist = Whitelist.relaxed();
        boolean isValid = Jsoup.isValid(html, whitelist);
        assertFalse(isValid);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String html = "<script>alert('XSS')</script><p>Hello World</p>";
        Whitelist whitelist = Whitelist.relaxed();
        String baseUri = "http://example.com";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = Jsoup.clean(html, baseUri, whitelist, outputSettings);
        assertTrue(cleanHtml.contains("Hello World"));
        assertNull(cleanHtml.matches(".*<script.*>.*"));
        assertTrue(cleanHtml.matches(".*<p>.*Hello World.*</p>.*"));
    }
}