package org.jsoup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.nodes.Document;
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
    public void testParseWithBaseUri() {
        String html = "<html><body><a href=\"/test/index.html\">Test Page</a></body></html>";
        String baseUri = "http://example.com";

        Document doc = Jsoup.parse(html, baseUri);
        assertEquals("http://example.com/test/index.html", doc.select("a[href]").attr("abs:href"));
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><a href=\"/test/index.html\">Test Page</a></body></html>";

        Document doc = Jsoup.parse(html);
        assertEquals("/test/index.html", doc.select("a[href]").attr("abs:href"));
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Document doc = Jsoup.connect(url).get();
        assertNotNull(doc);
    }

    @Test
    public void testParseFile() throws IOException {
        String charsetName = "UTF-8";
        File file = new File("path/to/your/file.html");
        Document doc = Jsoup.parse(file, charsetName);
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = this.getClass().getResourceAsStream("/path/to/your/file.html");
        Document doc = Jsoup.parse(inputStream, charsetName);
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test Body</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertEquals("<p>Test Body</p>", doc.html());
    }

    @Test
    public void testParseWithEmptyHtml() {
        Document doc = Jsoup.parse("");
        assertNull(doc);
    }

    @Test
    public void testClean() {
        String bodyHtml = "<html><head><script src=\"script.js\"></script></head><body><div id=\"testDiv\"><script>alert('Hello World')</script></div></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleTagNames("p", "div");
        Document dirty = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        Cleaner cleaner = new Cleaner(whitelist);
        Document clean = cleaner.clean(dirty);
        assertEquals("<p></p>", clean.html());
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<html><head><script src=\"script.js\"></script></head><body><div id=\"testDiv\"><script>alert('Hello World')</script></div></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleTagNames("p", "div");
        Document dirty = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        Cleaner cleaner = new Cleaner(whitelist);
        Document clean = cleaner.clean(dirty);
        clean.outputSettings(Document.OutputSettings.prettyPrint());
        assertEquals("<p></p>", clean.html());
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<html><body><p id=\"test\" class=\"test\"><a href=\"http://example.com\">Test</a></p></body></html>";
        Whitelist whitelist = Whitelist.simpleTagNames("p", "a");
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }

    @Test
    public void testIsNotValid() {   
        String bodyHtml = "<html><body><p id=\"test\" class=\"test\"><script>alert('Hello
        World')</script></p></body></html>"; 
        Whitelist whitelist = Whitelist.simpleTagNames("p");
        assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }
}