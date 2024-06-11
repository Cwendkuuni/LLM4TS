package org.jsoup.tests;

import org.junit.Assert;
import org.junit.Test;
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

/**
 * JUnit test cases for Jsoup class
 */
public class JsoupTest {

    /**
     * Test parsing HTML from a string
     */
    @Test
    public void testParseHtmlFromString() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    /**
     * Test parsing HTML from a string with a custom parser
     */
    @Test
    public void testParseHtmlFromStringWithCustomParser() {
        String html = "<xml><tag>Test</tag></xml>";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        Element tag = doc.select("tag").first();
        Assert.assertEquals("Test", tag.text());
    }

    /**
     * Test parsing HTML from a string without a base URI
     */
    @Test
    public void testParseHtmlFromStringWithoutBaseUri() {
        String html = "<img src=\"image.png\" alt=\"Image\">";
        Document doc = Jsoup.parse(html);
        Element img = doc.select("img").first();
        Assert.assertEquals("image.png", img.attr("src"));
    }

    /**
     * Test connecting to a URL and parsing the HTML
     */
    @Test
    public void testConnectAndParse() throws IOException {
        Connection connection = Jsoup.connect("https://www.google.com");
        Document doc = connection.get();
        Assert.assertEquals("Google", doc.title());
        Assert.assertTrue(doc.body().text().contains("Search"));
    }

    /**
     * Test parsing a file
     */
    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8");
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    /**
     * Test parsing a file with a base URI
     */
    @Test
    public void testParseFileWithBaseUri() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8", "http://example.com");
        Element image = doc.select("img[src]").first();
        Assert.assertEquals("http://example.com/image.png", image.attr("src"));
    }

    /**
     * Test parsing an input stream
     */
    @Test
    public void testParseInputStream() throws IOException {
        URL url = new URL("https://www.google.com");
        InputStream inputStream = url.openStream();
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        Assert.assertEquals("Google", doc.title());
        Assert.assertTrue(doc.body().text().contains("Search"));
    }

    /**
     * Test parsing a body fragment
     */
    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Hello World</h1>";
        Document doc = Jsoup.parseBodyFragment(html);
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    /**
     * Test parsing a URL and getting safe HTML using a whitelist
     */
    @Test
    public void testParseUrlAndClean() throws IOException {
        String url = "https://www.example.com/malicious.html";
        Whitelist whitelist = Whitelist.simpleTextOnly();
        String safeHtml = Jsoup.clean(Jsoup.connect(url).get().body().html(), url, whitelist);
        Assert.assertEquals("", safeHtml);
    }
}