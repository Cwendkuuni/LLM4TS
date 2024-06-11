package org.jsoup.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.Document;
import org.jsoup.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.HttpConnection;

import org.junit.Test;

public class JsoupTest {

    @Test
    public void testParseHtmlString() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertNotNull(doc.title());
        assertNotNull(doc.body());
        assertNotNull(doc.head());
    }

    @Test
    public void testParseHtmlStringWithBaseUri() {
        String html = "<html><body><img src=\"image.jpg\"></body></html>";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        assertNotNull(doc);
        assertNotNull(doc.select("img[src]").first().absUrl("src"));
    }

    @Test
    public void testParseHtmlStringWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        assertNotNull(doc);
        assertNotNull(doc.title());
        assertNotNull(doc.body());
        assertNotNull(doc.head());
    }

    @Test
    public void testParseHtmlStringWithEmptyBaseUri() {
        String html = "<html><body><img src=\"image.jpg\"></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
        assertThrows(IllegalArgumentException.class, () -> doc.select("img[src]").first().absUrl("src"));
    }

    @Test
    public void testConnectUrl() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        Document doc = connection.get();
        assertNotNull(doc);
        assertNotNull(doc.title());
    }

    @Test
    public void testParseFile() throws IOException {
        String fileName = "test.html";
        File file = new File(fileName);
        Document doc = Jsoup.parse(file, "UTF-8");
        assertNotNull(doc);
        assertNotNull(doc.title());
    }

    @Test
    public void testParseFileWithBaseUri() throws IOException {
        String fileName = "test.html";
        File file = new File(fileName);
        Document doc = Jsoup.parse(file, "UTF-8", "http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.select("img[src]").first().absUrl("src"));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new java.io.ByteArrayInputStream(html.getBytes("UTF-8"));
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        assertNotNull(doc);
        assertNotNull(doc.title());
        assertNotNull(doc.body());
        assertNotNull(doc.head());
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        String html = "<html><body><img src=\"image.jpg\"></body></html>";
        InputStream inputStream = new java.io.ByteArrayInputStream(html.getBytes("UTF-8"));
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(inputStream, "UTF-8", baseUri);
        assertNotNull(doc);
        assertNotNull(doc.select("img[src]").first().absUrl("src"));
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
        assertNotNull(doc.title());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.title());
    }

    @Test
    public void testParseUrl() throws IOException {
        String url = "http://example.com";
        Document doc = Jsoup.parse(url);
        assertNotNull(doc);
        assertNotNull(doc.title());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<script src=\"malicious.js\"></script><h1>Hello World</h1>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);
        assertNotNull(cleanHtml);
        assertNotNull(cleanHtml.contains("<h1>Hello World</h1>"));
        assertNull(cleanHtml.contains("<script>"));
    }

    @Test
    public void testCleanWithBaseUri() {
        String bodyHtml = "<script src=\"malicious.js\"></script><h1>Hello World</h1>";
        Whitelist whitelist = Whitelist.relaxed();
        String baseUri = "http://example.com";
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertNotNull(cleanHtml);
        assertNotNull(cleanHtml.contains("<h1>Hello World</h1>"));
        assertNull(cleanHtml.contains("<script>"));
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<script src=\"malicious.js\"></script><h1>Hello World</h1>";
        Whitelist whitelist = Whitelist.relaxed();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertFalse(isValid);
    }

    @Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<input type=\"text\" name=\"username\">";
        Whitelist whitelist = new Whitelist().addTags("input").addAttributes("input", "type", "name");
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        assertTrue(isValid);
    }
}