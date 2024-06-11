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

public class JsoupTest {

    @Test
    public void testParseValidHtml() throws IOException {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertEquals("Test Page", doc.title());
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseHtmlWithBaseUri() throws IOException {
        String html = "<img src=\"image.png\" />";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        Element img = doc.selectFirst("img");
        Assert.assertEquals("http://example.com/image.png", img.attr("abs:src"));
    }

    @Test
    public void testParseHtmlWithAlternateParser() throws IOException {
        String xml = "<root><tag>Test</tag></root>";
        Parser parser = Jsoup.parser().xmlParser();
        Document doc = Jsoup.parse(xml, "", parser);
        Assert.assertEquals("Test", doc.select("root > tag").text());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html><body><script>alert('Hello World')</script></body></html>";
        try {
            Jsoup.parse(html);
            Assert.fail("Expected an IOException for the invalid HTML.");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testConnectAndGet() throws IOException {
        String url = "http://example.com";
        Document doc = Jsoup.connect(url).get();
        Assert.assertEquals("HTML document", doc.outerHtml());
    }

    @Test
    public void testParseFile() throws IOException {
        String fileName = "test.html";
        File file = new File(fileName);
        Document doc = Jsoup.parse(file);
        Assert.assertEquals("Test Page", doc.title());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String fileName = "test.html";
        File file = new File(fileName);
        InputStream inputStream = new FileInputStream(file);
        Document doc = Jsoup.parse(inputStream, "UTF-8", "http://example.com");
        inputStream.close();
        Assert.assertEquals("Test Page", doc.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertEquals("Test", doc.body().text());
    }

    @Test
    public void testCleanValidHtml() {
        String bodyHtml = "<p>Test <a href=\"http://example.com\">Link</a></p>";
        Whitelist whitelist = new Whitelist(new TagNameFilter.AllowAllElements());
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        Assert.assertEquals("<p>Test <a href=\"http://example.com\">Link</a></p>", cleanHtml);
    }

    @Test
    public void testCleanInvalidHtml() {
        String bodyHtml = "<script>alert('Hello World')</script>";
        Whitelist whitelist = new Whitelist(new TagNameFilter.AllowAllElements());
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        Assert.assertEquals("", cleanHtml);
    }

    @Test
    public void testCleanHtmlWithBaseUri() {
        String bodyHtml = "<img src=\"image.png\" />";
        String baseUri = "http://example.com";
        Whitelist whitelist = new Whitelist(new TagNameFilter.AllowAllElements());
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist, baseUri);
        Assert.assertEquals("<img />", cleanHtml);
    }

    @Test
    public void testCleanHtmlWithWhitelist() {
        String bodyHtml = "<h1 id=\"test\">Test</h1>";
        Whitelist whitelist = new Whitelist(new TagNameFilter.AllowElements("h1"));
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        Assert.assertEquals("<h1 id=\"test\">Test</h1>", cleanHtml);
    }

    @Test
    public void testCleanHtmlWithOutputSettings() {
        String bodyHtml = "<p><script>alert('Hello World')</script></p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = new Whitelist();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        Assert.assertEquals("<p></p>", cleanHtml);
    }

    @Test
    public void testCleanHtmlWithInvalidInput() {
        String bodyHtml = "";
        String baseUri = "http://example.com";
        Whitelist whitelist = new Whitelist();
        try {
            String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
            Assert.fail("Expected an IllegalArgumentException for the empty input.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsValidHtml() {
        String bodyHtml = "<p><script>alert('Hello World')</script></p>";
        Whitelist whitelist = new Whitelist();
        Assert.assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }

    @Test
    public void testIsValidHtmlWithAllowedScripts() {
        String bodyHtml = "<p><script>alert('Hello World')</script></p>";
        Whitelist whitelist = new Whitelist().addTags("script");
        Assert.assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }
}