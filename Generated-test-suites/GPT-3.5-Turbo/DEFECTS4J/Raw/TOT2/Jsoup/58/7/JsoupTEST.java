package org.jsoup;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        String baseUri = "https://example.com";
        Document doc = Jsoup.parse(html, baseUri);

        assertEquals("https://example.com", doc.baseUri());
        assertEquals("Test", doc.title());
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testParseFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8", "https://example.com");

        assertNotNull(doc);
    }

    @Test
    public void testParseFromInputStream() throws IOException {
        InputStream in = JsoupTest.class.getResourceAsStream("/test.html");
        Document doc = Jsoup.parse(in, "UTF-8", "https://example.com");

        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello, World!</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);

        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<script>alert('XSS');</script><p>Hello, World!</p>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);

        assertFalse(cleanHtml.contains("<script>"));
        assertTrue(cleanHtml.contains("<p>"));
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<p>Hello, World!</p>";
        Whitelist whitelist = Whitelist.basic();

        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }
}