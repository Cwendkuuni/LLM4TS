package org.jsoup;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;
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

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Test</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertTrue(doc.tagName().equals("html"));
        assertTrue(doc.body().tagName().equals("body"));
        assertTrue(doc.body().child(0).tagName().equals("h1"));
        assertTrue(doc.body().child(0).text().equals("Test"));
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><base href='/test'></head><body><a href='about.html'></a></body></html>";
        Document doc = Jsoup.parse(html);
        String baseUri = "/test";
        String link = doc.body().child(0).attr("href");
        String absoluteLink = Jsoup.parse(link, baseUri).text();
        assertTrue(absoluteLink.equals("about.html"));
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Test</h1></body></html>";
        Parser parser = new Parser();
        Document doc = Jsoup.parse(html, parser);
        assertTrue(doc.tagName().equals("html"));
        assertTrue(doc.body().tagName().equals("body"));
        assertTrue(doc.body().child(0).tagName().equals("h1"));
        assertTrue(doc.body().child(0).text().equals("Test"));
    }

    @Test
    public void testParseWithEmptyHtml() {
        String html = "";
        Document doc = Jsoup.parse(html);
        assertTrue(doc.body().text().isEmpty());
    }

    @Test
    public void testConnect() {
        Connection connection = Jsoup.connect("https://www.google.com");
        Document doc = connection.get();
        assertTrue(doc.title().text().contains("Google"));
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        String html = DataUtil.readFile(file);
        Document doc = Jsoup.parse(html, file.getAbsolutePath());
        assertTrue(doc.title().text().contains("Test"));
    }

    @Test
    public void testParseInputStream() throws IOException {
        File file = new File("test.html");
        InputStream inputStream = new FileInputStream(file);
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        assertTrue(doc.title().text().contains("Test"));
        inputStream.close();
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<h1>Test</h1>";
        Document doc = Jsoup.parseBodyFragment(html);
        assertTrue(doc.tagName().equals("html"));
        assertTrue(doc.body().tagName().equals("body"));
        assertTrue(doc.body().child(0).tagName().equals("h1"));
        assertTrue(doc.body().child(0).text().equals("Test"));
    }

    @Test
    public void testParseUrl() throws IOException {
        URL url = new URL("https://www.google.com");
        Document doc = Jsoup.parse(url.openStream(), url.toString());
        assertTrue(doc.title().text().contains("Google"));
    }

    @Test
    public void testClean() {
        String html = "<script src='malicious.js'></script><p>Test</p>";
        Whitelist whitelist = Whitelist.simpleTags();
        String cleanHtml = Jsoup.clean(html, "", whitelist);
        assertTrue(cleanHtml.equals("<p>Test</p>"));
    }

    @Test
    public void testCleanWithBaseUri() {
        String html = "<img src='about.html' srcset='about.html 1x, about-2x.html 2x' alt='Profile picture'>";
        Whitelist whitelist = Whitelist.simpleTags();
        String baseUri = "/test";
        String cleanHtml = Jsoup.clean(html, baseUri, whitelist);
        assertTrue(cleanHtml.equals("<img alt='Profile picture'>"));
    }

    @Test
    public void testCleanWithOutputSettings() {
        String html = "<script src='malicious.js'></script><p>Test</p>";
        Whitelist whitelist = Whitelist.simpleTags();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint();
        String cleanHtml = Jsoup.clean(html, "", whitelist, outputSettings);
        assertTrue(cleanHtml.equals("<p>Test</p>"));
    }

    @Test
    public void testIsValid() {
        String html = "<p><script>alert('Hello World');</script></p>";
        Whitelist whitelist = Whitelist.simpleTags();
        assertFalse(Jsoup.isValid(html, whitelist));
    }

    @Test
    public void testIsValidWithCleanHtml() {
        String html = "<p><script>alert('Hello World');</script></p>";
        Whitelist whitelist = Whitelist.simpleTags();
        String cleanHtml = Jsoup.clean(html, "");
        assertTrue(Jsoup.isValid(cleanHtml, whitelist));
    }
}