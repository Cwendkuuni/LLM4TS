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
    public void testParseString() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseStringWithBaseUri() {
        String html = "<img src='image.jpg' />";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(html, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.select("img[src]").first().absUrl("src"));
        Assert.assertEquals("http://example.com/image.jpg", doc.select("img[src]").first().absUrl("src"));
    }

    @Test
    public void testParseStringWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = org.jsoup.parser.Jsoup.parser();
        Document doc = Jsoup.parse(html, "", parser);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseEmptyString() {
        Document doc = Jsoup.parse("");
        Assert.assertNotNull(doc);
        Assert.assertFalse(doc.body().text().isEmpty());
    }

    @Test(expected = IOException.class)
    public void testParseFileWithInvalidCharsetName() throws IOException {
        File file = new File("invalid_charset.html");
        Jsoup.parse(file, "invalid_charset", "");
    }

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseFileWithBaseUri() throws IOException {
        File file = new File("test.html");
        Document doc = Jsoup.parse(file, "", "");
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.baseUri());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        InputStream in = new java.io.ByteArrayInputStream(html.getBytes());
        Document doc = Jsoup.parse(in, "", "");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseInputStreamWithBaseUri() throws IOException {
        String html = "<img src='image.jpg' />";
        InputStream in = new java.io.ByteArrayInputStream(html.getBytes());
        String baseUri = "http://example.com";
        Document doc = Jsoup.parse(in, baseUri, "");
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.select("img[src]").first().absUrl("src"));
        Assert.assertEquals("http://example.com/image.jpg", doc.select("img[src]").first().absUrl("src"));
    }

    @Test
    public void testConnect() {
        Connection con = Jsoup.connect("http://example.com");
        Assert.assertNotNull(con);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>This is a test</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, "");
        Assert.assertNotNull(doc);
        Assert.assertEquals(bodyHtml, doc.body().html());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<img src='image.jpg' />";
        String baseUri = "http://example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.select("img[src]").first().absUrl("src"));
        Assert.assertEquals("http://example.com/image.jpg", doc.select("img[src]").first().absUrl("src"));
    }

    @Test
    public void testParseUrl() throws IOException {
        URL url = new URL("http://example.com");
        Document doc = Jsoup.parse(url, 5000);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testClean() {
        String bodyHtml = "<html><body><script src='script.js'></script><p>This is a test</p></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = new Whitelist();
        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertNotNull(cleanedHtml);
        Assert.assertEquals("<p>This is a test</p>", cleanedHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<html><body><script src='script.js'></script><p>This is a test</p></body></html>";
        String baseUri = "http://example.com";
        Whitelist whitelist = new Whitelist();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        Assert.assertNotNull(cleanedHtml);
        Assert.assertTrue(cleanedHtml.contains("\n"));
        Assert.assertEquals("<p>This is a test</p>\n", cleanedHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<html><body><script src='script.js'></script><p>This is a test</p></body></html>";
        Whitelist whitelist = new Whitelist();
        Assert.assertFalse(Jsoup.isValid(bodyHtml, whitelist));
    }

    @Test
    public void testIsValidWithAllowedTag() {
        String bodyHtml = "<html><body><script src='script.js'><p>This is a test</p></script></body></html>";
        Whitelist whitelist = new Whitelist().addTags("script");
        Assert.assertTrue(Jsoup.isValid(bodyHtml, whitelist));
    }
}