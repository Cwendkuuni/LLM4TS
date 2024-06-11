package org.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {
    @Test
    public void testParse() {
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>", "");
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello, world!", doc.body().text());
    }

    @Test
    public void testParseWithBaseUriAndParser() {
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>", "", parser);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello, world!", doc.body().text());
    }

    @Test
    public void testParseWithoutBaseUri() {
        Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>");
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello, world!", doc.body().text());
    }

    @Test
    public void testConnect() {
        Connection con = Jsoup.connect("http://example.com");
        assertNotNull(con);
        assertEquals("http://example.com", con.request().url());
    }

    @Test
    public void testParseFromFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello, world!", doc.body().text());
    }

    @Test
    public void testParseFromInputStream() throws IOException {
        InputStream in = DataUtil.class.getResourceAsStream("/test.html");
        Document doc = Jsoup.parse(in, "UTF-8", "http://example.com");
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello, world!", doc.body().text());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Jsoup.parseBodyFragment("<p>Hello, world!</p>", "http://example.com");
        assertNotNull(doc);
        assertEquals("Hello, world!", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        Document doc = Jsoup.parseBodyFragment("<p>Hello, world!</p>");
        assertNotNull(doc);
        assertEquals("Hello, world!", doc.body().text());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<p><span>Hello, world!</span></p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertNotNull(cleanHtml);
        assertEquals("<p>Hello, world!</p>", cleanHtml);
    }

    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<p><span>Hello, world!</span></p>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertNotNull(cleanHtml);
        assertEquals("<p>Hello, world!</p>", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<p><span>Hello, world!</span></p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertNotNull(cleanHtml);
        assertTrue(cleanHtml.contains("<p>\n"));
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<p><span>Hello, world!</span></p>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(isValid);
    }

    @Test
    public void testIsValidWithInvalidHtml() {
        String bodyHtml = "<p><span>Hello, world!</span></div>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertFalse(isValid);
    }
    
    @Test
    public void testParseWithURL() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 1000;
        Document doc = Jsoup.parse(url, timeoutMillis);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello, world!", doc.body().text());
    }
}