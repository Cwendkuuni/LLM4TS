package org.jsoup.test;

import static org.junit.Assert.*;

import org.জsoup.Jsoup;
import org.জsoup.nodes.Document;
import org.জsoup.nodes.Element;
import org.জsoup.parser.Parser;
import org.জsoup.safety.Whitelist;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><base href=\"http://example.com\"></base></head><body><a href=\"about.html\"></a></body></html>";
        Document doc = Jsoup.parse(html, "http://example.com");
        assertEquals("about.html", doc.select("a[href]").first().attr("href"));
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head></head><body><a href=\"about.html\"></a></body></html>";
        Document doc = Jsoup.parse(html);
        assertEquals("about.html", doc.select("a[href]").first().attr("href"));
    }

    @Test
    public void testConnectAndGet() {
        try {
            Document doc = Jsoup.connect("http://example.com").get();
            assertEquals("http://example.com", doc.location().toString());
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectNullUrl() {
        Jsoup.connect(null);
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8");
        assertEquals("test.html", doc.title());
    }

    @Test(expected = FileNotFoundException.class)
    public void testParseNonExistentFile() throws IOException {
        File file = new File("nonexistentfile.html");
        Jsoup.parse(file, "UTF-8");
    }

    @Test
    public void testParseInputStream() throws IOException {
        URL url = new URL("http://example.com/test.html");
        InputStream in = url.openStream();
        Document doc = Jsoup.parse(in, "UTF-8");
        assertEquals("test.html", doc.title());
        in.close();
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamIOException() throws IOException {
        URL url = new URL("http://example.com/test-invalid.html");
        InputStream in = url.openStream();
        Jsoup.parse(in, "UTF-8");
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test body fragment</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        Element p = doc.select("p").first();
        assertEquals("Test body fragment", p.text());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<script src=\"evil.js\"></script><p>Test body fragment</p>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals("Test body fragment", cleanHtml);
    }

    @Test
    public void testCleanWithBaseUri() {
        String bodyHtml = "<img src=\"images/image.jpg\" alt=\"Image\">";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, Whitelist.extendDefaults("img"));
        assertTrue(cleanHtml.contains("<p>"));
        assertTrue(cleanHtml.contains("<img src=\"images/image.jpg\" alt=\"Image\"></p>"));
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<p><img src=\"images/image.jpg\" alt=\"Image\"></p>";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setPrettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist, "", outputSettings);
        assertTrue(cleanHtml.contains("<p>"));
        assertTrue(cleanHtml.contains("<img src=\"images/image.jpg\" alt=\"Image\"></p>"));
        assertTrue(cleanHtml.contains("\n"));
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<p><script src=\"script.js\"></script></p>";
        Whitelist whitelist = Whitelist.simpleText();
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }

    @Test
    public void testIsValidWithInvalidScriptTag() {
        String bodyHtml = "<p><script src=\"script.js\"><!-- script --></script></p>";
        Whitelist whitelist = Whitelist.simpleText();
        assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }
}