
package org.jsoup;

import static org.junit.Assert.*;
import org.junit.Test;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello, World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = Jsoup.parse(html, baseUri, parser);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello, World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("Test", doc.title());
        assertEquals("Hello, World!", doc.body().select("h1").text());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection con = Jsoup.connect(url);
        assertNotNull(con);
    }

    @Test
    public void testParseFileWithCharsetName() {
        File in = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = Jsoup.parse(in, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseFileWithoutCharsetName() {
        File in = new File("test.html");
        String charsetName = null;
        try {
            Document doc = Jsoup.parse(in, charsetName);
            assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseInputStreamWithCharsetName() {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document doc = Jsoup.parse(in, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseInputStreamWithoutCharsetName() {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = null;
        String baseUri = "http://example.com";
        try {
            Document doc = Jsoup.parse(in, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Hello, World!</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertEquals("Hello, World!", doc.body().select("h1").text());
    }

    @Test
    public void testParseURLWithTimeout() {
        String url = "http://example.com";
        int timeoutMillis = 5000;
        try {
            Document doc = Jsoup.parse(new URL(url), timeoutMillis);
            assertNotNull(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<script>alert('XSS')</script><p>Hello, <b>World!</b></p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertEquals("<p>Hello, <b>World!</b></p>", cleanHtml);
    }

    @Test
    public void testCleanWithWhitelistWithoutBaseUri() {
        String bodyHtml = "<script>alert('XSS')</script><p>Hello, <b>World!</b></p>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertEquals("<p>Hello, <b>World!</b></p>", cleanHtml);
    }

    @Test
    public void testCleanWithWhitelistAndOutputSettings() {
        String bodyHtml = "<script>alert('XSS')</script><p>Hello, <b>World!</b></p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertEquals("<p>Hello, <b>World!</b></p>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<h1>Hello, <b>World!</b></h1>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(isValid);
    }
}
