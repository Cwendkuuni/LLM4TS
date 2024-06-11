package org.jsoup;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
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
        String html = "<html><base href=\"/test/\"><body><a href=\"about.html\">About</a></body></html>";
        Document doc = Jsoup.parse(html, "http://example.com/test/");
        assertEquals("about.html", doc.select("a[href]").attr("href"));
    }

    @Test
    public void testConnect() throws Exception {
        Document doc = Jsoup.connect("https://www.google.com").get();
        assertEquals("Google", doc.title());
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8");
        assertEquals("Test", doc.title());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream in = this.getClass().getResourceAsStream("/test.html");
        Document doc = Jsoup.parse(in, "UTF-8");
        assertEquals("Test", doc.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertEquals("Hello World", doc.title());
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        assertEquals("Hello World", doc.select("h1").text());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<html><body><script>alert('Test');</script><h1 id='test'>Test</h1></body></html>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "");
        String cleanHtml = Jsoup.clean(bodyHtml, Whitelist.simpleText());
        assertEquals(doc.body().html(), cleanHtml);
    }

    @Test
    public void testCleanWithBaseUri() {
        String bodyHtml = "<img src='image.jpg' alt='Test'>";
        String baseUri = "http://example.com/";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        String cleanHtml = Jsoup.clean(bodyHtml, Whitelist.simpleText(), baseUri);
        assertEquals(doc.body().html(), cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<html><body><script>alert('Test');</script><h1 id='test'>Test</h1></body></html>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "");
        String cleanHtml = Jsoup.clean(bodyHtml, Whitelist.simpleText(), null, Document.OutputSettings.prettyPrint());
        assertEquals(doc.body().html(), cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<html><body><script>alert('Test');</script><h1 id='test'>Test</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }

    @Test
    public void testIsNotValid() {
        String bodyHtml = "<html><body><script>alert('Test');</script></body></html>";
        Whitelist whitelist = Whitelist.simpleText();
        assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }

    @Test
    public void testConnectWithData() throws Exception {
        Document doc = Jsoup.connect("https://www.google.com").data("name", "John").userAgent("Mozilla").get();
        assertEquals("John", doc.select("input[name=name]").attr("value"));
    }

    @Test
    public void testConnectWithCookies() throws Exception {
        Document doc = Jsoup.connect("https://www.google.com").cookie("auth", "token").get();
        assertEquals("token", doc.cookie("auth"));
    }

    @Test
    public void testConnectWithHeaders() throws Exception {
        Document doc = Jsoup.connect("https://www.google.com").header("User-Agent", "Mozilla").get();
        assertEquals("Mozilla", doc.header("User-Agent"));
    }

    @Test
    public void testConnectWithMethod() throws Exception {
        Document doc = Jsoup.connect("https://www.google.com").method(HttpConnection.ConnectionMethod.POST).get();
        assertEquals(HttpConnection.ConnectionMethod.GET, doc.connection().method());
    }

    @Test
    public void testConnectWithTimeout() throws Exception {
        Document doc = Jsoup.connect("https://www.google.com").timeout(500).get();
        fail(); //Connection should time out
    }

    @Test
    public void testConnectWithExceptionThrown() throws Exception {
        Connection con = Jsoup.connect("https://www.google.com/nonexistent");
        try {
            con.get();
            fail();
        } catch (IOException e) {
            //expected
        }
    }

    @Test
    public void testConnectWithMalformedUrl() throws Exception {
        try {
            Jsoup.connect("file://test.html");
            fail();
        } catch (MalformedURLException e) {
            //expected
        }
    }

    @Test
    public void testConnectWithHttpStatusException() throws Exception {
        try {
            Jsoup.connect("https://www.google.com/status_code_404").get();
            fail();
        } catch (HttpStatusException e) {
            //expected
        }
    }

    @Test
    public void testConnectWithUnsupportedMimeTypeException() throws Exception {
        try {
            Jsoup.connect("https://www.google.com/mime_type_application_xml").get();
            fail();
        } catch (UnsupportedMimeTypeException e) {
            //expected
        }
    }
}