package org.jsoup;

import org.jsoup.helper.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void parseTest() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World!", document.body().select("h1").text());
    }

    @Test
    public void parseTestWithParser() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document document = Jsoup.parse(html, baseUri, parser);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World!", document.body().select("h1").text());
    }

    @Test
    public void parseWithoutBaseUriTest() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        Document document = Jsoup.parse(html);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World!", document.body().select("h1").text());
    }

    @Test
    public void connectTest() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        
        Assert.assertNotNull(connection);
        Assert.assertEquals(url, connection.request().url().toString());
    }

    @Test
    public void parseFileTest() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = Jsoup.parse(file, charsetName, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World!", document.body().select("h1").text());
    }

    @Test
    public void parseFileWithoutCharsetTest() throws IOException {
        File file = new File("test.html");
        String charsetName = null;
        
        Document document = Jsoup.parse(file, charsetName);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World!", document.body().select("h1").text());
    }

    @Test
    public void parseInputStreamTest() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World!", document.body().select("h1").text());
    }

    @Test
    public void parseInputStreamWithParserTest() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        
        Document document = Jsoup.parse(inputStream, charsetName, baseUri, parser);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World!", document.body().select("h1").text());
    }

    @Test
    public void parseBodyFragmentTest() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World!", document.body().select("h1").text());
    }

    @Test
    public void parseBodyFragmentWithoutBaseUriTest() {
        String bodyHtml = "<h1>Hello World!</h1>";
        
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World!", document.body().select("h1").text());
    }

    @Test
    public void parseURLTest() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 5000;
        
        Document document = Jsoup.parse(url, timeoutMillis);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("Hello World!", document.body().select("h1").text());
    }

    @Test
    public void cleanTest() {
        String bodyHtml = "<script>alert('XSS')</script><p>Hello <b>World</b>!</p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        
        Assert.assertEquals("<p>Hello <b>World</b>!</p>", cleanHtml);
    }

    @Test
    public void cleanTestWithoutBaseUri() {
        String bodyHtml = "<script>alert('XSS')</script><p>Hello <b>World</b>!</p>";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        
        Assert.assertEquals("<p>Hello <b>World</b>!</p>", cleanHtml);
    }

    @Test
    public void cleanTestWithOutputSettings() {
        String bodyHtml = "<script>alert('XSS')</script><p>Hello <b>World</b>!</p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        
        Assert.assertEquals("<p>Hello <b>World</b>!</p>", cleanHtml);
    }

    @Test
    public void isValidTest() {
        String bodyHtml = "<p>Hello <b>World</b>!</p>";
        Whitelist whitelist = Whitelist.basic();
        
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        
        Assert.assertTrue(isValid);
    }
}