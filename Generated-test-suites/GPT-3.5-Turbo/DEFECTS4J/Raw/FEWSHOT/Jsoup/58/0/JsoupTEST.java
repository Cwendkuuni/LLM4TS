package org.jsoup;

import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

public class JsoupTest {
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        Document document = Jsoup.parse(html);
        assertEquals("Hello World!", document.body().getElementById("h1").text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><body><a href='http://example.com'>Link</a></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertEquals("http://example.com", document.baseUri());
        assertEquals("http://example.com", document.select("a").first().absUrl("href"));
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse(html, "", parser);
        assertEquals("Hello World!", document.body().getElementById("h1").text());
    }

    @Test
    public void testConnect() throws IOException {
        Connection connection = Jsoup.connect("http://example.com");
        Document document = connection.get();
        assertEquals(200, connection.response().statusCode());
        assertEquals("Example Domain", document.title());
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("example.html");
        Document document = Jsoup.parse(file, "UTF-8", "");
        assertEquals("Hello World!", document.body().getElementById("h1").text());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("example.html");
        Document document = Jsoup.parse(inputStream, "UTF-8", "");
        assertEquals("Hello World!", document.body().getElementById("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        assertEquals("Hello World!", document.body().getElementById("h1").text());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<script>alert('XSS');</script><p>Hello World!</p>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);
        assertEquals("<p>Hello World!</p>", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<p>Hello World!</p>";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist, outputSettings);
        assertEquals("<p>\n Hello World!\n</p>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertEquals(true, isValid);
    }
}