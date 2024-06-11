package org.jsoup.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.HttpConnection;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParseStringWithBaseUri() {
        String html = "<html><head><base href=\"http://example.com\"></base></head><body><a href=\"/about.html\"></a></body></html>";
        Document doc = Jsoup.parse(html, "http://example.com");
        Assert.assertEquals("http://example.com/about.html", doc.select("a[href]").first().absUrl());
    }

    @Test
    public void testParseString() {
        String html = "<html><head><base href=\"/\"></base></head><body><a href=\"/about.html\"></a></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertEquals("", doc.select("a[href]").first().absUrl());
    }

    @Test
    public void testParseFileWithBaseUriAndCharset() throws IOException {
        String charset = "UTF-8";
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, charset, "http://example.com");
        Assert.assertEquals("http://example.com/images/logo.png", doc.select("img[src]").first().absUrl());
    }

    @Test
    public void testParseFileWithBaseUri() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "");
        Assert.assertEquals("", doc.select("img[src]").first().absUrl());
    }

    @Test
    public void testParseInputStreamWithBaseUriAndCharset() throws IOException {
        String charset = "UTF-8";
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = Jsoup.parse(inputStream, charset, "http://example.com");
        Assert.assertEquals("http://example.com/images/logo.png", doc.select("img[src]").first().absUrl());
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = Jsoup.parse(inputStream, "");
        Assert.assertEquals("", doc.select("img[src]").first().absUrl());
    }

    @Test
    public void testParseStringWithParser() {
        String html = "<html><body><script>document.write('<p>Hello World</p>')</script></body></html>";
        Parser parser = Parser.htmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        Assert.assertEquals("Hello World", doc.select("p").text());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Document doc = Jsoup.connect(url).get();
        Assert.assertEquals(url, doc.location());
    }

    @Test
    public void testConnectWithData() {
        String url = "http://example.com";
        Document doc = Jsoup.connect(url).data("name", "John Doe").get();
        Assert.assertEquals("John Doe", doc.select("input[name=name]").val());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<html><body><script>alert('XSS')</script><script>document.write('<p>Hello World</p>')</script></body></html>";
        String cleanedHtml = Jsoup.clean(bodyHtml, Whitelist.simpleText());
        Assert.assertEquals("Hello World", cleanedHtml);
    }

    @Test
    public void testCleanWithBaseUri() {
        String bodyHtml = "<html><body><img src=\"images/logo.png\"></body></html>";
        String charset = "UTF-8";
        String cleanedHtml = Jsoup.clean(bodyHtml, Whitelist.simpleText(), "http://example.com");
        Assert.assertEquals("<p></p>", cleanedHtml);
        Assert.assertFalse(new File("images/logo.png").exists());
    }

    @Test(expected = IOException.class)
    public void testParseFileWithInvalidCharset() throws IOException {
        String charset = "ISO-8859-1";
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, charset, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringWithInvalidBaseUri() {
        String html = "<html><body><a href=\"mailto:test@example.com\"></a></body></html>";
        Document doc = Jsoup.parse(html, "invalid:base-uri");
    }

    @Test(expected = HttpStatusException.class)
    public void testParseUrlWithInvalidStatusCode() throws IOException {
        String url = "http://example.com/404";
        Document doc = Jsoup.parse(url, 5000);
    }
}