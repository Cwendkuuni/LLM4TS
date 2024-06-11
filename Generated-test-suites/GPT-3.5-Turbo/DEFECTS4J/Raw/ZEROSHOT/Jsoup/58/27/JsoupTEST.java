
package org.jsoup;

import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
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
    public void testParse() {
        String html = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        Assert.assertEquals("Hello World!", document.select("h1").first().text());
    }
    
    @Test
    public void testParseWithParser() {
        String html = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = Jsoup.parse(html, baseUri, parser);
        Assert.assertEquals("Hello World!", document.select("h1").first().text());
    }
    
    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        Document document = Jsoup.parse(html);
        Assert.assertEquals("Hello World!", document.select("h1").first().text());
    }
    
    @Test
    public void testConnect() throws IOException {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        Assert.assertEquals(url, connection.request().url().toString());
    }
    
    @Test
    public void testParseFileWithCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(file, charsetName, baseUri);
        Assert.assertEquals("Hello World!", document.select("h1").first().text());
    }
    
    @Test
    public void testParseFileWithoutCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = null;
        Document document = Jsoup.parse(file, charsetName);
        Assert.assertEquals("Hello World!", document.select("h1").first().text());
    }
    
    @Test
    public void testParseInputStreamWithCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        Assert.assertEquals("Hello World!", document.select("h1").first().text());
    }
    
    @Test
    public void testParseInputStreamWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = Jsoup.parse(inputStream, charsetName, baseUri, parser);
        Assert.assertEquals("Hello World!", document.select("h1").first().text());
    }
    
    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertEquals("Hello World!", document.select("h1").first().text());
    }
    
    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertEquals("Hello World!", document.select("h1").first().text());
    }
    
    @Test
    public void testParseUrl() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 3000;
        Document document = Jsoup.parse(url, timeoutMillis);
        Assert.assertEquals("Hello World!", document.select("h1").first().text());
    }
    
    @Test
    public void testCleanWithStringBaseUriAndWhitelist() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertEquals("&lt;h1&gt;Hello World!&lt;/h1&gt;", cleanedHtml);
    }
    
    @Test
    public void testCleanWithStringBaseUriAndWhitelistAndOutputSettings() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        Assert.assertEquals("<h1>Hello World!</h1>", cleanedHtml);
    }
    
    @Test
    public void testCleanWithStringAndWhitelist() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        String cleanedHtml = Jsoup.clean(bodyHtml, whitelist);
        Assert.assertEquals("&lt;h1&gt;Hello World!&lt;/h1&gt;", cleanedHtml);
    }
    
    @Test
    public void testIsValidWithStringAndWhitelist() {
        String bodyHtml = "<h1>Hello World!</h1>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertTrue(isValid);
    }
}
