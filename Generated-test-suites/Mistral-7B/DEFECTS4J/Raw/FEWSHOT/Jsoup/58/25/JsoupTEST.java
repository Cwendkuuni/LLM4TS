package org.jsoup;

import static org.junit.Assert.*;
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
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertEquals("Hello World", doc.title());
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><body><a href=\"/about\">About</a></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertEquals("About", doc.select("a[href]").text());
        assertEquals("http://example.com/about", doc.select("a[href]").attr("abs:href"));
    }

    @Test
    public void testParseWithParser() {
        String html = "<xml><data>Hello World</data></xml>";
        Parser parser = org.jsoup.parser.XmlParser.xmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        assertEquals("Hello World", doc.select("data").text());
    }

    @Test
    public void testConnect() throws Exception {
        String url = "http://example.com";
        Document doc = Jsoup.connect(url).get();
        assertEquals(200, doc.statusCode());
        assertEquals("Example Domain", doc.title());
    }

    @Test
    public void testParseFromFile() throws IOException {
        File file = new File("input.html");
        Document doc = Jsoup.parse(file, "UTF-8");
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseFromInputStream() throws IOException {
        File file = new File("input.html");
        InputStream inputStream = new FileInputStream(file);
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String html = "<p>Hello World</p>";
        Document doc = Jsoup.parseBodyFragment(html);
        assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String html = "<p><a href=\"/about\">About</a></p>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parseBodyFragment(html, baseUri);
        assertEquals("About", doc.select("a[href]").text());
        assertEquals("http://example.com/about", doc.select("a[href]").attr("abs:href"));
    }

    @Test
    public void testParseFromURL() throws Exception {
        String url = "http://example.com";
        Document doc = Jsoup.parse(url);
        assertEquals(200, doc.statusCode());
        assertEquals("Example Domain", doc.title());
    }

    @Test
    public void testClean() {
        String html = "<p><script>alert('XSS');</script>Hello World</p>";
        Whitelist whitelist = Whitelist.simpleTags();
        String cleanHtml = Jsoup.clean(html, "", whitelist);
        assertEquals("Hello World", cleanHtml);
    }

    @Test
    public void testCleanWithBaseUri() {
        String html = "<p><img src=\"/images/image.jpg\" alt=\"Image\">Hello World</p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleTags();
        String cleanHtml = Jsoup.clean(html, baseUri, whitelist);
        assertEquals("<p><img src=\"http://example.com/images/image.jpg\" alt=\"Image\">Hello World</p>", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String html = "<p><script>alert('XSS');</script>Hello World</p>";
        Whitelist whitelist = Whitelist.simpleTags();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = Jsoup.clean(html, "", whitelist, outputSettings);
        assertTrue(cleanHtml.contains("<p>"));
        assertTrue(cleanHtml.contains("</p>"));
        assertFalse(cleanHtml.contains("<script>"));
    }

    @Test
    public void testIsValid() {
        String html = "<p><script>alert('XSS');</script>Hello World</p>";
        Whitelist whitelist = Whitelist.simpleTags();
        assertFalse(Jsoup.isValid(html, whitelist));
    }
}