package org.jsoup.test;

import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @org.junit.Test
    public void testParseHtml() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertEquals("Hello World", doc.title());
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    @org.junit.Test
    public void testParseHtmlWithBaseUri() throws IOException {
        String html = "<html><head><title>Test</title><base href=\"/test\"></head><body><a href=\"about.html\">About</a></body></html>";
        Document doc = Jsoup.parse(html, "http://example.com/test");
        Assert.assertEquals("About", doc.select("a[href=\"about.html\"]").text());
        Assert.assertEquals("http://example.com/test/about.html", doc.select("a[href=\"about.html\"]").attr("abs:href"));
    }

    @org.junit.Test
    public void testParseHtmlWithCustomParser() throws IOException {
        String html = "<xml><root><child>Hello</child></root></xml>";
        Parser parser = Jsoup.parser().xmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        Assert.assertEquals("Hello", doc.select("root > child").text());
    }

    @org.junit.Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, null);
        Assert.assertEquals("Test", doc.title());
    }

    @org.junit.Test
    public void testParseFileWithBaseUri() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, null, "http://example.com");
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("http://example.com/test.html", doc.location().toExternalForm());
    }

    @org.junit.Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        InputStream is = new java.io.ByteArrayInputStream(html.getBytes());
        Document doc = Jsoup.parse(is, null);
        Assert.assertEquals("Hello World", doc.title());
        Assert.assertEquals("Hello World", doc.select("h1").text());

        is.close();
    }

    @org.junit.Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        String html = "<html><head><title>Test</title><base href=\"/test\"></head><body><a href=\"about.html\">About</a></body></html>";
        InputStream is = new java.io.ByteArrayInputStream(html.getBytes());
        Document doc = Jsoup.parse(is, null, "http://example.com/test");
        Assert.assertEquals("About", doc.select("a[href=\"about.html\"]").text());
        Assert.assertEquals("http://example.com/test/about.html", doc.select("a[href=\"about.html\"]").attr("abs:href"));

        is.close();
    }

    @org.junit.Test
    public void testConnect() throws IOException {
        Connection connection = Jsoup.connect("http://example.com");
        Document doc = connection.get();
        Assert.assertEquals("Example Domain", doc.title());
    }

    @org.junit.Test
    public void testParseBodyFragment() throws IOException {
        String html = "<h1>Hello World</h1>";
        Document doc = Jsoup.parseBodyFragment(html);
        Assert.assertEquals("Hello World", doc.title());
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    @org.junit.Test
    public void testParseBodyFragmentWithBaseUri() throws IOException {
        String html = "<html><head><title>Test</title><base href=\"/test\"></head><body><a href=\"about.html\">About</a></body></html>";
        Document doc = Jsoup.parseBodyFragment(html, "http://example.com/test");
        Assert.assertEquals("About", doc.select("a[href=\"about.html\"]").text());
        Assert.assertEquals("http://example.com/test/about.html", doc.select("a[href=\"about.html\"]").attr("abs:href"));
    }

    @org.junit.Test
    public void testParseUrl() throws IOException {
        Document doc = Jsoup.parse(new URL("http://example.com"), 5000);
        Assert.assertEquals("Example Domain", doc.title());
    }

    @org.junit.Test
    public void testCleanHtml() throws IOException {
        String html = "<html><head><title>Test</title><script src=\"xss.js\"></script></head><body><script>alert('XSS')</script></body></html>";
        String cleanHtml = Jsoup.clean(html, "").toString();
        Assert.assertEquals("", cleanHtml.substring(cleanHtml.indexOf("<!--") + 4, cleanHtml.length()));
        Assert.assertFalse(cleanHtml.contains("<script>"));

        String cleanHtmlWithWhitelist = Jsoup.clean(html, Whitelist.simpleText());
        Assert.assertTrue(cleanHtmlWithWhitelist.isEmpty());
    }

    @org.junit.Test
    public void testCleanHtmlWithBaseUri() throws IOException {
        String html = "<html><head><title>Test</title><script src=\"xss.js\"></script><base href=\"/test\"></head><body><script>alert('XSS')</script></body></html>";
        String cleanHtml = Jsoup.clean(html, "").toString();
        Assert.assertEquals("", cleanHtml.substring(cleanHtml.indexOf("<!--") + 4, cleanHtml.length()));
        Assert.assertFalse(cleanHtml.contains("<script>"));

        String cleanHtmlWithWhitelist = Jsoup.clean(html, Whitelist.simpleText());
        Assert.assertTrue(cleanHtmlWithWhitelist.isEmpty());
    }

    @org.junit.Test
    public void testCleanHtmlWithOutputSettings() throws IOException {
        String html = "<html><head><title>Test</title><script src=\"xss.js\"></script><base href=\"/test\"></head><body><script>alert('XSS')</script><script>document.write('Hello')</script></body></html>";
        String cleanHtml = Jsoup.clean(html, "").toString();
        Assert.assertEquals("", cleanHtml.substring(cleanHtml.indexOf("<!--") + 4, cleanHtml.length()));
        Assert.assertFalse(cleanHtml.contains("<script>"));

        Document.OutputSettings outputSettings = Document.OutputSettings.xml();
        String cleanHtmlWithOutputSettings = Jsoup.clean(html, "").toString(outputSettings);
        Assert.assertEquals("Hello", cleanHtmlWithOutputSettings);

        String cleanHtmlWithWhitelistAndOutputSettings = Jsoup.clean(html, Whitelist.simpleText(), outputSettings);
        Assert.assertEquals("", cleanHtmlWithWhitelistAndOutputSettings);
    }

    @org.junit.Test
    public void testIsValidHtml() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        boolean valid = Jsoup.isValid(html, Whitelist.simpleText());
        Assert.assertTrue(valid);

        String invalidHtml = "<html><head><title>Test</title><script src=\"xss.js\"></script></head><body><script>alert('XSS')</script></body></html>";
        boolean invalid = Jsoup.isValid(invalidHtml, Whitelist.simpleText());
        Assert.assertFalse(invalid);
    }
}