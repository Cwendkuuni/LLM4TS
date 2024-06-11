package org.jsoup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
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
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><base href=\"/test\"><body><a href=\"about.html\">About</a></body></html>";
        Document doc = Jsoup.parse(html);
        String baseUri = "/test";
        assertEquals("about.html", doc.select("a[href]").attr("abs:href"));
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        assertNotNull(doc);
        assertNotNull(doc.body());
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testConnect() throws IOException {
        Connection con = Jsoup.connect("https://www.google.com");
        Document doc = con.get();
        assertNotNull(doc);
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file);
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        File file = new File("test.html");
        InputStream in = new FileInputStream(file);
        Document doc = Jsoup.parse(in, "UTF-8");
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("p").text());
    }

    @Test
    public void testParseUrl() throws IOException {
        URL url = new URL("https://www.google.com");
        Document doc = Jsoup.parse(url, 5000);
        assertNotNull(doc);
    }

    @Test
    public void testClean() {
        String bodyHtml = "<html><body><script>alert('Hello')</script><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);
        assertEquals("Hello World", cleanHtml);
    }

    @Test
    public void testCleanWithBaseUri() {
        String bodyHtml = "<html><base href=\"/test\"><body><a href=\"about.html\">About</a></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, "/test", whitelist);
        assertEquals("", cleanHtml);
    }

    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<html><body><h1>Hello World</h1><script>alert('Hello')</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);
        assertEquals("Hello World", cleanHtml);
    }

    @Test
    public void testCleanWithWhitelistAndOutputSettings() {
        String bodyHtml = "<html><body><h1>Hello World</h1><script>alert('Hello')</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = Document.OutputSettings.prettyPrint();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist, outputSettings);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<html><body><h1>Hello World</h1><script>alert('Hello')</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean valid = Jsoup.isValid(bodyHtml, whitelist);
        assertFalse(valid);
    }

    @Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<html><body><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean valid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(valid);
    }
}