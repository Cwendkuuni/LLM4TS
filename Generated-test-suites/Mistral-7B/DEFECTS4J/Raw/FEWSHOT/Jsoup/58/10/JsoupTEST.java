package org.jsoup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParseString() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseStringWithBaseUri() {
        String html = "<html><base href=\"/index\"><a href=\"about.html\">About</a></html>";
        Document doc = Jsoup.parse(html);
        assertEquals("about.html", doc.select("a[href]").attr("abs:href"));
    }

    @Test
    public void testParseStringWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser();
        Document doc = Jsoup.parse(html, "", parser);
        assertNotNull(doc);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseStringWithBaseUriAndParser() {
        String html = "<html><base href=\"/index\"><a href=\"about.html\">About</a></html>";
        Parser parser = new Parser();
        Document doc = Jsoup.parse(html, "", parser);
        assertEquals("about.html", doc.select("a[href]").attr("abs:href"));
    }

    @Test
    public void testParseStringNoBaseUri() {
        String html = "<html><base href=\"/index\"><a href=\"about.html\">About</a></html>";
        Document doc = Jsoup.parse(html);
        assertEquals("about.html", doc.select("a[href]").attr("abs:href"));
    }

    @Test
    public void testConnect() {
        Connection con = Jsoup.connect("https://www.google.com");
        Document doc = con.get();
        assertNotNull(doc);
    }

    @Test
    public void testParseFile() throws IOException {
        File inputFile = new File("test.html");
        Document doc = Jsoup.parse(inputFile, "UTF-8");
        assertNotNull(doc);
    }

    @Test
    public void testParseFileWithBaseUri() throws IOException {
        File inputFile = new File("test.html");
        Document doc = Jsoup.parse(inputFile, "UTF-8", inputFile.getAbsolutePath());
        assertEquals(inputFile.getAbsolutePath(), doc.baseUri());
    }

    @Test
    public void testParseInputStream() throws IOException {
        File inputFile = new File("test.html");
        InputStream inputStream = new FileInputStream(inputFile);
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        File inputFile = new File("test.html");
        InputStream inputStream = new FileInputStream(inputFile);
        Document doc = Jsoup.parse(inputStream, "UTF-8", inputFile.getAbsolutePath());
        assertEquals(inputFile.getAbsolutePath(), doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>This is a body fragment.</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertEquals("This is a body fragment.", doc.body().html());
    }

    @Test
    public void testParseUrl() throws IOException {
        URL url = new URL("https://www.google.com");
        Document doc = Jsoup.parse(url, 5000);
        assertNotNull(doc);
    }

    @Test
    public void testClean() {
        String bodyHtml = "<h1>Hello World <script>alert('XSS')</script></h1>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, null, whitelist);
        assertNull(cleanHtml.matches(".*<script.*>.*"));
        assertEquals("Hello World", cleanHtml);
    }

    @Test
    public void testCleanWithBaseUri() {
        String bodyHtml = "<link rel=\"stylesheet\" href=\"styles.css\"><script>alert('XSS')</script>";
        Whitelist whitelist = Whitelist.rels("link");
        String cleanHtml = Jsoup.clean(bodyHtml, "http://www.example.com", whitelist);
        assertNull(cleanHtml.matches(".*<script.*>.*"));
        assertEquals("<link rel=\"stylesheet\" href=\"styles.css\">", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<img src=\"image.jpg\" width=\"400\" height=\"300\">";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true).indentAmount(2);
        String cleanHtml = Jsoup.clean(bodyHtml, null, whitelist, outputSettings);
        assertEquals("<img src=\"image.jpg\" width=\"400\" height=\"300\">\n", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<input type=\"text\" name=\"username\" value=\"admin\">";
        Whitelist whitelist = Whitelist.formInputTypes("text");
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(isValid);
    }

    @Test
    public void testIsNotValid() {
        String bodyHtml = "<input type=\"password\" name=\"password\">";
        Whitelist whitelist = Whitelist.formInputTypes("text");
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertFalse(isValid);
    }
}