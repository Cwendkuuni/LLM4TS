package org.jsoup.test;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
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

    @org.junit.Test
    public void testParseStringWithBaseUri() {
        String html = "<html><body><a href='/about.html'>About</a></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        assertEquals("/about.html", document.select("a[href]").attr("abs:href"));
    }

    @org.junit.Test
    public void testParseString() {
        String html = "<html><body><a href='/about.html'>About</a></body></html>";
        Document document = Jsoup.parse(html);
        assertEquals("/about.html", document.select("a[href]").attr("abs:href"));
    }

    @org.junit.Test
    public void testConnect() {
        Connection connection = Jsoup.connect("http://example.com");
        Document document = connection.get();
        assertNotNull(document);
    }

    @org.junit.Test
    public void testParseFileWithBaseUriAndCharsetName() throws IOException {
        String charsetName = "UTF-8";
        String filePath = "testFile.html";
        Document document = Jsoup.parse(new File(filePath), charsetName, "/baseUri");
        assertNotNull(document);
    }

    @org.junit.Test
    public void testParseFileWithBaseUri() throws IOException {
        String filePath = "testFile.html";
        Document document = Jsoup.parse(new File(filePath), "/baseUri");
        assertNotNull(document);
    }

    @org.junit.Test
    public void testParseInputStreamWithBaseUriAndCharsetName() throws IOException {
        String charsetName = "UTF-8";
        String filePath = "testFile.html";
        InputStream inputStream = new FileInputStream(new File(filePath));
        Document document = Jsoup.parse(inputStream, charsetName, "/baseUri");
        assertNotNull(document);
    }

    @org.junit.Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        String filePath = "testFile.html";
        InputStream inputStream = new FileInputStream(new File(filePath));
        Document document = Jsoup.parse(inputStream, "/baseUri");
        assertNotNull(document);
    }

    @org.junit.Test(expected = IOException.class)
    public void testParseStringWithInvalidBaseUri() {
        String html = "<html><body><a href='data:text/html'>About</a></body></html>";
        String baseUri = "http://example.com";
        Jsoup.parse(html, baseUri);
    }

    @org.junit.Test(expected = IOException.class)
    public void testParseStringWithNullBaseUri() {
        String html = "<html><body><a href='/about.html'>About</a></body></html>";
        Document document = Jsoup.parse(html, null);
    }

    @org.junit.Test
    public void testCleanWithBaseUriAndWhitelist() {
        String bodyHtml = "<html><body><script src='malicious.js'></script></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertTrue(cleanedHtml.isEmpty());
    }

    @org.junit.Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<html><body><script src='malicious.js'></script></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanedHtml = Jsoup.clean(bodyHtml, whitelist);
        assertTrue(cleanedHtml.isEmpty());
    }

    @org.junit.Test
    public void testCleanWithBaseUriAndWhitelistAndOutputSettings() {
        String bodyHtml = "<html><body><script src='malicious.js'></script></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.simpleText();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertEquals("<html><body></body></html>", cleanedHtml);
    }

    @org.junit.Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<html><body><script src='malicious.js'></script></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }

    @org.junit.Test
    public void testIsValidWithWhitelistAndInvalidTag() {
        String bodyHtml = "<html><body><script src='malicious.js'><div>Hello World</div></script></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }

}