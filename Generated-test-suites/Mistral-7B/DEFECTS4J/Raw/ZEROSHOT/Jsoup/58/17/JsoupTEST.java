package org.jsoup.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.Document;
import org.jsoup.HttpConnection;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Test;

public class JsoupTest {

    @Test
    public void testParseString() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        assertNotNull(doc);
    }

    @Test
    public void testParseStringWithBaseUri() {
        String html = "<img src=\"img.jpg\" />";
        String baseUri = "http://example.com/";
        Document doc = Jsoup.parse(html, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseStringWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = Parser.xmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseEmptyString() {
        Document doc = Jsoup.parse("");
        assertNotNull(doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullString() {
        Jsoup.parse(null);
    }

    @Test
    public void testConnect() {
        Connection connection = Jsoup.connect("http://example.com");
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectNullUrl() {
        Jsoup.connect(null);
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "UTF-8");
        assertNotNull(doc);
    }

    @Test(expected = FileNotFoundException.class)
    public void testParseFileNotFound() throws IOException {
        File file = new File("non-existent-file.html");
        Document doc = Jsoup.parse(file, "UTF-8");
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamNotFound() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/non-existent-file.html");
        Document doc = Jsoup.parse(inputStream, "UTF-8");
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<img src=\"img.jpg\" />";
        String baseUri = "http://example.com/";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseBodyFragmentEmptyString() {
        Document doc = Jsoup.parseBodyFragment("");
        assertNotNull(doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBodyFragmentNullString() {
        Jsoup.parseBodyFragment(null);
    }

    @Test
    public void testParseUrl() throws IOException {
        Document doc = Jsoup.parse(new URL("http://example.com"), 5000);
        assertNotNull(doc);
    }

    @Test(expected = MalformedURLException.class)
    public void testParseUrlMalformedUrl() {
        Document doc = Jsoup.parse(new URL("non-existent-protocol://example.com"), 5000);
    }

    @Test(expected = IOException.class)
    public void testParseUrlTimeout() throws IOException {
        Document doc = Jsoup.parse(new URL("http://example.com"), 50);
    }

    @Test
    public void testClean() {
        String html = "<script src='script.js'></script><p>Hello World</p>";
        String baseUri = "http://example.com/";
        Whitelist whitelist = Whitelist.relaxed();
        String safeHtml = Jsoup.clean(html, baseUri, whitelist);
        assertNotNull(safeHtml);
        assertThrows(IllegalArgumentException.class, () -> Jsoup.clean(null, baseUri, whitelist));
    }

    @Test
    public void testCleanWithWhitelist() {
        String html = "<script src='script.js'></script><p>Hello World</p>";
        Whitelist whitelist = Whitelist.simpleTagNames("p");
        String safeHtml = Jsoup.clean(html, whitelist);
        assertNotNull(safeHtml);
        assertThrows(IllegalArgumentException.class, () -> Jsoup.clean(null, whitelist));
    }

    @Test
    public void testIsValid() {
        String html = "<script src='script.js'></script><p>Hello World</p>";
        Whitelist whitelist = Whitelist.simpleTagNames("p");
        boolean isValid = Jsoup.isValid(html, whitelist);
        assertFalse(isValid);
    }

    @Test
    public void testIsValidWithWhitelist() {
        String html = "<p>Hello World</p>";
        Whitelist whitelist = Whitelist.simpleTagNames("p");
        boolean isValid = Jsoup.isValid(html, whitelist);
        assertTrue(isValid);
    }

}