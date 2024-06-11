package org.jsoup.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParseStringWithBaseUri() {
        String html = "<html><head><base href='https://example.com'></base></head><body><a href='/about'>About</a></body></html>";
        Document doc = Jsoup.parse(html, "https://example.com");
        Assert.assertEquals("About", doc.select("a[href]").text());
    }

    @Test
    public void testParseString() {
        String html = "<html><head></head><body><a href='/about'>About</a></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertEquals("About", doc.select("a[href]").text());
    }

    @Test
    public void testParseFileWithBaseUriAndCharset() throws IOException {
        String fileName = "test_file.html";
        File file = new File(getClass().getResource(fileName).getFile());
        Document doc = Jsoup.parse(file, "UTF-8", "https://example.com");
        Assert.assertEquals("Test File", doc.title());
    }

    @Test
    public void testParseFileWithBaseUri() throws IOException {
        String fileName = "test_file.html";
        File file = new File(getClass().getResource(fileName).getFile());
        Document doc = Jsoup.parse(file, "https://example.com");
        Assert.assertEquals("Test File", doc.title());
    }

    @Test
    public void testParseInputStreamWithBaseUriAndCharset() throws IOException {
        String testData = "Test Input Stream Data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        Document doc = Jsoup.parse(inputStream, "UTF-8", "https://example.com");
        Assert.assertEquals(testData, doc.text());
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        String testData = "Test Input Stream Data";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document doc = Jsoup.parse(inputStream, "https://example.com");
        Assert.assertEquals(testData, doc.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test Heading</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertEquals("Test Heading", doc.title());
    }

    @Test(expected = MalformedURLException.class)
    public void testParseUrlWithInvalidUrl() {
        Jsoup.parse("test://example.com", 5000);
    }

    @Test(expected = HttpStatusException.class)
    public void testParseUrlWithNon200Response() throws IOException {
        Connection con = Jsoup.connect("http://example.com/error-page");
        Document doc = con.get();
        Assert.assertEquals(200, doc.statusCode());

        con.response().setStatusCode(500);
        doc = con.get();
        // The test should fail here
    }

    @Test(expected = UnsupportedMimeTypeException.class)
    public void testParseUrlWithUnsupportedMimeType() throws IOException {
        Connection con = Jsoup.connect("http://example.com/image.png");
        Document doc = con.get();
        Assert.assertEquals(200, doc.statusCode());

        con.response().setContentType("text/plain");
        doc = con.get();
        // The test should fail here
    }

    @Test(expected = SocketTimeoutException.class)
    public void testParseUrlWithTimeout() throws IOException {
        Connection con = Jsoup.connect("http://example.com/timeout");
        con.timeout(100);
        try {
            Document doc = con.get();
            // The test should fail here
        } catch (SocketTimeoutException e) {
            // Expected exception
        }
    }

    @Test
    public void testCleanWithBaseUriAndWhitelist() {
        String bodyHtml = "<html><body><script src='malicious.js'></script></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(bodyHtml, "https://example.com", whitelist);
        Assert.assertTrue(cleanHtml.isEmpty());
    }

    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<html><body><script src='malicious.js'></script></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        Assert.assertTrue(cleanHtml.isEmpty());
    }

    @Test
    public void testCleanWithBaseUriWhitelistAndOutputSettings() {
        String bodyHtml = "<html><body><script src='malicious.js'></script></body></html>";
        String baseUri = "https://example.com";
        Whitelist whitelist = Whitelist.relaxed();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true).escapeMode(Document.EscapeMode.html);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        Assert.assertEquals("<html><body></body></html>", cleanHtml);
    }

    @Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<html><body><script src='malicious.js'></script></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertFalse(isValid);
    }
}