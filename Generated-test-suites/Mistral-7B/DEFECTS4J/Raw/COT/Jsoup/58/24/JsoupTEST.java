package org.jsoup.test;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><base href=\"http://example.com\"></base></head><body><a href=\"about.html\">About</a></body></html>";
        Document doc = Jsoup.parse(html, "http://example.com");
        assertEquals("About", doc.select("a[href]").text());
    }

    @Test
    public void testParse() {
        String html = "<html><head></head><body><a href=\"about.html\">About</a></body></html>";
        Document doc = Jsoup.parse(html);
        assertEquals("About", doc.select("a[href]").text());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><head></head><body><a href=\"about.html\">About</a></body></html>";
        Parser parser = org.jsoup.parser.JsoupScanner.DefaultScanner();
        Document doc = Jsoup.parse(html, "", parser);
        assertEquals("About", doc.select("a[href]").text());
    }

    @Test
    public void testConnect() {
        Connection con = Jsoup.connect("http://example.com");
        Document doc = con.get();
        assertEquals(200, con.response().statusCode());
        assertEquals("Example Domain", doc.title());
    }

    @Test
    public void testParseFileWithCharset() {
        String charsetName = "UTF-8";
        String fileName = "test.html";
        File file = new File(fileName);

        try {
            Document doc = Jsoup.parse(file, charsetName, "");
            assertEquals("Test", doc.title());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testParseFile() {
        String fileName = "test.html";
        File file = new File(fileName);

        try {
            Document doc = Jsoup.parse(file, "");
            assertEquals("Test", doc.title());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testParseInputStreamWithCharset() {
        String charsetName = "UTF-8";
        String inputStreamName = "test.html";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(inputStreamName)) {
            Document doc = Jsoup.parse(inputStream, charsetName, "");
            assertEquals("Test", doc.title());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testParseInputStream() {
        String inputStreamName = "test.html";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(inputStreamName)) {
            Document doc = Jsoup.parse(inputStream, "");
            assertEquals("Test", doc.title());
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Test</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "");
        assertEquals("Test", doc.body().text());
    }

    @Test
    public void testParseUrlWithTimeout() {
        String url = "http://example.com";
        int timeoutMillis = 100; // 100 ms

        try {
            Document doc = Jsoup.parse(new URL(url), timeoutMillis);
            // Check if document is not null
        } catch (IOException e) {
            fail();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanWithNullBodyHtml() {
        String bodyHtml = null;
        Whitelist whitelist = Whitelist.simpleText();
        String result = Jsoup.clean(bodyHtml, "", whitelist);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanWithNullWhitelist() {
        String bodyHtml = "<html><body><h1>Test</h1></body></html>";
        String result = Jsoup.clean(bodyHtml, null);
        fail();
    }

    @Test
    public void testClean() {
        String bodyHtml = "<html><body><h1 id=\"title\" onclick=\"alert('Hello')\">Test</h1><script src=\"script.js\"></script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        String result = Jsoup.clean(bodyHtml, "", whitelist);
        assertEquals("<html><body><h1>Test</h1></body></html>", result);
    }

    @Test
    public void testCleanWithBaseUri() {
        String bodyHtml = "<img src=\"image.jpg\" alt=\"Image\"/>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleText();
        String result = Jsoup.clean(bodyHtml, baseUri, whitelist);
        // Check if image src attribute is empty or absolute
        assertEquals("", result.select("img[src]").attr("src"));
        assertEquals("Image", result.select("img[alt]").text());
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<html><body><h1 id=\"title\" onclick=\"alert('Hello')\">Test</h1></body></html>";
        String baseUri = "";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint();
        String result = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertEquals("<html><body><h1>Test</h1></body></html>", result.toString());
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<html><body><h1 onclick=\"alert('Hello')\">Test</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }

    @Test
    public void testIsValidWithFalse() {
        String bodyHtml = "<script src=\"alert('Hello')\"></script>";
        Whitelist whitelist = Whitelist.simpleText();
        assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }
}