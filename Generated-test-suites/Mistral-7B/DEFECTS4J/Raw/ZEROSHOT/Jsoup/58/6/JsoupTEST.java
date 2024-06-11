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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<img src=\"image.jpg\" />";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        Element img = doc.select("img").first();
        String imgSrc = img.attr("src");
        Assert.assertEquals("http://example.com/image.jpg", imgSrc);
    }

    @Test
    public void testParseWithParser() {
        String html = "<xml><tag>Test</tag></xml>";
        Parser parser = org.jsoup.parser.XmlParser.xmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        Element tag = doc.select("tag").first();
        Assert.assertNotNull(tag);
        Assert.assertEquals("Test", tag.text());
    }

    @Test
    public void testConnect() throws IOException {
        Document doc = Jsoup.connect("https://www.google.com").get();
        Assert.assertEquals("Google", doc.title());
    }

    @Test
    public void testParseFile() throws IOException {
        String html = DataUtil.load("test.html", "UTF-8");
        Document doc = Jsoup.parse(html);
        Assert.assertEquals("Test Page", doc.title());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        InputStream in = new ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = Jsoup.parse(in, "UTF-8");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Test</h1>";
        Document doc = Jsoup.parseBodyFragment(html);
        Assert.assertEquals("Test", doc.title());
    }

    @Test
    public void testParseUrl() throws IOException {
        String url = "https://www.google.com";
        Document doc = Jsoup.parse(url);
        Assert.assertEquals("Google", doc.title());
    }

    @Test
    public void testClean() {
        String html = "<script>alert('Test');</script><h1>Test</h1>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(html, "", whitelist);
        Assert.assertFalse(cleanHtml.contains("<script>"));
        Assert.assertEquals("Test", cleanHtml.split("<h1>")[1].split("</h1>")[0]);
    }

    @Test
    public void testIsValid() {
        String html = "<input type=\"text\" name=\"username\" value=\"test\">";
        Whitelist whitelist = Whitelist.relaxed();
        boolean isValid = Jsoup.isValid(html, whitelist);
        Assert.assertTrue(isValid);
    }
}