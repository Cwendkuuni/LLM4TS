package org.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
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
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><base href=\"/test\">" + System.lineSeparator() +
                "<body><a href=\"foo.html\">Link</a></body></html>";
        Document doc = Jsoup.parse(html);
        assertEquals("/test/foo.html", doc.select("a[href]").attr("href"));
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser();
        Document doc = Jsoup.parse(html, "", parser);
        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseEmpty() {
        Document doc = Jsoup.parse("");
        assertEquals("", doc.body().text());
    }

    @Test
    public void testConnect() {
        Connection connection = Jsoup.connect("https://www.google.com");
        Document doc = connection.get();
        assertEquals("Google", doc.title());
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8");
        assertEquals("Test", doc.title());
    }

    @Test
    public void testParseFileWithBaseUri() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8", file.getAbsolutePath());
        assertEquals("Test", doc.title());
    }

    @Test
    public void testParseInputStream() throws IOException {
        URL url = new URL("https://www.google.com");
        InputStream inputStream = url.openStream();
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        assertEquals("Google", doc.title());
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        URL url = new URL("https://www.google.com");
        InputStream inputStream = url.openStream();
        Parser parser = new Parser();
        Document doc = Jsoup.parse(inputStream, "UTF-8", parser);
        assertEquals("Google", doc.title());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Hello World</h1>";
        Document doc = Jsoup.parseBodyFragment(html);
        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String html = "<html><head><base href=\"/test\">" + System.lineSeparator() +
                "<body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parseBodyFragment(html);
        assertEquals("/test/", doc.baseUri());
        assertEquals("Hello World", doc.body().select("h1").text());
    }

    @Test
    public void testParseUrl() throws IOException {
        Document doc = Jsoup.parse(new URL("https://www.google.com"));
        assertEquals("Google", doc.title());
    }

    @Test
    public void testClean() {
        String html = "<html><body><script>alert('Hello World')</script><h1>Hello</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(html, "", whitelist);
        assertEquals("", cleanHtml.contains("<script>"));
        assertEquals("Hello", cleanHtml.contains("<h1>"));
    }

    @Test
    public void testCleanWithBaseUri() {
        String html = "<html><body><script src=\"evil.js\"></script><h1>Hello</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String baseUri = "https://www.example.com";
        String cleanHtml = Jsoup.clean(html, baseUri, whitelist);
        assertEquals("", cleanHtml.contains("<script src"));
        assertEquals("Hello", cleanHtml.contains("<h1>"));
    }

    @Test
    public void testCleanWithOutputSettings() {
        String html = "<html><body><script src=\"evil.js\"></script><h1>Hello</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = Jsoup.clean(html, "", whitelist, outputSettings);
        assertEquals("", cleanHtml.contains("<script src"));
        assertEquals("Hello", cleanHtml.contains("<h1>"));
        assertEquals(System.lineSeparator() + System.lineSeparator() + System.lineSeparator() +
                "<html><body><h1>Hello</h1></body></html>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String html = "<html><body><p class=\"foo\">Test</p></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        whitelist.addTags("p");
        assertFalse(Jsoup.isValid(html, whitelist));
    }

    @Test
    public void testParseWithTimeout() {
        Connection connection = Jsoup.connect("http://slow-response.com");
        connection.timeout(500);
        try {
            Document doc = connection.get();
            fail("Expected timeout");
        } catch (java.net.SocketTimeoutException e) {
            // Expected
        }
    }
}