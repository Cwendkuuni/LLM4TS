package org.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    public void testParseStringString() {
        Document doc = Jsoup.parse("<html><body><p>Hello, world!</p></body></html>", "http://example.com");
        assertTrue(doc != null);
        assertTrue(doc.select("p").size() == 1);
        assertTrue(doc.select("p").text().equals("Hello, world!"));
    }

    @Test
    public void testParseStringStringParser() {
        Parser parser = new Parser();
        Document doc = Jsoup.parse("<html><body><p>Hello, world!</p></body></html>", "http://example.com", parser);
        assertTrue(doc != null);
        assertTrue(doc.select("p").size() == 1);
        assertTrue(doc.select("p").text().equals("Hello, world!"));
    }

    @Test
    public void testParseString() {
        Document doc = Jsoup.parse("<html><body><p>Hello, world!</p></body></html>");
        assertTrue(doc != null);
        assertTrue(doc.select("p").size() == 1);
        assertTrue(doc.select("p").text().equals("Hello, world!"));
    }

    @Test
    public void testConnect() {
        Connection con = Jsoup.connect("http://example.com");
        assertTrue(con != null);
    }

    @Test
    public void testParseFileStringString() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8", "http://example.com");
        assertTrue(doc != null);
        assertTrue(doc.select("p").size() == 1);
        assertTrue(doc.select("p").text().equals("Hello, world!"));
    }

    @Test
    public void testParseFileString() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8");
        assertTrue(doc != null);
        assertTrue(doc.select("p").size() == 1);
        assertTrue(doc.select("p").text().equals("Hello, world!"));
    }

    @Test
    public void testParseInputStreamStringString() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        Document doc = Jsoup.parse(in, "UTF-8", "http://example.com");
        assertTrue(doc != null);
        assertTrue(doc.select("p").size() == 1);
        assertTrue(doc.select("p").text().equals("Hello, world!"));
    }

    @Test
    public void testParseInputStreamStringStringParser() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        Parser parser = new Parser();
        Document doc = Jsoup.parse(in, "UTF-8", "http://example.com", parser);
        assertTrue(doc != null);
        assertTrue(doc.select("p").size() == 1);
        assertTrue(doc.select("p").text().equals("Hello, world!"));
    }

    @Test
    public void testParseBodyFragmentStringString() {
        Document doc = Jsoup.parseBodyFragment("<p>Hello, world!</p>", "http://example.com");
        assertTrue(doc != null);
        assertTrue(doc.select("p").size() == 1);
        assertTrue(doc.select("p").text().equals("Hello, world!"));
    }

    @Test
    public void testParseBodyFragmentString() {
        Document doc = Jsoup.parseBodyFragment("<p>Hello, world!</p>");
        assertTrue(doc != null);
        assertTrue(doc.select("p").size() == 1);
        assertTrue(doc.select("p").text().equals("Hello, world!"));
    }

    @Test
    public void testParseURLInt() throws IOException {
        URL url = new URL("http://example.com");
        Document doc = Jsoup.parse(url, 5000);
        assertTrue(doc != null);
        assertTrue(doc.select("p").size() == 1);
        assertTrue(doc.select("p").text().equals("Hello, world!"));
    }

    @Test
    public void testCleanStringStringWhitelist() {
        String cleanHtml = Jsoup.clean("<p><script>alert('XSS');</script></p>", "http://example.com", Whitelist.basic());
        assertEquals("<p></p>", cleanHtml);
    }

    @Test
    public void testCleanStringWhitelist() {
        String cleanHtml = Jsoup.clean("<p><script>alert('XSS');</script></p>", Whitelist.basic());
        assertEquals("<p></p>", cleanHtml);
    }

    @Test
    public void testCleanStringStringWhitelistOutputSettings() {
        String cleanHtml = Jsoup.clean("<p><script>alert('XSS');</script></p>", "http://example.com", Whitelist.basic(), new Document.OutputSettings());
        assertEquals("<p></p>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        boolean isValid = Jsoup.isValid("<p>Hello, world!</p>", Whitelist.basic());
        assertTrue(isValid);
    }
}