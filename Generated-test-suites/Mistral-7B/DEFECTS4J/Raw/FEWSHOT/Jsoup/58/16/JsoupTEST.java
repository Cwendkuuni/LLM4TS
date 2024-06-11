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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParse() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertEquals(1, doc.numberOfElements());
        assertEquals("h1", doc.select("h1").get(0).tagName());
        assertEquals("Hello World", doc.select("h1").text());

        html = "<html><body></body></html>";
        doc = Jsoup.parse(html);
        assertEquals(0, doc.numberOfElements());
    }

    @Test
    public void testParseWithBaseUri() throws IOException {
        String html = "<html><head><base href=\"http://example.com\"></base></html><body><a href=\"page.html\"></a></body></html>";
        Document doc = Jsoup.parse(html);
        String link = doc.select("a").get(0).absUrl();
        assertEquals("http://example.com/page.html", link);
    }

    @Test
    public void testConnect() throws IOException {
        Connection con = Jsoup.connect("https://www.google.com");
        Document doc = con.get();
        assertEquals(1, doc.numberOfElements());
        assertEquals("html", doc.tagName());
    }

    @Test
    public void testParseFile() throws IOException {
        File htmlFile = new File("test.html");
        Document doc = Jsoup.parse(htmlFile, "UTF-8");
        assertEquals(1, doc.numberOfElements());
        assertEquals("html", doc.tagName());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        assertEquals(1, doc.numberOfElements());
        assertEquals("html", doc.tagName());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertEquals(1, doc.numberOfElements());
        assertEquals("p", doc.select("p").get(0).tagName());
        assertEquals("Hello World", doc.select("p").text());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<p><a href=\"page.html\">Link</a></p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "http://example.com");
        String link = doc.select("a").get(0).absUrl();
        assertEquals("http://example.com/page.html", link);
    }

    @Test
    public void testParseUrl() throws IOException {
        Document doc = Jsoup.parse(new URL("https://www.google.com"), 3000);
        assertEquals(1, doc.numberOfElements());
        assertEquals("html", doc.tagName());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<script>alert('Hello');</script><p>Hello World</p>";
        Whitelist whitelist = Whitelist.simpleTagNames("p");
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals("p", cleanHtml.select("p").get(0).tagName());
        assertEquals("Hello World", cleanHtml.select("p").text());

        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        Element script = doc.select("script").first();
        assertNotNull(script);
        doc = new Cleaner(whitelist).clean(doc);
        assertNull(doc.select("script").first());
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<p><a href=\"javascript:alert('Hello');\">Link</a></p>";
        Whitelist whitelist = Whitelist.simpleTagNames("p");
        assertFalse(Jsoup.isValid(bodyHtml, whitelist));

        bodyHtml = "<p><a href=\"#\">Link</a></p>";
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }
}