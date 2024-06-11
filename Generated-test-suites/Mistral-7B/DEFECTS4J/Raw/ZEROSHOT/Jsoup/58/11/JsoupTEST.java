package org.jsoup.test;

import org.junit.Test;
import org.junit.Assert.*;
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
    public void testParse() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertEquals("Hello, World!", doc.title());
        Assert.assertEquals(1, doc.select("h1").size());
        Element h1 = doc.selectFirst("h1");
        Assert.assertEquals("Hello, World!", h1.text());

        html = "<html><body><h1 id=\"title\">Hello, World!</h1></body></html>";
        doc = Jsoup.parse(html);
        Assert.assertEquals("Hello, World!", doc.select("#title").text());
    }

    @Test
    public void testParseWithBaseUri() {
        String baseUri = "http://example.com";
        String html = "<html><head><base href=\"" + baseUri + "\"></base></head><body><a href=\"index.html\">Go back</a></body></html>";
        Document doc = Jsoup.parse(html, baseUri);
        Assert.assertEquals("Go back", doc.select("a").text());
        Assert.assertEquals(baseUri + "/index.html", doc.select("a").attr("href"));
    }

    @Test
    public void testParseWithParser() {
        Parser parser = new Parser.MalformedEncodingParser();
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        Document doc = Jsoup.parse(html, "", parser);
        Assert.assertEquals("Hello, World!", doc.title());
        Assert.assertEquals(1, doc.select("h1").size());
    }

    @Test
    public void testConnect() throws IOException {
        String url = "http://example.com";
        Document doc = Jsoup.connect(url).get();
        Assert.assertEquals(200, doc.statusCode());
        Assert.assertEquals("Example Domain", doc.title());
    }

    @Test
    public void testParseFile() throws IOException {
        String charsetName = "UTF-8";
        String filePath = "test.html";
        Document doc = Jsoup.parse(new File(filePath), charsetName);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals(1, doc.select("h1").size());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        String filePath = "test.html";
        InputStream in = new FileInputStream(new File(filePath));
        Document doc = Jsoup.parse(in, charsetName);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals(1, doc.select("h1").size());
        in.close();
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertEquals(1, doc.select("h1").size());
    }

    @Test
    public void testParseUrl() throws IOException {
        String urlString = "http://example.com";
        Document doc = Jsoup.parse(new URL(urlString), 5000);
        Assert.assertEquals(200, doc.statusCode());
        Assert.assertEquals("Example Domain", doc.title());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<html><body><script>alert('Hello, World!');</script><h1 id=\"title\">Hello, World!</h1></body></html>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);
        Assert.assertFalse(cleanHtml.contains("script"));
        Assert.assertEquals("Hello, World!", cleanHtml.substring(cleanHtml.indexOf("title"), cleanHtml.indexOf("title") + 8));
    }

    @Test
    public void testCleanWithBaseUri() {
        String bodyHtml = "<html><body><img src=\"image.png\"></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertEquals(baseUri + "/image.png", cleanHtml.substring(cleanHtml.indexOf("src=\""), cleanHtml.indexOf("src=\""
                ) + cleanHtml.indexOf("image.png") - cleanHtml.indexOf("src=\"")));
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<html><body><script>alert('Hello, World!');</script><h1 id=\"title\">Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        Assert.assertTrue(cleanHtml.contains("<pre>"));
        Assert.assertTrue(cleanHtml.contains("</pre>"));
        Assert.assertFalse(cleanHtml.contains("script"));
        Assert.assertEquals("Hello, World!", cleanHtml.substring(cleanHtml.indexOf("title"), cleanHtml.indexOf("title") + 8));
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<html><body><script>alert('Hello, World!');</script><h1 id=\"title\">Hello, World!</h1></body></html>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValidWithClean() {
        String bodyHtml = "<html><body><script>alert('Hello, World!');</script><h1 id=\"title\">Hello, World!</h1></body></html>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);
        boolean isValid = Jsoup.isValid(cleanHtml, whitelist);
        Assert.assertTrue(isValid);
    }
}