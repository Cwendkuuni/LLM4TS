package org.jsoup;

import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;
import org.junit.Test;

public class JsoupTest {

    @Test
    public void testParseStringString() {
        Document document = Jsoup.parse("<html><head></head><body>Hello World</body></html>", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testParseStringStringParser() {
        Parser parser = new Parser();
        Document document = Jsoup.parse("<html><head></head><body>Hello World</body></html>", "http://example.com", parser);
        assertNotNull(document);
    }

    @Test
    public void testParseString() {
        Document document = Jsoup.parse("<html><head></head><body>Hello World</body></html>");
        assertNotNull(document);
    }

    @Test
    public void testConnect() {
        Connection connection = Jsoup.connect("http://example.com");
        assertNotNull(connection);
    }

    @Test
    public void testParseFileStringString() throws IOException {
        File file = new File("test.html");
        Document document = Jsoup.parse(file, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testParseFileString() throws IOException {
        File file = new File("test.html");
        Document document = Jsoup.parse(file, "UTF-8");
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamStringString() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Document document = Jsoup.parse(inputStream, "UTF-8", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testParseInputStreamStringStringParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        Parser parser = new Parser();
        Document document = Jsoup.parse(inputStream, "UTF-8", "http://example.com", parser);
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragmentStringString() {
        Document document = Jsoup.parseBodyFragment("<h1>Hello World</h1>", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testParseBodyFragmentString() {
        Document document = Jsoup.parseBodyFragment("<h1>Hello World</h1>");
        assertNotNull(document);
    }

    @Test
    public void testParseURLInt() throws IOException {
        URL url = new URL("http://example.com");
        Document document = Jsoup.parse(url, 5000);
        assertNotNull(document);
    }

    @Test
    public void testCleanStringStringWhitelist() {
        String cleanHtml = Jsoup.clean("<h1>Hello World</h1>", "http://example.com", Whitelist.basic());
        assertNotNull(cleanHtml);
    }

    @Test
    public void testCleanStringWhitelist() {
        String cleanHtml = Jsoup.clean("<h1>Hello World</h1>", Whitelist.basic());
        assertNotNull(cleanHtml);
    }

    @Test
    public void testCleanStringStringWhitelistOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String cleanHtml = Jsoup.clean("<h1>Hello World</h1>", "http://example.com", Whitelist.basic(), outputSettings);
        assertNotNull(cleanHtml);
    }

    @Test
    public void testIsValid() {
        boolean isValid = Jsoup.isValid("<h1>Hello World</h1>", Whitelist.basic());
        assertTrue(isValid);
    }
}