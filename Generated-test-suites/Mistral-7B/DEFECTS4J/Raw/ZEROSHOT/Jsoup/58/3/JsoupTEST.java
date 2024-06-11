package org.jsoup.test;

import static org.junit.Assert.*;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

/**
 * JsoupTest class to test all methods in Jsoup class.
 */
public class JsoupTest {

    @Test
    public void testParse() throws IOException {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html);
        assertNotNull(document);
        assertEquals("Hello World", document.select("h1").text());
    }

    @Test
    public void testParseWithBaseUri() throws IOException {
        String html = "<img src=\"image.jpg\" alt=\"Image\">";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        String imageUrl = document.select("img[src]").attr("src");
        assertEquals("http://example.com/image.jpg", imageUrl);
    }

    @Test
    public void testParseWithParser() throws IOException {
        String html = "<xml><data>Test</data></xml>";
        Parser parser = Jsoup.parser().xmlParser();
        Document document = Jsoup.parse(html, "", parser);
        assertEquals("Test", document.select("data").text());
    }

    @Test
    public void testConnectAndGet() throws IOException {
        String url = "http://example.com";
        Document document = Jsoup.connect(url).get();
        assertNotNull(document);
        assertEquals("Example Domain", document.title());
    }

    @Test
    public void testParseFile() throws IOException {
        String filePath = "test.html";
        Document document = Jsoup.parse(new File(filePath), "UTF-8");
        assertNotNull(document);
        assertEquals("Test File", document.title());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String filePath = "test.html";
        Document document = Jsoup.parse(Jsoup.connect(filePath).inputStream(), "UTF-8");
        assertNotNull(document);
        assertEquals("Test File", document.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        assertEquals("Test", document.select("h1").text());
    }

    @Test
    public void testCleanSafeHtml() {
        String bodyHtml = "<script>alert('Test');</script><h1>Test</h1>";
        String baseUri = "";
        String whitelistName = "standard";
        String safeHtml = Jsoup.clean(bodyHtml, baseUri, new Whitelist(whitelistName));
        assertEquals("Test", safeHtml);
    }

    @Test
    public void testCleanSafeHtmlWithOutputSettings() {
        String bodyHtml = "<script>alert('Test');</script><h1>Test</h1>";
        String baseUri = "";
        String whitelistName = "standard";
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
        String safeHtml = Jsoup.clean(bodyHtml, baseUri, new Whitelist(whitelistName), outputSettings);
        assertEquals("<h1>Test</h1>", safeHtml);
    }

    @Test
    public void testIsValidHtml() {
        String bodyHtml = "<script>alert('Test');</script><h1>Test</h1>";
        String whitelistName = "standard";
        boolean isValid = Jsoup.isValid(bodyHtml, new Whitelist(whitelistName));
        assertFalse(isValid);
    }

    @Test
    public void testParseUrl() throws IOException {
        String url = "http://example.com";
        Document document = Jsoup.parse(new URL(url), 5000);
        assertNotNull(document);
        assertEquals("Example Domain", document.title());
    }
}