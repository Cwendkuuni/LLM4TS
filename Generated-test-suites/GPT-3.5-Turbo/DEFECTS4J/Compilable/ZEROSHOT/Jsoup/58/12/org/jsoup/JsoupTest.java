package org.jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body>Hello World</body></html>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(html, baseUri);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  Hello World\n" +
                " </body>\n" +
                "</html>", document.toString());
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><head></head><body>Hello World</body></html>";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = Jsoup.parse(html, baseUri, parser);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  Hello World\n" +
                " </body>\n" +
                "</html>", document.toString());
    }

    @Test
    public void testParseWithoutBaseUri() {
        String html = "<html><head></head><body>Hello World</body></html>";
        Document document = Jsoup.parse(html);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  Hello World\n" +
                " </body>\n" +
                "</html>", document.toString());
    }

    @Test
    public void testConnect() throws IOException {
        Connection connection = Jsoup.connect("http://example.com");
        Assert.assertNotNull(connection);
    }

    @Test
    public void testParseFileWithCharsetAndBaseUri() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(file, charsetName, baseUri);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  Hello World\n" +
                " </body>\n" +
                "</html>", document.toString());
    }

    @Test
    public void testParseFileWithCharset() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        Document document = Jsoup.parse(file, charsetName);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  Hello World\n" +
                " </body>\n" +
                "</html>", document.toString());
    }

    @Test
    public void testParseInputStreamWithCharsetAndBaseUri() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = Jsoup.parse(inputStream, charsetName, baseUri);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  Hello World\n" +
                " </body>\n" +
                "</html>", document.toString());
    }

    @Test
    public void testParseInputStreamWithCharsetAndBaseUriWithParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = Jsoup.parse(inputStream, charsetName, baseUri, parser);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  Hello World\n" +
                " </body>\n" +
                "</html>", document.toString());
    }

    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyHtml = "<div>Hello World</div>";
        String baseUri = "http://example.com";
        Document document = Jsoup.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertEquals("<body>\n" +
                " <div>\n" +
                "  Hello World\n" +
                " </div>\n" +
                "</body>", document.toString());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div>Hello World</div>";
        Document document = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertEquals("<body>\n" +
                " <div>\n" +
                "  Hello World\n" +
                " </div>\n" +
                "</body>", document.toString());
    }

    @Test
    public void testParseURL() throws IOException {
        URL url = new URL("http://example.com");
        int timeoutMillis = 1000;
        Document document = Jsoup.parse(url, timeoutMillis);
        Assert.assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  Hello World\n" +
                " </body>\n" +
                "</html>", document.toString());
    }

    @Test
    public void testCleanWithBaseUriAndWhitelist() {
        String bodyHtml = "<div><a href='www.example.com'>Example</a></div>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        Assert.assertEquals("<div>\n" +
                " <a href=\"http://www.example.com\" rel=\"nofollow\">Example</a>\n" +
                "</div>", cleanedHtml);
    }

    @Test
    public void testCleanWithWhitelist() {
        String bodyHtml = "<div><a href='www.example.com'>Example</a></div>";
        Whitelist whitelist = Whitelist.basic();
        String cleanedHtml = Jsoup.clean(bodyHtml, whitelist);
        Assert.assertEquals("<div>\n" +
                " <a href=\"http://www.example.com\" rel=\"nofollow\">Example</a>\n" +
                "</div>", cleanedHtml);
    }

    @Test
    public void testCleanWithBaseUriAndWhitelistAndOutputSettings() {
        String bodyHtml = "<div><a href='www.example.com'>Example</a></div>";
        String baseUri = "http://example.com";
        Whitelist whitelist = Whitelist.basic();
        Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(true);
        String cleanedHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        Assert.assertEquals("<div>\n" +
                " <a href=\"http://www.example.com\" rel=\"nofollow\">\n" +
                "  Example\n" +
                " </a>\n" +
                "</div>", cleanedHtml);
    }

    @Test
    public void testIsValid() {
        String bodyHtml = "<div><a href='www.example.com'>Example</a></div>";
        Whitelist whitelist = Whitelist.basic();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertTrue(isValid);
    }
}