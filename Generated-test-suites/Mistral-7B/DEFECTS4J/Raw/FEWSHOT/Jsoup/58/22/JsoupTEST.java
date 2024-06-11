package org.jsoup;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.jsoup.Jsoup.*;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body><p>Hello World</p></body></html>";
        Document doc = parse(html);
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<img src=\"image.jpg\" alt=\"Image\">";
        String baseUri = "http://example.com";
        Document doc = parse(html, baseUri);
        String imgSrc = doc.select("img[src]").attr("src");
        assertEquals("http://example.com/image.jpg", imgSrc);
    }

    @Test
    public void testParseWithParser() {
        String html = "<xml><tag>Test</tag></xml>";
        Parser parser = org.jsoup.parser.XmlParser.xmlParser();
        Document doc = parse(html, "", parser);
        assertEquals("Test", doc.select("tag").text());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Document doc = connect(url).get();
        assertEquals(200, doc.statusCode());
        assertEquals("<html><head></head><body><p>Hello World</p></body></html>", doc.html());
    }

    @Test
    public void testParseFromFile() {
        File file = new File("path/to/file.html");
        Document doc = parse(file, "UTF-8");
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseFromInputStream() {
        String html = "<html><head></head><body><p>Hello World</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = parse(inputStream, "UTF-8");
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = parseBodyFragment(bodyHtml, "");
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseUrl() {
        String urlString = "http://example.com";
        Document doc = parse(new URL(urlString), 5000);
        assertEquals(200, doc.statusCode());
        assertEquals("<html><head></head><body><p>Hello World</p></body></html>", doc.html());
    }

    @Test
    public void testClean() {
        String html = "<html><head></head><body><script>alert('XSS')</script><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = clean(html, baseUri, whitelist);
        assertFalse(cleanHtml.contains("alert"));
        assertEquals("Hello World", cleanHtml);
    }

    @Test
    public void testCleanWithWhitelist() {
        String html = "<html><head></head><body><div class=\"foo\">Hello World</div></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleText().addAttribute("div", "class", "foo");
        String cleanHtml = clean(html, baseUri, whitelist);
        assertEquals("Hello World", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String html = "<html><head></head><body><div class=\"foo\">Hello World<script>alert('XSS')</script></div></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = clean(html, baseUri, whitelist, outputSettings);
        assertTrue(cleanHtml.contains("\n"));
        assertEquals("Hello World", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String html = "<html><head></head><body><p>Hello World</p><script>alert('XSS')</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        assertTrue(isValid(html, whitelist));
    }
}