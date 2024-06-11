package org.jsoup;

import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.select("h1").text());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.select("h1").text());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.select("h1").text());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        Assert.assertNotNull(connection);
        Assert.assertEquals(url, connection.request().url().toString());
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(file, charsetName, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("Test HTML", document.select("h1").text());
    }

    @Test
    public void testParseFileWithoutCharset() throws IOException {
        File file = new File("test.html");
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(file, null, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("Test HTML", document.select("h1").text());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("Test HTML", document.select("h1").text());
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("Test HTML", document.select("h1").text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.select("h1").text());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World", document.select("h1").text());
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 5000;
        Document document = Jsoup.parse(url, timeoutMillis);
        Assert.assertNotNull(document);
        Assert.assertEquals("Example Domain", document.select("title").text());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<h1>Hello <b>World</b></h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertNotNull(cleanHtml);
        Assert.assertEquals("<h1>Hello World</h1>", cleanHtml);
    }

    @Test
    public void testCleanWithoutBaseUri() {
        String bodyHtml = "<h1>Hello <b>World</b></h1>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        Assert.assertNotNull(cleanHtml);
        Assert.assertEquals("<h1>Hello World</h1>", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<h1>Hello <b>World</b></h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        Assert.assertNotNull(cleanHtml);
        Assert.assertEquals("<h1>\n Hello World\n</h1>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<h1>Hello <b>World</b></h1>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertTrue(isValid);
    }
}