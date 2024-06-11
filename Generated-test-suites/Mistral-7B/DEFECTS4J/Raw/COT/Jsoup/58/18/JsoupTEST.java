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
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParseStringWithBaseUri() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseString() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseFileWithCharsetNameAndBaseUri() throws IOException {
        String filePath = "path/to/your/file.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(new File(filePath), charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseFileWithBaseUri() throws IOException {
        String filePath = "path/to/your/file.html";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(new File(filePath), baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithCharsetNameAndBaseUri() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = this.getClass().getResourceAsStream("/yourfile.html");
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        String baseUri = "http://example.com";
        InputStream inputStream = this.getClass().getResourceAsStream("/yourfile.html");
        Document document = Jsoup.parse(inputStream, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseBodyFragmentEmpty() {
        Document document = Jsoup.parseBodyFragment("");
        Assert.assertNotNull(document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringNull() {
        String html = null;
        Jsoup.parse(html);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFileNull() {
        File file = null;
        Jsoup.parse(file);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputStreamNull() {
        InputStream inputStream = null;
        Jsoup.parse(inputStream);
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        Assert.assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectNull() {
        String url = null;
        Jsoup.connect(url);
    }

    @Test
    public void testClean() {
        String bodyHtml = "<script src=\"malicious.js\"></script><h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertNotNull(cleanHtml);
        Assert.assertEquals("<h1>Hello World</h1>", cleanHtml);
    }

    @Test
    public void testCleanEmpty() {
        String bodyHtml = "";
        String baseUri = "";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertNotNull(cleanHtml);
        Assert.assertEquals("", cleanHtml);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanNull() {
        String bodyHtml = null;
        String baseUri = "";
        Whitelist whitelist = Whitelist.relaxed();
        Jsoup.clean(bodyHtml, baseUri, whitelist);
    }

    @Test
    public void testCleanWithInvalidWhitelist() {
        String bodyHtml = "<html><body><script src=\"malicious.js\"></script></body></html>";
        String baseUri = "";
        Whitelist whitelist = new Whitelist(); // An empty whitelist is not valid
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertNull(cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<html><body><script src=\"malicious.js\"></script></body></html>";
        Whitelist whitelist = Whitelist.relaxed();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValidWithValidInput() {
        String bodyHtml = "<html><body><h1>Hello World</h1></body></html>";
        Whitelist whitelist = Whitelist.simpleTagNames("h1");
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertTrue(isValid);
    }
}