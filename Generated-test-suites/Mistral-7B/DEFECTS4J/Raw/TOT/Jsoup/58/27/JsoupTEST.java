package org.jsoup.test;

import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @org.junit.Test
    public void testParseValidHtml() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    @org.junit.Test
    public void testParseHtmlWithBaseUri() throws IOException {
        String html = "<img src=\"images/image.png\" alt=\"Image\">";
        String baseUri = "http://www.example.com";
        Document doc = Jsoup.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Element img = doc.selectFirst("img");
        Assert.assertEquals("http://www.example.com/images/image.png", img.absUrl("src"));
    }

    @org.junit.Test
    public void testParseHtmlWithCustomParser() throws IOException {
        String html = "<xml><name>John Doe</name></xml>";
        Parser parser = org.jsoup.parser.XmlParser.xmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        Assert.assertNotNull(doc);
        Assert.assertEquals("John Doe", doc.select("name").text());
    }

    @org.junit.Test
    public void testParseInvalidHtml() throws IOException {
        String html = "<html><body>Invalid HTML</body>"; // incomplete HTML
        try {
            Document doc = Jsoup.parse(html);
            Assert.fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testConnectValidUrl() throws IOException {
        String url = "http://www.example.com";
        Connection connection = Jsoup.connect(url);
        Document doc = connection.get();
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.title() != null && !doc.title().isEmpty());
    }

    @org.junit.Test
    public void testParseFileValidHtml() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        Document doc = Jsoup.parse(file, charsetName);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
    }

    @org.junit.Test
    public void testParseFileValidHtmlWithBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";
        Document doc = Jsoup.parse(file, charsetName, baseUri);
        Assert.assertNotNull(doc);
        Element img = doc.selectFirst("img");
        Assert.assertEquals("http://www.example.com/images/image.png", img.absUrl("src"));
    }

    @org.junit.Test
    public void testParseInputStreamValidHtml() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new java.io.ByteArrayInputStream(html.getBytes());
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World", doc.select("h1").text());
    }

    @org.junit.Test
    public void testParseInputStreamValidHtmlWithBaseUri() throws IOException {
        String html = "<img src=\"images/image.png\" alt=\"Image\">";
        InputStream inputStream = new java.io.ByteArrayInputStream(html.getBytes());
        String baseUri = "http://www.example.com";
        Document doc = Jsoup.parse(inputStream, "UTF-8", baseUri);
        Assert.assertNotNull(doc);
        Element img = doc.selectFirst("img");
        Assert.assertEquals("http://www.example.com/images/image.png", img.absUrl("src"));
    }

    @org.junit.Test
    public void testCleanValidHtml() throws IOException {
        String html = "<html><head><title>Test</title></head><body><h1>Hello <b>World</b></h1></body></html>";
        String baseUri = "";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanedHtml = Jsoup.clean(html, baseUri, whitelist);
        Document doc = Jsoup.parse(cleanedHtml);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test", doc.title());
        Assert.assertEquals("Hello World", doc.select("h1").text());
        Assert.assertFalse(doc.select("script").size() > 0);
    }

    @org.junit.Test
    public void testCleanInvalidHtml() {
        String html = "<script>alert('Hello World');</script>";
        String baseUri = "";
        Whitelist whitelist = Whitelist.relaxed();
        try {
            String cleanedHtml = Jsoup.clean(html, baseUri, whitelist);
            Assert.fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}