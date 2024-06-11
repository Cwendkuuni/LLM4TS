package org.jsoup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
    public void testParse() {
        String html = "<html><body><h1>Hello world</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertEquals("Hello world", doc.select("h1").text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><base href='/test'></head><body><a href='about.html'></a></body></html>";
        Document doc = Jsoup.parse(html, "http://example.com/test");
        assertEquals("about.html", doc.select("a[href]").attr("href"));
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Document doc = Jsoup.connect(url).get();
        assertNotNull(doc);
        assertEquals("", doc.title());
    }

    @Test
    public void testParseFromFile() {
        String charsetName = "UTF-8";
        String fileName = "test.html";
        File file = new File(fileName);
        Document doc = null;
        try {
            doc = Jsoup.parse(file, charsetName, "");
            assertNotNull(doc);
            assertEquals("", doc.title());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.delete();
        }
    }

    @Test
    public void testParseFromInputStream() {
        String charsetName = "UTF-8";
        String fileName = "test.html";
        File file = new File(fileName);
        try (InputStream inputStream = new FileInputStream(file)) {
            Document doc = Jsoup.parse(inputStream, charsetName, "");
            assertNotNull(doc);
            assertEquals("", doc.title());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.delete();
        }
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello world</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertEquals("Hello world", doc.select("h1").text());
    }

    @Test
    public void testParseWithAlternateParser() {
        String html = "<html><body><h1>Hello world</h1></body></html>";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        assertNotNull(doc);
        assertEquals("", doc.title());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<html><body><script src='script.js'></script><script>alert('XSS')</script></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);
        assertFalse(cleanHtml.contains("<script>"));
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<html><body><script src='script.js'></script><script>alert('XSS')</script></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }
}