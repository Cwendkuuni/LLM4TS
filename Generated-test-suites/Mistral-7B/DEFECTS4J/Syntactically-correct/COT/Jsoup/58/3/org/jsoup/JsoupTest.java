package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
    public void testParseWithBaseUri() {
        String html = "<html><head><base href=\"http://example.com\"/></head><body><a href=\"/about\">About</a></body></html>";
        Document doc = Jsoup.parse(html, "http://example.com");
        Assert.assertEquals("http://example.com/about", doc.select("a[href]").get(0).absUrl());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><head></head><body></body></html>";
        Parser parser = org.jsoup.parser.JsoupScanner.overrideDefaultParser();
        Document doc = Jsoup.parse(html, "", parser);
        Assert.assertEquals(parser, doc.parser());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head><base href=\"http://example.com\"/></head><body><a href=\"/about\">About</a></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertEquals("", doc.baseUri());
        Assert.assertEquals("http://example.com/about", doc.select("a[href]").get(0).absUrl());
    }

    @Test
    public void testParseFileWithCharset() throws IOException {
        String charsetName = "UTF-8";
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, charsetName, "");
        Assert.assertEquals(charsetName, doc.charset());
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "");
        Assert.assertEquals("", doc.charset());
    }

    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        String charsetName = "UTF-8";
        InputStream in = getClass().getResourceAsStream("/test.html");
        Document doc = Jsoup.parse(in, charsetName, "");
        Assert.assertEquals(charsetName, doc.charset());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream in = getClass().getResourceAsStream("/test.html");
        Document doc = Jsoup.parse(in, "");
        Assert.assertEquals("", doc.charset());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div id=\"content\">Hello World</div>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseWithUrl() throws IOException {
        String url = "http://example.com";
        Document doc = Jsoup.parse(url);
        Assert.assertEquals(url, doc.baseUri());
    }

    @Test(expected = IOException.class)
    public void testParseWithMalformedUrl() throws IOException {
        String url = "file:///test.html";
        Document doc = Jsoup.parse(url);
    }

    @Test(expected = HttpStatusException.class)
    public void testParseWithHttpError() throws IOException {
        String url = "http://example.com/error.html";
        Document doc = Jsoup.parse(url);
    }

    @Test(expected = UnsupportedMimeTypeException.class)
    public void testParseWithUnsupportedMimeType() throws IOException {
        String url = "http://example.com/image.png";
        Document doc = Jsoup.parse(url);
    }

    @Test(expected = SocketTimeoutException.class)
    public void testParseWithSocketTimeout() throws IOException {
        String url = "http://slow-server.com";
        Document doc = Jsoup.parse(url, 100);
    }

    @Test
    public void testClean() {
        String bodyHtml = "<div id=\"content\"><script>alert('Hello World');</script><p>Hello World</p></div>";
        String cleanHtml = Jsoup.clean(bodyHtml, Whitelist.simpleText());
        Document doc = Jsoup.parseBodyFragment(cleanHtml);
        Assert.assertEquals("Hello World", doc.body().text());
        Assert.assertFalse(doc.body().hasText());
        Assert.assertFalse(doc.body().hasChild(0));
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<div id=\"content\"><script>alert('Hello World');</script><p>Hello World</p></div>";
        Document.OutputSettings outputSettings = Document.OutputSettings.prettyPrintWhitespace(true);
        String cleanHtml = Jsoup.clean(bodyHtml, "", Whitelist.simpleText(), outputSettings);
        Document doc = Jsoup.parseBodyFragment(cleanHtml);
        Assert.assertEquals("Hello World", doc.body().text());
        Assert.assertEquals("", doc.head().html());
        Assert.assertEquals("", doc.body().html());
        Assert.assertEquals("\n  ", doc.body().outerHtml());
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<div id=\"content\"><script>alert('Hello World');</script><p>Hello World</p></div>";
        Assert.assertFalse(Jsoup.isValid(bodyHtml, Whitelist.simpleText()));
    }

    @Test
    public void testIsValidWithSimpleTag() {
        String bodyHtml = "<div id=\"content\"><p>Hello World</p></div>";
        Assert.assertTrue(Jsoup.isValid(bodyHtml, Whitelist.simpleTagNames()));
    }

    @Test
    public void testIsValidWithSimpleAttribute() {
        String bodyHtml = "<div id=\"content\"><p class=\"test\">Hello World</p></div>";
        Assert.assertTrue(Jsoup.isValid(bodyHtml, Whitelist.simpleTagsAndAttributes()));
    }

    @Test
    public void testIsValidWithComplexTag() {
        String bodyHtml = "<div id=\"content\"><p class=\"test\"><a href=\"http://example.com\">Link</a></p></div>";
        Assert.assertTrue(Jsoup.isValid(bodyHtml, Whitelist.allPermissions()));
    }
}