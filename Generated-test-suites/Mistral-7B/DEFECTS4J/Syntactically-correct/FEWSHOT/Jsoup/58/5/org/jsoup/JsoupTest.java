package org.jsoup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

public class JsoupTest {

    @Test
    public void testParseHtml() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        Element head = doc.head();
        Element body = doc.body();
        Element h1 = body.selectFirst("h1");
        assertTrue(h1.text().equals("Hello World"));
    }

    @Test
    public void testParseHtmlWithBaseUri() {
        String html = "<a href=\"test.html\">Link</a>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        String link = doc.selectFirst("a").attr("abs:href");
        assertTrue(link.startsWith(baseUri));
    }

    @Test
    public void testParseHtmlWithWhitelist() {
        String html = "<html><body><div id=\"test\" class=\"foo bar\"><p>Test</p></div></body></html>";
        Whitelist whitelist = Whitelist.simpleTags("div", "p");
        Document doc = Jsoup.parse(html, "", whitelist);
        Element div = doc.selectFirst("#test");
        assertTrue(div.text().contains("Test"));
    }

    @Test
    public void testParseHtmlWithOutputSettings() {
        String html = "<html><body><div id=\"test\" class=\"foo bar\"><p>Test</p></div></body></html>";
        Whitelist whitelist = Whitelist.simpleTags("div", "p");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        Document doc = Jsoup.parse(html, "", whitelist, outputSettings);
        String result = doc.body().html();
        assertTrue(result.contains("<div id=\"test\" class=\"foo bar\"><p>Test</p></div>"));
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Document doc = Jsoup.connect(url).get();
        assertNotNull(doc);
    }

    @Test
    public void testParseFile() throws IOException {
        String inputPath = "input.html";
        Document doc = Jsoup.parse(new File(inputPath));
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputPath = "input.html";
        Document doc = Jsoup.parse(new FileInputStream(inputPath), "UTF-8");
        assertNotNull(doc);
    }

    @Test
    public void testCleanHtml() {
        String html = "<html><body><script src=\"malicious.js\"></script></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleTags();
        String safeHtml = Jsoup.clean(html, baseUri, whitelist);
        assertTrue(safeHtml.contains("<body>"));
        assertFalse(safeHtml.contains("script"));
    }

    @Test
    public void testIsValidHtml() {
        String html = "<html><body><script src=\"malicious.js\"></script></body></html>";
        Whitelist whitelist = Whitelist.simpleTags();
        boolean isValid = Jsoup.isValid(html, whitelist);
        assertFalse(isValid);
    }
}