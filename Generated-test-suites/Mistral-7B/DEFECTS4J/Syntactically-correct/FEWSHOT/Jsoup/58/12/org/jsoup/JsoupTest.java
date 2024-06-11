package org.jsoup;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseWithBaseUri() {
        String baseUri = "http://example.com";
        String html = "<img src=\"image.png\" alt=\"Image\">";
        Document doc = Jsoup.parse(html, baseUri);
        String imgUrl = doc.select("img").attr("src");
        assertEquals("http://example.com/image.png", imgUrl);
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Document doc = Jsoup.connect(url).get();
        assertEquals("Text from example.com", doc.text());
    }

    @Test
    public void testParseFromFile() throws IOException {
        String charsetName = "UTF-8";
        String filePath = "test.html";
        Document doc = Jsoup.parse(new File(filePath), charsetName);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseFromInputStream() throws IOException {
        String charsetName = "UTF-8";
        String filePath = "test.html";
        InputStream inputStream = new FileInputStream(new File(filePath));
        Document doc = Jsoup.parse(inputStream, charsetName);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertEquals("Hello World", doc.text());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser();
        Document doc = Jsoup.parse(html, "", parser);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseWithBaseUriAndParser() {
        String baseUri = "http://example.com";
        String html = "<img src=\"image.png\" alt=\"Image\">";
        Parser parser = new Parser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        String imgUrl = doc.select("img").attr("src");
        assertEquals("http://example.com/image.png", imgUrl);
    }

    @Test
    public void testClean() {
        String bodyHtml = "<html><body><script>alert('XSS');</script><p>Hello World</p></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);
        assertEquals("Hello World", cleanHtml);
    }

    @Test
    public void testCleanWithBaseUri() {
        String bodyHtml = "<img src=\"image.png\" alt=\"Image\">";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, "http://example.com", whitelist);
        assertEquals("<p></p>", cleanHtml);
    }

    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<html><body><script>alert('XSS');</script><p>Hello World</p></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);
        assertEquals("Hello World", cleanHtml);
    }

    @Test
    public void testCleanWithBaseUriAndWhitelist() {
        String bodyHtml = "<img src=\"image.png\" alt=\"Image\">";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, "http://example.com", whitelist);
        assertEquals("<p></p>", cleanHtml);
    }

    @Test
    public void testCleanWithWhitelistAndOutputSettings() {
        String bodyHtml = "<html><body><script>alert('XSS');</script><p>Hello World</p></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist, outputSettings);
        assertEquals("Hello World", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<p><strong>Hello World</strong></p>";
        Whitelist whitelist = Whitelist.simpleText();
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }

    @Test
    public void testIsNotValid() {
        String bodyHtml = "<p><script>alert('XSS');</script></p>";
        Whitelist whitelist = Whitelist.simpleText();
        assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }

}