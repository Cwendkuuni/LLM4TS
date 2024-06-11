package org.jsoup.test;

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

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><base href='https://example.com/'></base></head><body><a href='about.html'>About</a></body></html>";
        Document doc = Jsoup.parse(html, "https://example.com");
        Element link = doc.selectFirst("a");
        String linkHref = link.attr("href");
        Assert.assertEquals("about.html", linkHref);
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        Assert.assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head><base href='https://example.com/'></base></head><body><a href='about.html'>About</a></body></html>";
        Document doc = Jsoup.parse(html);
        Element link = doc.selectFirst("a");
        Assert.assertNull(link);
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Document doc = Jsoup.connect(url).get();
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseFromFileWithInvalidCharset() throws IOException {
        String charsetName = "invalid-charset";
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, charsetName, "");
    }

    @Test
    public void testParseFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "");
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseFromInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        String charsetName = "invalid-charset";
        Document doc = Jsoup.parse(inputStream, charsetName, "");
    }

    @Test
    public void testParseFromInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = Jsoup.parse(inputStream, "");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "");
        Assert.assertEquals("Hello World", doc.body().text());
    }

    @Test(expected = IOException.class)
    public void testParseFromUrlWithTimeout() throws IOException {
        String url = "http://slow-responding-server.com";
        Document doc = Jsoup.parse(url, 50);
    }

    @Test
    public void testClean() {
        String bodyHtml = "<script src='script.js'></script><div id='myDiv'><p>Hello World</p></div>";
        Whitelist whitelist = Whitelist.relaxed();
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "");
        String cleanedHtml = Jsoup.clean(bodyHtml, whitelist);
        Assert.assertFalse(cleanedHtml.contains("script"));
        Assert.assertTrue(cleanedHtml.contains("p"));
        Assert.assertTrue(cleanedHtml.contains("Hello World"));
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<script src='script.js'></script><div id='myDiv'><p>Hello World</p></p>";
        Whitelist whitelist = Whitelist.simpleTagNames("p");
        Assert.assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }
}