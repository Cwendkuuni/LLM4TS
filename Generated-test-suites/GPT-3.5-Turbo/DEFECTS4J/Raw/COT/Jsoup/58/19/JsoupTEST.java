import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head></head><body><h1>Hello Jsoup</h1></body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        Assert.assertEquals("Hello Jsoup", document.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><head></head><body><h1>Hello Jsoup</h1></body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse(html, baseUri, parser);
        Assert.assertEquals("Hello Jsoup", document.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head></head><body><h1>Hello Jsoup</h1></body></html>";
        Document document = Jsoup.parse(html);
        Assert.assertEquals("Hello Jsoup", document.body().getElementsByTag("h1").text());
    }

    @Test
    public void testConnect() {
        String url = "http://example.com";
        Jsoup.connect(url);
        // Check that no exceptions are thrown
    }

    @Test
    public void testParseFileWithCharsetNameAndBaseUri() throws IOException {
        File file = new File("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(file, charsetName, baseUri);
        Assert.assertEquals("Hello Jsoup", document.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseFileWithCharsetName() throws IOException {
        File file = new File("example.html");
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(file, charsetName);
        Assert.assertEquals("Hello Jsoup", document.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseInputStreamWithCharsetNameAndBaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        Assert.assertEquals("Hello Jsoup", document.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseInputStreamWithCharsetNameAndBaseUriAndParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("example.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser();
        Document document = Jsoup.parse(inputStream, charsetName, baseUri, parser);
        Assert.assertEquals("Hello Jsoup", document.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<h1>Hello Jsoup</h1>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertEquals("Hello Jsoup", document.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseBodyFragmentWithoutBaseUri() {
        String bodyHtml = "<h1>Hello Jsoup</h1>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertEquals("Hello Jsoup", document.body().getElementsByTag("h1").text());
    }

    @Test
    public void testParseFromURL() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 5000;
        Document document = Jsoup.parse(url, timeoutMillis);
        Assert.assertEquals("Hello Jsoup", document.body().getElementsByTag("h1").text());
    }

    @Test
    public void testCleanWithBaseUriAndWhitelist() {
        String bodyHtml = "<p><a href='http://example.com'>Link</a></p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertEquals("<p><a href=\"http://example.com\" rel=\"nofollow\">Link</a></p>", cleanHtml);
    }

    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<p><a href='http://example.com'>Link</a></p>";
        Whitelist whitelist = Whitelist.basic();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        Assert.assertEquals("<p><a href=\"http://example.com\" rel=\"nofollow\">Link</a></p>", cleanHtml);
    }

    @Test
    public void testCleanWithBaseUriAndWhitelistAndSettings() {
        String bodyHtml = "<p><a href='http://example.com'>Link</a></p>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false).escapeMode(Document.EscapeMode.extended);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        Assert.assertEquals("<p><a href=\"http://example.com\" rel=\"nofollow\">Link</a></p>", cleanHtml);
    }

    @Test
    public void testIsValidWithWhitelist() {
        String bodyHtml = "<p><a href='http://example.com'>Link</a></p>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertTrue(isValid);
    }
}