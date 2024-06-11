
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
    public void testParseWithBaseUri() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.outerHtml());
    }
  
    @Test
    public void testParseWithBaseUriAndParser() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.outerHtml());
    }
  
    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head></head><body><h1>Test</h1></body></html>";
        Document document = Jsoup.parse(html);
        Assert.assertNotNull(document);
        Assert.assertEquals(html, document.outerHtml());
    }
  
    @Test
    public void testConnect() {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url).userAgent("Mozilla").data("name", "jsoup");
        Assert.assertNotNull(connection);
        Document document = connection.get();
        Assert.assertNotNull(document);
    }
  
    @Test
    public void testParseFileWithCharsetNameAndBaseUri() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "file:///path/to/file.html";
        File file = new File("file.html");
        Document document = Jsoup.parse(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }
  
    @Test
    public void testParseFileWithCharsetName() throws IOException {
        String charsetName = "UTF-8";
        File file = new File("file.html");
        Document document = Jsoup.parse(file, charsetName);
        Assert.assertNotNull(document);
    }
  
    @Test
    public void testParseInputStreamWithCharsetNameAndBaseUri() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new FileInputStream("file.html");
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }
  
    @Test
    public void testParseInputStreamWithCharsetNameAndBaseUriAndParser() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = new FileInputStream("file.html");
        Document document = Jsoup.parse(inputStream, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
    }
  
    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals(bodyHtml, document.body().html());
    }
  
    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Test</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertNotNull(document);
        Assert.assertEquals(bodyHtml, document.body().html());
    }
  
    @Test
    public void testParseUrlWithTimeout() throws IOException {
        String url = "http://example.com";
        int timeoutMillis = 5000;
        Document document = Jsoup.parse(new URL(url), timeoutMillis);
        Assert.assertNotNull(document);
    }
  
    @Test
    public void testCleanWithBaseUriAndWhitelist() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertNotNull(cleanHtml);
    }
  
    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<h1>Test</h1>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        Assert.assertNotNull(cleanHtml);
    }
  
    @Test
    public void testCleanWithBaseUriAndWhitelistAndOutputSettings() {
        String bodyHtml = "<h1>Test</h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true).escapeMode(EscapeMode.xhtml);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        Assert.assertNotNull(cleanHtml);
    }
  
    @Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<h1>Test</h1>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertTrue(isValid);
    }
}
