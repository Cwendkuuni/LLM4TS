package org.jsoup.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

/**
 * JsoupTest class
 */
public class JsoupTest {

    /**
     * Test parsing a valid HTML string.
     */
    @Test
    public void testParseValidHtmlString() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Welcome to JSoup</h1></body></html>";
        Document document = Jsoup.parse(html);

        Assert.assertNotNull(document);
        Assert.assertEquals("Test Page", document.title());
        Assert.assertEquals("Welcome to JSoup", document.select("h1").text());
    }

    /**
     * Test parsing a valid HTML file.
     */
    @Test
    public void testParseValidHtmlFile() throws IOException {
        String filePath = "path/to/valid.html";
        Document document = Jsoup.parse(new File(filePath));

        Assert.assertNotNull(document);
        Assert.assertNotEmpty(document.select("p"));
    }

    /**
     * Test parsing a valid HTML URL.
     */
    @Test
    public void testParseValidHtmlUrl() throws IOException {
        String urlString = "http://example.com/valid.html";
        Document document = Jsoup.connect(urlString).get();

        Assert.assertNotNull(document);
        Assert.assertNotEmpty(document.select("h1"));
    }

    /**
     * Test parsing an invalid HTML string.
     */
    @Test(expected = org.jsoup.parser.Parser.MalformedHtmlException.class)
    public void testParseInvalidHtmlString() {
        String html = "<html><body<h1>Welcome to JSoup</h1>";
        Document document = Jsoup.parse(html);
    }

    /**
     * Test parsing an invalid HTML file.
     */
    @Test(expected = org.jsoup.parser.Parser.MalformedHtmlException.class)
    public void testParseInvalidHtmlFile() throws IOException {
        String filePath = "path/to/invalid.html";
        Document document = Jsoup.parse(new File(filePath));
    }

    /**
     * Test parsing an invalid HTML URL.
     */
    @Test(expected = org.jsoup.parser.Parser.MalformedHtmlException.class)
    public void testParseInvalidHtmlUrl() throws IOException {
        String urlString = "http://example.com/invalid.html";
        Document document = Jsoup.connect(urlString).get();
    }

    /**
     * Test parsing HTML with a specified parser.
     */
    @Test
    public void testParseWithSpecifiedParser() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Welcome to JSoup</h1></body></html>";
        Document document = Jsoup.parse(html, Parser.xmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("Test Page", document.title());
        Assert.assertEquals("Welcome to JSoup", document.select("h1").text());
    }

    /**
     * Test cleaning HTML using a whitelist.
     */
    @Test
    public void testCleanHtmlWithWhitelist() {
        String bodyHtml = "<html><body><script>alert('XSS');</script><a href='http://example.com'>Link</a></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleText();

        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);

        Assert.assertNotNull(cleanedHtml);
        Assert.assertEquals("Link", cleanedHtml.split("<a href=")[1].split(">")[0]);
    }

    /**
     * Test validating HTML using a whitelist.
     */
    @Test
    public void testValidateHtmlWithWhitelist() {
        String bodyHtml = "<html><body><script>alert('XSS');</script><a href='http://example.com'>Link</a></body></html>";
        Whitelist whitelist = Whitelist.simpleText();

        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);

        Assert.assertFalse(isValid);
    }
}