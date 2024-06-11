package org.jsoup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

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
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html);
        assertNotNull(document);
        assertEquals("Hello World", document.select("h1").text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<img src=\"image.png\" />";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertEquals("image.png", document.select("img[src]").attr("src"));
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser();
        Document document = Jsoup.parse(html, "", parser);
        assertNotNull(document);
        assertEquals("Hello World", document.select("h1").text());
    }

    @Test
    public void testConnect() {
        Connection connection = Jsoup.connect("http://example.com");
        assertNotNull(connection);
    }

    @Test
    public void testParseFile() throws IOException {
        String charsetName = "UTF-8";
        String filePath = "test.html";
        Document document = Jsoup.parse(new File(filePath), charsetName);
        assertNotNull(document);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        String filePath = "test.html";
        InputStream inputStream = new FileInputStream(new File(filePath));
        Document document = Jsoup.parse(inputStream, charsetName);
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(document);
        assertEquals("Hello World", document.select("p").text());
    }

    @Test
    public void testParseUrl() throws IOException {
        String url = "http://example.com";
        Document document = Jsoup.parse(new URL(url), 5000);
        assertNotNull(document);
    }

    @Test
    public void testClean() {
        String bodyHtml = "<p><script>alert('Hello World')</script>Hello World</p>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);
        assertNull(cleanHtml.matches(".*<script.*>.*"));
        assertEquals("Hello World", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<p><script>alert('Hello World')</script>Hello World</p>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertFalse(isValid);
    }
}