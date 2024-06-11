package org.jsoup.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParseStringWithBaseUri() {
        String html = "<html><head><base href=\"https://example.com\"></base></head><body><a href=\"/about\">About</a></body></html>";
        Document document = Jsoup.parse(html, "https://example.com");
        Element link = document.selectFirst("a");
        String href = link.attr("href");
        Assert.assertEquals("About", href);
    }

    @Test
    public void testParseString() {
        String html = "<html><head></head><body><a href=\"/about\">About</a></body></html>";
        Document document = Jsoup.parse(html);
        Element link = document.selectFirst("a");
        String href = link.attr("href");
        Assert.assertEquals("/about", href);
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document document = Jsoup.parse(file, "UTF-8");
        Element title = document.title();
        String titleText = title.text();
        Assert.assertEquals("Test Page", titleText);
    }

    @Test
    public void testConnect() {
        Connection connection = Jsoup.connect("https://example.com");
        Document document = connection.get();
        Element title = document.title();
        String titleText = title.text();
        Assert.assertTrue(titleText.contains("Example Domain"));
    }

    @Test(expected = IOException.class)
    public void testParseFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        Document document = Jsoup.parse(file, "ISO-8859-15"); // invalid charset
    }

    @Test
    public void testParseStringWithInvalidBaseUri() {
        String html = "<html><head><base href=\"<!--base href=\"file:///example.com-->\"></base></head><body><a href=\"/about\">About</a></body></html>";
        Document document = Jsoup.parse(html);
        try {
            String linkHref = document.selectFirst("a").attr("href");
            Assert.fail("Exception expected: MalformedURLException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseStringWithParser() {
        String html = "<xml><tag attr=\"value\">Content</tag></xml>";
        Parser parser = org.jsoup.parser.JsoupScanner.xmlParser();
        Document document = Jsoup.parse(html, "", parser);
        Element tag = document.selectFirst("tag");
        String attrValue = tag.attr("attr");
        Assert.assertEquals("value", attrValue);
    }

    @Test
    public void testParseInputStream() throws IOException {
        URL url = new URL("file:test.html");
        InputStream inputStream = url.openStream();
        Document document = Jsoup.parse(inputStream, "UTF-8");
        Element title = document.title();
        String titleText = title.text();
        Assert.assertEquals("Test Page", titleText);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        URL url = new URL("file:test.html");
        InputStream inputStream = url.openStream();
        Document document = Jsoup.parse(inputStream, "ASCII"); // invalid charset
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Test</p></div>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        Element p = document.selectFirst("p");
        String text = p.text();
        Assert.assertEquals("Test", text);
    }

    @Test
    public void testClean() {
        String bodyHtml = "<html><body><script src=\"malicious.js\"></script><div><p>Test</p></div></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanedHtml = Jsoup.clean(bodyHtml, "", whitelist);
        Document document = Jsoup.parseBodyFragment(cleanedHtml);
        Element p = document.selectFirst("p");
        String text = p.text();
        Assert.assertEquals("Test", text);
    }

    @Test
    public void testCleanWithBaseUri() {
        String bodyHtml = "<html><body><script src=\"malicious.js\"></script><img src=\"/evil.jpg\"></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanedHtml = Jsoup.clean(bodyHtml, "https://example.com", whitelist);
        Document document = Jsoup.parseBodyFragment(cleanedHtml);
        Element img = document.selectFirst("img");
        String src = img.attr("src");
        Assert.assertEquals("", src);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<html><body><script src=\"malicious.js\"></script><img src=\"evil.jpg\"></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanedHtml = Jsoup.clean(bodyHtml, "", whitelist, outputSettings);
        Document document = Jsoup.parseBodyFragment(cleanedHtml);
        Element img = document.selectFirst("img");
        String src = img.attr("src");
        Assert.assertEquals("", src);
        String html = document.html();
        Assert.assertTrue(html.contains("\n"));
    }

    @Test(expected = NullPointerException.class)
    public void testCleanWithNullBodyHtml() {
        String bodyHtml = null;
        Whitelist whitelist = Whitelist.relaxed();
        Jsoup.clean(bodyHtml, "", whitelist);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<html><body><p>Test</p></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean valid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertTrue(valid);
    }

    @Test
    public void testIsValidWithInvalidBodyHtml() {
        String bodyHtml = "<html><body><script>alert('Hello World')</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean valid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertFalse(valid);
    }

}