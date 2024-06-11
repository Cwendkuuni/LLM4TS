
package org.jsoup;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document document = Jsoup.parse(html, baseUri);
        
        Assert.assertEquals("Test", document.title());
    }
    
    @Test
    public void testParseWithAlternateParser() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        
        Document document = Jsoup.parse(html, baseUri, parser);
        
        Assert.assertEquals("Test", document.title());
    }
    
    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>";

        Document document = Jsoup.parse(html);
        
        Assert.assertEquals("Test", document.title());
    }
    
    @Test
    public void testConnect() throws IOException {
        String url = "http://example.com";
        
        Connection connection = Jsoup.connect(url);
        
        Assert.assertNotNull(connection);
    }
    
    @Test
    public void testParseFileWithCharsetNameAndBaseUri() throws IOException {
        File file = new File("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = Jsoup.parse(file, charsetName, baseUri);
        
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testParseFileWithCharsetName() throws IOException {
        File file = new File("example.html");
        String charsetName = "UTF-8";
        
        Document document = Jsoup.parse(file, charsetName);
        
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testParseInputStreamWithCharsetNameAndBaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testParseInputStreamWithCharsetName() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/example.html");
        String charsetName = "UTF-8";
        
        Document document = Jsoup.parse(inputStream, charsetName);
        
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertEquals("Hello World!", document.body().text());
    }
    
    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Hello World!</h1>";
        
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        
        Assert.assertEquals("Hello World!", document.body().text());
    }
    
    @Test
    public void testParseURLWithTimeout() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 5000;
        
        Document document = Jsoup.parse(url, timeoutMillis);
        
        Assert.assertNotNull(document);
    }
    
    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<h1><script>alert('XSS')</script></h1>";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        
        Assert.assertEquals("<h1></h1>", cleanHtml);
    }
    
    @Test
    public void testCleanWithBaseUriAndWhitelist() {
        String bodyHtml = "<h1><script>alert('XSS')</script></h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        
        Assert.assertEquals("<h1></h1>", cleanHtml);
    }
    
    @Test
    public void testCleanWithBaseUriAndWhitelistOutputSettings() {
        String bodyHtml = "<h1><script>alert('XSS')</script></h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
        
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        
        Assert.assertEquals("<h1></h1>", cleanHtml);
    }
    
    @Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<h1><script>alert('XSS')</script></h1>";
        Whitelist whitelist = Whitelist.basic();
        
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        
        Assert.assertFalse(isValid);
    }
    
}
