package org.jsoup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Document;
import org.jsoup.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {
    @Test
    public void testParseString() throws IOException {
        String html = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseStringWithBaseUri() throws IOException {
        String html = "<img src=\"image.png\" alt=\"Image\">";
        String baseUri = "https://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertEquals("image.png", doc.imageUrl());
    }

    @Test
    public void testParseStringWithParser() throws IOException {
        String html = "<html><head></head><body><p>Hello World</p></body></html>";
        Parser parser = new Parser();
        Document doc = Jsoup.parse(html, "", parser);
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseStringEmpty() throws IOException {
        Document doc = Jsoup.parse("");
        assertNull(doc);
    }

    @Test
    public void testConnect() throws IOException {
        Connection con = Jsoup.connect("https://example.com");
        Document doc = con.get();
        assertEquals("https://example.com", doc.location());
    }

    @Test
    public void testParseFile() throws IOException {
        File inputFile = new File("input.html");
        Document doc = Jsoup.parse(inputFile, "UTF-8");
        assertNotNull(doc);
    }

    @Test
    public void testParseFileWithBaseUri() throws IOException {
        File inputFile = new File("input.html");
        Document doc = Jsoup.parse(inputFile, "UTF-8", "https://example.com");
        assertEquals("https://example.com", doc.baseUri());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/input.html");
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        assertEquals("input.html", doc.inputEncoding());
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/input.html");
        Document doc = Jsoup.parse(inputStream, "UTF-8", "https://example.com");
        assertEquals("https://example.com", doc.baseUri());
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/input.html");
        Parser parser = new Parser();
        Document doc = Jsoup.parse(inputStream, "UTF-8", parser);
        assertEquals("UTF-8", doc.inputEncoding());
    }

    @Test
    public void testParseBodyFragment() throws IOException {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() throws IOException {
        String bodyHtml = "<img src=\"image.png\" alt=\"Image\">";
        String baseUri = "https://example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertEquals("image.png", doc.imageUrl());
    }

    @Test
    public void testParseUrl() throws IOException {
        URL url = new URL("https://example.com");
        Document doc = Jsoup.parse(url, 5000);
        assertEquals("https://example.com", doc.location());
    }

    @Test
    public void testClean() throws IOException {
        String bodyHtml = "<script src=\"script.js\">...</script><p>Hello World</p>";
        String baseUri = "https://example.com";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertEquals("Hello World", cleanHtml);
    }

    @Test
    public void testCleanWhiteList() throws IOException {
        String bodyHtml = "<script src=\"script.js\">...</script><p>Hello World</p>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals("Hello World", cleanHtml);
    }

    @Test
    public void testCleanBodyFragment() throws IOException {
        String bodyHtml = "<p>Hello World</p>";
        String cleanHtml = Jsoup.clean(bodyHtml, "").body().html();
        assertEquals("Hello World", cleanHtml);
    }

    @Test
    public void testCleanBodyFragmentWithBaseUri() throws IOException {
        String bodyHtml = "<img src=\"image.png\" alt=\"Image\">";
        String baseUri = "https://example.com";
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri).body().html();
        assertEquals("", cleanHtml);
    }

    @Test
    public void testCleanBodyFragmentWithWhiteList() throws IOException {
        String bodyHtml = "<img src=\"image.png\" alt=\"Image\">";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist).body().html();
        assertEquals("", cleanHtml);
    }

    @Test
    public void testCleanBodyFragmentWithWhiteListAndOutputSettings() throws IOException {
        String bodyHtml = "<img src=\"image.png\" alt=\"Image\">";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist, outputSettings).body().html();
        assertEquals("", cleanHtml);
    }

    @Test
    public void testIsValid() throws IOException {
        String bodyHtml = "<p><script>alert('Hello')</script>Hello World</p>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean valid = Jsoup.isValid(bodyHtml, whitelist);
        assertFalse(valid);
    }
}