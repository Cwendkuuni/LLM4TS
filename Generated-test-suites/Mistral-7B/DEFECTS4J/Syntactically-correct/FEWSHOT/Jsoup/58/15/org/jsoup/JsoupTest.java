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
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><base href='/test'></head><body><a href='about.html'>About</a></body></html>";
        Document doc = Jsoup.parse(html);
        assertEquals("about.html", doc.select("a[href]").attr("href"));
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = Jsoup.parser();
        Document doc = parser.parseInput(html, "");
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testConnect() throws IOException {
        Connection connection = Jsoup.connect("https://example.com");
        Document doc = connection.get();
        assertNotNull(doc);
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8");
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/test.html");
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<p><a href='about.html'>About</a></p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "/test");
        assertEquals("about.html", doc.select("a[href]").attr("href"));
    }

    @Test
    public void testParseUrl() throws IOException {
        Document doc = Jsoup.parse(new URL("https://example.com"), 5000);
        assertNotNull(doc);
    }

    @Test
    public void testClean() {
        String html = "<html><body><script>alert('Hello World')</script><p>Safe text</p></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(html, "", whitelist);
        assertEquals("<p>Safe text</p>", cleanHtml);
    }

    @Test
    public void testCleanWithBaseUri() {
        String html = "<html><body><script src='evil.js'></script><p>Safe text</p></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(html, "/test", whitelist);
        assertEquals("<p>Safe text</p>", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String html = "<html><body><script src='evil.js'></script><p>Safe text <img src='image.jpg'></p></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = Jsoup.clean(html, "", whitelist, outputSettings);
        assertEquals("<p>Safe text <img alt=\"\" src=\"image.jpg\"/></p>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String html = "<html><body><input type='text' name='username'></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        assertFalse(Jsoup.isValid(html, whitelist));
    }
}