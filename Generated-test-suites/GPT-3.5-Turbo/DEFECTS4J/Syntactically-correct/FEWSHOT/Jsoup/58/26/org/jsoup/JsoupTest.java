package org.jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

public class JsoupTest {

    @Test
    public void testParse() {
        Document doc = Jsoup.parse("<html><body><p>Hello, World!</p></body></html>");
        assertEquals("Hello, World!", doc.body().text());
    }

    @Test
    public void testParseWithBaseUri() {
        Document doc = Jsoup.parse("<html><body><p>Hello, World!</p></body></html>", "http://example.com");
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseWithParser() {
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse("<html><body><p>Hello, World!</p></body></html>", "", parser);
        assertTrue(doc instanceof org.jsoup.parser.XmlTreeBuilder);
    }

    @Test
    public void testConnect() {
        Connection con = Jsoup.connect("http://example.com");
        assertEquals("http://example.com", con.request().url().toString());
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8", "http://example.com");
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream in = JsoupTest.class.getResourceAsStream("test.html");
        Document doc = Jsoup.parse(in, "UTF-8", "http://example.com");
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream in = JsoupTest.class.getResourceAsStream("test.html");
        Parser parser = Parser.htmlParser();
        Document doc = Jsoup.parse(in, "UTF-8", "http://example.com", parser);
        assertTrue(doc instanceof org.jsoup.parser.HtmlTreeBuilder);
    }

    @Test
    public void testParseBodyFragment() {
        Document doc = Jsoup.parseBodyFragment("<p>Hello, World!</p>");
        assertEquals("p", doc.body().child(0).tagName());
        assertEquals("Hello, World!", doc.body().child(0).text());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        Document doc = Jsoup.parseBodyFragment("<p>Hello, World!</p>", "http://example.com");
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testClean() {
        String cleanHtml = Jsoup.clean("<p><strong>Hello, World!</strong></p>", Whitelist.basic());
        assertEquals("<p><strong>Hello, World!</strong></p>", cleanHtml);
    }

    @Test
    public void testCleanWithBaseUri() {
        String cleanHtml = Jsoup.clean("<p><strong>Hello, World!</strong></p>", "http://example.com", Whitelist.basic());
        assertEquals("<p><strong>Hello, World!</strong></p>", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String cleanHtml = Jsoup.clean("<p><strong>Hello, World!</strong></p>", "http://example.com", Whitelist.basic(), outputSettings);
        assertEquals("<p><strong>Hello, World!</strong></p>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        boolean isValid = Jsoup.isValid("<p><strong>Hello, World!</strong></p>", Whitelist.basic());
        assertTrue(isValid);
    }
     
}