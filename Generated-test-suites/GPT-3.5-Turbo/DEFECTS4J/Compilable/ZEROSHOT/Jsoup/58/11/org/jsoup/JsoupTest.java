package org.jsoup;
import org.jsoup.*;
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
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document document = Jsoup.parse(html);
        String title = document.title();
        Assert.assertEquals("Hello World", title);
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        String title = document.title();
        Assert.assertEquals("Hello World", title);
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = Parser.htmlParser();
        Document document = Jsoup.parse(html, "", parser);
        String title = document.title();
        Assert.assertEquals("Hello World", title);
    }

    @Test
    public void testConnect() throws IOException {
        String url = "http://example.com";
        Connection connection = Jsoup.connect(url);
        Assert.assertNotNull(connection);
    }

    @Test
    public void testParseFromFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(file, charsetName, baseUri);
        String title = document.title();
        Assert.assertEquals("Test HTML", title);
    }

    @Test
    public void testParseFromInputStream() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(in, charsetName, baseUri);
        String title = document.title();
        Assert.assertEquals("Test HTML", title);
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        String title = document.title();
        Assert.assertEquals("", title);
    }

    @Test
    public void testClean() {
        String bodyHtml = "<h1>Hello <script>alert('World');</script></h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertEquals("<h1>Hello </h1>", cleanHtml);
    }

    @Test
    public void testCleanWithOutputSettings() {
        String bodyHtml = "<h1>Hello <script>alert('World');</script></h1>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        Assert.assertEquals("<h1>Hello </h1>", cleanHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<h1>Hello <script>alert('World');</script></h1>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertFalse(isValid);
    }

    @Test
    public void testParseWithURL() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 5000;
        Document document = Jsoup.parse(url, timeoutMillis);
        String title = document.title();
        Assert.assertEquals("Example Domain", title);
    }

}