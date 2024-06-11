package org.jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsoupTest {

    @Test
    public void testParse() {
        Document doc = Jsoup.parse("<html><body>Hello World!</body></html>", "http://example.com");
        assertTrue(doc != null);
        assertEquals("<html><head></head><body>Hello World!</body></html>", doc.html());
    }

    @Test
    public void testParseWithParser() {
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse("<html><body>Hello World!</body></html>", "http://example.com", parser);
        assertTrue(doc != null);
        assertEquals("<html><head></head><body>Hello World!</body></html>", doc.html());
    }

    @Test
    public void testParseWithoutBaseUri() {
        Document doc = Jsoup.parse("<html><body>Hello World!</body></html>");
        assertTrue(doc != null);
        assertEquals("<html><head></head><body>Hello World!</body></html>", doc.html());
    }

    @Test
    public void testConnect() {
        Connection con = Jsoup.connect("http://example.com");
        assertTrue(con != null);
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8", "http://example.com");
        assertTrue(doc != null);
        assertEquals("<html><head></head><body>Hello World!</body></html>", doc.html());
    }

    @Test
    public void testParseFileWithoutBaseUri() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8");
        assertTrue(doc != null);
        assertEquals("<html><head></head><body>Hello World!</body></html>", doc.html());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        Document doc = Jsoup.parse(in, "UTF-8", "http://example.com");
        assertTrue(doc != null);
        assertEquals("<html><head></head><body>Hello World!</body></html>", doc.html());
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(in, "UTF-8", "http://example.com", parser);
        assertTrue(doc != null);
        assertEquals("<html><head></head><body>Hello World!</body></html>", doc.html());
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Jsoup.parseBodyFragment("<p>Hello World!</p>", "http://example.com");
        assertTrue(doc != null);
        assertEquals("<body><p>Hello World!</p></body>", doc.html());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        Document doc = Jsoup.parseBodyFragment("<p>Hello World!</p>");
        assertTrue(doc != null);
        assertEquals("<body><p>Hello World!</p></body>", doc.html());
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com");
        Document doc = Jsoup.parse(url, 5000);
        assertTrue(doc != null);
        assertEquals("<html><head></head><body>Hello World!</body></html>", doc.html());
    }

    @Test
    public void testClean() {
        String html = "<script>alert('XSS')</script>";
        String cleanHtml = Jsoup.clean(html, "http://example.com", Whitelist.basic());
        assertEquals("", cleanHtml);
    }

    @Test
    public void testCleanWithoutBaseUri() {
        String html = "<script>alert('XSS')</script>";
        String cleanHtml = Jsoup.clean(html, Whitelist.basic());
        assertEquals("", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String html = "<p>Hello World!</p>";
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
        String cleanHtml = Jsoup.clean(html, "http://example.com", Whitelist.basic(), outputSettings);
        assertEquals("<p>\n" +
                " Hello World!\n" +
                "</p>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String html = "<p>Hello World!</p>";
        boolean valid = Jsoup.isValid(html, Whitelist.basic());
        assertTrue(valid);
    }
}