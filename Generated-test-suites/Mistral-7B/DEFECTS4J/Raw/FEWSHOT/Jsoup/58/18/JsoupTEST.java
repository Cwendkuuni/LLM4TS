package org.jsoup;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<img src=\"image.png\" />";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        String imgSrc = doc.select("img").attr("src");
        assertEquals("http://example.com/image.png", imgSrc);
    }

    @Test
    public void testParseWithParser() {
        String html = "<xml><tag>Hello World</tag></xml>";
        Parser parser = Jsoup.parser();
        Document doc = parser.parseInput(html, "");
        assertEquals("Hello World", doc.select("tag").text());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Document doc = Jsoup.connect(url).get();
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseFile() {
        String fileName = "sample.html";
        String charsetName = "UTF-8";
        Document doc = null;
        try {
            doc = Jsoup.parse(new File(fileName), charsetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseInputStream() {
        String charsetName = "UTF-8";
        InputStream inputStream = getClass().getResourceAsStream("/sample.html");
        Document doc = Jsoup.parse(inputStream, charsetName);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseWithBaseUriAndBodyFragment() {
        String bodyHtml = "<img src=\"image.png\" />";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        String imgSrc = doc.select("img").attr("src");
        assertEquals("http://example.com/image.png", imgSrc);
    }

    @Test
    public void testConnectTimeout() {
        String url = "http://slow.example.com";
        try {
            Document doc = Jsoup.connect(url).timeout(500).get();
            fail("Should have thrown IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testClean() {
        String bodyHtml = "<html><body><script>alert('Hello World');</script></body></html>";
        String baseUri = "";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertTrue(cleanHtml.isEmpty());
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<html><body><script>alert('Hello World');</script></body></html>";
        String baseUri = "";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertTrue(cleanHtml.isEmpty());
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<html><body><script>alert('Hello World');</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }

    @Test
    public void testIsNotValid() {
        String bodyHtml = "<html><body><script>alert('Hello World');</script><iframe src=\"evil.com\"></iframe></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }
}