package org.jsoup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body><div>Hello</div></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals(DocumentType.HTML, doc.documentType());
        assertEquals("html", doc.tagName());
        assertEquals("div", doc.body().tagName());
        assertEquals("Hello", doc.body().text());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><head></head><body><div>Hello</div></body></html>";
        Document doc = Jsoup.parse(html, Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("html", doc.tagName());
        assertEquals("div", doc.body().tagName());
        assertEquals("Hello", doc.body().text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head></head><body><a href=\"/index.html\">Go to index</a></body></html>";
        Document doc = Jsoup.parse(html, "http://example.com");
        assertEquals("http://example.com/index.html", doc.baseUri());
    }

    @Test
    public void testParseUrl() {
        Document doc = Jsoup.connect("https://example.com").get();
        assertNotNull(doc);
        assertEquals("https://example.com", doc.baseUri());
    }

    @Test
    public void testParseFile() throws IOException {
        File inputFile = new File("input.html");
        Document doc = Jsoup.parse(inputFile, "UTF-8");
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/input.html");
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Hello</div>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertEquals("div", doc.tagName());
        assertEquals("Hello", doc.text());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<a href=\"/index.html\">Go to index</a>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "http://example.com");
        assertEquals("http://example.com/index.html", doc.baseUri());
    }

    @Test
    public void testConnect() {
        Connection connection = Jsoup.connect("http://example.com");
        assertNotNull(connection);
    }

    @Test
    public void testClean() {
        String html = "<html><head><script>alert('Hello World')</script></head><body><div id='test'><button onclick='alert(\"Click me\")'>Click me</button></div></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(html, whitelist);
        assertEquals("<html><head></head><body><div id='test'><button>Click me</button></div></body></html>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String html = "<html><head><script>alert('Hello World')</script></head><body><div id='test'><button onclick='alert(\"Click me\")'>Click me</button></div></body></html>";
        Whitelist whitelist = Whitelist.simpleTagNames("div", "button");
        boolean isValid = Jsoup.isValid(html, whitelist);
        assertFalse(isValid);
    }
}