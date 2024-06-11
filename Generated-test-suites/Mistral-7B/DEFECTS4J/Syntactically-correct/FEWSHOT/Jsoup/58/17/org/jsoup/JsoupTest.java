package org.jsoup;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.jsoup.parser.Parser.parseBodyFragment;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
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
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><body><a href=\"/about\">About</a></body></html>";
        Document doc = Jsoup.parse(html, "http://example.com");
        assertEquals("http://example.com/about", doc.select("a[href]").attr("href"));
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = Parser.htmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseWithEmptyString() {
        Document doc = Jsoup.parse("");
        assertEquals(0, doc.childElementCount());
    }

    @Test
    public void testConnect() {
        Connection conn = Jsoup.connect("http://example.com");
        assertEquals("http://example.com", conn.url());
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8");
        assertEquals("Test", doc.select("h1").text());
    }

    @Test
    public void testParseFileWithBaseUri() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8", "http://example.com");
        assertEquals("http://example.com/test.html", doc.baseUri());
    }

    @Test
    public void testParseInputStream() throws IOException {
        File file = new File("test.html");
        InputStream input = new FileInputStream(file);
        Document doc = Jsoup.parse(input, "UTF-8");
        assertEquals("Test", doc.select("h1").text());
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        File file = new File("test.html");
        InputStream input = new FileInputStream(file);
        Document doc = Jsoup.parse(input, "UTF-8", "http://example.com");
        assertEquals("http://example.com/test.html", doc.baseUri());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = parseBodyFragment(html, "");
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String html = "<html><body><a href=\"/about\">About</a></body></html>";
        Document doc = parseBodyFragment(html, "http://example.com");
        assertEquals("http://example.com/about", doc.select("a[href]").attr("href"));
    }

    @Test
    public void testParseUrl() throws IOException {
        Document doc = Jsoup.parse(new URL("http://example.com"), 5000);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testClean() {
        String html = "<html><body><script>alert('Hello World');</script><h1>Test</h1></body></html>";
        Document dirty = parseBodyFragment(html, "");
        Whitelist whitelist = Whitelist.simpleTags();
        String clean = Jsoup.clean(dirty.body().html(), whitelist);
        assertTrue(clean.matches("(?s).*<h1>Test</h1>.*"));
        assertFalse(clean.matches("(?s).*<script>.*"));
    }

    @Test
    public void testCleanWithBaseUri() {
        String html = "<html><body><script src=\"/script.js\"></script><h1>Test</h1></body></html>";
        Document dirty = parseBodyFragment(html, "");
        Whitelist whitelist = Whitelist.simpleTags();
        String clean = Jsoup.clean(dirty.body().html(), whitelist);
        assertTrue(clean.matches("(?s).*<h1>Test</h1>.*"));
        assertFalse(clean.matches("(?s).*<script src=\".*\"[^>]*>.*"));
        assertEquals("http://example.com/script.js", dirty.select("script[src]").attr("src"));
        assertEquals("", clean.select("script").toString());
    }

    @Test
    public void testCleanWithOutputSettings() throws IOException {
        String html = "<html><body><script src=\"/script.js\">&lt;script&gt;alert('Hello World');&lt;/script&gt;</script><h1>Test</h1></body></html>";
        Document dirty = parseBodyFragment(html, "");
        Whitelist whitelist = Whitelist.simpleTags();
        Document.OutputSettings outputSettings = Document.OutputSettings.indentTags(true).prettyPrint(false);
        String clean = Jsoup.clean(dirty.body().html(), whitelist, outputSettings);
        assertTrue(clean.matches("(?s).*<h1>Test</h1>.*"));
        assertFalse(clean.matches("(?s).*<script src=\".*\"[^>]*>.*"));
        assertEquals("http://example.com/script.js", dirty.select("script[src]").attr("src"));
        assertEquals("", clean.select("script").toString());
        assertEquals("\n<h1>Test</h1>\n", clean);
    }

    @Test
    public void testIsValid() {
        String html = "<html><body><h1 id='test' data-foo='bar'>Test</h1></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        assertTrue(Jsoup.isValid(html, whitelist));
    }

    @Test
    public void testIsValidWithScriptTag() {
        String html = "<html><body><h1 id='test' data-foo='bar'><script>alert('Hello World');</script></h1></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        assertFalse(Jsoup.isValid(html, whitelist));
    }

}