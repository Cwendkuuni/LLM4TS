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
        String html = "<html><head></head><body><p>Hello World</p></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head></head><body><img src=\"image.jpg\"></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("image.jpg", doc.imageUrl());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><head></head><body><p>Hello World</p></body></html>";
        Parser parser = Jsoup.parser();
        Document doc = Jsoup.parse(html, parser);
        assertNotNull(doc);
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseWithEmptyHtml() {
        String html = "";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("", doc.body().html());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection con = Jsoup.connect(url);
        assertNotNull(con);
    }

    @Test
    public void testParseFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8");
        assertNotNull(doc);
        assertEquals("Test", doc.title());
    }

    @Test
    public void testParseFromInputStream() throws IOException {
        File file = new File("test.html");
        InputStream is = new FileInputStream(file);
        Document doc = Jsoup.parse(is, "UTF-8");
        assertNotNull(doc);
        assertEquals("Test", doc.title());
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
        String bodyHtml = "<img src=\"image.jpg\">";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("image.jpg", doc.imageUrl());
    }

    @Test
    public void testParseWithTimeout() {
        String url = "http://slow-website.com";
        Document doc = Jsoup.parse(url, 5000);
        assertNotNull(doc);
    }

    @Test
    public void testClean() {
        String bodyHtml = "<script>alert('Hello')</script><p>Test</p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertNotNull(cleanHtml);
        assertEquals("Test", cleanHtml);
    }

    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<p>Test <a href=\"http://example.com\">Link</a></p>";
        Whitelist whitelist = new Whitelist().addTags("p", "a");
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertNotNull(cleanHtml);
        assertEquals("<p>Test <a href=\"http://example.com\">Link</a></p>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<p>Test <script>alert('Hello')</script></p>";
        Whitelist whitelist = new Whitelist();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertFalse(isValid);
    }

    @Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<p>Test <a href=\"http://example.com\">Link</a></p>";
        Whitelist whitelist = new Whitelist().addTags("p", "a");
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(isValid);
    }
}