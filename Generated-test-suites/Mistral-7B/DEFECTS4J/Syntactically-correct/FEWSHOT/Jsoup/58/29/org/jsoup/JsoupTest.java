package org.jsoup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("Test", doc.title().text());
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><title>Test</title></head><body><p><a href=\"/about\">About</a></p></body></html>";
        Document doc = Jsoup.parse(html, "http://example.com");
        Element link = doc.selectFirst("a[href]");
        assertEquals("http://example.com/about", link.absUrl("href"));
    }

    @Test
    public void testParseWithParser() {
        String html = "<xml><data>Hello World!</data></xml>";
        Document doc = Jsoup.parse(html, new org.jsoup.parser.XmlParser());
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testParseWithEmptyHtml() {
        Document doc = Jsoup.parse("");
        assertEquals("", doc.html());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Document doc = Jsoup.connect(url).get();
        assertEquals("200", doc.statusCode());
    }

    @Test
    public void testParseFile() {
        String filePath = "test.html";
        File file = new File(filePath);
        Document doc = null;
        try {
            doc = Jsoup.parse(file, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(doc);
    }

    @Test
    public void testParseInputStream() {
        String filePath = "test.html";
        File file = new File(filePath);
        Document doc = null;
        try {
            doc = Jsoup.parse(new FileInputStream(file), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World!</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "");
        assertEquals("Hello World!", doc.body().text());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<p><script src=\"evil.js\"></script>Hello World!</p>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals("Hello World!", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<p><script src=\"evil.js\"></script>Hello World!<img id=\"x\" src=\"evil.gif\"></p>";
        Whitelist whitelist = Whitelist.simpleText();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertFalse(isValid);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<p><script src=\"evil.js\"></script>Hello World!<img id=\"x\" src=\"evil.gif\"></p>";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist, outputSettings);
        assertEquals("<p></p>", cleanHtml);
    }
}